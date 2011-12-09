#include <jni.h>
#include <jvmti.h>
#include <assert.h>
#include <stdlib.h>
#include <classfile_constants.h>
#include <string.h>

#include "agent_main.h"
#include "state.h"
#include "agent.h"
#include "common.h"
#include "options.h"
#include "java_method.h"
#include "jnicheck.h"

#define MAX_BINDINGS (4000)
#define MAX_DEFERRED_METHODS (200)
#define MAX_INTERMEDIATE_PROXY_BYTES (20)

struct native_method_bind {

  /* intermediate code */
  unsigned char intermediate_proxy_code[MAX_INTERMEDIATE_PROXY_BYTES];

  /* identification of the native method */
  void* original_native_method_address;
  jmethodID mid;

  int is_user_method;      /* Whether or not user native method. */

  j2c_proxyid proxy_id;
  int active;              /* Whether or not the proxy is active. */

  int argument_slot_size;  /* The number of arguments including JNI env pointer */
  bda_jmethod_id jmethod_id;

  int num_jni_refs;

  /* Statistics */
  int j2c_count;
  int j2c_count_user;

  jlong j2c_hwcount;
  jlong j2c_hwcount_user;
};

struct deferred_proxy_info {
    jmethodID method;
    void* address;
};

/* a list of native methods and their proxies. */
struct native_method_bind bindings[MAX_BINDINGS];
int num_bindings = 0;

/* a list of of deferred proxies. */
static struct deferred_proxy_info pridomial_methods[MAX_DEFERRED_METHODS];
static unsigned int num_pridomial_method = 0;

static void bda_j2c_proxy(JNIEnv *env, jobject classOrObject, ...);

static void bda_analyze_native_method(jvmtiEnv *jvmti, struct native_method_bind * bind);

static struct native_method_bind * bda_allocate_native_method_bind(
    jmethodID method, void* address);

static void* proxy_address = bda_j2c_proxy;

#if (defined(__GNUC__) && defined(__i386__)) || defined(_WIN32)
static void bda_generate_intermediate_proxy(struct native_method_bind *bind)
{
    unsigned char* buf = bind->intermediate_proxy_code;
    unsigned int disp_proxy_address = (unsigned int)&proxy_address;
    int nid = bind->proxy_id;
    int i = 0;

    /* mov nid, %eax */
    buf[i++] = 0xb8;
    buf[i++] = ((nid) & 0x000000FF);
    buf[i++] = ((nid) & 0x0000FF00) >> 8;
    buf[i++] = ((nid) & 0x00FF0000) >> 16;
    buf[i++] = ((nid) & 0xFF000000) >> 24;

    /* jmp (*proxy_address) */
    buf[i++] = 0xff;
    buf[i++] = ((0 & 3) << 6) | (( 4 & 7) << 3) | (5 & 7);
    buf[i++] = (disp_proxy_address & 0x000000FF);
    buf[i++] = (disp_proxy_address & 0x0000FF00) >> 8;
    buf[i++] = (disp_proxy_address & 0x00FF0000) >> 16;
    buf[i++] = (disp_proxy_address & 0xFF000000) >> 24;

    assert(i < MAX_INTERMEDIATE_PROXY_BYTES);
    while(i < MAX_INTERMEDIATE_PROXY_BYTES) {
        buf[i++] = 0;
    }
}
#else 
#error "Unsupported"
#endif

static struct native_method_bind * bda_allocate_native_method_bind(
    jmethodID method, void* address) 
{
  j2c_proxyid proxy_id;
  struct native_method_bind *bind;

  //allocate a proxy
  assert(num_bindings < MAX_BINDINGS);
  proxy_id = num_bindings++;
  bind = &bindings[proxy_id];
  bind->proxy_id = proxy_id;
  bind->mid = method;
  bind->original_native_method_address = address;

  bind->j2c_count = 0;
  bind->j2c_count_user = 0;
  
  return bind;
}

