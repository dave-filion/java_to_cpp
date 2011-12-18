/*
 * Object-Oriented Programming
 * Copyright (C) 2006-2011 Robert Grimm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package xtc.oop;

public class Test {

  public static final Object R1 = new Object();
  public static final Object R2 = new Object();
  public static final Object R3 = new Object();
  public static final Object R4 = new Object();

  public int count;

  public Test() {
    count = 0;
  }

  public Object m1() {
    return R1;
  }

  public static Object m2() {
    return R3;
  }

  public Test m3() {
    count++;
    return this;
  }

  public Test m4() {
    count++;
    return this;
  }

  public Test m5(Test t) {
    return t.m3().m4();
  }

  public Object m6(Test t) {
    return R1;
  }

  public Object m6(Rest r) {
    return R2;
  }

  public Object m7(Object o) {
    return R3;
  }

  public Object m7(String s) {
    return R4;
  }

  public Object m7(Test t) {
    return R1;
  }

  public Object m7(Rest r) {
    return R2;
  }

  public Object m8(Test t) {
    return R1;
  }

  public Object m8(Rest r) {
    return R2;
  }

  public Object m8(Test t1, Test t2) {
    return R3;
  }

  public Object m8(Rest r, Test t) {
    return R4;
  }

  public Object m9(short n1) {
    return null;
  }

  public Object m9(int n1) {
    return null;
  }

  public Object m9(long n1) {
    return null;
  }

  public Object m10(int n1) {
    return null;
  }

  public Object m10(long n1) {
    return null;
  }

  public static void main(String[] args) {
    short n = 1;
    Test t;
    Rest r;
    Object o = null;

    int test = 0;
    int success = 0;

    // -----------------------------------------------------------------------

    System.out.println("PASS Test.main()");
    success++;
    test++;

    // -----------------------------------------------------------------------

    if ((R1 != null) && (R1 != R2) && (R1 != R3) && (R1 != R4)) {
      System.out.println("PASS Object.<init>()");
      success++;
    } else {
      System.out.println("FAIL Object.<init>()");
    }
    test++;

    // -----------------------------------------------------------------------

    r = new Rest();
    o = r.m1();

    if (R2 == o) {
      System.out.println("PASS r.m1()");
      success++;
    } else {
      System.out.println("FAIL r.m1()");
    }
    test++;

    // -----------------------------------------------------------------------

    t = r;

    if (t == r) {
      System.out.println("PASS t == r");
      success++;
    } else {
      System.out.println("FAIL t == r");
    }
    test++;

    // -----------------------------------------------------------------------

    if (t.equals(r)) {
      System.out.println("PASS t.equals(r)");
      success++;
    } else {
      System.out.println("FAIL t.equals(r)");
    }
    test++;

    // -----------------------------------------------------------------------

    if (r.equals(t)) {
      System.out.println("PASS r.equals(t)");
      success++;
    } else {
      System.out.println("FAIL r.equals(t)");
    }
    test++;

    // -----------------------------------------------------------------------

    int h = r.hashCode();

    if (7353 == h) {
      System.out.println("PASS 7353 == r.hashCode()");
      success++;
    } else {
      System.out.println("FAIL 7353 == r.hashCode()");
    }
    test++;

    // -----------------------------------------------------------------------

    String s1 = t.toString();
    String s2 = r.toString();

    if (s1.equals(s2)) {
      System.out.println("PASS t.toString().equals(r.toString())");
      success++;
    } else {
      System.out.println("FAIL t.toString().equals(r.toString())");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m1();

    if (R2 == o) {
      System.out.println("PASS t.m1()");
      success++;
    } else {
      System.out.println("FAIL t.m1()");
    }
    test++;

    // -----------------------------------------------------------------------

    o = Rest.m2();

    if (R4 == o) {
      System.out.println("PASS Rest.m2()");
      success++;
    } else {
      System.out.println("FAIL Rest.m2()");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m2();

    if (R4 == o) {
      System.out.println("PASS r.m2()");
      success++;
    } else {
      System.out.println("FAIL r.m2()");
    }
    test++;

    // -----------------------------------------------------------------------

    Test tr = r;

    o = tr.m2();

    if (R3 == o) {
      System.out.println("PASS tr.m2()");
      success++;
    } else {
      System.out.println("FAIL tr.m2()");
    }
    test++;

    // -----------------------------------------------------------------------

    o = Test.m2();

    if (R3 == o) {
      System.out.println("PASS Test.m2()");
      success++;
    } else {
      System.out.println("FAIL Test.m2()");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m2();

    if (R3 == o) {
      System.out.println("PASS t.m2()");
      success++;
    } else {
      System.out.println("FAIL t.m2()");
    }
    test++;

    // -----------------------------------------------------------------------

    t = new Test();

    if (t != r) {
      System.out.println("PASS t != r");
      success++;
    } else {
      System.out.println("FAIL t != r");
    }
    test++;

    // -----------------------------------------------------------------------

    if (! t.equals(r)) {
      System.out.println("PASS ! t.equals(r)");
      success++;
    } else {
      System.out.println("FAIL ! t.equals(r)");
    }
    test++;

    // -----------------------------------------------------------------------

    s1 = t.toString();

    if (! s1.equals(s2)) {
      System.out.println("PASS ! t.toString().equals(r.toString())");
      success++; 
    } else {
      System.out.println("FAIL ! t.toString().equals(r.toString())");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m1();

    if (R1 == o) {
      System.out.println("PASS t.m1()");
      success++;
    } else {
      System.out.println("FAIL t.m1()");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t;

    if (o instanceof Test) {
      System.out.println("PASS o instanceof Test");
      success++;
    } else {
      System.out.println("FAIL o instanceof Test");
    }
    test++;

    // -----------------------------------------------------------------------

    if (o instanceof Object) {
      System.out.println("PASS o instanceof Object");
      success++;
    } else {
      System.out.println("FAIL o instanceof Object");
    }
    test++;

    // -----------------------------------------------------------------------

    if (! (o instanceof String)) {
      System.out.println("PASS ! (o instanceof String)");
      success++;
    } else {
      System.out.println("FAIL ! (o instanceof String)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = ((Test)o).m1();

    if (R1 == o) {
      System.out.println("PASS (Test)o");
      success++;
    } else {
      System.out.println("FAIL (Test)o");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m2();

    if (R3 == o) {
      System.out.println("PASS t.m2()");
      success++;
    } else {
      System.out.println("FAIL t.m2()");
    }
    test++;

    // -----------------------------------------------------------------------

    if (0 == t.count) {
      System.out.println("PASS Test.<init>()");
      success++;
    } else {
      System.out.println("FAIL Test.<init>()");
    }
    test++;

    // -----------------------------------------------------------------------

    if ((0 == r.round) && (0 == r.count)) {
      System.out.println("PASS Rest.<init>()");
      success++;
    } else {
      System.out.println("FAIL Rest.<init>()");
    }
    test++;

    // -----------------------------------------------------------------------

    t.m3().m4();

    if (2 == t.count) {
      System.out.println("PASS t.m3().m4()");
      success++;
    } else {
      System.out.println("FAIL t.m3().m4()");
    }
    test++;

    // -----------------------------------------------------------------------

    r.m3().m4();

    if ((1 == r.round) && (1 == r.count)) {
      System.out.println("PASS r.m3().m4()");
      success++;
    } else {
      System.out.println("FAIL r.m3().m4()");
    }
    test++;

    // -----------------------------------------------------------------------

    t.count = 0;
    t.m5(t).m3().m4();

    if (4 == t.count) {
      System.out.println("PASS t.m5(t).m3().m4()");
      success++;
    } else {
      System.out.println("FAIL t.m5(t).m3().m4()");
    }
    test++;

    // -----------------------------------------------------------------------

    r.count = 0;
    r.round = 0;
    r.m5(r).m3().m4();

    if ((2 == r.round) && (2 == r.count)) {
      System.out.println("PASS r.m5(r).m3().m4()");
      success++;
    } else {
      System.out.println("FAIL r.m5(r).m3().m4()");
    }
    test++;

    // -----------------------------------------------------------------------

    t.count = 0;
    r.count = 0;
    r.round = 0;
    r.m5(t).m3().m4();

    if ((0 == r.round) && (0 == r.count) && (4 == t.count)) {
      System.out.println("PASS r.m5(t).m3().m4()");
      success++;
    } else {
      System.out.println("FAIL r.m5(t).m3().m4()");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m6(t);

    if (R1 == o) {
      System.out.println("PASS t.m6(t)");
      success++;
    } else {
      System.out.println("FAIL t.m6(t)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m6(r);

    if (R2 == o) {
      System.out.println("PASS t.m6(r)");
      success++;
    } else {
      System.out.println("FAIL t.m6(r)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m6(t);

    if (R1 == o) {
      System.out.println("PASS r.m6(t)");
      success++;
    } else {
      System.out.println("FAIL r.m6(t)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m6(r);

    if (R2 == o) {
      System.out.println("PASS r.m6(r)");
      success++;
    } else {
      System.out.println("FAIL r.m6(r)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m7(t);

    if (R1 == o) {
      System.out.println("PASS t.m7(t)");
      success++;
    } else {
      System.out.println("FAIL t.m7(t)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m7(r);

    if (R2 == o) {
      System.out.println("PASS t.m7(r)");
      success++;
    } else {
      System.out.println("FAIL t.m7(r)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m7(o);

    if (R3 == o) {
      System.out.println("PASS t.m7(o)");
      success++;
    } else {
      System.out.println("FAIL t.m7(o)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m7(s1);

    if (R4 == o) {
      System.out.println("PASS t.m7(s1)");
      success++;
    } else {
      System.out.println("FAIL t.m7(s1)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m7(t);

    if (R3 == o) {
      System.out.println("PASS r.m7(t)");
      success++;
    } else {
      System.out.println("FAIL r.m7(t)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m7(r);

    if (R2 == o) {
      System.out.println("PASS r.m7(r)");
      success++;
    } else {
      System.out.println("FAIL r.m7(r)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m8(t);

    if (R1 == o) {
      System.out.println("PASS t.m8(t)");
      success++;
    } else {
      System.out.println("FAIL t.m8(t)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = t.m8(r);

    if (R2 == o) {
      System.out.println("PASS t.m8(r)");
      success++;
    } else {
      System.out.println("FAIL t.m8(r)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m8(t);

    if (R1 == o) {
      System.out.println("PASS r.m8(t)");
      success++;
    } else {
      System.out.println("FAIL r.m8(t)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m8(r);

    if (R2 == o) {
      System.out.println("PASS r.m8(r)");
      success++;
    } else {
      System.out.println("FAIL r.m8(r)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m8(t, t);

    if (R3 == o) {
      System.out.println("PASS r.m8(t, t)");
      success++;
    } else {
      System.out.println("FAIL r.m8(t, t)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m8(tr, t);

    if (R3 == o) {
      System.out.println("PASS r.m8(tr, t)");
      success++;
    } else {
      System.out.println("FAIL r.m8(tr, t)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m8(r, t);

    if (R4 == o) {
      System.out.println("PASS r.m8(r, t)");
      success++;
    } else {
      System.out.println("FAIL r.m8(r, t)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m9(n);

    if (R1 == o) {
      System.out.println("PASS r.m9(n)");
      success++;
    } else {
      System.out.println("FAIL r.m9(n)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m9(n + n);

    if (R2 == o) {
      System.out.println("PASS r.m9(n+n)");
      success++;
    } else {
      System.out.println("FAIL r.m9(n+n)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m9(n + 5l);

    if (R3 == o) {
      System.out.println("PASS r.m9(n+5l)");
      success++;
    } else {
      System.out.println("FAIL r.m9(n+5l)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m10(n);

    if (R2 == o) {
      System.out.println("PASS r.m10(n)");
      success++;
    } else {
      System.out.println("FAIL r.m10(n)");
    }
    test++;

    // -----------------------------------------------------------------------

    o = r.m10(n + n);

    if (R2 == o) {
      System.out.println("PASS r.m10(n+n)");
      success++;
    } else {
      System.out.println("FAIL r.m10(n+n)");
    }
    test++;

    // -----------------------------------------------------------------------

    Class k1 = t.getClass();
    Class k2 = r.getClass();

    if (k1 != k2) {
      System.out.println("PASS k1 != k2");
      success++;
    } else {
      System.out.println("FAIL K1 != k2");
    }
    test++;

    // -----------------------------------------------------------------------

    if (k1.getName().equals("xtc.oop.Test")) {
      System.out.println("PASS k1.getName().equals(\"xtc.oop.Test\")");
      success++;
    } else {
      System.out.println("FAIL k1.getName().equals(\"xtc.oop.Test\")");
    }
    test++;

    // -----------------------------------------------------------------------

    if ("xtc.oop.Test".equals(k1.getName())) {
      System.out.println("PASS \"xtc.oop.Test\".equals(k1.getName())");
      success++;
    } else {
      System.out.println("FAIL \"xtc.oop.Test\".equals(k1.getName())");
    }
    test++;

    // -----------------------------------------------------------------------

    if (k1.toString().equals("class xtc.oop.Test")) {
      System.out.println("PASS k1.toString().equals(\"class xtc.oop.Test\")");
      success++;
    } else {
      System.out.println("FAIL k1.toString().equals(\"class xtc.oop.Test\")");
    }
    test++;

    // -----------------------------------------------------------------------

    if (! k1.equals(k2)) {
      System.out.println("PASS ! k1.equals(k2)");
      success++;
    } else {
      System.out.println("FAIL ! k1.equals(k2)");
    }
    test++;

    // -----------------------------------------------------------------------

    k2 = k2.getSuperclass();

    if (k1 == k2) {
      System.out.println("PASS k1 == k2.super()");
      success++;
    } else {
      System.out.println("FAIL K1 == k2.super()");
    }
    test++;

    // -----------------------------------------------------------------------

    if (k1.equals(k2)) {
      System.out.println("PASS k1.equals(k2.super())");
      success++;
    } else {
      System.out.println("FAIL k1.equals(k2.super())");
    }
    test++;

    // -----------------------------------------------------------------------

    k1 = k1.getSuperclass();
    k2 = k2.getSuperclass();

    if (k1 == k2) {
      System.out.println("PASS k1.super() == k2.super().super()");
      success++;
    } else {
      System.out.println("FAIL K1.super() == k2.super().super()");
    }
    test++;

    // -----------------------------------------------------------------------

    if (k1.equals(k2)) {
      System.out.println("PASS k1.super().equals(k2.super().super())");
      success++;
    } else {
      System.out.println("FAIL k1.super().equals(k2.super().super())");
    }
    test++;

    // -----------------------------------------------------------------------

    k1 = k1.getSuperclass();

    if (null == k1) {
      System.out.println("PASS null == k1.super().super()");
      success++;
    } else {
      System.out.println("FAIL null == k1.super().super()");
    }
    test++;

    // -----------------------------------------------------------------------

    s1 = "Hello Kitty #1";
    s2 = "Hello Kitty #1";
    if (s1.equals(s2)) {
      System.out.println("PASS s1.equals(String)");
      success++;
    } else {
      System.out.println("FAIL s1.equals(String)");
    }
    test++;

    // -----------------------------------------------------------------------

    s2 = "Hel" + "lo Kitty #1";
    if (s1.equals(s2)) {
      System.out.println("PASS s1.equals(String + String)");
      success++;
    } else {
      System.out.println("FAIL s1.equals(String + String)");
    }
    test++;

    // -----------------------------------------------------------------------

    s2 = "He" + "ll" + "o Kitty #1";
    if (s1.equals(s2)) {
      System.out.println("PASS s1.equals(String + String + String)");
      success++;
    } else {
      System.out.println("FAIL s1.equals(String + String + String)");
    }
    test++;

    // -----------------------------------------------------------------------

    s2 = "Hello Kitty #" + 1;
    if (s1.equals(s2)) {
      System.out.println("PASS s1.equals(String + int)");
      success++;
    } else {
      System.out.println("FAIL s1.equals(String + int)");
    }
    test++;

    // -----------------------------------------------------------------------

    s2 = "Hello Kitty #" + '1';
    if (s1.equals(s2)) {
      System.out.println("PASS s1.equals(String + char)");
      success++;
    } else {
      System.out.println("FAIL s1.equals(String + char)");
    }
    test++;

    // -----------------------------------------------------------------------

    s2 = (char)72 + "ello Kitty #1";
    if (s1.equals(s2)) {
      System.out.println("PASS s1.equals(char + String)");
      success++;
    } else {
      System.out.println("FAIL s1.equals(char + String)");
    }
    test++;

    // -----------------------------------------------------------------------

    char c = 72;
    s2 = c + "ello Kitty #1";
    if (s1.equals(s2)) {
      System.out.println("PASS s1.equals(char + String)");
      success++;
    } else {
      System.out.println("FAIL s1.equals(char + String)");
    }
    test++;

    // -----------------------------------------------------------------------

    s2 = 'H' + "ello Kitty #1";
    if (s1.equals(s2)) {
      System.out.println("PASS s1.equals(char + String)");
      success++;
    } else {
      System.out.println("FAIL s1.equals(char + String)");
    }
    test++;

    // -----------------------------------------------------------------------

    short[] a0 = new short[0];

    if (a0.length == 0) {
      System.out.println("PASS short[0].length");
      success++;
    } else {
      System.out.println("FAIL short[0].length");
    }
    test++;

    // -----------------------------------------------------------------------

    short[] a1 = new short[1];

    if (a1.length == 1) {
      System.out.println("PASS short[1].length");
      success++;
    } else {
      System.out.println("FAIL short[1].length");
    }
    test++;

    // -----------------------------------------------------------------------

    short a2[] = new short[2];

    if (a2.length == 2) {
      System.out.println("PASS short[2].length");
      success++;
    } else {
      System.out.println("FAIL short[2].length");
    }
    test++;

    // -----------------------------------------------------------------------

    if (a1[0] == 0 && a2[0] == 0 && a2[1] == 0) {
      System.out.println("PASS short[i] == 0");
      success++;
    } else {
      System.out.println("FAIL short[i] == 0");
    }
    test++;

    // -----------------------------------------------------------------------

    a1[0] = (short)32768;
    if (a1[0] == -32768) {
      System.out.println("PASS short[0] = (short)32768");
      success++;
    } else {
      System.out.println("FAIL short[0] = (short)32768");
    }
    test++;

    // -----------------------------------------------------------------------

    if (a0.getClass().getName().equals("[S")) {
      System.out.println("PASS short[0].getClass().getName()");
      success++;
    } else {
      System.out.println("FAIL short[0].getClass().getName()");
    }
    test++;

    // -----------------------------------------------------------------------

    System.out.println();
    System.out.println(success + " out of " + test + " tests have passed.");
  }

}
