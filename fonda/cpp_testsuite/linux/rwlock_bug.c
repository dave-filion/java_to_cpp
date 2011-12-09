#ifndef __LINUX_SPINLOCK_H
#define __LINUX_SPINLOCK_H

/*
 * include/linux/spinlock.h - generic spinlock/rwlock declarations
 *
 * here's the role of the various spinlock/rwlock related include files:
 *
 * on SMP builds:
 *
 *  asm/spinlock_types.h: contains the arch_spinlock_t/arch_rwlock_t and the
 *                        initializers
 *
 *  linux/spinlock_types.h:
 *                        defines the generic type and initializers
 *
 *  asm/spinlock.h:       contains the arch_spin_*()/etc. lowlevel
 *                        implementations, mostly inline assembly code
 *
 *   (also included on UP-debug builds:)
 *
 *  linux/spinlock_api_smp.h:
 *                        contains the prototypes for the _spin_*() APIs.
 *
 *  linux/spinlock.h:     builds the final spin_*() APIs.
 *
 * on UP builds:
 *
 *  linux/spinlock_type_up.h:
 *                        contains the generic, simplified UP spinlock type.
 *                        (which is an empty structure on non-debug builds)
 *
 *  linux/spinlock_types.h:
 *                        defines the generic type and initializers
 *
 *  linux/spinlock_up.h:
 *                        contains the arch_spin_*()/etc. version of UP
 *                        builds. (which are NOPs on non-debug, non-preempt
 *                        builds)
 *
 *   (included on UP-non-debug builds:)
 *
 *  linux/spinlock_api_up.h:
 *                        builds the _spin_*() APIs.
 *
 *  linux/spinlock.h:     builds the final spin_*() APIs.
 */

#include <linux/typecheck.h>
#include <linux/preempt.h>
#include <linux/linkage.h>
#include <linux/compiler.h>
#include <linux/irqflags.h>
#include <linux/thread_info.h>
#include <linux/kernel.h>
#include <linux/stringify.h>
#include <linux/bottom_half.h>

#include <asm/system.h>

/*
 * Must define these before including other files, inline functions need them
 */
#define LOCK_SECTION_NAME ".text..lock."KBUILD_BASENAME

#define LOCK_SECTION_START(extra)               \
        ".subsection 1\n\t"                     \
        extra                                   \
        ".ifndef " LOCK_SECTION_NAME "\n\t"     \
        LOCK_SECTION_NAME ":\n\t"               \
        ".endif\n"

#define LOCK_SECTION_END                        \
        ".previous\n\t"

#define __lockfunc __attribute__((section(".spinlock.text")))

/*
 * Pull the arch_spinlock_t and arch_rwlock_t definitions:
 */
#include <linux/spinlock_types.h>

/*
 * Pull the arch_spin*() functions/declarations (UP-nondebug doesn't need them):
 */
#ifdef CONFIG_SMP
# include <asm/spinlock.h>
#else
# include <linux/spinlock_up.h>
#endif

#ifdef CONFIG_DEBUG_SPINLOCK
  extern void __raw_spin_lock_init(raw_spinlock_t *lock, const char *name,
				   struct lock_class_key *key);
