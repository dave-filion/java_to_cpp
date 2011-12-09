int main() {

#ifdef CONFIG_A
while (a) {
#ifdef CONFIG_C
  a = 9;
 }
 while (c) {
   c = 10;
#endif
#ifdef CONFIG_B
while (b) {
#endif
#endif

  int x;

  x = 1;

#ifdef CONFIG_A
#ifdef CONFIG_B
}
#endif
}
#endif

}
