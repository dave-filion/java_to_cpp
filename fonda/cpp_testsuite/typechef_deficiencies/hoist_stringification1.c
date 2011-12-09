#define _str(x) # x

char *s = _str(
#ifdef CONFIG_A
1
#else
2
#endif
  );
