#pragma once
#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"


using namespace java::lang;
namespace easy_test{

struct __Hello;
struct __Hello_VT;
typedef __rt::Ptr<__Hello> Hello;

struct __Hello{
__Hello_VT* __vptr;
__Hello();
static void __delete(__Hello*);
static void hello(Hello);
static void main_String(Hello, String);
static Class __class();
static __Hello_VT __vtable;
};

struct __Hello_VT {

Class __isa;
void (*__delete)(__Hello*);
int32_t (*hashCode)(Hello);
bool (*equals)(Hello, Object);
Class (*getClass)(Hello);
String (*toString)(Hello);
void (*hello)(Hello);
void (*main_String)(Hello, String);

__Hello_VT()
: __isa(__Hello::__class()),
__delete(&__Hello::__delete),
hashCode((int32_t(*)(Hello))&__Object::hashCode),
equals((bool(*)(Hello,Object))&__Object::equals),
getClass((Class(*)(Hello))&__Object::getClass),
toString((String(*)(Hello))&__Object::toString),
hello(&__Hello::hello),
main_String(&__Hello::main_String){ }
};
}
