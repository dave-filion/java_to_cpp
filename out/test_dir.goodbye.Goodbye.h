#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"
#include "test_dir.hello.Hello.h"


using namespace java::lang;
namespace test_dir{

namespace goodbye{

struct __Goodbye;
struct ____Goodbye_VT;
typedef __rt::Ptr<__Goodbye> Goodbye;

struct __Goodbye{
__Goodbye_VT* __vptr;
__Goodbye();
static void __delete(__Goodbye*);
static String returnString(Goodbye);
static int returnInt_String(Goodbye, String);
static String returnString_int(Goodbye, int);
static void chainTest(Goodbye);
static void main_String(Goodbye, String);
static Class __class();
static ____Goodbye_VT __vtable;
};

struct __Goodbye_VT {

int32_t (*hashCode)(Goodbye);
bool (*equals)(Goodbye, Object);
Class (*getClass)(Goodbye);
String (*toString)(Goodbye);
void (*sayHi)(Goodbye);
void (*sayHi_String)(Goodbye, String);
void (*main_String)(Goodbye, String);
String (*returnString)(Goodbye);
int (*returnInt_String)(Goodbye, String);
String (*returnString_int)(Goodbye, int);
void (*chainTest)(Goodbye);

__Goodbye_VT()
: __isa(__Goodbye::__class()),
__delete(&__Goodbye::__delete),
,
hashCode((int32_t(*)(Goodbye))&__Object::hashCode),
equals((bool(*)(Goodbye,Object))&__Object::equals),
getClass((Class(*)(Goodbye))&__Object::getClass),
toString((String(*)(Goodbye))&__Hello::toString),
sayHi((void(*)(Goodbye))&__Hello::sayHi),
sayHi_String((void(*)(Goodbye,String))&__Hello::sayHi_String),
main_String(&__Goodbye::main_String),
,
returnString(&__Goodbye::returnString),
returnInt_String(&__Goodbye::returnInt_String),
returnString_int(&__Goodbye::returnString_int),
chainTest(&__Goodbye::chainTest),
{ }
};
}
}
