static
#if A
inline
#elif B
__inline__      __attribute__((always_inline))
#endif
void f() {
#if C
  x = 1;
#else
  y = 2;
#endif
}
