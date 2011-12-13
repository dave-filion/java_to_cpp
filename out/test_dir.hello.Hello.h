#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"
#include "test_dir.goodbye.Goodbye.h"
#include "test_dir.goodbye.deepGoodbye.DeepGoodBye.h"


using namespace java::lang;
namespace test_dir{

namespace hello{

struct __Hello;
struct ____Hello_VT;
typedef __rt::Ptr<__Hello> Hello;

struct __Hello{
__Hello_VT* __vptr;
__Hello();
static void __delete(__Hello*);
static void sayHi(Hello);
static void sayHi_String(Hello, String);
static void main_String(Hello, String);
static String toString(Hello);
static Class __class();
static ____Hello_VT __vtable;
};

struct __Hello_VT {

int32_t (*hashCode)(Hello);
bool (*equals)(Hello, Object);
Class (*getClass)(Hello);
String (*toString)(Hello);
void (*sayHi)(Hello);
void (*sayHi_String)(Hello, String);
void (*main_String)(Hello, String);

__Hello_VT()
: __isa(__Hello::__class()),
__delete(&__Hello::__delete),
,
hashCode((int32_t(*)(Hello))&__Object::hashCode),
equals((bool(*)(Hello,Object))&__Object::equals),
getClass((Class(*)(Hello))&__Object::getClass),
toString(&__Hello::toString),
sayHi(&__Hello::sayHi),
sayHi_String(&__Hello::sayHi_String),
main_String(&__Hello::main_String),
{ }
};
}
}
