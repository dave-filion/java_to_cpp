#include <assert.h>
#include <stdarg.h>
#include <jni.h>
#include <jvmti.h>
#include <classfile_constants.h>

#include "agent_main.h"
#include "java_method.h"
#include "common.h"

static bda_jmethod_id next_method_id = 0;
static struct bda_java_method_info bda_java_methods[MAX_JAVA_METHODS];

static void bda_jmethod_add_argument(struct bda_java_method_info * minfo,
                                     int n_words, int begin_mdesc, int end_mdesc);

static void bda_jmethod_add_return(struct bda_java_method_info * minfo,
                                    int n_words, int begin_mdesc, int end_mdesc);

static const struct bda_java_method_info * bda_jmethod_lookup(JNIEnv *env, jmethodID mid);
static const struct bda_java_method_info * bda_jmethod_ensure_from_jvmti(JNIEnv *env, jmethodID mid);


bda_jmethod_id bda_jmethod_info_init(jmethodID mid, int is_static,
                                     const char *cdesc, const char *mname, const char * mdesc)
{
  enum STATE {NONE, ARGUMENTS, ARGUMENT_ARRAY, ARGUMENT_REF, RETURN, } state = NONE;
  int i, mdesc_len, mdesc_begin_ref, buf_len;
  struct bda_java_method_info * minfo;
  bda_jmethod_id id;

  id = next_method_id++; /* No synchronization here is OK? */
  assert(id < MAX_JAVA_METHODS);
  minfo = &(bda_java_methods[id]);

  minfo->mid = mid;

  /* class name */
  buf_len = 0;
  minfo->fullname = minfo->buf + buf_len;
  assert(cdesc[0] == 'L');
  for(i=1;cdesc[i] != '\0' && (buf_len+1) < sizeof(minfo->buf);i++) {
    char c = cdesc[i];
    if (c == ';') {break;}
    else if (c == '/') {c = '.';}
    minfo->buf[buf_len++] = c;
  }
  assert(cdesc[i]== ';' && cdesc[i+1] == '\0');

  /* method name */
  if ((buf_len+1) < sizeof(minfo->buf)) {
    minfo->buf[buf_len++] = '.';
  }
  for(i=0;mname[i] != '\0' && (buf_len+1) < sizeof(minfo->buf);i++) {
    minfo->buf[buf_len++] = mname[i];
  }
  minfo->buf[buf_len++] = '\0';

  /* method descritor */
  mdesc_len=0;
  minfo->mdesc = minfo->buf + buf_len;
  for(i=0;mdesc[i] != '\0' && (buf_len+1) < sizeof(minfo->buf);i++) {
    minfo->buf[buf_len++] = mdesc[i];
    mdesc_len++;
  }
  assert(buf_len < sizeof(minfo->buf));
  minfo->buf[buf_len] = '\0';

  /* scan the descritor.*/
  mdesc_begin_ref = -1;
  state = NONE;
  for(i=0; i < mdesc_len;i++) {
      char c = minfo->mdesc[i];
      switch(state) {
      case NONE:
          if(c == '(') {state=ARGUMENTS;}
          else {assert(0);}
          break;
      case ARGUMENTS:
          switch(c) {
          case 'B': case 'C': case 'I':  case 'S':case 'Z':
          case 'F':
              bda_jmethod_add_argument(minfo, 1, i, i);
              break;
          case 'D':
          case 'J' :
              bda_jmethod_add_argument(minfo, 2, i, i);
              break;
          case 'L':
              assert(mdesc_begin_ref == -1);
              mdesc_begin_ref = i;
              state = ARGUMENT_REF;
              break;
          case '[':
              assert(mdesc_begin_ref == -1);
              mdesc_begin_ref = i;
              state = ARGUMENT_ARRAY;
              break;
          case ')':
              state = RETURN;
          }
          break;
      case ARGUMENT_ARRAY:
          switch(c) {
          case 'B': case 'C': case 'F': case 'I':  case 'S':case 'Z':
          case 'D': case 'J':
              bda_jmethod_add_argument(minfo, 1, mdesc_begin_ref, i);
              mdesc_begin_ref = -1;
              state = ARGUMENTS;
              break;
          case '[': break;
          case 'L': state = ARGUMENT_REF;break;
          default: assert(0);
              break;
          }
          break;
      case ARGUMENT_REF:
          if (c == ';') {
              bda_jmethod_add_argument(minfo, 1, mdesc_begin_ref, i);
              mdesc_begin_ref = -1;
              state = ARGUMENTS;
          }
          else {}
          break;
      case RETURN:
          switch(c) {
          case 'V':
              bda_jmethod_add_return(minfo, 0, i, i);
              goto FINISH;
          case 'B': case 'C': case 'I':  case 'S':case 'Z':
          case 'F':
              bda_jmethod_add_return(minfo, 1, i, i);
              goto FINISH;
          case 'D':
          case 'J' :
              bda_jmethod_add_return(minfo, 2, i, i);
              goto FINISH;
          case 'L':
          case '[':
              bda_jmethod_add_return(minfo, 1, i, mdesc_len-1);
              goto FINISH;
          default: assert(0);
              break;
          }
          break;
      default:
          assert(0);
      }
  }
FINISH:
  return id;
}

