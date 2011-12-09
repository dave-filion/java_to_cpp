static int check_part[]={
#ifdef A
1,
#elif defined B
2,
#endif
#ifdef C
3,
#elif defined D
4,
#endif
NULL
};

