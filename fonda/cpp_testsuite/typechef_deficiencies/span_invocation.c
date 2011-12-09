#ifdef A
#define cpu _cpu
#endif

#define _cpu(x) 1 * x

int x = cpu(2);
