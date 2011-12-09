int main() {
  before1;
  before2;
  if (a) j = 1;
  #if A
  #else
  else j = 2;
  #endif
  after1;
  after2;
}
