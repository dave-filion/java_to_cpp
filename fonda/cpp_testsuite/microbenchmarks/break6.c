#ifdef CONFIG_A
int x;
#endif

#ifdef CONFIG_B
int y;
#endif

#ifdef CONFIG_C
int z;
#endif

#ifdef CONFIG_D
int a;
#endif


typedef struct {
#ifdef CONFIG_E
  char t;
#endif

#ifdef CONFIG_F
  char u;
#endif

#ifdef CONFIG_G
  struct {
    
#ifdef CONFIG_I
    int inner1;
#endif

#ifdef CONFIG_J
    int inner2;
#endif

#ifdef CONFIG_K
    int inner3;
#endif

    int none;
  } v;
#endif

#ifdef CONFIG_H
  char w;
#endif

  char none;

} bob;
