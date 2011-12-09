#define _paste(x) ID_ ## x
#define paste(x) _paste(x)

#ifdef CONFIG_A
#define M a
#else
#define M b
#endif

int paste(M);
