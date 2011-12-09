int f(


#ifdef CONFIG_A
int
#endif

#ifdef CONFIG_B
x
#endif

#ifdef CONFIG_A
,
#endif

#ifdef CONFIG_B
int
#endif

#ifdef CONFIG_A
y
#endif

#ifdef CONFIG_B
,
#endif

#ifdef CONFIG_A
int
#endif

#ifdef CONFIG_B
z
#endif


      );
