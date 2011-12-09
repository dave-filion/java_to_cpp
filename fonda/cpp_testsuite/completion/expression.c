int main() {
#if A + B
a;
#elif defined A && defined B
b;
#elif 1 + 1
c;
#elif 1 - 1
d;
#elif A B
e;
#elif (defined B && A + 2) < 3
f;
#elif (defined B && A + 2) < 3 || A
g;
#endif

#if A
a;
#endif
}
