
#if (defined X)

#elif 1
void foo(int x) {
}

#endif
int main() {
  
#if (defined X)
do {} while (0)
#else
foo(x)

#endif
;
  x;
}
