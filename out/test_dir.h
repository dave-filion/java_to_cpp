#pragma once
#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"

using namespace java::lang;
namespace test_dir{

namespace hello{

struct __Hello;
struct __Hello_VT;
typedef __rt::Ptr<__Hello> Hello;
}
}
namespace test_dir{

namespace goodbye{

namespace deepGoodbye{

struct __DeepGoodBye;
struct __DeepGoodBye_VT;
typedef __rt::Ptr<__DeepGoodBye> DeepGoodBye;
}
}
}
namespace test_dir{

namespace goodbye{

struct __Goodbye;
struct __Goodbye_VT;
typedef __rt::Ptr<__Goodbye> Goodbye;
}
}
namespace test_dir{

namespace hello{


struct __Hello{
__Hello_VT* __vptr;
__Hello();
static void __delete(__Hello*);
static void sayHi(Hello);
static void sayHi_String(Hello, String);
static void main();
static String toString(Hello);
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
void (*sayHi)(Hello);
void (*sayHi_String)(Hello, String);

__Hello_VT()
: __isa(__Hello::__class()),
__delete(&__Hello::__delete),
hashCode((int32_t(*)(Hello))&java::lang::__Object::hashCode),
equals((bool(*)(Hello,Object))&java::lang::__Object::equals),
getClass((Class(*)(Hello))&java::lang::__Object::getClass),
toString((String(*)(Hello))&test_dir::hello::__Hello::toString),
sayHi((void(*)(Hello))&test_dir::hello::__Hello::sayHi),
sayHi_String((void(*)(Hello,String))&test_dir::hello::__Hello::sayHi_String){ }
};
}
}
namespace test_dir{

namespace goodbye{

namespace deepGoodbye{


struct __DeepGoodBye{
__DeepGoodBye_VT* __vptr;
__DeepGoodBye();
static void __delete(__DeepGoodBye*);
static Class __class();
static __DeepGoodBye_VT __vtable;
};

struct __DeepGoodBye_VT {

Class __isa;
void (*__delete)(__DeepGoodBye*);
int32_t (*hashCode)(DeepGoodBye);
bool (*equals)(DeepGoodBye, Object);
Class (*getClass)(DeepGoodBye);
String (*toString)(DeepGoodBye);

__DeepGoodBye_VT()
: __isa(__DeepGoodBye::__class()),
__delete(&__DeepGoodBye::__delete),
hashCode((int32_t(*)(DeepGoodBye))&java::lang::__Object::hashCode),
equals((bool(*)(DeepGoodBye,Object))&java::lang::__Object::equals),
getClass((Class(*)(DeepGoodBye))&java::lang::__Object::getClass),
toString((String(*)(DeepGoodBye))&java::lang::__Object::toString){ }
};
}
}
}
namespace test_dir{

namespace goodbye{


struct __Goodbye{
__Goodbye_VT* __vptr;
__Goodbye();
static void __delete(__Goodbye*);
static String returnString(Goodbye);
static int returnInt_String(Goodbye, String);
static String returnString_int(Goodbye, int);
static void chainTest(Goodbye);
static void main();
static Class __class();
static __Goodbye_VT __vtable;
};

struct __Goodbye_VT {

Class __isa;
void (*__delete)(__Goodbye*);
int32_t (*hashCode)(Goodbye);
bool (*equals)(Goodbye, Object);
Class (*getClass)(Goodbye);
String (*toString)(Goodbye);
void (*sayHi)(Goodbye);
void (*sayHi_String)(Goodbye, String);
String (*returnString)(Goodbye);
int (*returnInt_String)(Goodbye, String);
String (*returnString_int)(Goodbye, int);
void (*chainTest)(Goodbye);

__Goodbye_VT()
: __isa(__Goodbye::__class()),
__delete(&__Goodbye::__delete),
hashCode((int32_t(*)(Goodbye))&java::lang::__Object::hashCode),
equals((bool(*)(Goodbye,Object))&java::lang::__Object::equals),
getClass((Class(*)(Goodbye))&java::lang::__Object::getClass),
toString((String(*)(Goodbye))&test_dir::hello::__Hello::toString),
sayHi((void(*)(Goodbye))&test_dir::hello::__Hello::sayHi),
sayHi_String((void(*)(Goodbye,String))&test_dir::hello::__Hello::sayHi_String),
returnString((String(*)(Goodbye))&test_dir::goodbye::__Goodbye::returnString),
returnInt_String((int(*)(Goodbye,String))&test_dir::goodbye::__Goodbye::returnInt_String),
returnString_int((String(*)(Goodbye,int))&test_dir::goodbye::__Goodbye::returnString_int),
chainTest((void(*)(Goodbye))&test_dir::goodbye::__Goodbye::chainTest){ }
};
}
}
