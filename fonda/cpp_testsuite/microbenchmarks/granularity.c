//test the granularity of the configuration-preserving C parser
#ifdef A
#define macro 1
#else
#define macro 2
#endif

int x = macro;

int y =
#ifdef B
100
#else
101
#endif
;

int z =
#ifdef C
200;
#else
201;
#endif

