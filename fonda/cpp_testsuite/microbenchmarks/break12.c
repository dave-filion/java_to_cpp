int a[] = {

#ifdef CONFIG_A
  1,
#endif

#ifdef CONFIG_B
  2,
#endif

#ifdef CONFIG_C
  3,
#endif


#ifdef CONFIG_BOB
};

int b[] = {
#endif


#ifdef CONFIG_D
  4,
#endif

#ifdef CONFIG_E
  5,
#endif

#ifdef CONFIG_F
  6,
#endif


};
