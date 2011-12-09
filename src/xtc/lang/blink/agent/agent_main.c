#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include <jni.h>
#include <jvmti.h>

#include "state.h"
#include "options.h"
#include "common.h"
#include "jnicheck.h"

/* function declarations */
static void JNICALL agent_vm_init(jvmtiEnv *jvmti, JNIEnv *env, jthread thread);
static void JNICALL agent_vm_start(jvmtiEnv *jvmti, JNIEnv* env);
static void JNICALL agent_vm_bind(jvmtiEnv *jvmti, JNIEnv *env,
                                  jthread thread, jmethodID method,
                                  void* address, void** new_address_ptr);
static void JNICALL agent_vm_death(jvmtiEnv *jvmti, JNIEnv *env);
static void JNICALL agent_vm_thread_start(jvmtiEnv *jvmti, JNIEnv * env, jthread t);
static void JNICALL agent_vm_thread_end(jvmtiEnv *jvmti, JNIEnv * env, jthread t);

/* variable definitions */
JavaVM* bda_jvm = NULL; /* The Java virtual machine */
jvmtiEnv * bda_jvmti = NULL; /* This JVMTI handle from JVM. */
jniNativeInterface* bda_orig_jni_funcs = NULL; /* JNI function table */

/* function definitions. */
JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *vm, char *options, void *reserved)
{
  jvmtiError err;
  jvmtiCapabilities capa;
  jvmtiEventCallbacks callbacks;
  jvmtiEnv *jvmti;
  int rc;
  unsigned int nthread;

  nthread = GET_NATIVE_THREADID();

  assert(bda_jvm == NULL && vm != NULL);
  bda_jvm = vm;

  /* Parse incoming options. */
  agent_parse_options(options);

  /* Ensure JVMTI agent capabilities. */
  rc = (*vm)->GetEnv(vm, (void**)&jvmti, JVMTI_VERSION_1);
  assert(rc == JNI_OK);
  err = (*jvmti)->GetCapabilities(jvmti, &capa);
  assert(err == JVMTI_ERROR_NONE);
  if (!agent_options.nointerpose) {
      if (!capa.can_generate_native_method_bind_events) {
          capa.can_generate_native_method_bind_events = 1;
          err = (*jvmti)->AddCapabilities(jvmti, &capa);
          assert(err == JVMTI_ERROR_NONE);
      }
      if (!capa.can_get_bytecodes) {
          capa.can_get_bytecodes = 1;
          err = (*jvmti)->AddCapabilities(jvmti, &capa);
          assert(err == JVMTI_ERROR_NONE);
      }
      if (!capa.can_get_line_numbers) {
          capa.can_get_line_numbers = 1;
          err = (*jvmti)->AddCapabilities(jvmti, &capa);
          assert(err == JVMTI_ERROR_NONE);
      }
  }

  /* Initialize the JVMTI event call backs. */
  memset(&callbacks, 0, sizeof(callbacks));
  callbacks.VMInit = &agent_vm_init;
  callbacks.VMStart = &agent_vm_start;
  callbacks.VMDeath = &agent_vm_death;
  callbacks.ThreadStart = &agent_vm_thread_start;
  callbacks.ThreadEnd = &agent_vm_thread_end;
  if (!agent_options.nointerpose) {
      callbacks.NativeMethodBind = &agent_vm_bind;
  }

  err = (*jvmti)->SetEventCallbacks(jvmti, &callbacks, sizeof(callbacks));
  assert(err == JVMTI_ERROR_NONE);

  err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, 
                                           JVMTI_EVENT_VM_DEATH, NULL);
  assert(err == JVMTI_ERROR_NONE);

  if (!agent_options.nointerpose) {
      err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE,
                                               JVMTI_EVENT_NATIVE_METHOD_BIND, NULL);
      assert(err == JVMTI_ERROR_NONE);
  }

  err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, 
                                           JVMTI_EVENT_VM_INIT, NULL);
  assert(err == JVMTI_ERROR_NONE);

  err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, 
                                           JVMTI_EVENT_VM_START, NULL);
  assert(err == JVMTI_ERROR_NONE);

  err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, 
                                           JVMTI_EVENT_THREAD_START, NULL);
  assert(err == JVMTI_ERROR_NONE);

  err = (*jvmti)->SetEventNotificationMode(jvmti, JVMTI_ENABLE, 
                                           JVMTI_EVENT_THREAD_END, NULL);
  assert(err == JVMTI_ERROR_NONE);

  bda_jvmti = jvmti;

  return 0;
}

