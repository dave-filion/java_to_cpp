#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"
#include "System.h"
#include "test_dir.h"

using test_dir::hello::__Hello;
using test_dir::hello::Hello;
using test_dir::goodbye::deepGoodbye::__DeepGoodBye;
using test_dir::goodbye::deepGoodbye::DeepGoodBye;
using test_dir::goodbye::__Goodbye;
using test_dir::goodbye::Goodbye;

namespace test_dir{

namespace hello{

__Hello::__Hello() : __vptr(&__vtable) { }
void __Hello::__delete(__Hello* __this){
delete __this;
}

void __Hello::sayHi (Hello __this)
{
System::out.println(__rt::literal("Hi"));
}

void __Hello::sayHi_String (Hello __this,String name)
{
System::out.println(__rt::literal("HIII"));
}

void __Hello::main()
{
System::out.println(__rt::literal("Hello world"));
}

String __Hello::toString (Hello __this)
{
return __rt::literal("HELLO");
}

Class __Hello::__class() {
static Class k = new __Class(__rt::literal("Hello"), __Object::__class());
return k;
}
__Hello_VT __Hello::__vtable;
}
}
namespace test_dir{

namespace goodbye{

namespace deepGoodbye{

__DeepGoodBye::__DeepGoodBye() : __vptr(&__vtable) { }
void __DeepGoodBye::__delete(__DeepGoodBye* __this){
delete __this;
}

Class __DeepGoodBye::__class() {
static Class k = new __Class(__rt::literal("DeepGoodBye"), __Object::__class());
return k;
}
__DeepGoodBye_VT __DeepGoodBye::__vtable;
}
}
}
namespace test_dir{

namespace goodbye{

__Goodbye::__Goodbye() : __vptr(&__vtable) { }
void __Goodbye::__delete(__Goodbye* __this){
delete __this;
}

String __Goodbye::returnString (Goodbye __this)
{
return __rt::literal("Hi");
}

int __Goodbye::returnInt_String (Goodbye __this,String word)
{
return 42;
}

String __Goodbye::returnString_int (Goodbye __this,int something)
{
return __rt::literal("Hi 2");
}

void __Goodbye::chainTest (Goodbye __this)
{
__this->__vptr ->returnString_int(__this,4);
String something = __this->__vptr ->returnString_int(__this,__this->__vptr ->returnInt_String(__this,__rt::literal("Hi There")));
}

void __Goodbye::main()
{
Goodbye b = new __Goodbye(  );
b->__vptr ->chainTest(b);
}

Class __Goodbye::__class() {
static Class k = new __Class(__rt::literal("Goodbye"), __Hello::__class());
return k;
}
__Goodbye_VT __Goodbye::__vtable;
}
}
