#define A 1

#if MONKEY
#define A 2 /* jflds */
#elif BOB
#define A 2.5
#else !MONKEY && !BOB
#define A 3
#endif

#define EMPTY

#ifdef JOE extra tokens
#define A 2.3
#endif

#define EMPTY2
/* what the */
#define EMPTY3
/* hjfldks */

