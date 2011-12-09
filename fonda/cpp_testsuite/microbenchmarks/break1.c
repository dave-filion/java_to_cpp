int main() {

  int a = 0 +

#ifdef CONFIG_A
1 +
#endif

#ifdef CONFIG_B
2 +
#endif

#ifdef CONFIG_C
3 +
#endif

#ifdef CONFIG_D
4 +
#endif

#ifdef CONFIG_E
5 +
#endif




#ifdef CONFIG_F
#endif

#ifdef CONFIG_G
#endif


6

#ifdef CONFIG_H
    + 7;
#else
  ;
#endif













}
