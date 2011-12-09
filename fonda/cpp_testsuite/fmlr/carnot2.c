#ifdef CONFIG_CGROUP_SCHED

/*
 * Return the group to which this tasks belongs.
 *
 * We use task_subsys_state_check() and extend the RCU verification
 * with lockdep_is_held(&task_rq(p)->lock) because cpu_cgroup_attach()
 * holds that lock for each task it moves into the cgroup. Therefore
 * by holding that lock, we pin the task to the current cgroup.
 */
static inline struct task_group *task_group(struct task_struct *p)
{
  struct task_group *tg;
  struct cgroup_subsys_state *css;

  if (p->flags & PF_EXITING)
    return &root_task_group;

  css = task_subsys_state_check(p, cpu_cgroup_subsys_id,
                                lockdep_is_held(&task_rq(p)->lock));

  return autogroup_task_group(p, tg);
}

/* Change a task's cfs_rq and parent entity if it moves across CPUs/groups */
static inline void set_task_rq(struct task_struct *p, unsigned int cpu)
{
#ifdef CONFIG_FAIR_GROUP_SCHED
  p->se.cfs_rq = task_group(p)->cfs_rq[cpu];
  p->se.parent = task_group(p)->se[cpu];
#endif

#ifdef CONFIG_RT_GROUP_SCHED
  p->rt.rt_rq  = task_group(p)->rt_rq[cpu];
  p->rt.parent = task_group(p)->rt_se[cpu];
#endif
}

#else /* CONFIG_CGROUP_SCHED */

static inline void set_task_rq(struct task_struct *p, unsigned int cpu) { }
static inline struct task_group *task_group(struct task_struct *p)
{
  return NULL;
}

#endif /* CONFIG_CGROUP_SCHED */
