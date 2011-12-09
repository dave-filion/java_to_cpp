/*
 * xtc - The eXTensible Compiler
 * Copyright (C) 2006-2007 Robert Grimm
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
package xtc.util;

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for LinkedList.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.1 $
 */
public class LinkedListTest {

  LinkedList<String> list;

  /** Create a new test class. */
  public LinkedListTest() { /* Nothing to do. */ }

  /** Initialize a new linked list. */
  @Before public void setUp() {
    list = new LinkedList<String>();
  }

  /** Remove the linked list. */
  @After public void tearDown() {
    list = null;
  }

  /** Test {@link LinkedList#add(String)}. */
  @Test public void testAdd() {
    list.add(0, "hello");
    assertTrue(list.get(0).equals("hello"));
    assertTrue(list.size() == 1);

    list.add(1, "world");
    assertTrue(list.get(1).equals("world"));
    assertTrue(list.size() == 2);

    list.add(2, "!");
    assertTrue(list.get(2).equals("!"));
    assertTrue(list.size() == 3);
   }

  /** Test {@link LinkedList#add(String)} exception. */
  @Test(expected= IndexOutOfBoundsException.class) public void testAddOut1() {
    list.add(2, "!");
  }

  /** Test {@link LinkedList#add(String)} exception. */
  @Test(expected= IndexOutOfBoundsException.class) public void testAddOut2() {
    list.add(-1, "!");
  }

  /** Test iteration. */
  @Test public void testIteration() {
    StringBuilder sb = new StringBuilder();

    list.add(0, "hello");
    list.add(1, "world");
    list.add(2, "!");

    for (String s : list) {
      sb.append(s);
    }

    assertTrue(sb.toString().equals("helloworld!"));
  }

  /** Test iterator .set illegal state. */
  @Test(expected= IllegalStateException.class) public void testIllegalState1() {
    ListIterator<String> it = list.listIterator(0);

    it.set("bob");
  }

  /** Test iterator .remove illegal state. */
  @Test(expected= IllegalStateException.class) public void testIllegalState2() {
    ListIterator<String> it = list.listIterator(0);

    it.remove();
  }

  /** Test iterator next index. */
  @Test public void testNextIndex() {
    list.add(0, "hello");
    list.add(1, "world");
    list.add(2, "!");

    ListIterator<String> it = list.listIterator(0);

    int i = 0;

    while (it.hasNext()) {
      int nextIndex = it.nextIndex();
      it.next();

      assert (i++) == nextIndex;
    }
  }

  /** Test iterator previous index. */
  @Test public void testPreviousIndex() {
    list.add(0, "hello");
    list.add(1, "world");
    list.add(2, "!");

    ListIterator<String> it = list.listIterator(2);

    int i = 2;

    while (it.hasPrevious()) {
      int previousIndex = it.previousIndex();
      it.previous();

      assert (--i) == previousIndex;
    }
  }

  /** Test equality of element when alternating next() and previous(). */
  @Test public void testNextPreviousEquality() {
    list.add(0, "hello");
    list.add(1, "world");
    list.add(2, "!");

    ListIterator<String> it = list.listIterator(1);

    String next = it.next();
    String previous = it.previous();

    assertTrue(next.equals(previous));

    next = it.next();
    previous = it.previous();

    assertTrue(next.equals(previous));
  }

  /** Test iterator set method. */
  @Test public void testSet() {
    list.add(0, "hello");
    list.add(1, "world");
    list.add(2, "!");

    ListIterator<String> it = list.listIterator(1);

    list.set(1, "girl");
    assert(list.get(1).equals("girl"));
  }

  /** Test iterator set interaction with next and previous. */
  @Test public void testSetInteraction() {
    list.add(0, "hello");
    list.add(1, "world");
    list.add(2, "!");

    ListIterator<String> it = list.listIterator(3);

    it.previous();
    it.set("BANG");
    assertTrue(list.get(2).equals("BANG"));

    it.next();
    it.set("EXCLAMATION");
    assertTrue(list.get(2).equals("EXCLAMATION"));
  }

