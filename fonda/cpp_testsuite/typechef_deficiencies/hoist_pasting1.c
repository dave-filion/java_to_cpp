#define F(x) ID_ ## x

int F(
#ifdef CONFIG_A
1
#else
2
#endif
  );
