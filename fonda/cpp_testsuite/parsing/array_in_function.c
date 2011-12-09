#if CONFIG
int main() {
  int array[] = {
    #if A
    watermelon,
    #endif
    #if B
    pineapple,
    #endif
  };
}
#else
typedef int myint;
myint main();
#endif

