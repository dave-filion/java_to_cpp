//test configuration-aware lexer hack
#ifdef TYPE
typedef int T;
#else
int T;
int num;
#endif

int main() {
  T*num;  // declaration or statement
}
