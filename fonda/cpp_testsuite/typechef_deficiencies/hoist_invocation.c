#ifdef CONFIG_A
#define F(a, b) a + b
#endif

#ifdef CONFIG_B
#define F(x) # x
#endif

F(
1
#ifdef CONFIG_A
,
#endif
2
  );

