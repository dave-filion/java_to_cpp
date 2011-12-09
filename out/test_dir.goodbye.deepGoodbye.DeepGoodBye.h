#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"


using namespace java::lang;
namespace test_dir{

  namespace goodbye{

    namespace deepGoodbye{

      struct __DeepGoodBye;
      struct ____DeepGoodBye_VT;
      typedef __rt::Ptr<__DeepGoodBye> DeepGoodBye;

      struct __DeepGoodBye{
        __DeepGoodBye_VT* __vptr;
        __DeepGoodBye();
        static void __delete(__DeepGoodBye*);
        static Class __class();
        static ____DeepGoodBye_VT __vtable;
      };

      struct __DeepGoodBye_VT {

        int32_t (*hashCode)(DeepGoodBye);
        bool (*equals)(DeepGoodBye, Object);
        Class (*getClass)(DeepGoodBye);
        String (*toString)(DeepGoodBye);

        __DeepGoodBye_VT()
        : __isa(__DeepGoodBye::__class()),
        __delete(&__DeepGoodBye::__delete),
,
        hashCode((int32_t(*)(DeepGoodBye))&__Object::hashCode),
        equals((bool(*)(DeepGoodBye,Object))&__Object::equals),
        getClass((Class(*)(DeepGoodBye))&__Object::getClass),
        toString((String(*)(DeepGoodBye))&__Object::toString)        { }
      }
    }
  }
}