extern bda_jmethod_id bda_jmethod_info_add(JNIEnv *env,
                                           jmethodID mid,
                                           int is_static,
                                           const char * mname,
                                           const char * mdesc)
{
  jvmtiError err;
  char *cdesc;
  jclass clazz;
  bda_jmethod_id id;

  assert(bda_jvmti != NULL);
  err = (*bda_jvmti)->GetMethodDeclaringClass(bda_jvmti, mid, &clazz);
  assert(err == JVMTI_ERROR_NONE);
  err = (*bda_jvmti)->GetClassSignature(bda_jvmti, clazz, &cdesc, NULL);
  assert(err == JVMTI_ERROR_NONE);
  bda_orig_jni_funcs->DeleteLocalRef(env, clazz);

  id = bda_jmethod_info_init(mid, is_static, cdesc, mname, mdesc);

  err = (*bda_jvmti)->Deallocate(bda_jvmti, (unsigned char *) cdesc);
  assert(err == JVMTI_ERROR_NONE);

  return id;
}

static void bda_jmethod_add_argument(struct bda_java_method_info * minfo, 
                                     int n_words, int begin_mdesc, int end_mdesc)
{
  struct bda_java_argument_info * arg_info;
  assert( minfo->num_arguments < (MAX_METHOD_ARGUMENTS -1));

  arg_info = &(minfo->arguments[minfo->num_arguments]);
  arg_info->n_words = n_words;
  arg_info->type = minfo->mdesc[begin_mdesc];
  arg_info->mdesc_begin = begin_mdesc;
  arg_info->mdesc_end = end_mdesc;

  minfo->num_arguments += 1;
}

const struct bda_java_method_info * bda_jmethod_ensure(bda_jmethod_id id)
{
    return & bda_java_methods[id];
}

static void bda_jmethod_add_return(struct bda_java_method_info * minfo,
                                   int n_words, int begin_mdesc, int end_mdesc)
{
  struct bda_java_argument_info * return_info;

  return_info = &minfo->returnInfo;
  return_info->n_words = n_words;
  return_info->type = minfo->mdesc[begin_mdesc];
  return_info->mdesc_begin = begin_mdesc;
  return_info->mdesc_end = end_mdesc;
}

const struct bda_java_method_info * bda_jmethod_find(jmethodID mid)
{
  int num = next_method_id;
  int i;
  
  for(i=0;i < num;i++) {
    if (bda_java_methods[i].mid == mid) {
        return &(bda_java_methods[i]);
    }
  }

  return NULL;
}

static const struct bda_java_method_info * bda_jmethod_lookup(JNIEnv *env, jmethodID mid)
{
  int num = next_method_id;
  int i;
  
  for(i=0;i < num;i++) {
    if (bda_java_methods[i].mid == mid) {
        return &(bda_java_methods[i]);
    }
  }

  return bda_jmethod_ensure_from_jvmti(env, mid);
}