static void bda_j2c_proxy(JNIEnv *env, jobject classOrObject, ...)
{
  int nid;

  /* for incoming parameters and outgoing result.*/
  unsigned int return_eax, return_edx;
  void *arg_begin, *saved_my_esp;

  /* information about the orignial native method. */
  struct native_method_bind *bind;
  void* original_native_target;
  unsigned int jni_arg_size;

  /* for stitching call frame. */
  void *saved_return_addr, *saved_prev_fp;
  void *c2j_return_addr, *c2j_prev_fp;
  struct bda_c2j_info const * c2j;
  struct bda_j2c_info j2c_info;
  struct bda_state_info *s;
  const struct bda_java_method_info *  minfo;

  /* obtain the target native method infomation.*/
#if defined(__GNUC__) && defined(__i386__)
  asm("movl %%eax, %0;" : "=m" (nid));
#elif defined(_WIN32)
  __asm mov nid, eax;
#else 
#error "Unsupported"
#endif

  bind = &bindings[nid];
  s = bda_get_state_info(env);

  /* statistics. */
  if (s != NULL) {
    bind->j2c_count++;
    if (bind->is_user_method) { bind->j2c_count_user++;}
  }

  minfo = bda_jmethod_ensure(bind->jmethod_id);
  original_native_target = bind->original_native_method_address;
  jni_arg_size = bind->argument_slot_size; 
  GET_RETURN_ADDRESS(saved_return_addr);
  GET_PREVIOUS_FRAME_POINTER(saved_prev_fp);

  if (s != NULL) {
    /* stitch frame. */
    c2j = s->head_c2j;
    if (c2j != NULL) {
      c2j_return_addr = c2j->return_addr;
      c2j_prev_fp = c2j->caller_fp;
    } else {
      c2j_return_addr = NULL;
      c2j_prev_fp = NULL;
    }
    SET_RETURN_ADDRESS(c2j_return_addr);
    SET_PREVIOUS_FRAME_POINTER(c2j_prev_fp);

    /* notify j2c_call event. */
    j2c_info.methodID = bind->mid; 
    j2c_info.native_method_address = original_native_target;
    j2c_info.is_user_method = bind->is_user_method;
    j2c_info.env = env;
    bda_state_j2c_call(s, &j2c_info);

    if (agent_options.jniassert) {
      int i;
      va_list argp;

      bda_local_ref_enter(s, bind->argument_slot_size, 1);
      if (classOrObject != NULL) {bda_local_ref_add(s, classOrObject);}
      va_start(argp, classOrObject);
      for(i=0; i < minfo->num_arguments;i++) {
          const struct bda_java_argument_info * a = &(minfo->arguments[i]);
          switch(a->type) {
          case 'Z': case 'B': case 'C': case 'I': case 'S': case 'F': {
              va_arg(argp, jint);
              break;
          }
          case 'J': case 'D': {
              va_arg(argp, jint);
              va_arg(argp, jint);
              break;
          }
          case 'L': case '[': {
              jobject v = (jobject)va_arg(argp, jint);
              if (v != NULL) {
                  bda_local_ref_add(s, v);
              }
              break;
          }
          default:
              assert(0);
              break;
          }
      }
      va_end(argp);
      bda_local_ref_enter(s, 16, 0);
    }
  }

  /* Now, call the original native method.*/
  arg_begin = &env + (jni_arg_size-1);
#if defined(__GNUC__) && defined(__i386__)
  asm(
      "movl %%esp, %0;"
      "movl %5, %%ecx;"
      "movl %4, %%eax;"
      "L: pushl (%%eax);"
      "sub $4, %%eax;"
      "loop L;"
      "movl %6, %%eax;"
      "call *%%eax;"
      "mov %%eax, %1;"
      "mov %%edx, %2;"
      "movl %3, %%eax;"
      "movl %%eax, %%esp;"
      : "=m"(saved_my_esp), "=m"(return_eax), "=m"(return_edx)
      : "m"(saved_my_esp), "m"(arg_begin), "m"(jni_arg_size), "m"(original_native_target)
      : "%eax","%ecx","%edx","%esp"
      );
#elif defined(_WIN32)
  __asm
  {
      mov saved_my_esp, esp
      mov ecx, jni_arg_size
      mov eax, arg_begin
   L_2:
      push [eax]
      sub eax, 4
      loop L_2
      mov eax, original_native_target
      call eax
      mov return_eax, eax
      mov return_edx, edx
      mov eax, saved_my_esp
      mov esp,eax
  }
#else 
#error "Unsupported"

#endif

  if (s != NULL) {

    if (agent_options.jniassert) {
        int success  = 1;

        if (success) {
            if ((minfo->returnInfo.type == 'L') ||(minfo->returnInfo.type == '[')) {
                if ((jobject)return_eax != NULL) {
                    success = bda_check_ref_dangling(s, (jobject)return_eax, 0, minfo->fullname);
                }
            }
        }

        if (success) {
            success = bda_check_local_frame_double_free(s);
        }
        bda_local_ref_leave(s);

        if (success) {
            success = bda_check_local_frame_leak(s); //the frame must be sential.
        }
        bda_local_ref_leave(s); //sential frame
    }

    /* notify j2c_return event. */
    bda_state_j2c_return(s, &j2c_info);
      
    /* unstitch this frame.*/
    SET_PREVIOUS_FRAME_POINTER(saved_prev_fp);
    SET_RETURN_ADDRESS(saved_return_addr);
  }

    /* return to caller.*/
#if defined(__GNUC__) && defined(__i386__)
  asm(
      "mov %0, %%eax;"
      "mov %1, %%edx;"
      :
      : "m"(return_eax), "m"(return_edx)
      );
#elif defined(_WIN32)
  __asm
  {
    mov eax, return_eax
    mov edx, return_edx
  }
#else 
#error "Unsupported"
#endif
}


j2c_proxyid bda_j2c_proxy_add(jvmtiEnv *jvmti, JNIEnv *env, jmethodID method, 
                              void* address, void** new_address_ptr)
{
  struct native_method_bind *bind;

  assert(address != NULL);

  //allocate a proxy
  bind = bda_allocate_native_method_bind(method, address);

  bda_analyze_native_method(jvmti, bind);

  //redirect the native method call to the proxy.
  bda_generate_intermediate_proxy(bind);
  *new_address_ptr = bind->intermediate_proxy_code;
  bind->active = 1;

  return bind->proxy_id;
}

