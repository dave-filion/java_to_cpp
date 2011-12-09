#define _str(x) # x
#define str(x) _str(x)

#ifdef CONFIG_A
#define M a
#else
#define M b
#endif

char *s = str(M);
