#include <jni.h>
#include <jvmti.h>
#include <assert.h>

#include "agent_main.h"
#include "state.h"
#include "options.h"
#include "agent.h"
#include "common.h"
#include "state.h"
#include "agent_class.h"

/* declaration. */
JNIEXPORT void JNICALL bda_j2c(JNIEnv *env, jclass clz);
JNIEXPORT jint JNICALL bda_get_process_id(JNIEnv *env, jclass clz);
JNIEXPORT jint JNICALL bda_dummy_native_jni(JNIEnv *env, jclass clz);

/* agent classes*/
static jclass agent_class = NULL;
static jclass agent_variable_class = NULL;

/* a list of Blink agent's special native methods. */
static const JNINativeMethod agent_native_methods[] =
{
  { "getProcessID", "()I", bda_get_process_id },
  { "j2c", "()V", bda_j2c },
  { "dummyNative", "()I", bda_dummy_native_jni},
};

/* agent java methods. */
struct java_static_method {
    jmethodID methodid;
    const char * name;
    const char * desc;
};

static struct java_static_method agent_java_method_init = 
{NULL, "init", "()V"};
static struct java_static_method agent_java_method_jbp = 
{NULL, "jbp", "()V"};
static struct java_static_method agent_java_method_dummy_java =
{NULL, "dummyJava", "()I"};

static struct java_static_method agent_var_set_vj_boolean = 
{NULL, "setVjFromJavaExpr", "(Z)I"};
static struct java_static_method agent_var_set_vj_int = 
{NULL, "setVjFromJavaExpr", "(I)I"};
static struct java_static_method agent_var_set_vj_double = 
{NULL, "setVjFromJavaExpr", "(D)I"};
static struct java_static_method agent_var_set_vj_object = 
{NULL, "setVjFromJavaExpr", "(Ljava/lang/Object;)I"};

static struct java_static_method agent_var_get_vj_boolean = 
{NULL, "get_vj_jboolean", "(I)Z"};
static struct java_static_method agent_var_get_vj_int = 
{NULL, "get_vj_jint", "(I)I"};
static struct java_static_method agent_var_get_vj_double =
{NULL, "get_vj_jdouble", "(I)D"};
static struct java_static_method agent_var_get_vj_object = 
{NULL, "get_vj_jobject", "(I)Ljava/lang/Object;"};

/* Java and c transition breakpoint points */
int bda_j2c_call_breakpoint = 0;
int bda_j2c_return_breakpoint = 0;
int bda_c2j_call_breakpoint = 0;
int bda_c2j_return_breakpoint = 0;
int bda_transition_count = 0;

/* jdwp region. */
void* bda_jdwp_region_begin = NULL;
void* bda_jdwp_region_end = NULL;

static void bda_cache_static_java_method_id(JNIEnv* env, jclass clazz, 
                                            int is_static,
                                            struct java_static_method *meth)
{
    const char * mname = meth->name;
    const char * mdesc = meth->desc;
    jmethodID mid;
    if (is_static) { 
        mid = bda_orig_jni_funcs->GetStaticMethodID(env, clazz, mname, mdesc);
    } else {
        mid = bda_orig_jni_funcs->GetMethodID(env, clazz, mname, mdesc);
    }
    assert(mid);
    meth->methodid = mid;
}

static jclass load_global_agent_class(JNIEnv* env, const char* cname, const jbyte *buf, jsize len)
{
  jclass jclass_ref_local, jclass_ref_global;

  jclass_ref_local = bda_orig_jni_funcs->DefineClass(env, cname, NULL, buf, len);
  assert(jclass_ref_local != NULL);
  jclass_ref_global = bda_orig_jni_funcs->NewGlobalRef(env, jclass_ref_local);
  assert(jclass_ref_global != NULL);
  return jclass_ref_global;
}

