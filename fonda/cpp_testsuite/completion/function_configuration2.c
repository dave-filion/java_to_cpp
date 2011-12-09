static 
#if A
inline __attribute__((always_inline))
#elif 1
inline
#endif
 clear_ti_thread_flag(struct thread_info *ti, int flag)
{
}
