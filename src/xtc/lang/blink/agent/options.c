#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "options.h"

struct agent_options agent_options =
{
    0, /* mcount */
    1, /* jnissert */
    0, /* bia */
    0, /* nointerpose */
};

struct agent_option_key_value {
  const char * name;
  int name_len;
  const char * value;
  int value_len;
};

static void agent_usage(const char * reason)
{
  if (reason) {
    printf("can not understand: %s\n", reason);
  }
  printf(
    "    Dynamic Error Detector for JNI\n"
    "usage: java -agentlib:jinn=[help]|[<option>=<value>, ...]\n"
    "Options\n"
    "mcount=y|n        n  Report the execution counts for Java native methods and JNI functions.\n"
    "jniassert=y|n     y  Throw JNI assertion failure error.\n"
    "bia=y|n           n  Turn on Blink intermediate agent.\n"
    "nointerpose=y|n   n  Do not interpose transitions for experimental measurement.\n"
    );
  exit(0);
}

static int agent_strcmp(const char * s1, int s1_len, const char* s2)
{
  return (s1_len == strlen(s2)) && (strncmp(s1, s2, s1_len) == 0);
}

static int agent_handle_boolean_option(const char *opt_name, int *value,
                                       struct agent_option_key_value * opt,
                                       const char *usage)
{
  if ( (opt->name_len != strlen(opt_name)) || !agent_strcmp(opt->name, opt->name_len, opt_name))  {
    return 0;
  }
  if (agent_strcmp(opt->value, opt->value_len, "y")) {
    *value = 1;
  } else if (agent_strcmp(opt->value, opt->value_len, "n")) {
    *value = 0;
  } else {
    agent_usage(usage);
  }
  return 1;
}

static void agent_handle_option(struct agent_option_key_value * opt)
{
  if (agent_handle_boolean_option("mcount", &agent_options.mcount, opt, "mcount takes either y or n")
      || agent_handle_boolean_option("jniassert", &agent_options.jniassert, opt, "jniassert takes either y or n")
      || agent_handle_boolean_option("bia", &agent_options.bia, opt, "bia takes either y or n")
      || agent_handle_boolean_option("nointerpose", &agent_options.nointerpose, opt, "nointerpose takes either y or n")) {
  } else {
    agent_usage("can not recognize the option");
  }
}

void agent_parse_options(const char * options)
{
  /* parse options. */
  if (options != NULL) {
    int s = 0, i;
    int name_index = 0, name_length = 0;
    int value_index, value_length;
    for(i = 0; options[i] != '\0';i++) {
      char c = options[i];
      if (!s) {
        if ( c != '=') {
          name_length++;
        } else {
          value_index = i + 1;
          value_length = 0;
          s = 1;
        }
      } else {
        if (c != ',') {
          value_length++;
        } else {
          struct agent_option_key_value opt = {
            options + name_index, name_length, 
            options + value_index, value_length};
          agent_handle_option(&opt);
          name_index = i+1;
          name_length = 0;
          s = 0;
        }
      }
    }
    if (!s) {
      if (agent_strcmp(options + name_index, name_length, "help")) {
        agent_usage(NULL);
      } else if (name_length > 0) {
        agent_usage(options);
      }
    } else {
      struct agent_option_key_value opt = {
        options + name_index, name_length, 
        options + value_index, value_length};
      agent_handle_option(&opt);
    }
  }
}
