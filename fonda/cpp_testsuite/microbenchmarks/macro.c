#define __read_mostly __attribute__ ((read_mostly))

#ifdef CONFIG_SCHED_DEBUG
# define const_debug __read_mostly
#else
# define const_debug static const
#endif

const_debug int func();

