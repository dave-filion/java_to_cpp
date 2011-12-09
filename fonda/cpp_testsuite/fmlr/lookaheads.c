
int main() {
#ifdef A
#ifdef A1
#else
1;
#endif
2;

#elif defined B
3;

#elif defined C
#ifdef C1
#else
4;
#endif
#ifdef C2
#else
5;
#endif

#elif defined D
#ifdef D1
6;
#else
7;
#endif

#endif
8;

}
