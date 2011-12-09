#ifndef _COMMON_H_
#define _COMMON_H_

#include "state.h"
#include "agent.h"

/* Handling call stack. */
#if defined(__GNUC__)
#include <sys/types.h>
#include <unistd.h>
#include <pthread.h>

#if defined(__i386__)
#define GET_CURRENT_PROCESS_ID() getpid()
#define GET_NATIVE_THREADID() ((unsigned int)pthread_self())
#define GET_FRAME_POINTER(fp) asm("mov %%ebp, %0;": "=m"(fp));
#define GET_RETURN_ADDRESS(addr) \
  asm("movl 4(%%ebp), %%eax;movl %%eax, %0;":"=m"(addr));
#define GET_PREVIOUS_FRAME_POINTER(fp) \
  asm("movl 0(%%ebp), %%eax;movl %%eax, %0;":"=m"(fp));
#define SET_RETURN_ADDRESS(addr) \
  asm("movl %0, %%eax;movl %%eax, 4(%%ebp);"::"m"(addr));
#define SET_PREVIOUS_FRAME_POINTER(fp) \
  asm("movl %0, %%eax;movl %%eax, 0(%%ebp);"::"m"(fp));

#elif defined(__powerpc__)
#define GET_CURRENT_PROCESS_ID() getpid()
#define GET_FRAME_POINTER(fp) asm("stw %%r1, %0": "=m"(fp));
#define GET_RETURN_ADDRESS(addr) \
  asm("lwz %%r3, 0(%%r1);lwz %%r3, 4(%%r3);stw %%r3, %0":"=m"(addr));
#define GET_PREVIOUS_FRAME_POINTER(fp) asm("lwz %0, 0(%%r1);" : "=r"(fp));
#define SET_RETURN_ADDRESS(addr) \
  asm("lwz %%r3, 0(%%r1);stw %0, 4(%%r3);"::"r"(addr));
#define SET_PREVIOUS_FRAME_POINTER(fp) asm("stw %0, 0(%%r1);" :: "r"(fp));

#else
#error "x86 and ppc support only for gcc"
#endif

#elif defined(_WIN32)
#include <windows.h>
#define GET_CURRENT_PROCESS_ID() GetCurrentProcessId()
#define GET_NATIVE_THREADID() ((unsigned int)GetCurrentThreadId())
#define GET_FRAME_POINTER(fp)  _asm { _asm mov fp, ebp }
#define GET_RETURN_ADDRESS(addr) \
  _asm { _asm mov eax, [ebp + 4] _asm mov addr, eax }
#define GET_PREVIOUS_FRAME_POINTER(fp) \
  _asm { _asm mov eax, [ebp] _asm mov fp, eax }
#define SET_RETURN_ADDRESS(addr) \
  _asm { _asm mov eax, addr  _asm mov [ebp + 4], eax }
#define SET_PREVIOUS_FRAME_POINTER(fp) \
  _asm { _asm mov eax, fp  _asm mov [ebp], eax }

#else

#endif /* _GNUC_ and _WIN32 */

#define MAX_CLASS_NAME (256)
#define MAX_METHOD_NAME (256)
#define MAX_METHOD_DESCRIPTOR (1024)

struct java_source_location {
  char cname[MAX_CLASS_NAME];
  char mname[MAX_METHOD_NAME];
  char mdesc[MAX_METHOD_DESCRIPTOR];
  int line_number;
  int bcindex;
};

typedef void (*bda_pt_mdesc_callback)(
    const char * desc, void* result, int arg_order, 
    int begin_index, int end_index,
    int num_words, int is_primitive);


extern int bda_is_agent_native_method(void* address);

extern void bda_cdesc2cname(const char * cdesc, char * cname, int max_cname_size);
extern void bda_strcpyn(const char * src, char * dst, int max_dst_size);

extern void bda_get_c2j_target(struct bda_state_info *s, struct bda_c2j_info * c2j_info, 
                               struct java_source_location *jloc);

extern void bda_set_java_location(jvmtiEnv *jvmti, jmethodID mid, 
                                  struct java_source_location * jloc);

extern int bda_method_name_and_desc_match(jvmtiEnv *jvmti, jmethodID m1, jmethodID m2);

extern void bda_obtain_j2c_return_source_location(jvmtiEnv *jvmti,
                                                  struct java_source_location * loc);

extern int bda_is_in_jdwp_region(void* address);

extern void bda_init_native_location(struct bda_location *loc, const void* address);

extern void bda_init_java_location(struct bda_location *loc, 
                                   struct java_source_location *jloc);

extern void bda_parse_method_descriptor(const char * desc, void * result, bda_pt_mdesc_callback cb);

extern const char * bda_cstr_jclass(JNIEnv *env, jclass cls);

#endif

