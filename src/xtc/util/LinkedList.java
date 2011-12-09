/*
 * xtc - The eXTensible Compiler
 * Copyright (C) 2011 New York University
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package xtc.util;

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A doubly-linked list.  This implementation allows users to access
 * the list element's containers which is analogous to working with
 * pointers to linked-list nodes.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.8 $
 *
 */
public class LinkedList<D> extends AbstractSequentialList<D> {
  /** The beginning of the list. */
  final private Element head;

  /** The end of the list. */
  final private Element tail;

  /** The current size of the list. */
  private int size;

  /** Construct a new doubly-linked list. */
  public LinkedList() {
    this.size = 0;
    this.head = new Element(null, null, null);
    this.tail = new Element(null, null, null);
    this.head.next = this.tail;
    this.tail.previous = this.head;
  }

  /**
   * The state of iteration in an anonymous ListIterator class.  Used
   * for set() and remove() which cannot be used until next() or
   * previous() has been called and not after remove() or add() has
   * been called.
   */
  private static enum IteratorState {
    // Neither next() or previous() has been called yet, or
    // remove() or set() was called.
    NONE,
    // The next() method was called last.
    NEXT,
    // The previous() method was called last.
    PREVIOUS,
  }

  /** 
   * Returns a list iterator over the elements in this list (in proper
   * sequence).
   *
   * See {@link java.util.AbstractSequentialList#listIterator(int)}
   * for more information.
   *
   * @param index The first element returned by iterator.next().  Note
   * that index is marked "final" for use in the anonymous class
   * returned by this method.
   * @return A list iterator over the elements in this list.
   */
  public ListIterator<D> listIterator(final int index) {
    if (index < 0 || index > size) throw new IndexOutOfBoundsException();

    // Find the container starting at the given index.
    Element c = head;

    for (int i = 0; i < index; i++) {
      c = c.next;
    }

    // The element on which the iterator begin.  It's "final" for the
    // following anonymous class below.
    final Element startCursor = c;

    return new ListIterator<D>() {
      /**
       * The cursor position.  It is the index of the element just
       * before the next() element.
       */
      private int cursorPosition = index - 1;

      /** The cursor.  It points to the container just before next. */
      private Element cursor = startCursor;

      private IteratorState state = IteratorState.NONE;

      public void add(D e) {
        // Add an element immediately before the next() element.
        Element newElement = new Element(e, cursor, cursor.next);

        newElement.previous.next = newElement;
        newElement.next.previous = newElement;

        size++;

        cursor = newElement;
        cursorPosition++;

        state = IteratorState.NONE;
      }

      public boolean hasNext() {
        return cursor.next != tail;
      }

      public boolean hasPrevious() {
        return cursor != head;
      }

      public D next() {
        // Return next and move cursor position
        if (! hasNext()) {
          throw new NoSuchElementException();
        } else {
          cursor = cursor.next;
          cursorPosition++;
          
          state = IteratorState.NEXT;

          return cursor.data;
        }
      }

      public int nextIndex() {
        if (hasNext()) {
          return cursorPosition + 1;
        } else {
          // If there is no next index, return the size per
          // ListIterator javadoc.
          return size;
        }
      }

      public D previous() {
        if (! hasPrevious()) {
          throw new NoSuchElementException();
        } else {
          Element previous = cursor;

          cursor = cursor.previous;
          cursorPosition--;

          state = IteratorState.PREVIOUS;

          return previous.data;
        }
      }

      public int previousIndex() {
        if (hasPrevious()) {
          return cursorPosition;
        } else {
          // If there is no next index, return -1 per the
          // ListIterator javadoc.
          return -1;
        }
      }

      public void remove() {
        switch (state) {
        case NEXT:
          // "cursor" holds the last element that was returned by
          // next()
          cursor.previous.next = cursor.next;
          cursor.next.previous = cursor.previous;

          cursor = cursor.previous;
          cursorPosition--;

          size--;
          state = IteratorState.NONE;
          break;

        case PREVIOUS:
          // "cursor.next" holds the last element that was returned by
          // previous()
          Element removed = cursor.next;

          removed.previous.next = removed.next;
          removed.next.previous = removed.previous;

          cursor = removed.previous;

          size--;
          state = IteratorState.NONE;
          break;

        default:
          // An illegal state exception per the ListIterator
          // specification.
          throw new IllegalStateException();
        }
      }

      public void set(D e) {
        switch (state) {
        case NEXT:
          // "cursor" holds the last element that was returned by
          // next()
          cursor.data = e;
          break;

        case PREVIOUS:
          // "cursor.next" holds the last element that was returned by
          // previous()
          cursor.next.data = e;
          break;

        default:
          // An illegal state exception per the ListIterator
          // specification.
          throw new IllegalStateException();
        }
      }
    };
  }

