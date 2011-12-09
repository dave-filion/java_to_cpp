#ifdef CONFIG_CGROUP_SCHED
int a()
{
}

int b()
{
}

#else /* CONFIG_CGROUP_SCHED */

char c()
{
}

char d()
{
}

#endif /* CONFIG_CGROUP_SCHED */
