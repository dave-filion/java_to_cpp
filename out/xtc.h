#pragma once
#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"

using namespace java::lang;
namespace xtc{

namespace oop{

struct __Test;
struct __Test_VT;
typedef __rt::Ptr<__Test> Test;
}
}
namespace xtc{

namespace oop{

struct __Rest;
struct __Rest_VT;
typedef __rt::Ptr<__Rest> Rest;
}
}
namespace xtc{

namespace oop{


struct __Test{
__Test_VT* __vptr;
__Test();
static Object R1;
static Object R2;
static Object R3;
static Object R4;
int count;
static void __delete(__Test*);
static Object m1(Test);
static Object m2();
static Test m3(Test);
static Test m4(Test);
static Test m5_Test(Test,Test);
static Object m6_Test(Test,Test);
static Object m6_Rest(Test,Rest);
static Object m7_Object(Test,Object);
static Object m7_String(Test,String);
static Object m7_Test(Test,Test);
static Object m7_Rest(Test,Rest);
static Object m8_Test(Test,Test);
static Object m8_Rest(Test,Rest);
static Object m8_Test_Test(Test,Test,Test);
static Object m8_Rest_Test(Test,Rest,Test);
static Object m9_short(Test,short);
static Object m9_int(Test,int);
static Object m9_long(Test,long);
static Object m10_int(Test,int);
static Object m10_long(Test,long);
static void main();
static Class __class();
static __Test_VT __vtable;
};

struct __Test_VT {

Class __isa;
void (*__delete)(__Test*);
int32_t (*hashCode)(Test);
bool (*equals)(Test, Object);
Class (*getClass)(Test);
String (*toString)(Test);
Object (*m1)(Test);
Test (*m3)(Test);
Test (*m4)(Test);
Test (*m5_Test)(Test, Test);
Object (*m6_Test)(Test, Test);
Object (*m6_Rest)(Test, Rest);
Object (*m7_Object)(Test, Object);
Object (*m7_String)(Test, String);
Object (*m7_Test)(Test, Test);
Object (*m7_Rest)(Test, Rest);
Object (*m8_Test)(Test, Test);
Object (*m8_Rest)(Test, Rest);
Object (*m8_Test_Test)(Test, Test, Test);
Object (*m8_Rest_Test)(Test, Rest, Test);
Object (*m9_short)(Test, short);
Object (*m9_int)(Test, int);
Object (*m9_long)(Test, long);
Object (*m10_int)(Test, int);
Object (*m10_long)(Test, long);

__Test_VT()
: __isa(__Test::__class()),
__delete(&__Test::__delete),
hashCode((int32_t(*)(Test))&java::lang::__Object::hashCode),
equals((bool(*)(Test,Object))&java::lang::__Object::equals),
getClass((Class(*)(Test))&java::lang::__Object::getClass),
toString((String(*)(Test))&java::lang::__Object::toString),
m1((Object(*)(Test))&xtc::oop::__Test::m1),
m3((Test(*)(Test))&xtc::oop::__Test::m3),
m4((Test(*)(Test))&xtc::oop::__Test::m4),
m5_Test((Test(*)(Test,Test))&xtc::oop::__Test::m5_Test),
m6_Test((Object(*)(Test,Test))&xtc::oop::__Test::m6_Test),
m6_Rest((Object(*)(Test,Rest))&xtc::oop::__Test::m6_Rest),
m7_Object((Object(*)(Test,Object))&xtc::oop::__Test::m7_Object),
m7_String((Object(*)(Test,String))&xtc::oop::__Test::m7_String),
m7_Test((Object(*)(Test,Test))&xtc::oop::__Test::m7_Test),
m7_Rest((Object(*)(Test,Rest))&xtc::oop::__Test::m7_Rest),
m8_Test((Object(*)(Test,Test))&xtc::oop::__Test::m8_Test),
m8_Rest((Object(*)(Test,Rest))&xtc::oop::__Test::m8_Rest),
m8_Test_Test((Object(*)(Test,Test,Test))&xtc::oop::__Test::m8_Test_Test),
m8_Rest_Test((Object(*)(Test,Rest,Test))&xtc::oop::__Test::m8_Rest_Test),
m9_short((Object(*)(Test,short))&xtc::oop::__Test::m9_short),
m9_int((Object(*)(Test,int))&xtc::oop::__Test::m9_int),
m9_long((Object(*)(Test,long))&xtc::oop::__Test::m9_long),
m10_int((Object(*)(Test,int))&xtc::oop::__Test::m10_int),
m10_long((Object(*)(Test,long))&xtc::oop::__Test::m10_long){ }
};
}
}
namespace xtc{

namespace oop{


struct __Rest{
__Rest_VT* __vptr;
__Rest();
int round;
static void __delete(__Rest*);
static Object m1(Rest);
static Object m2();
static Test m4(Rest);
static Object m7_Test(Rest,Test);
static Object m9_short(Rest,short);
static Object m9_int(Rest,int);
static Object m9_long(Rest,long);
static Object m10_int(Rest,int);
static Object m10_long(Rest,long);
static int hashCode(Rest);
static void main();
static Class __class();
static __Rest_VT __vtable;
};

struct __Rest_VT {

Class __isa;
void (*__delete)(__Rest*);
int32_t (*hashCode)(Rest);
bool (*equals)(Rest, Object);
Class (*getClass)(Rest);
String (*toString)(Rest);
Object (*m1)(Rest);
Test (*m3)(Rest);
Test (*m4)(Rest);
Test (*m5_Test)(Rest, Test);
Object (*m6_Test)(Rest, Test);
Object (*m6_Rest)(Rest, Rest);
Object (*m7_Object)(Rest, Object);
Object (*m7_String)(Rest, String);
Object (*m7_Test)(Rest, Test);
Object (*m7_Rest)(Rest, Rest);
Object (*m8_Test)(Rest, Test);
Object (*m8_Rest)(Rest, Rest);
Object (*m8_Test_Test)(Rest, Test, Test);
Object (*m8_Rest_Test)(Rest, Rest, Test);
Object (*m9_short)(Rest, short);
Object (*m9_int)(Rest, int);
Object (*m9_long)(Rest, long);
Object (*m10_int)(Rest, int);
Object (*m10_long)(Rest, long);

__Rest_VT()
: __isa(__Rest::__class()),
__delete(&__Rest::__delete),
hashCode((int32_t(*)(Rest))&xtc::oop::__Rest::hashCode),
equals((bool(*)(Rest,Object))&java::lang::__Object::equals),
getClass((Class(*)(Rest))&java::lang::__Object::getClass),
toString((String(*)(Rest))&java::lang::__Object::toString),
m1((Object(*)(Rest))&xtc::oop::__Rest::m1),
m3((Test(*)(Rest))&xtc::oop::__Test::m3),
m4((Test(*)(Rest))&xtc::oop::__Rest::m4),
m5_Test((Test(*)(Rest,Test))&xtc::oop::__Test::m5_Test),
m6_Test((Object(*)(Rest,Test))&xtc::oop::__Test::m6_Test),
m6_Rest((Object(*)(Rest,Rest))&xtc::oop::__Test::m6_Rest),
m7_Object((Object(*)(Rest,Object))&xtc::oop::__Test::m7_Object),
m7_String((Object(*)(Rest,String))&xtc::oop::__Test::m7_String),
m7_Test((Object(*)(Rest,Test))&xtc::oop::__Rest::m7_Test),
m7_Rest((Object(*)(Rest,Rest))&xtc::oop::__Test::m7_Rest),
m8_Test((Object(*)(Rest,Test))&xtc::oop::__Test::m8_Test),
m8_Rest((Object(*)(Rest,Rest))&xtc::oop::__Test::m8_Rest),
m8_Test_Test((Object(*)(Rest,Test,Test))&xtc::oop::__Test::m8_Test_Test),
m8_Rest_Test((Object(*)(Rest,Rest,Test))&xtc::oop::__Test::m8_Rest_Test),
m9_short((Object(*)(Rest,short))&xtc::oop::__Rest::m9_short),
m9_int((Object(*)(Rest,int))&xtc::oop::__Rest::m9_int),
m9_long((Object(*)(Rest,long))&xtc::oop::__Rest::m9_long),
m10_int((Object(*)(Rest,int))&xtc::oop::__Rest::m10_int),
m10_long((Object(*)(Rest,long))&xtc::oop::__Rest::m10_long){ }
};
}
}
