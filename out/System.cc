#include<iostream>
#include"System.h"


void OutStream::print(Object o) {
  std::cout << o -> __vptr -> toString(o);
}

void OutStream::println(Object o) {
  std::cout << o -> __vptr -> toString(o) << std::endl;
}

void OutStream::print(int32_t i) {
  std::cout << i;
}

void OutStream::println(int32_t i) {
  std::cout << i << std::endl;
}

void OutStream::print() {
}

void OutStream::println() {
   std::cout << std::endl;
}

void OutStream::println(int32_t one, String two, int32_t three, String four){
   std::cout << one << two << three << four << std::endl;
}

