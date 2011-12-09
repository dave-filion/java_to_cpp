#ifndef _AGENT_H_
#define _AGENT_H_

/* Java Agent classes */
#define AGENT_CLASS_NAME "xtc/lang/blink/agent/Agent"
#define AGENT_VARIABLE_CLASS_NAME "xtc/lang/blink/agent/AgentVariable"
#define AGENT_JNI_ASSERTION_FAILURE_CLASS_NAME "xtc/lang/blink/agent/JNIAssertionFailure"

/* The internal break point type. */
typedef enum {
    J2C_DEBUGGER, JNI_WARNING,
    J2C_JNI_CALL, J2C_JNI_RETURN,
    C2J_JNI_CALL, C2J_JNI_RETURN,
} breakpoint_type;

/* Java or native program location. */
struct bda_location {
    const void * native_address; /* native code address */
    const char * cname; /* Java class name */
    const char * mname; /* Java method name */
    const char * mdesc; /* Java descriptor name */
    int line_number; /* line number */
    int bcindex; /* bytecode index */
};

/* The Blink internal break point. */
extern void bda_cbp(
    breakpoint_type bptype,
    struct bda_state_info * state,
    struct bda_location * target);

extern void bda_check_handle_assertion_fail(
    struct bda_state_info * state, 
    jthrowable pending_exception, 
    const char* fmt, ...);


/* If set to be 1, activate the j2c_call breakpoint.*/
extern int bda_j2c_call_breakpoint;

/* If set to be 1, activate the j2c_return breakpoint. */
extern int bda_j2c_return_breakpoint;

/* If set to be 1, activate the c2j_call breakpoint.*/
extern int bda_c2j_call_breakpoint;

/* If set to be 1, activate the c2j_return breakpoint.*/
extern int bda_c2j_return_breakpoint;

/* If set to be nonzeoro, the [j2c|c2j]_[call|return] event is
   triggered only when the number of language transitions on the call
   stack matches this transition count.*/
extern int bda_transition_count;

/* The beginning and ending of the JDWP agent memory region. */
extern void* bda_jdwp_region_begin;
extern void* bda_jdwp_region_end;

extern void bda_agent_init(JNIEnv *env); 

/* Ensure the JNI environment identifier at a breakpoint. */
extern JNIEnv* bda_ensure_jnienv();

/* Trigger c2j transition. */
extern int bda_c2j();

/* call to dummy. */
extern int bda_dummy_java();

extern int bda_dummy_native();

/* inspecting the current thread state*/
extern int bda_get_current_transition_count();

/* The followings are related to the expression evaluation. */
extern int bda_set_vj_from_cexpr_jboolean(JNIEnv *env, jboolean exprValue);
extern int bda_set_vj_from_cexpr_jint(JNIEnv *env, jint exprValue);
extern int bda_set_vj_from_cexpr_jdouble(JNIEnv *env, jdouble exprValue);
extern int bda_set_vj_from_cexpr_jobject(JNIEnv *env, jobject exprValue);

extern jboolean bda_get_cvalue_from_vj_jboolean(JNIEnv *env, int vjid);
extern jint bda_get_cvalue_from_vj_jint(JNIEnv *env, int vjid);
extern jdouble bda_get_cvalue_from_vj_jdouble(JNIEnv *env, int vjid);
extern jobject bda_get_cvalue_from_vj_jobject(JNIEnv *env, int vjid);

#endif /* _AGENT_H_ */
