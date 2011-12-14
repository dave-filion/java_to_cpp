#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"
#include "System.h"
#include "easy_test.Hello.h"
namespace easy_test{

__Hello::__Hello() : __vptr(&__vtable) { }
void __Hello::__delete(__Hello* __this){
delete __this;
}

void __Hello::hello (Hello __this)
{
System::out.println(__rt::literal("Hey there"));
}

void __Hello::hello_String (Hello __this,String name)
{
System::out.println(name);
}

void __Hello::main()
{
Hello h = new __Hello(  );
h->__vptr ->hello(h);
h->__vptr ->hello_String(h,__rt::literal("dave"));
}

Class __Hello::__class() {
static Class k = new __Class(__rt::literal("Hello"), __Object::__class());
return k;
}
__Hello_VT __Hello::__vtable;
}
