int main() {
#ifdef A
#ifdef A1
  a1;
#elif defined A2
  a2;
#endif

#ifdef A3
  a3;
#else
  aelse;
#endif

  a;
#elif defined B
  b;
#endif
  end;
}
