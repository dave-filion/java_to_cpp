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
String something = __this->__vptr ->returnString_int(__this,null__this->__vptr ->returnInt_String(__this,new __String()));
}

void __Goodbye::main_String (Goodbye __this,String args)
{
Goodbye b = new __Goodbye(  );
b->__vptr ->chainTest(__this);
}

}
}