  public int size() {
    return size;
  }

  /**
   * Get the first element's container.
   *
   * @return The first element's container.
   */
  public Element getFirst() {
    if (size <= 0) throw new NoSuchElementException();

    return head.next;
  }

  /**
   * Get the last element's container.
   *
   * @return The last element's container.
   */
  public Element getLast() {
    if (size <= 0) throw new NoSuchElementException();

    return tail.previous;
  }

  /**
   * Add a new element to the end of the list.
   *
   * @param data The new element.
   * @return The container for the new element.
   */
  public Element addLast(D data) {
    Element added = new Element(data, tail.previous, tail);

    added.previous.next = added;
    added.next.previous = added;

    size++;

    return added;
  }

  /**
   * Add a new element to the beginning of the list.
   *
   * @param data The new element.
   * @return The container for the new element.
   */
  public Element addFirst(D data) {
    Element added = new Element(data, head, head.next);

    added.previous.next = added;
    added.next.previous = added;

    size++;

    return added;
  }

  /**
   * Remove the first element in the list.
   *
   * @return The element.
   */
  public D removeFirst() throws NoSuchElementException {
    if (size <= 0) throw new NoSuchElementException();

    Element removed = head.next;

    removed.next.previous = removed.previous;
    removed.previous.next = removed.next;

    removed.next = null;
    removed.previous = null;

    size--;

    return removed.data;
  }

  /**
   * Remove the last element in the list.
   *
   * @return The element.
   */
  public D removeLast() throws NoSuchElementException {
    if (size <= 0) throw new NoSuchElementException();

    Element removed = tail.previous;

    removed.next.previous = removed.previous;
    removed.previous.next = removed.next;

    removed.next = null;
    removed.previous = null;

    size--;

    return removed.data;
  }

  /**
   * A container for one element of the doubly-linked list.
   */
  public class Element {
    /** The element's data. */
    private D data;

    /** The previous element in the list. */
    private Element previous;

    /** The next element in the list. */
    private Element next;

    /**
     * Create a new container for a list element.  New containers are
     * only created by the list modifier methods such as add.
     *
     * @param data The element data.
     * @param previous The previous element in the list.
     * @param next The next element in the list.
     */
    private Element(D data, Element previous, Element next) {
      this.data = data;
      this.previous = previous;
      this.next = next;
    }

    /**
     * Return the container's data.
     *
     * @return The container's data.
     */
    public D data() {
      return this.data;
    }

    /**
     * Get the list that this container is a part of.
     *
     * @return The list.
     */
    public LinkedList<D> list() {
      return LinkedList.this;
    }

    /**
     * Add a new element after this one.
     *
     * @param data The element's value.
     * @return The container for the new element.
     */
    public Element addAfter(D data) {
      Element added = new Element(data, this, this.next);

      added.previous.next = added;
      added.next.previous = added;

      size++;

      return added;
    }

    /**
     * Add a new element before this one.
     *
     * @param data The element's value.
     * @return The container for the new element.
     */
    public Element addBefore(D data) {
      Element added = new Element(data, this.previous, this);

      added.previous.next = added;
      added.next.previous = added;

      size++;

      return added;
    }

    /**
     * Get the next container in the list.
     *
     * @return The next container.
     */
    public Element next() throws NoSuchElementException {
      if (size <= 0
          || tail == next) {
        throw new NoSuchElementException();
      }

      return next;
    }

    /**
     * Get the previous container in the list.
     *
     * @return The previous container.
     */
    public Element previous() throws NoSuchElementException {
      if (size <= 0
          || head == previous) {
        throw new NoSuchElementException();
      }

      return previous;
    }

    /**
     * Returns true if this is the last element in the list.
     *
     * @return true if this is the last element in the list.
     */
    public boolean isLast() {
      return tail == this.next;
    }

    /**
     * Returns true if this is the first element in the list.
     *
     * @return true if this is the first element in the list.
     */
    public boolean isFirst() {
      return head == this.previous;
    }

    /**
     * Remove the container from the list.
     */
    public void remove() {
      Element next = this.next;

      this.previous.next = this.next;
      this.next.previous = this.previous;

      this.next = null;
      this.previous = null;

      size--;
    }
  }
}