void bda_agent_init(JNIEnv *env)
{
    jint jni_result;

    /* obtain a agent class reference. */
    agent_class = load_global_agent_class(
        env, AGENT_CLASS_NAME, class_agent, sizeof(class_agent));
    agent_variable_class = load_global_agent_class(
        env, AGENT_VARIABLE_CLASS_NAME, class_agent_variable,
        sizeof(class_agent_variable));

    /* obtain agent Java method identifiers. */
    bda_cache_static_java_method_id(env, agent_class, 1, &agent_java_method_init);
    bda_cache_static_java_method_id(env, agent_class, 1, &agent_java_method_jbp);
    bda_cache_static_java_method_id(env, agent_class, 1, &agent_java_method_dummy_java);
    bda_cache_static_java_method_id(env, agent_variable_class, 1, &agent_var_set_vj_boolean);
    bda_cache_static_java_method_id(env, agent_variable_class, 1, &agent_var_set_vj_int);
    bda_cache_static_java_method_id(env, agent_variable_class, 1, &agent_var_set_vj_double);
    bda_cache_static_java_method_id(env, agent_variable_class, 1, &agent_var_set_vj_object);
    bda_cache_static_java_method_id(env, agent_variable_class, 1, &agent_var_get_vj_boolean);
    bda_cache_static_java_method_id(env, agent_variable_class, 1, &agent_var_get_vj_int);
    bda_cache_static_java_method_id(env, agent_variable_class, 1, &agent_var_get_vj_double);
    bda_cache_static_java_method_id(env, agent_variable_class, 1, &agent_var_get_vj_object);

    /* register agent's native methods. */
    jni_result = bda_orig_jni_funcs->RegisterNatives(env, agent_class,
                                         agent_native_methods,
                                         sizeof(agent_native_methods)/sizeof(JNINativeMethod));
    assert(!jni_result);
    
    /* initialize java part of the agent. */
    bda_orig_jni_funcs->CallStaticVoidMethod(env, agent_class, agent_java_method_init.methodid);
    if (bda_orig_jni_funcs->ExceptionCheck(env)) {
        bda_orig_jni_funcs->ExceptionDescribe(env);
        assert(0);
    }
}

/* 
 * The entry procedure for the c2j method. This is for the native code
 * to call the "Agent.c2j() method. 
 */
int bda_c2j()
{
    JNIEnv *env;
    assert(agent_java_method_jbp.methodid != NULL);
    env = bda_ensure_jnienv();
    bda_orig_jni_funcs->CallStaticVoidMethod(env, agent_class, agent_java_method_jbp.methodid);
    return 1;
}

JNIEXPORT void JNICALL bda_j2c(JNIEnv *env, jclass clz)
{
    void *c2j_return_addr, *c2j_prev_fp;
    void *saved_return_addr, *saved_prev_fp;
    /*This is executed by JDWP agent internally, but we want to skip
    the JDWP agent's stack. Therefore, bda_j2c has special call frame
    stitching unlike the other j2c proxies. */
    struct bda_state_info*  s = bda_get_state_info(env);
    struct bda_c2j_info const * c2j = s->head_c2j;
    while(c2j != NULL && bda_is_in_jdwp_region(c2j->return_addr)) {
        c2j = c2j->prev;
    }
    if (c2j != NULL) {
        c2j_return_addr = c2j->return_addr;
        c2j_prev_fp =  c2j->caller_fp;
    } else {
        c2j_return_addr = NULL;
        c2j_prev_fp = NULL;
    }
    
    /* stitch this frame. */
    GET_RETURN_ADDRESS(saved_return_addr);
    GET_PREVIOUS_FRAME_POINTER(saved_prev_fp);
    SET_RETURN_ADDRESS(c2j_return_addr);
    SET_PREVIOUS_FRAME_POINTER(c2j_prev_fp);

    /* trigger break point. */
    bda_cbp(J2C_DEBUGGER, s, NULL);

    /* unstitch this frame.*/
    SET_RETURN_ADDRESS(saved_return_addr);
    SET_PREVIOUS_FRAME_POINTER(saved_prev_fp);
}

JNIEXPORT jint JNICALL bda_get_process_id(JNIEnv *env, jclass clz)
{
  jint pid = GET_CURRENT_PROCESS_ID();
  return pid;
}

JNIEXPORT jint JNICALL bda_dummy_native_jni(JNIEnv *env, jclass clz)
{
    return bda_dummy_native();
}

int bda_dummy_native()
{
    int i = 0;
    i = i + 1;
    return i;
}

/* get jnienv pointer value for the current thread. */
JNIEnv* bda_ensure_jnienv()
{
  JNIEnv *env;
  jint res;

  assert(bda_jvm != NULL);
  res = (*bda_jvm)->AttachCurrentThread(bda_jvm, (void **)&env, NULL);
  assert(res >= 0 && env != NULL);

  return env;
}


extern int bda_dummy_java() 
{
  JNIEnv *env;
  int result;
  env = bda_ensure_jnienv();
  result = bda_orig_jni_funcs->CallStaticIntMethod(env, agent_class, 
                               agent_java_method_dummy_java.methodid);
  return result;
}

/* Dummy function for the portable internal Blink break point. */
void bda_cbp(breakpoint_type bptype, struct bda_state_info * state, struct bda_location * target)
{
}

int bda_is_agent_native_method(void* address) 
{
  int i;
  for(i=0; i < sizeof(agent_native_methods)/sizeof(JNINativeMethod);i++) {
      if (agent_native_methods[i].fnPtr == address ) {
          return 1;
      }
  }
  return 0;
}


