#ifdef CONFIG_A
#define file hoist_stringification1.c
#else
#define file hoist_stringification2.c
#endif

#include file
