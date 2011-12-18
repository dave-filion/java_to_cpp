#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"
#include "System.h"
#include "grimm.h"

using grimm::__Test;
using grimm::Test;
using grimm::__Rest;
using grimm::Rest;

namespace grimm{

__Test::__Test() : __vptr(&__vtable) { }
Object __Test::R1 = new __Object(  );
Object __Test::R2 = new __Object(  );
Object __Test::R3 = new __Object(  );
Object __Test::R4 = new __Object(  );
void __Test::__delete(__Test* __this){
delete __this;
}

Object __Test::m1 (Test __this)
{
return __Test::R1;
}

Object __Test::m2 ()
{
return __Test::R3;
}

Test __Test::m3 (Test __this)
{
__this -> count++;
return __this;
}

Test __Test::m4 (Test __this)
{
__this -> count++;
return __this;
}

Test __Test::m5_Test (Test __this,Test t)
{
return t->__vptr ->m3(t)->__vptr ->m4(__this);
}

Object __Test::m6_Test (Test __this,Test t)
{
return __Test::R1;
}

Object __Test::m6_Rest (Test __this,Rest r)
{
return __Test::R2;
}

Object __Test::m7_Object (Test __this,Object o)
{
return __Test::R3;
}

Object __Test::m7_String (Test __this,String s)
{
return __Test::R4;
}

Object __Test::m7_Test (Test __this,Test t)
{
return __Test::R1;
}

Object __Test::m7_Rest (Test __this,Rest r)
{
return __Test::R2;
}

Object __Test::m8_Test (Test __this,Test t)
{
return __Test::R1;
}

Object __Test::m8_Rest (Test __this,Rest r)
{
return __Test::R2;
}

Object __Test::m8_Test_Test (Test __this,Test t1,Test t2)
{
return __Test::R3;
}

Object __Test::m8_Rest_Test (Test __this,Rest r,Test t)
{
return __Test::R4;
}

Object __Test::m9_short (Test __this,short n1)
{
return __rt::null();
}

Object __Test::m9_int (Test __this,int n1)
{
return __rt::null();
}

Object __Test::m9_long (Test __this,long n1)
{
return __rt::null();
}

Object __Test::m10_int (Test __this,int n1)
{
return __rt::null();
}

Object __Test::m10_long (Test __this,long n1)
{
return __rt::null();
}

void __Test::main()
{
short n = 1;
Test t;
Rest r;
Object o = __rt::null();
int test = 0;
int success = 0;
System::out.println(__rt::literal("PASS Test.main()"));
success++;
test++;
if (__Test::R1!=__rt::null() && __Test::R1!=__Test::R2 && __Test::R1!=__Test::R3 && __Test::R1!=__Test::R4){
System::out.println(__rt::literal("PASS Object.<init>()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL Object.<init>()"));
}
test++;
r=new __Rest(  );
o=r->__vptr ->m1(r);
if (__Test::R2==o){
System::out.println(__rt::literal("PASS r.m1()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m1()"));
}
test++;
t=r;
if (t==r){
System::out.println(__rt::literal("PASS t == r"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t == r"));
}
test++;
if (t->__vptr ->equals(t,r)){
System::out.println(__rt::literal("PASS t.equals(r)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.equals(r)"));
}
test++;
if (r->__vptr ->equals(r,t)){
System::out.println(__rt::literal("PASS r.equals(t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.equals(t)"));
}
test++;
int h = r->__vptr ->hashCode(r);
if (7353==h){
System::out.println(__rt::literal("PASS 7353 == r.hashCode()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL 7353 == r.hashCode()"));
}
test++;
String s1 = t->__vptr ->toString(t);
String s2 = r->__vptr ->toString(r);
if (s1-> __vptr ->equals(s1,s2)){
System::out.println(__rt::literal("PASS t.toString().equals(r.toString())"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.toString().equals(r.toString())"));
}
test++;
o=t->__vptr ->m1(t);
if (__Test::R2==o){
System::out.println(__rt::literal("PASS t.m1()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m1()"));
}
test++;
o=__Test::m2();
if (__Test::R3==o){
System::out.println(__rt::literal("PASS Test.m2()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL Test.m2()"));
}
test++;
o=t->m2();
if (__Test::R3==o){
System::out.println(__rt::literal("PASS t.m2()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m2()"));
}
test++;
}

Class __Test::__class() {
static Class k = new __Class(__rt::literal("Test"), __Object::__class());
return k;
}
__Test_VT __Test::__vtable;
}
namespace grimm{

__Rest::__Rest() : __vptr(&__vtable) { }
void __Rest::__delete(__Rest* __this){
delete __this;
}

Object __Rest::m1 (Rest __this)
{
return __Test::R2;
}

Object __Rest::m2 ()
{
return __Test::R4;
}

Test __Rest::m4 (Rest __this)
{
__this -> round++;
return __this;
}

Object __Rest::m7_Test (Rest __this,Test t)
{
return __Test::R3;
}

Object __Rest::m9_short (Rest __this,short n1)
{
return __Test::R1;
}

Object __Rest::m9_int (Rest __this,int n1)
{
return __Test::R2;
}

Object __Rest::m9_long (Rest __this,long n1)
{
return __Test::R3;
}

Object __Rest::m10_int (Rest __this,int n1)
{
return __Test::R2;
}

Object __Rest::m10_long (Rest __this,long n1)
{
return __Test::R3;
}

int __Rest::hashCode (Rest __this)
{
return 7353;
}

void __Rest::main()
{
System::out.println(__rt::literal("FAIL Test.main()"));
System::out.println();
System::out.println(__rt::literal("0 out of n tests have passed."));
}

Class __Rest::__class() {
static Class k = new __Class(__rt::literal("Rest"), __Test::__class());
return k;
}
__Rest_VT __Rest::__vtable;
}
