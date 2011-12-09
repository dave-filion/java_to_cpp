/*
 * xtc - The eXTensible Compiler
 * Copyright (C) 2011 Robert Grimm, New York University
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
package xtc.lang.cpp;

import java.util.Collection;

import xtc.lang.cpp.ForkMergeParser.Lookahead;
import xtc.lang.cpp.ForkMergeParser.Subparser;

import xtc.util.Pair;

/**
 * Support for semantic actions and parser context. *
 * @author Robert Grimm, Paul Gazzillo
 * @version $Revision: 1.4 $
 */
public class Actions {

  /**
   * Determine whether a production represents a complete semantic
   * unit.  The default implementation returns <code>true</code>.
   *
   * @param id The production ID.
   * @return <code>true</code> if the production is a complete semantc
   *   unit.
   */
  public boolean isComplete(int id) {
    return true;
  }

  /** The types of semantic values. */
  public static enum ValueType {
    /** Create a regular node. */
    NODE,
    /** Create a node with all list elements. */
    LIST,
    /** Treat as layout, i.e., not having a value. */
    LAYOUT,
    /** Pass through the single value of an alternative. */
    PASS_THROUGH,
    /** Invoke arbitrary semantic action. */
    ACTION
  }

  /**
   * Determine a production's value type.  The default implementation
   * simply returns {@link ValueType#NODE}.
   *
   * @param id The production ID.
   * @return The corresponding value type.
   */
  public ValueType getValueType(int id) {
    return ValueType.NODE;
  }

  /**
   * Get the semantic value for the specific production.  The default
   * implementation signals an invalid state.
   *
   * @param id The production's symbol ID.
   * @param String The production's name.
   * @param values The values that make up the production.
   * @return The value of the production.
   */
  public Object getValue(int id, String name, Pair<Object> values) {
    throw new
      IllegalStateException("Invalid semantic action for production " + id);
  }

  /**
   * Dispatch a semantic action.  Does nothing by default.
   */
  public void dispatch(int id, Subparser subparser) {
  }

  /**
   * Determine whether the parser requires context.  The default
   * implementation returns false.
   *
   * @return true if the parser has context.
   */
  public boolean hasContext() {
    return false;
  }

  /**
   * Create the initial context.  The default implementation signals
   * an invalid state.
   *
   * @return The initial context.
   */
  public Context getInitialContext() {
    throw new IllegalStateException("Parser has no context");
  }

  /** The context interface. */
  public static interface Context {

    /**
     * Fork this context.  This method is invoked on regular fork
     * operations but not on fork operations due to context.
     *
     * @return The forked context.
     */
    public Context fork();

    /**
     * Given the follow-set, reclassify tokens in the set and return
     * any new tokens that result from implicit conditionals, e.g. the
     * typedef/var name ambiguity in C.  Do not add tokens the
     * follow-set.
     *
     * @param set The follow set.
     * @return Any new tokens or null if there are none.
     */
    public Collection<Lookahead> reclassify(Collection<Lookahead> set);

    /**
     * Determine whether this context can merge with another.  This
     * method is invoked on merge candidates that already observe the
     * merge discipline (modulo context).
     *
     * @param other The other context.
     * @return true if the contexts may merge.
     */
    public boolean mayMerge(Context other);

    /**
     * Merge this context with another.
     *
     * @param other The other context.
     * @return The merged context.
     */
    public Context merge(Context other);

    /**
     * Free any memory this context may be using.
     */
    public void free();

  }

}