JNIEXPORT void JNICALL Agent_OnUnload(JavaVM *vm)
{
}

static void JNICALL agent_vm_start(jvmtiEnv *jvmti, JNIEnv* env)
{
  unsigned int nthread;

  nthread = GET_NATIVE_THREADID();
}

static void JNICALL agent_vm_init(jvmtiEnv *jvmti, JNIEnv *env, jthread thread)
{
  unsigned int nthread;
  jvmtiError err;

  nthread = GET_NATIVE_THREADID();

  /* set c2j jni proxies. */
  err = (*jvmti)->GetJNIFunctionTable(jvmti, &bda_orig_jni_funcs);
  assert(err == JVMTI_ERROR_NONE);
  if (!agent_options.nointerpose) {
      bda_c2j_proxy_install(jvmti);
  }

  if (agent_options.jniassert) {
      bda_jnicheck_init(env);
  }

  if (agent_options.bia) {
      bda_agent_init(env);
  }

  /* enable deferred native proxies during pridomial phase. */
  if (!agent_options.nointerpose) {
      bda_j2c_proxy_deferred_methods_reregister(jvmti, env);
  }
}


static void JNICALL agent_vm_death(jvmtiEnv *jvmti, JNIEnv *env)
{
  if (agent_options.jniassert) {
      bda_jnicheck_exit(env);
  }
  if (agent_options.mcount) {
    bda_j2c_proxy_dump_stat();
    bda_c2j_proxy_dump_stat();
  }
  bda_jvmti = NULL;
}

static void JNICALL agent_vm_bind(jvmtiEnv *jvmti, JNIEnv *env,
                                jthread thread, jmethodID method,
                                void* address, void** new_address_ptr)
{
  jvmtiPhase phase;
  jvmtiError err;
  err = (*jvmti)->GetPhase(jvmti, &phase);
  assert(err == JVMTI_ERROR_NONE);
  if (!bda_is_agent_native_method(address)) {
    switch(phase) {
    case JVMTI_PHASE_ONLOAD:
    case JVMTI_PHASE_PRIMORDIAL:
        bda_j2c_proxy_add_deferred(method, address);
      break;
    case JVMTI_PHASE_START:
    case JVMTI_PHASE_LIVE: {
        bda_j2c_proxy_add(jvmti, env, method, address, new_address_ptr);
        break;
    }
    default:
      assert(0); /* not reachable. */
      break;
    }
  }
}

static void JNICALL agent_vm_thread_start(jvmtiEnv *jvmti, JNIEnv * env, jthread t)
{
  jvmtiError err;
  jvmtiPhase phase;
  jvmtiThreadInfo tinfo;
  unsigned int nthread;
  bda_state_id bid;
  struct bda_state_info * s;

  nthread = GET_NATIVE_THREADID();

  /* check phase*/
  err = (*jvmti)->GetPhase(jvmti, &phase);
  assert(err == JVMTI_ERROR_NONE);
  if (phase != JVMTI_PHASE_LIVE) {
    return;
  }

  bid = bda_state_allocate(env);
  s = bda_state_get(bid);
  s->nthreadid = nthread;
  err = (*jvmti)->SetThreadLocalStorage(jvmti, NULL, (void*)bid);
  assert(err == JVMTI_ERROR_NONE);

  /* thread name*/
  err = (*jvmti)->GetThreadInfo(jvmti, NULL, &tinfo);
  assert(err == JVMTI_ERROR_NONE);
  assert(tinfo.name != NULL);
  strncpy(s->name, tinfo.name, sizeof(s->name));
  if (sizeof(s->name) > 0) { s->name[sizeof(s->name) - 1] = '\0';}
  err = (*jvmti)->Deallocate(jvmti, (unsigned char*)tinfo.name);

}

static void JNICALL agent_vm_thread_end(jvmtiEnv *jvmti, JNIEnv * env, jthread t)
{
  jvmtiError err;
  bda_state_id bid;

  struct bda_state_info * s = bda_state_find(env);

  if (s != NULL) {
    err = (*jvmti)->GetThreadLocalStorage(jvmti, NULL, (void**)&bid);
    assert(err == JVMTI_ERROR_NONE);
    
    bda_state_free(bid);
    err = (*jvmti)->SetThreadLocalStorage(jvmti, NULL, NULL);
    assert(err == JVMTI_ERROR_NONE);
  }
}