# define raw_spin_lock_init(lock)				\
do {								\
	static struct lock_class_key __key;			\
								\
	__raw_spin_lock_init((lock), #lock, &__key);		\
} while (0)

#else
# define raw_spin_lock_init(lock)				\
	do { *(lock) = __RAW_SPIN_LOCK_UNLOCKED(lock); } while (0)
#endif

#define raw_spin_is_locked(lock)	arch_spin_is_locked(&(lock)->raw_lock)

#ifdef CONFIG_GENERIC_LOCKBREAK
#define raw_spin_is_contended(lock) ((lock)->break_lock)
#else

#ifdef arch_spin_is_contended
#define raw_spin_is_contended(lock)	arch_spin_is_contended(&(lock)->raw_lock)
#else
#define raw_spin_is_contended(lock)	(((void)(lock), 0))
#endif /*arch_spin_is_contended*/
#endif

/* The lock does not imply full memory barrier. */
#ifndef ARCH_HAS_SMP_MB_AFTER_LOCK
static inline void smp_mb__after_lock(void) { smp_mb(); }
#endif

/**
 * raw_spin_unlock_wait - wait until the spinlock gets unlocked
 * @lock: the spinlock in question.
 */
#define raw_spin_unlock_wait(lock)	arch_spin_unlock_wait(&(lock)->raw_lock)

#define raw_spin_trylock(lock)	__cond_lock(lock, _raw_spin_trylock(lock))

#define raw_spin_lock(lock)	_raw_spin_lock(lock)

#ifdef CONFIG_DEBUG_LOCK_ALLOC
# define raw_spin_lock_nested(lock, subclass) \
	_raw_spin_lock_nested(lock, subclass)

# define raw_spin_lock_nest_lock(lock, nest_lock)			\
	 do {								\
		 typecheck(struct lockdep_map *, &(nest_lock)->dep_map);\
		 _raw_spin_lock_nest_lock(lock, &(nest_lock)->dep_map);	\
	 } while (0)
#else
# define raw_spin_lock_nested(lock, subclass)		_raw_spin_lock(lock)
# define raw_spin_lock_nest_lock(lock, nest_lock)	_raw_spin_lock(lock)
#endif

#if defined(CONFIG_SMP) || defined(CONFIG_DEBUG_SPINLOCK)

#define raw_spin_lock_irqsave(lock, flags)			\
	do {						\
		typecheck(unsigned long, flags);	\
		flags = _raw_spin_lock_irqsave(lock);	\
	} while (0)

#ifdef CONFIG_DEBUG_LOCK_ALLOC
#define raw_spin_lock_irqsave_nested(lock, flags, subclass)		\
	do {								\
		typecheck(unsigned long, flags);			\
		flags = _raw_spin_lock_irqsave_nested(lock, subclass);	\
	} while (0)
#else
#define raw_spin_lock_irqsave_nested(lock, flags, subclass)		\
	do {								\
		typecheck(unsigned long, flags);			\
		flags = _raw_spin_lock_irqsave(lock);			\
	} while (0)
#endif

#else

#define raw_spin_lock_irqsave(lock, flags)		\
	do {						\
		typecheck(unsigned long, flags);	\
		_raw_spin_lock_irqsave(lock, flags);	\
	} while (0)

#define raw_spin_lock_irqsave_nested(lock, flags, subclass)	\
	raw_spin_lock_irqsave(lock, flags)

#endif

#define raw_spin_lock_irq(lock)		_raw_spin_lock_irq(lock)
#define raw_spin_lock_bh(lock)		_raw_spin_lock_bh(lock)
#define raw_spin_unlock(lock)		_raw_spin_unlock(lock)
#define raw_spin_unlock_irq(lock)	_raw_spin_unlock_irq(lock)

#define raw_spin_unlock_irqrestore(lock, flags)		\
	do {							\
		typecheck(unsigned long, flags);		\
		_raw_spin_unlock_irqrestore(lock, flags);	\
	} while (0)
#define raw_spin_unlock_bh(lock)	_raw_spin_unlock_bh(lock)

#define raw_spin_trylock_bh(lock) \
	__cond_lock(lock, _raw_spin_trylock_bh(lock))

#define raw_spin_trylock_irq(lock) \
({ \
	local_irq_disable(); \
	raw_spin_trylock(lock) ? \
	1 : ({ local_irq_enable(); 0;  }); \
})

#define raw_spin_trylock_irqsave(lock, flags) \
({ \
	local_irq_save(flags); \
	raw_spin_trylock(lock) ? \
	1 : ({ local_irq_restore(flags); 0; }); \
})

/**
 * raw_spin_can_lock - would raw_spin_trylock() succeed?
 * @lock: the spinlock in question.
 */
#define raw_spin_can_lock(lock)	(!raw_spin_is_locked(lock))



/* Include rwlock functions */
//#include <linux/rwlock.h>

#ifndef __LINUX_RWLOCK_H
#define __LINUX_RWLOCK_H

#ifndef __LINUX_SPINLOCK_H
# error "please don't include this file directly"
#endif

/*
 * rwlock related methods
 *
 * split out from spinlock.h
 *
 * portions Copyright 2005, Red Hat, Inc., Ingo Molnar
 * Released under the General Public License (GPL).
 */

#ifdef CONFIG_DEBUG_SPINLOCK
  extern void __rwlock_init(rwlock_t *lock, const char *name,
			    struct lock_class_key *key);
# define rwlock_init(lock)					\
do {								\
	static struct lock_class_key __key;			\
								\
	__rwlock_init((lock), #lock, &__key);			\
} while (0)
#else
# define rwlock_init(lock)					\
	do { *(lock) = __RW_LOCK_UNLOCKED(lock); } while (0)
#endif

#ifdef CONFIG_DEBUG_SPINLOCK
#define do_raw_read_lock_flags(lock, flags) do_raw_read_lock(lock)
#define do_raw_write_lock_flags(lock, flags) do_raw_write_lock(lock)
#else
# define do_raw_read_lock(rwlock)	do {__acquire(lock); arch_read_lock(&(rwlock)->raw_lock); } while (0)
# define do_raw_read_lock_flags(lock, flags) \
		do {__acquire(lock); arch_read_lock_flags(&(lock)->raw_lock, *(flags)); } while (0)
# define do_raw_read_trylock(rwlock)	arch_read_trylock(&(rwlock)->raw_lock)
# define do_raw_read_unlock(rwlock)	do {arch_read_unlock(&(rwlock)->raw_lock); __release(lock); } while (0)
# define do_raw_write_lock(rwlock)	do {__acquire(lock); arch_write_lock(&(rwlock)->raw_lock); } while (0)
# define do_raw_write_lock_flags(lock, flags) \
		do {__acquire(lock); arch_write_lock_flags(&(lock)->raw_lock, *(flags)); } while (0)
# define do_raw_write_trylock(rwlock)	arch_write_trylock(&(rwlock)->raw_lock)
# define do_raw_write_unlock(rwlock)	do {arch_write_unlock(&(rwlock)->raw_lock); __release(lock); } while (0)
#endif

#define read_can_lock(rwlock)		arch_read_can_lock(&(rwlock)->raw_lock)
#define write_can_lock(rwlock)		arch_write_can_lock(&(rwlock)->raw_lock)

/*
 * Define the various rw_lock methods.  Note we define these
 * regardless of whether CONFIG_SMP or CONFIG_PREEMPT are set. The various
 * methods are defined as nops in the case they are not required.
 */
#define read_trylock(lock)	__cond_lock(lock, _raw_read_trylock(lock))
#define write_trylock(lock)	__cond_lock(lock, _raw_write_trylock(lock))

#define write_lock(lock)	_raw_write_lock(lock)
#define read_lock(lock)		_raw_read_lock(lock)

#if defined(CONFIG_SMP) || defined(CONFIG_DEBUG_SPINLOCK)

#define read_lock_irqsave(lock, flags)			\
	do {						\
		typecheck(unsigned long, flags);	\
		flags = _raw_read_lock_irqsave(lock);	\
	} while (0)
#define write_lock_irqsave(lock, flags)			\
	do {						\
		typecheck(unsigned long, flags);	\
		flags = _raw_write_lock_irqsave(lock);	\
	} while (0)

#else

#define read_lock_irqsave(lock, flags)			\
	do {						\
		typecheck(unsigned long, flags);	\
		_raw_read_lock_irqsave(lock, flags);	\
	} while (0)
#define write_lock_irqsave(lock, flags)			\
	do {						\
		typecheck(unsigned long, flags);	\
		_raw_write_lock_irqsave(lock, flags);	\
	} while (0)

#endif

#define read_lock_irq(lock)		_raw_read_lock_irq(lock)
#define read_lock_bh(lock)		_raw_read_lock_bh(lock)
#define write_lock_irq(lock)		_raw_write_lock_irq(lock)
#define write_lock_bh(lock)		_raw_write_lock_bh(lock)
#define read_unlock(lock)		_raw_read_unlock(lock)
#define write_unlock(lock)		_raw_write_unlock(lock)
#define read_unlock_irq(lock)		_raw_read_unlock_irq(lock)
#define write_unlock_irq(lock)		_raw_write_unlock_irq(lock)

#define read_unlock_irqrestore(lock, flags)			\
	do {							\
		typecheck(unsigned long, flags);		\
		_raw_read_unlock_irqrestore(lock, flags);	\
	} while (0)
#define read_unlock_bh(lock)		_raw_read_unlock_bh(lock)

#define write_unlock_irqrestore(lock, flags)		\
	do {						\
		typecheck(unsigned long, flags);	\
		_raw_write_unlock_irqrestore(lock, flags);	\
	} while (0)
#define write_unlock_bh(lock)		_raw_write_unlock_bh(lock)

#define write_trylock_irqsave(lock, flags) \
({ \
	local_irq_save(flags); \
	write_trylock(lock) ? \
	1 : ({ local_irq_restore(flags); 0; }); \
})

#endif /* __LINUX_RWLOCK_H */





/*
 * Pull the _spin_*()/_read_*()/_write_*() functions/declarations:
 */
#if defined(CONFIG_SMP) || defined(CONFIG_DEBUG_SPINLOCK)
//# include <linux/spinlock_api_smp.h>


#ifndef __LINUX_SPINLOCK_API_SMP_H
#define __LINUX_SPINLOCK_API_SMP_H

#ifndef __LINUX_SPINLOCK_H
# error "please don't include this file directly"
#endif

#define assert_raw_spin_locked(x)	BUG_ON(!raw_spin_is_locked(x))

#ifdef CONFIG_INLINE_SPIN_LOCK
#define _raw_spin_lock(lock) __raw_spin_lock(lock)
#endif

#ifdef CONFIG_INLINE_SPIN_LOCK_BH
#define _raw_spin_lock_bh(lock) __raw_spin_lock_bh(lock)
#endif

#ifdef CONFIG_INLINE_SPIN_LOCK_IRQ
#define _raw_spin_lock_irq(lock) __raw_spin_lock_irq(lock)
#endif

#ifdef CONFIG_INLINE_SPIN_LOCK_IRQSAVE
#define _raw_spin_lock_irqsave(lock) __raw_spin_lock_irqsave(lock)
#endif

#ifdef CONFIG_INLINE_SPIN_TRYLOCK
#define _raw_spin_trylock(lock) __raw_spin_trylock(lock)
#endif

#ifdef CONFIG_INLINE_SPIN_TRYLOCK_BH
#define _raw_spin_trylock_bh(lock) __raw_spin_trylock_bh(lock)
#endif

#ifdef CONFIG_INLINE_SPIN_UNLOCK
#define _raw_spin_unlock(lock) __raw_spin_unlock(lock)
#endif

#ifdef CONFIG_INLINE_SPIN_UNLOCK_BH
#define _raw_spin_unlock_bh(lock) __raw_spin_unlock_bh(lock)
#endif

#ifdef CONFIG_INLINE_SPIN_UNLOCK_IRQ
#define _raw_spin_unlock_irq(lock) __raw_spin_unlock_irq(lock)
#endif

#ifdef CONFIG_INLINE_SPIN_UNLOCK_IRQRESTORE
#define _raw_spin_unlock_irqrestore(lock, flags) __raw_spin_unlock_irqrestore(lock, flags)
#endif





//#include <linux/rwlock_api_smp.h>



#ifndef __LINUX_RWLOCK_API_SMP_H
#define __LINUX_RWLOCK_API_SMP_H

#ifndef __LINUX_SPINLOCK_API_SMP_H
# error "please don't include this file directly"
#endif


#ifdef CONFIG_INLINE_READ_LOCK
#define _raw_read_lock(lock) __raw_read_lock(lock)
#endif

#ifdef CONFIG_INLINE_WRITE_LOCK
#define _raw_write_lock(lock) __raw_write_lock(lock)
#endif

#ifdef CONFIG_INLINE_READ_LOCK_BH
#define _raw_read_lock_bh(lock) __raw_read_lock_bh(lock)
#endif

#ifdef CONFIG_INLINE_WRITE_LOCK_BH
#define _raw_write_lock_bh(lock) __raw_write_lock_bh(lock)
#endif

#ifdef CONFIG_INLINE_READ_LOCK_IRQ
#define _raw_read_lock_irq(lock) __raw_read_lock_irq(lock)
#endif

#ifdef CONFIG_INLINE_WRITE_LOCK_IRQ
#define _raw_write_lock_irq(lock) __raw_write_lock_irq(lock)
#endif

#ifdef CONFIG_INLINE_READ_LOCK_IRQSAVE
#define _raw_read_lock_irqsave(lock) __raw_read_lock_irqsave(lock)
#endif

#ifdef CONFIG_INLINE_WRITE_LOCK_IRQSAVE
#define _raw_write_lock_irqsave(lock) __raw_write_lock_irqsave(lock)
#endif

#ifdef CONFIG_INLINE_READ_TRYLOCK
#define _raw_read_trylock(lock) __raw_read_trylock(lock)
#endif

#ifdef CONFIG_INLINE_WRITE_TRYLOCK
#define _raw_write_trylock(lock) __raw_write_trylock(lock)
#endif

#ifdef CONFIG_INLINE_READ_UNLOCK
#define _raw_read_unlock(lock) __raw_read_unlock(lock)
#endif

#ifdef CONFIG_INLINE_WRITE_UNLOCK
#define _raw_write_unlock(lock) __raw_write_unlock(lock)
#endif

#ifdef CONFIG_INLINE_READ_UNLOCK_BH
#define _raw_read_unlock_bh(lock) __raw_read_unlock_bh(lock)
#endif

#ifdef CONFIG_INLINE_WRITE_UNLOCK_BH
#define _raw_write_unlock_bh(lock) __raw_write_unlock_bh(lock)
#endif

#ifdef CONFIG_INLINE_READ_UNLOCK_IRQ
#define _raw_read_unlock_irq(lock) __raw_read_unlock_irq(lock)
#endif

#ifdef CONFIG_INLINE_WRITE_UNLOCK_IRQ
#define _raw_write_unlock_irq(lock) __raw_write_unlock_irq(lock)
#endif

#ifdef CONFIG_INLINE_READ_UNLOCK_IRQRESTORE
#define _raw_read_unlock_irqrestore(lock, flags) \
	__raw_read_unlock_irqrestore(lock, flags)
#endif

#ifdef CONFIG_INLINE_WRITE_UNLOCK_IRQRESTORE
#define _raw_write_unlock_irqrestore(lock, flags) \
	__raw_write_unlock_irqrestore(lock, flags)
#endif

/*
 * If lockdep is enabled then we use the non-preemption spin-ops
 * even on CONFIG_PREEMPT, because lockdep assumes that interrupts are
 * not re-enabled during lock-acquire (which the preempt-spin-ops do):
 */
#if !defined(CONFIG_GENERIC_LOCKBREAK) || defined(CONFIG_DEBUG_LOCK_ALLOC)

static inline void __raw_read_lock(rwlock_t *lock)
{
  //	preempt_disable();
  //	rwlock_acquire_read(&lock->dep_map, 0, 0, _RET_IP_);
	LOCK_CONTENDED(lock, do_raw_read_trylock, do_raw_read_lock);
}

int x;

/*static inline unsigned long __raw_read_lock_irqsave(rwlock_t *lock)
{
	unsigned long flags;

	local_irq_save(flags);
	preempt_disable();
	rwlock_acquire_read(&lock->dep_map, 0, 0, _RET_IP_);
	LOCK_CONTENDED_FLAGS(lock, do_raw_read_trylock, do_raw_read_lock,
			     do_raw_read_lock_flags, &flags);
	return flags;
}

static inline void __raw_read_lock_irq(rwlock_t *lock)
{
	local_irq_disable();
	preempt_disable();
	rwlock_acquire_read(&lock->dep_map, 0, 0, _RET_IP_);
	LOCK_CONTENDED(lock, do_raw_read_trylock, do_raw_read_lock);
}

static inline void __raw_read_lock_bh(rwlock_t *lock)
{
	local_bh_disable();
	preempt_disable();
	rwlock_acquire_read(&lock->dep_map, 0, 0, _RET_IP_);
	LOCK_CONTENDED(lock, do_raw_read_trylock, do_raw_read_lock);
}

static inline unsigned long __raw_write_lock_irqsave(rwlock_t *lock)
{
	unsigned long flags;

	local_irq_save(flags);
	preempt_disable();
	rwlock_acquire(&lock->dep_map, 0, 0, _RET_IP_);
	LOCK_CONTENDED_FLAGS(lock, do_raw_write_trylock, do_raw_write_lock,
			     do_raw_write_lock_flags, &flags);
	return flags;
}

static inline void __raw_write_lock_irq(rwlock_t *lock)
{
	local_irq_disable();
	preempt_disable();
	rwlock_acquire(&lock->dep_map, 0, 0, _RET_IP_);
	LOCK_CONTENDED(lock, do_raw_write_trylock, do_raw_write_lock);
}

static inline void __raw_write_lock_bh(rwlock_t *lock)
{
	local_bh_disable();
	preempt_disable();
	rwlock_acquire(&lock->dep_map, 0, 0, _RET_IP_);
	LOCK_CONTENDED(lock, do_raw_write_trylock, do_raw_write_lock);
}

static inline void __raw_write_lock(rwlock_t *lock)
{
	preempt_disable();
	rwlock_acquire(&lock->dep_map, 0, 0, _RET_IP_);
	LOCK_CONTENDED(lock, do_raw_write_trylock, do_raw_write_lock);
}
*/

#endif /* CONFIG_PREEMPT */

/*static inline void __raw_write_unlock(rwlock_t *lock)
{
	rwlock_release(&lock->dep_map, 1, _RET_IP_);
	do_raw_write_unlock(lock);
	preempt_enable();
}

static inline void __raw_read_unlock(rwlock_t *lock)
{
	rwlock_release(&lock->dep_map, 1, _RET_IP_);
	do_raw_read_unlock(lock);
	preempt_enable();
}

static inline void
__raw_read_unlock_irqrestore(rwlock_t *lock, unsigned long flags)
{
	rwlock_release(&lock->dep_map, 1, _RET_IP_);
	do_raw_read_unlock(lock);
	local_irq_restore(flags);
	preempt_enable();
}

static inline void __raw_read_unlock_irq(rwlock_t *lock)
{
	rwlock_release(&lock->dep_map, 1, _RET_IP_);
	do_raw_read_unlock(lock);
	local_irq_enable();
	preempt_enable();
}

static inline void __raw_read_unlock_bh(rwlock_t *lock)
{
	rwlock_release(&lock->dep_map, 1, _RET_IP_);
	do_raw_read_unlock(lock);
	preempt_enable_no_resched();
	local_bh_enable_ip((unsigned long)__builtin_return_address(0));
}

static inline void __raw_write_unlock_irqrestore(rwlock_t *lock,
					     unsigned long flags)
{
	rwlock_release(&lock->dep_map, 1, _RET_IP_);
	do_raw_write_unlock(lock);
	local_irq_restore(flags);
	preempt_enable();
}

static inline void __raw_write_unlock_irq(rwlock_t *lock)
{
	rwlock_release(&lock->dep_map, 1, _RET_IP_);
	do_raw_write_unlock(lock);
	local_irq_enable();
	preempt_enable();
}

static inline void __raw_write_unlock_bh(rwlock_t *lock)
{
	rwlock_release(&lock->dep_map, 1, _RET_IP_);
	do_raw_write_unlock(lock);
	preempt_enable_no_resched();
	local_bh_enable_ip((unsigned long)__builtin_return_address(0));
}
*/
#endif /* __LINUX_RWLOCK_API_SMP_H */





#endif /* __LINUX_SPINLOCK_API_SMP_H */

#endif


#endif /* __LINUX_SPINLOCK_H */
