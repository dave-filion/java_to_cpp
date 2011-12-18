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

public class Rest extends Test {

  public int round;

  public Rest() {
    round = 0;
  }

  public Object m1() {
    return Test.R2;
  }

  public static Object m2() {
    return Test.R4;
  }

  public Test m4() {
    round++;
    return this;
  }

  public Object m7(Test t) {
    return R3;
  }

  public Object m9(short n1) {
    return R1;
  }

  public Object m9(int n1) {
    return R2;
  }

  public Object m9(long n1) {
    return R3;
  }

  public Object m10(int n1) {
    return R2;
  }

  public Object m10(long n1) {
    return R3;
  }

  public int hashCode() {
    return 7353;
  }

  public static void main(String[] args) {
    System.out.println("FAIL Test.main()");
    System.out.println();
    System.out.println("0 out of n tests have passed.");
  }

}