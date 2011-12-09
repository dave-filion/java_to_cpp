static int check_part[]={
#ifdef A
1,
#elif defined B
1.5,
#endif
#ifdef C
2,
#elif defined D
2.5,
#endif
#ifdef E
3,
#endif
/* 12 more conditional array entries... */
NULL
};

