/*int x
#ifdef A
()
#endif
;*/

int main() {

#ifdef A
int y;
#endif

#ifdef A
y
#else
int y
#endif
= 1;

/*#ifdef A
int
#endif
z
= 1;*/

}