  /** Test list index after add. */
  @Test public void testIndexAdd() {
    ListIterator<String> it = list.listIterator(0);

    assertTrue(it.nextIndex() == 0);
    assertTrue(it.previousIndex() == -1);

    it.add("bye");
    assertTrue(list.get(0).equals("bye"));

    // The add function "...increases by one the value that would be
    // returned by a call to nextIndex or previousIndex."  See {@link
    // java.util.ListIterator#add(E).

    assertTrue(it.nextIndex() == 1);
    assertTrue(it.previousIndex() == 0);
  }

  /** Test iterator add. */
  @Test public void testIteratorAdd() {
    ListIterator<String> it;

    list.add("bye");

    it = list.listIterator(1);
    it.add("now");
    assertTrue(list.get(1).equals("now"));

    it = list.listIterator(1);
    assertTrue(it.next().equals("now"));
  }

  /** Test iterator add interacting with next and previous. */
  @Test public void testIteratorAddInteraction() {
    // After add is called "..a subsequent call to next would be
    // unaffected, and a subsequent call to previous would return the
    // new element".  See {@link java.util.ListIterator#add(E).

    list.add("bye");
    list.add("now");

    ListIterator<String> it = list.listIterator(1);
    it.add("go");
    assertTrue(it.next().equals("now"));  // FIXME side-effect

    it = list.listIterator(2);
    it.add("away");
    assertTrue(it.previous().equals("away"));  // FIXME side-effect
    assertTrue(list.get(2).equals("away"));
  }

  /** Test remove. */
  @Test public void testRemove() {
    list.add("bye");
    list.add("go");
    list.add("away");
    list.add("now");

    list.remove(2);
    assertTrue(list.get(2).equals("now"));
  }

  /** Test index interaction with remove. */
  @Test public void testIndicesInteraction() {
    list.add("bye");
    list.add("go");
    list.add("now");

    ListIterator<String> it = list.listIterator();

    assertTrue(it.nextIndex() == 0);
    assertTrue(it.previousIndex() == -1);
    assertTrue(it.next().equals("bye"));  // FIXME side-effect
    assertTrue(list.size() == 3);
    assertTrue(it.nextIndex() == 1);
    assertTrue(it.previousIndex() == 0);

    it.remove();

    assertTrue(list.size() == 2);
    assertTrue(it.nextIndex() == 0);
    assertTrue(it.previousIndex() == -1);

    assertTrue(it.next().equals("go"));  // FIXME side-effect
    assertTrue(it.nextIndex() == 1);
    assertTrue(it.previousIndex() == 0);
    assertTrue(it.previous().equals("go"));  // FIXME side-effect
    assertTrue(it.nextIndex() == 0);
    assertTrue(it.previousIndex() == -1);

    it.remove();

    assertTrue(list.size() == 1);
    assertTrue(it.nextIndex() == 0);
    assertTrue(it.previousIndex() == -1);
  }

