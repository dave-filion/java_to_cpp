static int check_part[]={
#ifdef WRAP
  {
#endif
#ifdef CONFIG_ACORN_PARTITION_ICS
1,
#elif defined A
1.5,
#endif
#ifdef CONFIG_ACORN_PARTITION_POWERTEC
2,
#elif defined B
2.5,
#endif
#ifdef CONFIG_ACORN_PARTITION_EESOX
3,
#endif
/* 12 more conditional array entries... */
NULL
#ifdef WRAP
  }
#endif
};