void bda_get_c2j_target(struct bda_state_info * s, struct bda_c2j_info * c2j_info, struct java_source_location *jloc)
{
    enum bda_c2j_call_type c2j_type = c2j_info->call_type;
    JNIEnv *env = s->env;
    jclass clazz = c2j_info->class;
    jobject obj = c2j_info->object;
    jmethodID mid = c2j_info->mid;

    switch(c2j_type) {
    case JNI_CALL_STATIC: {
        bda_set_java_location(bda_jvmti, mid, jloc);
        break;
    }
    case JNI_CALL_INSTANCE: {
        jvmtiError err;
        jmethodID target_method;
        jclass target_clazz;

        assert(clazz == NULL);
        clazz = bda_orig_jni_funcs->GetObjectClass(env, obj);
        assert(clazz != NULL);

        //search for the actual target virtual method.
        target_method = NULL;
        target_clazz = clazz;
        while(target_clazz != NULL && (target_method == NULL)) {
            jint method_count;
            jmethodID* methods;
            int i;

            err = (*bda_jvmti)->GetClassMethods(bda_jvmti, target_clazz, 
                                                &method_count, &methods);
            assert(err == JVMTI_ERROR_NONE);
            for(i=0;i < method_count;i++) {
                if(bda_method_name_and_desc_match(bda_jvmti, mid, methods[i])) {
                    target_method = methods[i];
                    break;
                }
            }
            (*bda_jvmti)->Deallocate(bda_jvmti, (unsigned char *)methods);

            if (target_method == NULL) {
                target_clazz = bda_orig_jni_funcs->GetSuperclass(env, target_clazz);
            }
        }
        assert(target_method != NULL);
        bda_set_java_location(bda_jvmti, target_method, jloc);
        break;
    }
    case JNI_CALL_NONVIRTUAL: {
        bda_set_java_location(bda_jvmti, mid, jloc);
        break;
    }
    default:
        assert(0); /* not reachable. */
        break;
    }
}

/* FIXME: perhaps just too bad implementation now. */
const char* bda_cstr(jstring jstr) 
{
    static char buf[256];
    int i;

    JNIEnv* env = bda_ensure_jnienv();
    int strsize = bda_orig_jni_funcs->GetStringUTFLength(env, jstr);
    const char *str = bda_orig_jni_funcs->GetStringUTFChars(env, jstr, NULL);

    if (strsize > 128) {
        strsize = 128;
    }
    for(i=0; i < strsize;i++) {
        buf[i] = str[i];
    }
    buf[i] = '\0';

    bda_orig_jni_funcs->ReleaseStringUTFChars(env, jstr, str);

    return buf;
}

int bda_set_vj_from_cexpr_jboolean(JNIEnv *env, jboolean exprValue)
{
    return (*env)->CallStaticIntMethod(env, agent_variable_class, 
                                       agent_var_set_vj_boolean.methodid, 
                                       exprValue);
}

int bda_set_vj_from_cexpr_jint(JNIEnv *env, jint exprValue)
{
    return (*env)->CallStaticIntMethod(env, agent_variable_class, 
                                       agent_var_set_vj_int.methodid, 
                                       exprValue);
}

int bda_set_vj_from_cexpr_jdouble(JNIEnv *env, jdouble exprValue)
{
    return (*env)->CallStaticIntMethod(env, agent_variable_class, 
                                       agent_var_set_vj_double.methodid,
                                       exprValue);
}

int bda_set_vj_from_cexpr_jobject(JNIEnv *env, jobject exprValue)
{
    return (*env)->CallStaticIntMethod(env, agent_variable_class, 
                                       agent_var_set_vj_object.methodid,
                                       exprValue);
}

jboolean bda_get_cvalue_from_vj_jboolean(JNIEnv *env, int vjid)
{
    return (*env)->CallStaticBooleanMethod(env, agent_variable_class,
                                           agent_var_get_vj_boolean.methodid,
                                           vjid);
}

jint bda_get_cvalue_from_vj_jint(JNIEnv *env, int vjid)
{
    return (*env)->CallStaticIntMethod(env, agent_variable_class,
                                       agent_var_get_vj_int.methodid,
                                       vjid);
}

jdouble bda_get_cvalue_from_vj_jdouble(JNIEnv *env, int vjid)
{
    return (*env)->CallStaticDoubleMethod(env, agent_variable_class,
                                          agent_var_get_vj_double.methodid,
                                          vjid);
}

jobject bda_get_cvalue_from_vj_jobject(JNIEnv *env, int vjid)
{
    return (*env)->CallStaticObjectMethod(env, agent_variable_class,
                                          agent_var_get_vj_object.methodid,
                                          vjid);
}
