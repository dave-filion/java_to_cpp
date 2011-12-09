#include <assert.h>
#include <string.h>
#include <jni.h>
#include <jvmti.h>

#include "agent_main.h"
#include "common.h"
#include "agent.h"

void bda_cdesc2cname(const char * cdesc, char * cname, int max_cname_size)
{
    int i,j;
    assert(cdesc[0] == 'L');
    for(i=1,j=0; cdesc[i] != '\0' && cdesc[i] != ';' && i < MAX_CLASS_NAME;i++,j++) {
        if (cdesc[i] == '/') {
            cname[j] = '.';
        } else {
            cname[j] = cdesc[i];
        }
    }
    cname[j] = '\0';
}

void bda_strcpyn(const char * src, char * dst, int max_dst_size)
{
    int i;
    for(i=0;i < (max_dst_size-1) && src[i] != '\0';i++) {
        dst[i]=src[i];
    }
    dst[i] = '\0';
}

static int bda_str_equal(const char *s1, const char *s2) 
{
    while(*s1 == *s2 && *s1 != '\0' && *s2 != '\0' ) {
        s1++;
        s2++;
    }
    return *s1 == '\0' && *s2 == '\0';
}

static int bda_location_to_line_number(jvmtiEnv* jvmti, 
                                       jmethodID method,
                                       jlocation jloc)
{
    jvmtiError err;
    jvmtiLineNumberEntry* line_number_table;
    jint num_entries_line_number_table;
    int line_number, i;

    //now get the line number
    err = (*jvmti)->GetLineNumberTable(jvmti, method, 
                                       &num_entries_line_number_table,
                                       &line_number_table);
    if (err == JVMTI_ERROR_ABSENT_INFORMATION) {
        return 0;
    }
    assert(err == JVMTI_ERROR_NONE);
    for(i=0;i < num_entries_line_number_table;i++) {
        jlocation start_loc = line_number_table[i].start_location;
        if (start_loc > jloc) {
            break;
        }
    }
    assert(i>=1);
    line_number = line_number_table[i-1].line_number;

    err = (*jvmti)->Deallocate(jvmti, (unsigned char*)line_number_table);
    assert(err == JVMTI_ERROR_NONE);

    return line_number;
}

int bda_method_name_and_desc_match(jvmtiEnv *jvmti, jmethodID m1, jmethodID m2)
{
    jvmtiError err;
    char *mname1, *mdesc1;
    char *mname2, *mdesc2;

    err = (*jvmti)->GetMethodName(jvmti, m1, &mname1, &mdesc1, NULL);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->GetMethodName(jvmti, m2, &mname2, &mdesc2, NULL);
    assert(err == JVMTI_ERROR_NONE);

    err = (*jvmti)->Deallocate(jvmti, (unsigned char *) mname1);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->Deallocate(jvmti, (unsigned char *) mdesc1);
    assert(err == JVMTI_ERROR_NONE);

    err = (*jvmti)->Deallocate(jvmti, (unsigned char *) mname2);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->Deallocate(jvmti, (unsigned char *) mdesc2);
    assert(err == JVMTI_ERROR_NONE);
    
    return bda_str_equal(mname1, mname2) && bda_str_equal(mdesc1, mdesc2);
}

void bda_set_java_location(jvmtiEnv *jvmti, jmethodID mid, 
                           struct java_source_location * jloc) 
{
    jvmtiError err;
    char *cdesc, *mname, *mdesc;
    jclass clazz;
    jlocation start, end;

    err = (*jvmti)->GetMethodDeclaringClass(jvmti, mid, &clazz);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->GetClassSignature(jvmti, clazz, &cdesc, NULL);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->GetMethodName(jvmti, mid, &mname, &mdesc, NULL);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->GetMethodLocation(jvmti, mid, &start, &end);
    assert(err == JVMTI_ERROR_NONE);

