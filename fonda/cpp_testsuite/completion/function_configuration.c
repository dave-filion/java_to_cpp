static 
#if !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && (__GNUC__ >= 4) && (defined CONFIG_ARCH_SUPPORTS_OPTIMIZED_INLINING) && (defined CONFIG_OPTIMIZE_INLINING) && (__GNUC__ < 4) || !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && (__GNUC__ >= 4) && (defined CONFIG_ARCH_SUPPORTS_OPTIMIZED_INLINING) && !(defined CONFIG_OPTIMIZE_INLINING) || !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && (__GNUC__ >= 4) && !(defined CONFIG_ARCH_SUPPORTS_OPTIMIZED_INLINING)
inline		__attribute__((always_inline))
#elif !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && (__GNUC__ >= 4) && (defined CONFIG_ARCH_SUPPORTS_OPTIMIZED_INLINING) && (defined CONFIG_OPTIMIZE_INLINING) && !(__GNUC__ < 4)
inline
#elif !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && !(__GNUC__ >= 4) && (__GNUC__ == 3) && !(__GNUC_MINOR__ >= 2) || !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && !(__GNUC__ >= 4) && !(__GNUC__ == 3)
inline
#elif !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && !(__GNUC__ >= 4) && (__GNUC__ == 3) && (__GNUC_MINOR__ >= 2) && (defined CONFIG_ARCH_SUPPORTS_OPTIMIZED_INLINING) && !(defined CONFIG_OPTIMIZE_INLINING) || !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && !(__GNUC__ >= 4) && (__GNUC__ == 3) && (__GNUC_MINOR__ >= 2) && (defined CONFIG_ARCH_SUPPORTS_OPTIMIZED_INLINING) && (defined CONFIG_OPTIMIZE_INLINING) && (__GNUC__ < 4) || !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && !(__GNUC__ >= 4) && (__GNUC__ == 3) && (__GNUC_MINOR__ >= 2) && !(defined CONFIG_ARCH_SUPPORTS_OPTIMIZED_INLINING)
inline		__attribute__((always_inline))
#elif !(defined _LINUX_TYPES_H) && (defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__) && !(__GNUC__ >= 4) && (__GNUC__ == 3) && (__GNUC_MINOR__ >= 2) && (defined CONFIG_ARCH_SUPPORTS_OPTIMIZED_INLINING) && (defined CONFIG_OPTIMIZE_INLINING) && !(__GNUC__ < 4)
inline
#elif !(defined _LINUX_TYPES_H) && !(defined __KERNEL__) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && !(defined __ASSEMBLY__)
inline
#elif !(defined _LINUX_TYPES_H) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && !(defined __LINUX_COMPILER_H) && (defined __ASSEMBLY__)
inline
#elif !(defined _LINUX_TYPES_H) && !(defined _LINUX_POSIX_TYPES_H) && !(defined _LINUX_STDDEF_H) && (defined __LINUX_COMPILER_H)
inline
#elif !(defined _LINUX_TYPES_H) && !(defined _LINUX_POSIX_TYPES_H) && (defined _LINUX_STDDEF_H)
inline
#elif !(defined _LINUX_TYPES_H) && (defined _LINUX_POSIX_TYPES_H)
inline
#elif (defined _LINUX_TYPES_H)
inline
#endif
 void clear_ti_thread_flag(struct thread_info *ti, int flag)
{
	clear_bit(flag, (unsigned long *)&ti->flags);
}
