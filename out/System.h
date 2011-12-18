#pragma once
#include"java_lang.h"


using namespace java::lang;

typedef struct system System;
typedef struct outstream OutStream;

struct system {
  static OutStream out;
};

struct outstream {

  static void print(Object);

  static void println(Object);

  static void print(int32_t);

  static void println(int32_t);

  static void print();

  static void println();
};