    bda_cdesc2cname(cdesc, jloc->cname, sizeof(jloc->cname));
    bda_strcpyn(mname, jloc->mname, sizeof(jloc->mname));
    bda_strcpyn(mdesc, jloc->mdesc, sizeof(jloc->mdesc));
    jloc->bcindex = 0;
    jloc->line_number = bda_location_to_line_number(jvmti, mid, start);
    
    err = (*jvmti)->Deallocate(jvmti, (unsigned char *) cdesc);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->Deallocate(jvmti,(unsigned char *) mname);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->Deallocate(jvmti, (unsigned char *) mdesc);
    assert(err == JVMTI_ERROR_NONE);
}

#define OPCODE_INVOKE_VIRTUAL (0xb6) 
#define OPCODE_INVOKE_SPECIAL (0xb7)
#define OPCODE_INVOKE_STATIC  (0xb8)
#define OPCODE_INVOKE_INTERFACE (0xb9)

static void bda_get_return_address_from_callsite(jvmtiEnv *jvmti, 
                                                 jmethodID method, 
                                                 jlocation bcindex_call,
                                                 jlocation *bcindex_return,
                                                 int *line_number)
{
    jvmtiError err;
    jlocation bcindex_start, bcindex_end;
    unsigned char * bytecodes;
    unsigned int bcode;
    jint bytecodes_size;

    err = (*jvmti)->GetMethodLocation(jvmti, method, &bcindex_start, &bcindex_end);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->GetBytecodes(jvmti, method, &bytecodes_size, &bytecodes);
    assert(err == JVMTI_ERROR_NONE);
    //now decode the byte code to get the next line number.
    bcode = bytecodes[bcindex_call-bcindex_start];
    switch(bcode) {
    case OPCODE_INVOKE_VIRTUAL:
    case OPCODE_INVOKE_SPECIAL:
    case OPCODE_INVOKE_STATIC:
        *bcindex_return = bcindex_call + 3;
        break;
    case OPCODE_INVOKE_INTERFACE:
        *bcindex_return = bcindex_call + 5;
        break;
    default:
        assert(0); /*not call instruct at the call site?.*/
        break;
    }

    *line_number = bda_location_to_line_number(jvmti, method, *bcindex_return);

}

void bda_obtain_j2c_return_source_location(jvmtiEnv *jvmti,
                                           struct java_source_location * loc)
{
    jvmtiError err;

    jvmtiFrameInfo frames[5];
    jint fcount; 

    jmethodID method;
    jlocation bcindex_call, bcindex_return;
    int line_number;
    int i;

    //find the most recent java frame.
    assert(jvmti != NULL);
    err = (*jvmti)->GetStackTrace(jvmti, NULL, 0, 5, frames, &fcount);
    assert(err == JVMTI_ERROR_NONE);
    method = NULL;
    for(i=0; i < fcount;i++) {
        jboolean is_native;
        err = (*jvmti)->IsMethodNative(jvmti, frames[i].method, &is_native);
        assert(err == JVMTI_ERROR_NONE);
        if (!is_native) {
            method = frames[i].method;
            bcindex_call = frames[i].location;
            break;
        }
    }
    assert(method != NULL);

    bda_set_java_location(jvmti, method, loc);
    bda_get_return_address_from_callsite(jvmti, method, bcindex_call, 
                                         &bcindex_return, &line_number);
    loc->bcindex = bcindex_return;
    loc->line_number = line_number;
}


int bda_is_in_jdwp_region(void* address)
{
    
    return 
        ((bda_jdwp_region_begin != NULL) && 
        (bda_jdwp_region_end != NULL) &&
        (address >= bda_jdwp_region_begin) &&
            (address < bda_jdwp_region_end));
}

