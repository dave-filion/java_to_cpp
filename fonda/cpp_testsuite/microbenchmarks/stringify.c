#ifdef BOB
#define __GNUC__ 4
#else
#define __GNUC__ 3
#endif

#define __gcc_header(x) #x
#define _gcc_header(x) __gcc_header(x)

int main() {
  char *s = _gcc_header(__GNUC__);
}

