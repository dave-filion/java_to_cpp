#if (defined INLINE)
static
inline
foo() 
#else
static
foo() 
#endif
{
  int x = 127;

  return;
}
