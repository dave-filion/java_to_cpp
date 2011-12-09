#ifndef _STATE_H_
#define _STATE_H_

#include <jni.h>
#include <jvmti.h>

#include "java_method.h"

enum bda_mode {JVM, SYS_NATIVE, USR_NATIVE};

/* per-thread JNI state info. */
struct bda_state_info {
  JNIEnv* env;
  char name[128];
  unsigned int nthreadid;
  
  enum bda_mode mode;
  struct bda_c2j_info* head_c2j;
  int transitions_in_stack;
  
  struct bda_local_frame * local_frame_top;
  int critical;
  struct hashtable * open_criticals;
  char msgbuf[1024];
};

enum bda_c2j_call_type {
  JNI_CALL_NOT_CLASSIFIED,
  JNI_CALL_INSTANCE,
  JNI_CALL_NONVIRTUAL,
  JNI_CALL_STATIC,
};

struct bda_c2j_info {
  void* caller_fp;
  void* return_addr;
  int jdwp_context;
  struct bda_c2j_info * prev;

  /* For Call...Method only. */
  enum bda_c2j_call_type call_type;
  jclass class;
  jobject object;
  jmethodID mid;
};

struct bda_j2c_info {
  JNIEnv *env;
  jmethodID methodID;
  void* native_method_address;
  int is_user_method;
};

typedef int bda_state_id;
typedef int j2c_proxyid;

/* notify JNI state change. */
extern bda_state_id bda_state_allocate(JNIEnv *env);
extern void bda_state_free(bda_state_id bid);
extern struct bda_state_info * bda_state_get(bda_state_id bid);

extern void bda_state_j2c_call(struct bda_state_info * s, struct bda_j2c_info * j2c_info);
extern void bda_state_j2c_return(struct bda_state_info * s, struct bda_j2c_info * j2c_info);
extern void bda_state_c2j_call(struct bda_state_info * s, struct bda_c2j_info * c2j_info);
extern void bda_state_c2j_return(struct bda_state_info * s, struct bda_c2j_info * c2j_info);

/* query JNI state. */
extern int bda_state_get_transition_count(JNIEnv* env);
extern struct bda_state_info * bda_state_find(JNIEnv *env);
extern struct bda_state_info* bda_get_state_info(JNIEnv *env);
extern int bda_get_current_transition_count();

/* breakpoints */
extern void bda_reset_transition_breakpoints();

extern void bda_c2j_proxy_install(jvmtiEnv *jvmti);
extern void bda_c2j_proxy_dump_stat();

extern j2c_proxyid bda_j2c_proxy_add(jvmtiEnv *jvmti, JNIEnv *env, jmethodID method, 
                                     void* address, void** new_address_ptr);
extern bda_jmethod_id bda_j2c_proxy_get_method_id(j2c_proxyid pid);
extern int bda_j2c_proxy_is_user_method(j2c_proxyid pid);
extern void bda_j2c_proxy_deferred_methods_reregister(jvmtiEnv *jvmti, JNIEnv *env);
extern void bda_j2c_proxy_add_deferred(jmethodID method, void *address);
extern void bda_j2c_proxy_dump_stat();

#endif /** _STATE_H_ */
