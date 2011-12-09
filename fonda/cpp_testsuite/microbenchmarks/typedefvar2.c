//test configuration-aware lexer hack with invalid configurations
#if !(defined TYPE) && !(defined VAR)
#error "please define either TYPE or VAR"
#else

# ifdef TYPE
typedef int T;
# endif

# ifdef VAR
int T;
int num;
# endif

int main() {
  T*num;
}
#endif
