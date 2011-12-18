#include <stdint.h>
#include <string>
#include <iostream>
#include <cstring>
#include "ptr.h"
#include "java_lang.h"
#include "System.h"
#include "grimm.h"

using grimm::__Rest;
using grimm::Rest;
using grimm::__Test;
using grimm::Test;

namespace grimm{

__Rest::__Rest() : __vptr(&__vtable) { }
void __Rest::__delete(__Rest* __this){
delete __this;
}

Object __Rest::m1 (Rest __this)
{
return Test->R2;
}

Object __Rest::m2 (Rest __this)
{
return Test->R4;
}

Test __Rest::m4 (Rest __this)
{
round++;
return __this;
}

Object __Rest::m7_Test (Rest __this,Test t)
{
return R3;
}

Object __Rest::m9_short (Rest __this,short n1)
{
return R1;
}

Object __Rest::m9_int (Rest __this,int n1)
{
return R2;
}

Object __Rest::m9_long (Rest __this,long n1)
{
return R3;
}

Object __Rest::m10_int (Rest __this,int n1)
{
return R2;
}

Object __Rest::m10_long (Rest __this,long n1)
{
return R3;
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
namespace grimm{

__Test::__Test() : __vptr(&__vtable) { }
void __Test::__delete(__Test* __this){
delete __this;
}

Object __Test::m1 (Test __this)
{
return R1;
}

Object __Test::m2 (Test __this)
{
return R3;
}

Test __Test::m3 (Test __this)
{
count++;
return __this;
}

Test __Test::m4 (Test __this)
{
count++;
return __this;
}

Test __Test::m5_Test (Test __this,Test t)
{
return t->__vptr ->m3(t)->__vptr ->m4(__this);
}

Object __Test::m6_Test (Test __this,Test t)
{
return R1;
}

Object __Test::m6_Rest (Test __this,Rest r)
{
return R2;
}

Object __Test::m7_Object (Test __this,Object o)
{
return R3;
}

Object __Test::m7_String (Test __this,String s)
{
return R4;
}

Object __Test::m7_Test (Test __this,Test t)
{
return R1;
}

Object __Test::m7_Rest (Test __this,Rest r)
{
return R2;
}

Object __Test::m8_Test (Test __this,Test t)
{
return R1;
}

Object __Test::m8_Rest (Test __this,Rest r)
{
return R2;
}

Object __Test::m8_Test_Test (Test __this,Test t1,Test t2)
{
return R3;
}

Object __Test::m8_Rest_Test (Test __this,Rest r,Test t)
{
return R4;
}

Object __Test::m9_short (Test __this,short n1)
{
return ;
}

Object __Test::m9_int (Test __this,int n1)
{
return ;
}

Object __Test::m9_long (Test __this,long n1)
{
return ;
}

Object __Test::m10_int (Test __this,int n1)
{
return ;
}

Object __Test::m10_long (Test __this,long n1)
{
return ;
}

void __Test::main()
{
short n = 1;
Test t;
Rest r;
Object o = ;
int test = 0;
int success = 0;
System::out.println(__rt::literal("PASS Test.main()"));
success++;
test++;
if (R1!= && R1!=R2 && R1!=R3 && R1!=R4){
System::out.println(__rt::literal("PASS Object.<init>()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL Object.<init>()"));
}
test++;
r=new __Rest(  );
o=r->__vptr ->m1(r);
if (R2==o){
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
if (t(t,r)){
System::out.println(__rt::literal("PASS t.equals(r)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.equals(r)"));
}
test++;
if (r(r,t)){
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
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS t.toString().equals(r.toString())"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.toString().equals(r.toString())"));
}
test++;
o=t->__vptr ->m1(t);
if (R2==o){
System::out.println(__rt::literal("PASS t.m1()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m1()"));
}
test++;
o=Rest.m2DO_ARGS_HERE;
if (R4==o){
System::out.println(__rt::literal("PASS Rest.m2()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL Rest.m2()"));
}
test++;
o=r.m2(r);
if (R4==o){
System::out.println(__rt::literal("PASS r.m2()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m2()"));
}
test++;
Test tr = r;
o=tr.m2(tr);
if (R3==o){
System::out.println(__rt::literal("PASS tr.m2()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL tr.m2()"));
}
test++;
o=Test.m2DO_ARGS_HERE;
if (R3==o){
System::out.println(__rt::literal("PASS Test.m2()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL Test.m2()"));
}
test++;
o=t.m2(t);
if (R3==o){
System::out.println(__rt::literal("PASS t.m2()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m2()"));
}
test++;
t=new __Test(  );
if (t!=r){
System::out.println(__rt::literal("PASS t != r"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t != r"));
}
test++;
if (t(t,r)){
System::out.println(__rt::literal("PASS ! t.equals(r)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL ! t.equals(r)"));
}
test++;
s1=t->__vptr ->toString(t);
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS ! t.toString().equals(r.toString())"));
success++;
}
else {
System::out.println(__rt::literal("FAIL ! t.toString().equals(r.toString())"));
}
test++;
o=t->__vptr ->m1(t);
if (R1==o){
System::out.println(__rt::literal("PASS t.m1()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m1()"));
}
test++;
o=t;
if (oTest ){
System::out.println(__rt::literal("PASS o instanceof Test"));
success++;
}
else {
System::out.println(__rt::literal("FAIL o instanceof Test"));
}
test++;
if (oObject ){
System::out.println(__rt::literal("PASS o instanceof Object"));
success++;
}
else {
System::out.println(__rt::literal("FAIL o instanceof Object"));
}
test++;
if (oString ){
System::out.println(__rt::literal("PASS ! (o instanceof String)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL ! (o instanceof String)"));
}
test++;
o=->__vptr ->m1(__this);
if (R1==o){
System::out.println(__rt::literal("PASS (Test)o"));
success++;
}
else {
System::out.println(__rt::literal("FAIL (Test)o"));
}
test++;
o=t.m2(t);
if (R3==o){
System::out.println(__rt::literal("PASS t.m2()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m2()"));
}
test++;
if (0==t->count){
System::out.println(__rt::literal("PASS Test.<init>()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL Test.<init>()"));
}
test++;
if (0==r->round && 0==r->count){
System::out.println(__rt::literal("PASS Rest.<init>()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL Rest.<init>()"));
}
test++;
t->__vptr ->m3(t)->__vptr ->m4(__this);
if (2==t->count){
System::out.println(__rt::literal("PASS t.m3().m4()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m3().m4()"));
}
test++;
r->__vptr ->m3(r)->__vptr ->m4(__this);
if (1==r->round && 1==r->count){
System::out.println(__rt::literal("PASS r.m3().m4()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m3().m4()"));
}
test++;
t->count=0;
t->__vptr ->m5_Test(t,t)->__vptr ->m3(__this)->__vptr ->m4(__this);
if (4==t->count){
System::out.println(__rt::literal("PASS t.m5(t).m3().m4()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m5(t).m3().m4()"));
}
test++;
r->count=0;
r->round=0;
r(r,r)->__vptr ->m3(__this)->__vptr ->m4(__this);
if (2==r->round && 2==r->count){
System::out.println(__rt::literal("PASS r.m5(r).m3().m4()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m5(r).m3().m4()"));
}
test++;
t->count=0;
r->count=0;
r->round=0;
r->__vptr ->m5_Test(r,t)->__vptr ->m3(__this)->__vptr ->m4(__this);
if (0==r->round && 0==r->count && 4==t->count){
System::out.println(__rt::literal("PASS r.m5(t).m3().m4()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m5(t).m3().m4()"));
}
test++;
o=t->__vptr ->m6_Test(t,t);
if (R1==o){
System::out.println(__rt::literal("PASS t.m6(t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m6(t)"));
}
test++;
o=t->__vptr ->m6_Rest(t,r);
if (R2==o){
System::out.println(__rt::literal("PASS t.m6(r)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m6(r)"));
}
test++;
o=r->__vptr ->m6_Test(r,t);
if (R1==o){
System::out.println(__rt::literal("PASS r.m6(t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m6(t)"));
}
test++;
o=r->__vptr ->m6_Rest(r,r);
if (R2==o){
System::out.println(__rt::literal("PASS r.m6(r)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m6(r)"));
}
test++;
o=t->__vptr ->m7_Test(t,t);
if (R1==o){
System::out.println(__rt::literal("PASS t.m7(t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m7(t)"));
}
test++;
o=t->__vptr ->m7_Rest(t,r);
if (R2==o){
System::out.println(__rt::literal("PASS t.m7(r)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m7(r)"));
}
test++;
o=t->__vptr ->m7_Object(t,o);
if (R3==o){
System::out.println(__rt::literal("PASS t.m7(o)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m7(o)"));
}
test++;
o=t->__vptr ->m7_String(t,s1);
if (R4==o){
System::out.println(__rt::literal("PASS t.m7(s1)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m7(s1)"));
}
test++;
o=r->__vptr ->m7_Test(r,t);
if (R3==o){
System::out.println(__rt::literal("PASS r.m7(t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m7(t)"));
}
test++;
o=r->__vptr ->m7_Rest(r,r);
if (R2==o){
System::out.println(__rt::literal("PASS r.m7(r)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m7(r)"));
}
test++;
o=t->__vptr ->m8_Test(t,t);
if (R1==o){
System::out.println(__rt::literal("PASS t.m8(t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m8(t)"));
}
test++;
o=t->__vptr ->m8_Rest(t,r);
if (R2==o){
System::out.println(__rt::literal("PASS t.m8(r)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL t.m8(r)"));
}
test++;
o=r->__vptr ->m8_Test(r,t);
if (R1==o){
System::out.println(__rt::literal("PASS r.m8(t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m8(t)"));
}
test++;
o=r->__vptr ->m8_Rest(r,r);
if (R2==o){
System::out.println(__rt::literal("PASS r.m8(r)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m8(r)"));
}
test++;
o=r->__vptr ->m8_Test_Test(r,t,t);
if (R3==o){
System::out.println(__rt::literal("PASS r.m8(t, t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m8(t, t)"));
}
test++;
o=r->__vptr ->m8_Test_Test(r,tr,t);
if (R3==o){
System::out.println(__rt::literal("PASS r.m8(tr, t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m8(tr, t)"));
}
test++;
o=r->__vptr ->m8_Rest_Test(r,r,t);
if (R4==o){
System::out.println(__rt::literal("PASS r.m8(r, t)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m8(r, t)"));
}
test++;
o=r->__vptr ->m9_short(r,n);
if (R1==o){
System::out.println(__rt::literal("PASS r.m9(n)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m9(n)"));
}
test++;
o=r(r,n,n);
if (R2==o){
System::out.println(__rt::literal("PASS r.m9(n+n)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m9(n+n)"));
}
test++;
o=r(r,n,5l);
if (R3==o){
System::out.println(__rt::literal("PASS r.m9(n+5l)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m9(n+5l)"));
}
test++;
o=r(r,n);
if (R2==o){
System::out.println(__rt::literal("PASS r.m10(n)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m10(n)"));
}
test++;
o=r(r,n,n);
if (R2==o){
System::out.println(__rt::literal("PASS r.m10(n+n)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL r.m10(n+n)"));
}
test++;
Class k1 = t->__vptr ->getClass(t);
Class k2 = r->__vptr ->getClass(r);
if (k1!=k2){
System::out.println(__rt::literal("PASS k1 != k2"));
success++;
}
else {
System::out.println(__rt::literal("FAIL K1 != k2"));
}
test++;
if (k1.getNameDO_ARGS_HERE(__this,__rt::literal("xtc.oop.Test"))){
System::out.println(__rt::literal("PASS k1.getName().equals(\"xtc.oop.Test\")"));
success++;
}
else {
System::out.println(__rt::literal("FAIL k1.getName().equals(\"xtc.oop.Test\")"));
}
test++;
if ((__this,k1,k1(__this))){
System::out.println(__rt::literal("PASS \"xtc.oop.Test\".equals(k1.getName())"));
success++;
}
else {
System::out.println(__rt::literal("FAIL \"xtc.oop.Test\".equals(k1.getName())"));
}
test++;
if (k1.toStringDO_ARGS_HERE(__this,__rt::literal("class xtc.oop.Test"))){
System::out.println(__rt::literal("PASS k1.toString().equals(\"class xtc.oop.Test\")"));
success++;
}
else {
System::out.println(__rt::literal("FAIL k1.toString().equals(\"class xtc.oop.Test\")"));
}
test++;
if (k1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS ! k1.equals(k2)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL ! k1.equals(k2)"));
}
test++;
k2=k2.getSuperclassDO_ARGS_HERE;
if (k1==k2){
System::out.println(__rt::literal("PASS k1 == k2.super()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL K1 == k2.super()"));
}
test++;
if (k1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS k1.equals(k2.super())"));
success++;
}
else {
System::out.println(__rt::literal("FAIL k1.equals(k2.super())"));
}
test++;
k1=k1.getSuperclassDO_ARGS_HERE;
k2=k2.getSuperclassDO_ARGS_HERE;
if (k1==k2){
System::out.println(__rt::literal("PASS k1.super() == k2.super().super()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL K1.super() == k2.super().super()"));
}
test++;
if (k1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS k1.super().equals(k2.super().super())"));
success++;
}
else {
System::out.println(__rt::literal("FAIL k1.super().equals(k2.super().super())"));
}
test++;
k1=k1.getSuperclassDO_ARGS_HERE;
if (==k1){
System::out.println(__rt::literal("PASS null == k1.super().super()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL null == k1.super().super()"));
}
test++;
s1=__rt::literal("Hello Kitty #1");
s2=__rt::literal("Hello Kitty #1");
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS s1.equals(String)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL s1.equals(String)"));
}
test++;
s2=__rt::literal("Hel") + __rt::literal("lo Kitty #1");
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS s1.equals(String + String)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL s1.equals(String + String)"));
}
test++;
s2=__rt::literal("He") + __rt::literal("ll") + __rt::literal("o Kitty #1");
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS s1.equals(String + String + String)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL s1.equals(String + String + String)"));
}
test++;
s2=__rt::literal("Hello Kitty #") + 1;
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS s1.equals(String + int)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL s1.equals(String + int)"));
}
test++;
s2=__rt::literal("Hello Kitty #") + ;
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS s1.equals(String + char)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL s1.equals(String + char)"));
}
test++;
s2=char 72 + __rt::literal("ello Kitty #1");
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS s1.equals(char + String)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL s1.equals(char + String)"));
}
test++;
char c = 72;
s2=c + __rt::literal("ello Kitty #1");
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS s1.equals(char + String)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL s1.equals(char + String)"));
}
test++;
s2= + __rt::literal("ello Kitty #1");
if (s1.equalsDO_ARGS_HERE){
System::out.println(__rt::literal("PASS s1.equals(char + String)"));
success++;
}
else {
System::out.println(__rt::literal("FAIL s1.equals(char + String)"));
}
test++;
short a0 = short 0;
if (a0->length==0){
System::out.println(__rt::literal("PASS short[0].length"));
success++;
}
else {
System::out.println(__rt::literal("FAIL short[0].length"));
}
test++;
short a1 = short 1;
if (a1->length==1){
System::out.println(__rt::literal("PASS short[1].length"));
success++;
}
else {
System::out.println(__rt::literal("FAIL short[1].length"));
}
test++;
short a2 = short 2;
if (a2->length==2){
System::out.println(__rt::literal("PASS short[2].length"));
success++;
}
else {
System::out.println(__rt::literal("FAIL short[2].length"));
}
test++;
if (a10==0 && a20==0 && a21==0){
System::out.println(__rt::literal("PASS short[i] == 0"));
success++;
}
else {
System::out.println(__rt::literal("FAIL short[i] == 0"));
}
test++;
a10=short 32768;
if (a10==-32768){
System::out.println(__rt::literal("PASS short[0] = (short)32768"));
success++;
}
else {
System::out.println(__rt::literal("FAIL short[0] = (short)32768"));
}
test++;
if (a0.getClassDO_ARGS_HERE(__this)(__this,__rt::literal("[S"))){
System::out.println(__rt::literal("PASS short[0].getClass().getName()"));
success++;
}
else {
System::out.println(__rt::literal("FAIL short[0].getClass().getName()"));
}
test++;
System::out.println();
System::out.println(success);
}

Class __Test::__class() {
static Class k = new __Class(__rt::literal("Test"), __Object::__class());
return k;
}
__Test_VT __Test::__vtable;
}
