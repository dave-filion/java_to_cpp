int main() {

#ifdef CONFIG_A
while (a) {
#ifdef CONFIG_C
  a = 9;
 }
 while (c) {
   c = 10;
 }
#else
#endif
#endif

#ifdef CONFIG_A
}
#endif

}
