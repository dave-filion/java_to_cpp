#ifndef _JAVA_METHOD_H_
#define _JAVA_METHOD_H_

#define MAX_METHOD_ARGUMENTS (40)
#define MAX_NAME_BUFFER_BYTES (1024)
#define MAX_JAVA_METHODS (4096)

typedef unsigned int bda_jmethod_id;

struct bda_java_argument_info {
  int n_words; /* Number of words for the argument. */
  int mdesc_begin;
  int mdesc_end;
  char type;   /* {V}:void {[,L}: reference type, {B,C,I,S,Z,J,F,D}:primitive types */
};

struct bda_java_method_info {
  jmethodID mid;

  int num_arguments;       /* Number of arguments */
  struct bda_java_argument_info arguments[MAX_METHOD_ARGUMENTS];
  struct bda_java_argument_info returnInfo;

  int  is_static;
  char buf[MAX_NAME_BUFFER_BYTES];
  const char * mdesc;
  const char * fullname;
};

extern bda_jmethod_id bda_jmethod_info_init(jmethodID mid, 
                                            int is_static,
                                            const char * cdesc, 
                                            const char * mname, 
                                            const char * mdesc);

extern bda_jmethod_id bda_jmethod_info_add(JNIEnv *env,
                                           jmethodID mid,
                                           int is_static,
                                           const char * mname,
                                           const char * mdesc);

extern const struct bda_java_method_info * bda_jmethod_ensure(bda_jmethod_id id);
extern const struct bda_java_method_info * bda_jmethod_find(jmethodID mid);

extern const char* bda_jmethod_primitive_name(char c);

extern void bda_jmethod_print_argument_from_va_list(JNIEnv *env, jmethodID mid, va_list args);
extern void bda_jmethod_print_argument_from_array(JNIEnv *env, jmethodID mid, const jvalue * args);

#endif /* _JAVA_METHOD_H_ */
