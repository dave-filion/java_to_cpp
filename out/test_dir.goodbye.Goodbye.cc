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
println;
}

void __Goodbye::sayBye_String (Goodbye __this,String name)
{
println;
}

void __Goodbye::sayBye (Goodbye __this)
{
println;
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

void __Goodbye::chainTest (Goodbye __this)
{
startsWith;
}

}
}
