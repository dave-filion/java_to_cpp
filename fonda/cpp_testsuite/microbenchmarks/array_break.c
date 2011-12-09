static int check_part[]={
0,
#ifdef CONFIG_ACORN_PARTITION_ICS
1,
#endif
#ifdef CONFIG_ACORN_PARTITION_POWERTEC
2,
#endif
#ifdef CONFIG_ACORN_PARTITION_EESOX
3,
#endif
#ifdef CONFIG_ACORN_PARTITION_CUMANA
4,
#endif
#ifdef CONFIG_ACORN_PARTITION_ADFS
5,
//}
#endif
#ifdef CONFIG_EFI_PARTITION
6,
#endif
/* 12 more conditional array entries... */
NULL
#ifdef CONFIG_A
//#ifdef A
};
#else
, 12
};
#endif

