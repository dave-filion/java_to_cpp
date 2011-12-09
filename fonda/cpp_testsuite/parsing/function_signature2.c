#if A
static
inline
void f() 
#elif !A && B
static
__inline__      __attribute__((always_inline))
void f() 
#else
static
void f() 
#endif
{
#if C
  x = 1;
#else
  y = 2;
#endif
}
