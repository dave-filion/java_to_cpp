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
      static void sayHi(Goodbye);
      static void sayBye(Goodbye);
      static Class __class();
      static ____Goodbye_VT __vtable;
    };

    struct __Goodbye_VT {

      int32_t (*hashCode)(Goodbye);
      bool (*equals)(Goodbye, Object);
      Class (*getClass)(Goodbye);
      String (*toString)(Goodbye);
      void (*sayHi)(Goodbye);
      void (*main)(Goodbye, String);
      void (*sayBye)(Goodbye);
      __Goodbye_VT()
      : __isa(__Goodbye::__class()),
      __delete(&__Goodbye::__delete),
,
      hashCode((int32_t(*)(Goodbye))&__Object::hashCode),
      equals((bool(*)(Goodbye,Object))&__Object::equals),
      getClass((Class(*)(Goodbye))&__Object::getClass),
      toString((String(*)(Goodbye))&__Hello::toString),
      sayHi(&__Goodbye::sayHi),
      main((void(*)(Goodbye,String))&__Hello::main),
,
,
      sayBye(&__Goodbye::sayBye)      { }
    }
  }
}
