#ifndef _OPTIONS_H_
#define _OPTIONS_H_

#include "state.h"

struct agent_options {
  int mcount;      /* Turn on reporting statistics on language transitions. */
  int jniassert;   /* Turn on Jinn error checking. */
  int bia;         /* Turn on the Blink intermediate agent. */
  int nointerpose; /* Turn off the transition interposition. */
};

extern struct agent_options agent_options;

extern void agent_parse_options(const char * options);

#endif /* _OPTIONS_H_ */
