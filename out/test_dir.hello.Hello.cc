#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"
#include "test_dir.hello.Hello.h"
namespace test_dir{

namespace hello{

void __Hello::__delete(__Hello* __this){
delete __this;
}

void __Hello::sayHi (Hello __this)
{
System.out.println(__rt::literal("Hi"));
}

void __Hello::sayHi_String (Hello __this,String name)
{
System.out.println(__rt::literal("HIII"));
}

void __Hello::main_String (Hello __this,String args)
{
System.out.println(__rt::literal("Hello world"));
}

String __Hello::toString (Hello __this)
{
return __rt::literal("HELLO");
}

}
}
