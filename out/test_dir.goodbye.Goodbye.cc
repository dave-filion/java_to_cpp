#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"
#include "test_dir.goodbye.Goodbye.h"
namespace test_dir{

namespace goodbye{

void __Goodbye::__delete(__Goodbye* __this){
delete __this;
}

void __Goodbye::sayHi (Goodbye __this)
{
System.out;
}

void __Goodbye::sayBye_String (Goodbye __this,String name)
{
int dave = 5;
String bob = __rt::literal("hey there");
System.out;
}

void __Goodbye::sayBye (Goodbye __this)
{
System.out;
}

void __Goodbye::forTest (Goodbye __this)
{
int q = 5;
for(int i = 0;i<10;i++){
q+=1;
}
}

void __Goodbye::whileTest (Goodbye __this)
{
int i = 0;
while (i<10){
i++;
}
}

String __Goodbye::returnString (Goodbye __this)
{
return __rt::literal("Hi");
}

String __Goodbye::returnString_int (Goodbye __this,int something)
{
return __rt::literal("Hi 2");
}

void __Goodbye::chainTest (Goodbye __this)
{
returnString_int;
}

}
}
