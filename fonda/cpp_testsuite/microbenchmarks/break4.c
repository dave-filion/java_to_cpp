int f(


int a,

#ifdef CONFIG_B
int b,
#endif

#ifdef CONFIG_C
int c,
#endif

#ifdef CONFIG_A
int d
#endif
      );
