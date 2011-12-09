#include <assert.h>
#include <stdarg.h>
#include <string.h>
#include <jvmti.h>

#include "agent_main.h"
#include "options.h"
#include "state.h"
#include "agent.h"
#include "common.h"
#include "jnicheck.h"

#define MAX_JNI_ENV (300)

/* 0'th entry is reserved for invalid identifier. */
static struct bda_state_info bda_state_info_list[1 + MAX_JNI_ENV];
static unsigned int bda_state_num = 0;

bda_state_id bda_state_allocate(JNIEnv *env)
{
  bda_state_id i;
  struct bda_state_info* s;

  assert(bda_state_num < MAX_JNI_ENV && MAX_JNI_ENV > 1);
  for(i=1;i < MAX_JNI_ENV;i++) {
    s = &bda_state_info_list[i];
    if (s->env == NULL) {
      break;
    }
  }
  s->env = env;
  s->name[0] = '\0';
  s->mode = JVM;
  s->head_c2j = NULL;
  s->transitions_in_stack = 0;

  bda_thread_context_init(s);
  bda_state_num++;
  return i;
}

void bda_state_free(bda_state_id bid)
{
  struct bda_state_info* s;

  assert( (bid >= 1) && (bid < MAX_JNI_ENV) );

  s = &bda_state_info_list[bid];

  bda_thread_context_destroy(s);
  assert(s->env != NULL);
  s->env = NULL;
  bda_state_num--;
}

struct bda_state_info * bda_state_find(JNIEnv *env)
{
  int i;

  for(i=1;i < MAX_JNI_ENV;i++) {
    struct bda_state_info* s = &bda_state_info_list[i];
    if (s->env == env) {
      return s;
    }
  }
  return NULL;
}

struct bda_state_info * bda_state_get(bda_state_id bid)
{
  assert( (bid >= 1) && (bid < MAX_JNI_ENV) );

  return  &bda_state_info_list[bid];
}

struct bda_state_info* bda_get_state_info(JNIEnv* env)
{
  jvmtiError err;
  bda_state_id bid;
  struct bda_state_info * s;

  if (bda_jvmti != NULL) {
      err = (*bda_jvmti)->GetThreadLocalStorage(bda_jvmti, NULL, (void**)&bid);
      assert(err == JVMTI_ERROR_NONE);
      if (bid == 0 ) {
          s = NULL;
      } else {
          s = bda_state_get(bid);
      }
  } else {
      s = bda_state_find(env);
  }
  return s;
}

int bda_get_current_transition_count()
{
  bda_state_id bid;
  struct bda_state_info * s;
  jvmtiError err;

  assert(bda_jvmti != NULL);
  err = (*bda_jvmti)->GetThreadLocalStorage(bda_jvmti, NULL, (void**)&bid);
  assert(err == JVMTI_ERROR_NONE);

  s = bda_state_get(bid);
  return s->transitions_in_stack;
}

void reset_transition_breakpoints()
{
  bda_j2c_call_breakpoint = 0;
  bda_j2c_return_breakpoint = 0;
  bda_c2j_call_breakpoint = 0;
  bda_c2j_return_breakpoint = 0;
}

static int is_transition_count_ok(int count)
{
  return ( bda_transition_count == 0) || (bda_transition_count == count);
}

void bda_state_j2c_call(struct bda_state_info * s, struct bda_j2c_info * j2c_info)
{
  s->mode = j2c_info->is_user_method ? USR_NATIVE:SYS_NATIVE;
  s->transitions_in_stack++;

  if (bda_j2c_call_breakpoint && (bda_jvmti != NULL)
      && is_transition_count_ok(s->transitions_in_stack)) {
    struct bda_location loc;
    bda_init_native_location(&loc, j2c_info->native_method_address);
    bda_cbp(J2C_JNI_CALL, s, &loc);
  }
}

void bda_state_j2c_return(struct bda_state_info * s, struct bda_j2c_info * j2c_info)
{
  if (bda_j2c_return_breakpoint && (bda_jvmti != NULL)
      && is_transition_count_ok(s->transitions_in_stack)) {
    struct java_source_location jloc;
    struct bda_location loc;
    bda_obtain_j2c_return_source_location(bda_jvmti, &jloc);
    bda_init_java_location(&loc, &jloc);
    bda_cbp(J2C_JNI_RETURN, s, &loc);
  }

  s->mode = JVM;
  s->transitions_in_stack--;
}

void bda_state_c2j_call(struct bda_state_info * s, struct bda_c2j_info * c2j_info)
{
  assert(s != NULL && c2j_info->caller_fp != NULL && c2j_info->return_addr != NULL);

  /* push the recent c2j. */
  if (!c2j_info->jdwp_context) {
    c2j_info->prev = s->head_c2j;
    s->head_c2j = c2j_info;
    s->transitions_in_stack++;
  }

  if (bda_c2j_call_breakpoint && bda_jvmti && !c2j_info->jdwp_context
      && (c2j_info->call_type != JNI_CALL_NOT_CLASSIFIED)
      && is_transition_count_ok(s->transitions_in_stack)) {
    struct java_source_location jloc;
    struct bda_location loc;
        
    bda_get_c2j_target(s, c2j_info, &jloc);
    bda_init_java_location(&loc, &jloc);
    bda_cbp(C2J_JNI_CALL, s, &loc);
  }
  s->mode = JVM;
}

void bda_state_c2j_return(struct bda_state_info * s, struct bda_c2j_info * c2j_info)
{
  assert (s != NULL && c2j_info != NULL);
  assert (c2j_info->jdwp_context || s->head_c2j == c2j_info);

  if (bda_c2j_return_breakpoint && bda_jvmti && !c2j_info->jdwp_context
      && (c2j_info->call_type != JNI_CALL_NOT_CLASSIFIED)
      && is_transition_count_ok(s->transitions_in_stack)) {
    struct java_source_location jloc;
    struct bda_location loc;

    bda_init_java_location(&loc, &jloc);
    bda_cbp(C2J_JNI_RETURN, s, &loc);
  }
    
  /* pop the recent c2j. */
  if (!c2j_info->jdwp_context) {
    s->head_c2j = s->head_c2j->prev;
    s->transitions_in_stack--;
  }
}

int bda_state_get_transition_count(JNIEnv* env)
{
  struct bda_state_info* s = bda_state_find(env);
  if (s == NULL) {
      return -1;
  } else {
      return s->transitions_in_stack;
  }
}
