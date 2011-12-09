#if CONFIG
int main() {
}
#else
typedef int myint;
myint main();
#endif
