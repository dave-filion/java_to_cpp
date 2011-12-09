int main(int a)

#ifdef CONFIG_A
{
#else
;
int a;
#endif

 int b;

#ifdef CONFIG_B
 int c;
#else
}
#endif