void bda_j2c_proxy_dump_stat()
{
  int i;
  int sum = 0;
  int sum_user = 0;

  printf("%d java native methods\n", num_bindings);
  printf("%21s", "Method counts");
  printf("%10s %10s %s\n", "System", "User", "Native method");
  for(i=0;i<num_bindings;i++){
    struct native_method_bind *bind = &bindings[i];        
    const struct bda_java_method_info * minfo = bda_jmethod_ensure(bind->jmethod_id);

    sum += bind->j2c_count;
    sum_user += bind->j2c_count_user;

    printf("%10d %10d %s%s\n",
           bind->j2c_count, bind->j2c_count_user,
           minfo->fullname, minfo->mdesc);
  }
  printf("%10d %10d %s\n", sum, sum_user, "All the Java native methods");
}


void bda_j2c_proxy_add_deferred(jmethodID method, void *address)
{
    unsigned int id;

#ifdef DEBUG
    printf("bda_j2c_proxy_add_deferred: %d\n", num_pridomial_method);
#endif

    assert(num_pridomial_method < MAX_DEFERRED_METHODS);
    id = num_pridomial_method++;
    pridomial_methods[id].method = method;
    pridomial_methods[id].address = address;
}

void bda_j2c_proxy_deferred_methods_reregister(jvmtiEnv *jvmti, JNIEnv *env)
{
  int i;
  jvmtiError err;
  jint jni_err;

  for(i=0; i < num_pridomial_method;i++) {
    struct deferred_proxy_info *info = &pridomial_methods[i];
    JNINativeMethod jni_method;
    jmethodID method = info->method;
    void* address = info->address;
    jclass clazz;
    char *mname, *mdesc;
    err = (*jvmti)->GetMethodDeclaringClass(jvmti, method, &clazz);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->GetMethodName(jvmti, method, &mname, &mdesc, NULL);
    assert(err == JVMTI_ERROR_NONE);

    jni_method.name = mname;
    jni_method.signature = mdesc;
    jni_method.fnPtr = address;
    assert(bda_orig_jni_funcs != NULL);
    jni_err = bda_orig_jni_funcs->RegisterNatives(env, clazz, &jni_method, 1);
    assert(jni_err == 0);

    err = (*jvmti)->Deallocate(jvmti, (unsigned char*)mname);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->Deallocate(jvmti, (unsigned char*)mdesc);
    assert(err == JVMTI_ERROR_NONE);
  }
}

bda_jmethod_id bda_j2c_proxy_get_method_id(j2c_proxyid pid)
{
    return bindings[pid].jmethod_id;
}

int bda_j2c_proxy_is_user_method(j2c_proxyid pid) 
{
  return bindings[pid].is_user_method;
}

static void bda_analyze_native_method(jvmtiEnv *jvmti, struct native_method_bind * bind)
{
  jvmtiError err;
  jclass clazz;
  jobject cloader;
  jmethodID method;
  const struct bda_java_method_info * minfo;
  char *cdesc, *mname, *mdesc;
  int modifier, argument_slot_size, i;

  method = bind->mid;
  

  err = (*jvmti)->GetMethodDeclaringClass(jvmti, method, &clazz); 
  assert(err == JVMTI_ERROR_NONE);
  err = (*jvmti)->GetClassLoader(jvmti, clazz, &cloader);
  assert(err == JVMTI_ERROR_NONE);

  bind->is_user_method = cloader != NULL;
  err = (*jvmti)->GetMethodModifiers(jvmti, method, &modifier);
  assert(err == JVMTI_ERROR_NONE);

  err = (*jvmti)->GetClassSignature(jvmti, clazz, &cdesc, NULL);
  assert(err == JVMTI_ERROR_NONE);

  err = (*jvmti)->GetMethodName(jvmti, method, &mname, &mdesc, NULL);
  assert(err == JVMTI_ERROR_NONE);
  
  bind->jmethod_id = bda_jmethod_info_init(bind->mid, modifier & JVM_ACC_STATIC, cdesc, mname, mdesc);
  minfo = bda_jmethod_ensure(bind->jmethod_id);

  /* Compute the number of slots for the incoming arguments. */
  argument_slot_size = 2; /* JNIEnv and [class|object] are default native method arguments. */
  for(i=0;i < minfo->num_arguments;i++) {
    argument_slot_size += minfo->arguments[i].n_words;
  }
  bind->argument_slot_size = argument_slot_size;

  err = (*jvmti)->Deallocate(jvmti, (unsigned char*)mname);
  assert(err == JVMTI_ERROR_NONE);

  err = (*jvmti)->Deallocate(jvmti, (unsigned char*)mdesc);
  assert(err == JVMTI_ERROR_NONE);

  err = (*jvmti)->Deallocate(jvmti, (unsigned char*)cdesc);
  assert(err == JVMTI_ERROR_NONE);

}