  /** Test element add and remove. */
  @Test public void testElementAddRemove() {
    // FIXME break into smaller unit tests
    list.add("now");
    list.addLast("hear");
    list.addLast("this");

    assertTrue(list.get(0).equals("now"));
    assertTrue(list.get(1).equals("hear"));
    assertTrue(list.get(2).equals("this"));
    assertTrue(list.size() == 3);

    LinkedList<String>.Element c = list.addFirst("hey");

    assertTrue(list.get(0).equals("hey"));
    assertTrue(list.get(1).equals("now"));
    assertTrue(list.get(2).equals("hear"));
    assertTrue(list.get(3).equals("this"));
    assertTrue(list.size() == 4);
    
    c = c.addAfter("you");

    assertTrue(list.get(0).equals("hey"));
    assertTrue(list.get(1).equals("you"));
    assertTrue(list.get(2).equals("now"));
    assertTrue(list.get(3).equals("hear"));
    assertTrue(list.get(4).equals("this"));
    assertTrue(list.size() == 5);

    c.addBefore("there");

    assertTrue(list.get(0).equals("hey"));
    assertTrue(list.get(1).equals("there"));
    assertTrue(list.get(2).equals("you"));
    assertTrue(list.get(3).equals("now"));
    assertTrue(list.get(4).equals("hear"));
    assertTrue(list.get(5).equals("this"));
    assertTrue(list.size() == 6);

    assertTrue(list.removeFirst().equals("hey"));

    assertTrue(list.get(0).equals("there"));
    assertTrue(list.get(1).equals("you"));
    assertTrue(list.get(2).equals("now"));
    assertTrue(list.get(3).equals("hear"));
    assertTrue(list.get(4).equals("this"));
    assertTrue(list.size() == 5);

    assertTrue(list.removeFirst().equals("there"));

    assertTrue(list.get(0).equals("you"));
    assertTrue(list.get(1).equals("now"));
    assertTrue(list.get(2).equals("hear"));
    assertTrue(list.get(3).equals("this"));
    assertTrue(list.size() == 4);

    assertTrue(list.removeLast().equals("this"));

    assertTrue(list.getFirst().data().equals("you"));
    assertTrue(list.get(0).equals("you"));
    assertTrue(list.get(1).equals("now"));
    assertTrue(list.get(2).equals("hear"));
    assertTrue(list.getLast().data().equals("hear"));
    assertTrue(list.size() == 3);

    assertTrue(list.removeLast().equals("hear"));

    assertTrue(list.getFirst().data().equals("you"));
    assertTrue(list.get(0).equals("you"));
    assertTrue(list.get(1).equals("now"));
    assertTrue(list.getLast().data().equals("now"));
    assertTrue(list.size() == 2);

    c = list.getFirst();
    assertTrue(c.isFirst() == true);
    assertTrue(c.isLast() == false);

    // test Element.next() and Element.previous()
    assertTrue(c.next().data().equals("now"));
    c = c.next();
    assertTrue(c.previous().data().equals("you"));
    assertTrue(c.isFirst() == false);
    assertTrue(c.isLast() == true);

    assertTrue(list.removeLast().equals("now"));

    assertTrue(list.getFirst().data().equals("you"));
    assertTrue(list.get(0).equals("you"));
    assertTrue(list.getLast().data().equals("you"));
    assertTrue(list.size() == 1);


    c = list.getFirst();
    assertTrue(c.isFirst() == true);
    assertTrue(c.isLast() == true);
  }

  /** Test element next exception. */
  @Test(expected=NoSuchElementException.class) public void testNextException() {
    LinkedList<String>.Element c = list.addFirst("bob");
    
    c.next();
  }

  /** Test element previous exception. */
  @Test(expected=NoSuchElementException.class) public void testPrevException() {
    LinkedList<String>.Element c = list.addFirst("bob");
    
    c.previous();
  }

  /** Test element isFirst and isLast. */
  @Test public void testIsFirstLast() {
    LinkedList<String>.Element c = list.addFirst("bob");
    
    assertTrue(c.isLast());
    assertTrue(c.isFirst());
  }

  /** Test element removeLast and removeFirst. */
  @Test public void testElementRemove() {
    LinkedList<String>.Element c = list.addLast("bob");
    LinkedList<String>.Element d = list.addLast("noxious");

    assertTrue(list.removeFirst().equals("bob"));
    assertTrue(list.removeLast().equals("noxious"));
    assertTrue(list.size() == 0);
  }

  /** Test element removeFirst exception. */
  @Test(expected=NoSuchElementException.class) public void testRemFExc() {
    list.removeFirst();
  }

  /** Test element removeLast exception. */
  @Test(expected=NoSuchElementException.class) public void testRemLExc() {
    list.removeLast();
  }

  /** Test element getFirst exception. */
  @Test(expected=NoSuchElementException.class) public void testGetFExc() {
    list.getFirst();
  }

  /** Test element getLast exception. */
  @Test(expected=NoSuchElementException.class) public void testGetLExc() {
    list.getLast();
  }

  /** Test element remove and list. */
  @Test public void testElementRemoveAndList() {
    LinkedList<String>.Element c;

    c = list.addFirst("first");
    c = c.addAfter("second");
    c.addAfter("third");

    assertTrue(list.toString().equals("[first, second, third]"));
    assertTrue(list.size() == 3);

    LinkedList<String>.Element nextElem = c.next();
    c.remove();
    c = nextElem;

    assertTrue(list.toString().equals("[first, third]"));
    assertTrue(list.size() == 2);

    assertTrue(c.data().equals("third"));

    assertTrue(c.list().get(0).equals("first"));

    assertTrue(c.isLast());

    c.remove();

    assertTrue(list.toString().equals("[first]"));
    assertTrue(list.size() == 1);
  }
}