void bda_print_method_info(jmethodID mid)
{
    jvmtiError err;
    char *mname1, *mdesc1;
    jvmtiEnv* jvmti = bda_jvmti;
    if (jvmti == NULL) {
        printf("agent is not initialized\n");
    }
    err = (*jvmti)->GetMethodName(jvmti, mid, &mname1, &mdesc1, NULL);
    assert(err == JVMTI_ERROR_NONE);
    
    printf("name = %s and desc = %s\n", mname1, mdesc1);

    err = (*jvmti)->Deallocate(jvmti, (unsigned char *) mname1);
    assert(err == JVMTI_ERROR_NONE);
    err = (*jvmti)->Deallocate(jvmti, (unsigned char *) mdesc1);
    assert(err == JVMTI_ERROR_NONE);    
}

void bda_init_state(struct bda_state_info *state, const char * fmt, ...)
{
    va_list ap;

    va_start(ap, fmt);
    vsnprintf(state->msgbuf, sizeof(state->msgbuf), fmt, ap);
    va_end(ap);
    puts(state->msgbuf);
}

void bda_init_native_location(struct bda_location *loc, const void* address)
{
    loc->native_address = address;
    loc->cname = loc->mname = loc->mdesc = NULL;
    loc->line_number = loc->bcindex = 0;
}

void bda_init_java_location(struct bda_location *loc, struct java_source_location *jloc)
{
    loc->native_address = 0;
    loc->cname = jloc->cname;
    loc->mname = jloc->mname;
    loc->mdesc = jloc->mdesc;
    loc->line_number = jloc->line_number;
    loc->bcindex = jloc->bcindex;
}



void bda_parse_method_descriptor(const char * desc, void * result, bda_pt_mdesc_callback cb) 
{
  int i, mdesc_len, mdesc_begin_ref;
  int order = 0;

  enum STATE {NONE, ARGUMENTS, ARGUMENT_ARRAY, ARGUMENT_REF, RETURN, } state = NONE;

  mdesc_begin_ref = -1;
  mdesc_len=strlen(desc);
  state = NONE;
  for(i=0; i < mdesc_len;i++) {
      char c = desc[i];
      switch(state) {
      case NONE:
          if(c == '(') {state=ARGUMENTS;}
          else {assert(0);}
          break;
      case ARGUMENTS:
          switch(c) {
          case 'B': case 'C': case 'I':  case 'S':case 'Z':
          case 'F':
              (*cb)(desc, result, order++, i, i, 1, 1);
              break;
          case 'D':
          case 'J' :
              (*cb)(desc, result, order++, i, i, 2, 1);
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
              (*cb)(desc, result, order++, mdesc_begin_ref, i, 1, 0);
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
              (*cb)(desc, result, order++, mdesc_begin_ref, i, 1, 0);
              mdesc_begin_ref = -1;
              state = ARGUMENTS;
          }
          else {}
          break;
      case RETURN:
          switch(c) {
          case 'V':
              (*cb)(desc, result, -1, i, i, 0, 1);
              goto FINISH;
          case 'B': case 'C': case 'I':  case 'S':case 'Z':
          case 'F':
              (*cb)(desc, result, -1, i, i, 1, 1);
              goto FINISH;
          case 'D':
          case 'J' :
              (*cb)(desc, result, -1, i, i, 2, 1);
              goto FINISH;
          case 'L':
          case '[':
              (*cb)(desc, result, -1, i, mdesc_len-1, 1, 0);
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
  return;
}

const char * bda_cstr_jclass(JNIEnv *env, jclass clazz)
{
  jvmtiError err;
  char * cdesc;
  static char buf[1024];

  err = (*bda_jvmti)->GetClassSignature(bda_jvmti, clazz, &cdesc, NULL);
  assert(err == JVMTI_ERROR_NONE);

  strncpy(buf, cdesc, 1024);
  buf[1023] = '\0';

  err = (*bda_jvmti)->Deallocate(bda_jvmti, (unsigned char *)cdesc);
  assert(err == JVMTI_ERROR_NONE);
  return buf;
}