static const struct bda_java_method_info * bda_jmethod_ensure_from_jvmti(JNIEnv *env, jmethodID mid)
{
  jvmtiError err;
  char *cdesc, *mname, *mdesc;
  jclass clazz;
  int modifier;
  const struct bda_java_method_info * minfo;
  bda_jmethod_id id;

  assert(bda_jvmti != NULL);
  err = (*bda_jvmti)->GetMethodDeclaringClass(bda_jvmti, mid, &clazz);
  assert(err == JVMTI_ERROR_NONE);
  err = (*bda_jvmti)->GetClassSignature(bda_jvmti, clazz, &cdesc, NULL);
  assert(err == JVMTI_ERROR_NONE);
  bda_orig_jni_funcs->DeleteLocalRef(env, clazz);
  err = (*bda_jvmti)->GetMethodName(bda_jvmti, mid, &mname, &mdesc, NULL);
  assert(err == JVMTI_ERROR_NONE);
  err = (*bda_jvmti)->GetMethodModifiers(bda_jvmti, mid, &modifier);
  assert(err == JVMTI_ERROR_NONE);

  id = bda_jmethod_info_init(mid, modifier & JVM_ACC_STATIC, cdesc, mname, mdesc);
  assert(id < MAX_JAVA_METHODS);
  minfo = & bda_java_methods[id];

  err = (*bda_jvmti)->Deallocate(bda_jvmti, (unsigned char *) cdesc);
  assert(err == JVMTI_ERROR_NONE);
  err = (*bda_jvmti)->Deallocate(bda_jvmti,(unsigned char *) mname);
  assert(err == JVMTI_ERROR_NONE);
  err = (*bda_jvmti)->Deallocate(bda_jvmti, (unsigned char *) mdesc);
  assert(err == JVMTI_ERROR_NONE);

  return minfo;
}

const char* bda_jmethod_primitive_name(char c)
{
  switch(c) {
  case 'Z': return "jboolean";
  case 'B': return "jbyte";
  case 'C': return "jchar";
  case 'I': return "jint";
  case 'S': return "jshort";
  case 'J': return "jlong";
  case 'F': return "jfloat";
  case 'D': return "jdouble";
  default:
    assert(0);
    return "";
  }
}

static void bda_print_argument(char type, const jvalue value)
{
    switch(type) {
    case 'Z':
        printf(" %d(jboolean)", value.z);
        break;
    case 'B':
        printf(" %d(jbyte)", value.b);
        break;
    case 'C':
        printf(" %d(jchar)", value.c);
        break;
    case 'S':
        printf(" %d(jshort)", value.s);
        break;
    case 'I':
        printf(" %d(jint)", value.i);
        break;
    case 'J':
        printf(" %lld(jlong)", value.j);
        break;
    case 'F':
        printf(" %f(jfloat)", value.f);
        break;
    case 'D':
        printf(" %lf(jdouble)", value.d);
        break;
    case '[':
    case 'L':
        printf(" %p(jobject)", value.l);
        break;
    default:
        break;
    }
}

void bda_jmethod_print_argument_from_va_list(JNIEnv *env, jmethodID mid, va_list args)
{
  const struct bda_java_method_info * minfo;
  int i;

  if (args == NULL) {return;}
  minfo = bda_jmethod_lookup(env, mid);
  if (minfo == NULL) {return;}
  printf("%60s %s", "", minfo->fullname);
  for(i=0; i < minfo->num_arguments;i++) {
    const struct bda_java_argument_info  * ainfo = &(minfo->arguments[i]);
    jvalue val;
    switch(ainfo->type) {
    case 'Z':
        val.z = va_arg(args, int);
        break;
    case 'B':
        val.b = va_arg(args, int);
        break;
    case 'C':
        val.c = va_arg(args, int);
        break;
    case 'S':
        val.s = va_arg(args, int);
        break;
    case 'I':
        val.i = va_arg(args, int);
        break;
    case 'J':
        val.j = va_arg(args, jlong);
        break;
    case 'F':
        val.f = va_arg(args, double);
        break;
    case 'D':
        val.d = va_arg(args, jdouble);
        break;
    case '[':
    case 'L':
        val.l = va_arg(args, jobject);
        break;
    default:
        assert(0);
        break;
    }
    bda_print_argument(ainfo->type, val);
  }
  va_end(args);
  printf("\n");
}

void bda_jmethod_print_argument_from_array(JNIEnv *env, jmethodID mid, const jvalue * args)
{
  const struct bda_java_method_info * minfo;
  int i;
  
  if (args == NULL) {return;}
  minfo = bda_jmethod_lookup(env, mid);
  printf("%60s %s", "", minfo->fullname);
  for(i=0; i < minfo->num_arguments;i++) {
    const struct bda_java_argument_info * ainfo = &(minfo->arguments[i]);
    bda_print_argument(ainfo->type, args[i]);
  }
  printf("\n");
}

