/*
 * xtc - The eXTensible Compiler
 * Copyright (C) 2009-2011 New York University
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

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import xtc.lang.cpp.Syntax.LanguageTag;
import xtc.lang.cpp.Syntax.Layout;
import xtc.lang.cpp.Syntax.Language;
import xtc.lang.cpp.Syntax.Text;
import xtc.lang.cpp.Syntax.Directive;
import xtc.lang.cpp.Syntax.Conditional;

import xtc.util.Runtime;

import xtc.lang.cpp.ContextManager.Context;

/** The conditional macro table.
  *
  * A few properties:
  * (1) no entry means the macro is free,
  * (2) the union of all entry's conditions is TRUE,
  * (3) each entry's definitions' conditions are disjoint,
  * (4) there is at most one free and one undefined element
  * (5) the table is optimized by having no duplicate definitions
  *
  * @author Paul Gazzillo
  * @version $Revision: 1.52 $
  */
public class MacroTable {
  /** The xtc runtime. */
  Runtime runtime;

  /** The token creator. */
  TokenCreator tokenCreator;

  /** The mapping from names to lists of (condition, definition) pairs */
  Map<String, List<Entry>> table;
  
  /** Track enabled/disabled macro names */
  Set<String> disabled;
  
  /** Make a new empty macro table */
  public MacroTable(Runtime runtime, TokenCreator tokenCreator) {
    this.runtime = runtime;
    this.tokenCreator = tokenCreator;

    table = new HashMap<String, List<Entry>>();
    disabled = new HashSet<String>();
  }
  
  /** Define a macro under a given context.  This function will
    * ensure that all conditions are disjoint and that there are no more
    * than one of each unique definition.
    */
  public void define(String name, Macro macro, ContextManager contextManager) {
    Context context;

    if (contextManager.isFalse()) return; //invalid configuration

    /* If this is a boolean variable already, then remove FREE and
     * replace with equivalent defined M and ! defined M
     */
    if (contains(name)
        && contextManager.getVariableManager().hasDefinedVariable(name)) {
      Context var;
      Context not;
      List<Entry> entries;
      
      var = contextManager.new Context(contextManager.getVariableManager().getDefinedVariable(name));
      not = var.not();
      
      entries = table.get(name);
      
      for (int i = 0; i < entries.size(); i++) {
        Entry e;
        
        e = entries.get(i);
        
        if (Macro.State.FREE == e.macro.state) {
          List<Syntax> definition;
          Context andvar, andnot;
          
          andvar = e.context.and(var);
          andnot = e.context.and(not);

          definition = new LinkedList<Syntax>();
          {  // Mark an unknown definition.
            Language<?> deftoken;
            
            deftoken = tokenCreator.createIdentifier(name);
            deftoken.setFlag(Preprocessor.NO_EXPAND);
            deftoken.setFlag(Preprocessor.UNKNOWN_DEF);
            definition.add(deftoken);
          }
          _define(name, new Macro.Object(definition), andvar);
          
          _define(name, Macro.undefined, andnot);

          andvar.delRef();
          andnot.delRef();
        }
      }
    }

    context = contextManager.reference();

    _define(name, macro, context);
    
    context.delRef();
  }
  
  /** Update the symbol table with a new entry.  The context passed
    * will gain a reference, so dereference it yourself if you no
    * longer need it.
    */
  protected void _define(String name, Macro macro, Context context) {
    if (context.isFalse()) return;
    
    if (! contains(name)) {  //new macro
      List<Entry> defs;
      
      defs = new LinkedList<Entry>();
      table.put(name, defs);

      if (! context.isTrue()) {
        Context negation;
        
        negation = context.not();
        if (runtime.test("cppmode")
            || (null != runtime.getString("TypeChef-x")
                && ! name.startsWith(runtime.getString("TypeChef-x")))) {

          // Assume the macro is undefined.

          defs.add(new Entry(Macro.undefined, negation));

        } else {

          // Let the macro be free.

          defs.add(new Entry(Macro.free, negation));
        }
        negation.delRef();
      }

      defs.add(new Entry(macro, context));
      
    } else {  //update macro entries
      Entry duplicate;
      List<Entry> defs;
      
      duplicate = null;
      defs = table.get(name);
      
      //make entries disjoint
      for (int d = 0; d < defs.size(); d++) {
        Entry entry = defs.get(d);
        Context newContext;
        
        newContext = entry.context.andNot(context);
        entry.context.delRef();
        entry.context = newContext;
        
        if (newContext.isFalse()) {
          newContext.delRef();
          defs.remove(d);
          d--;
        }
      }
      
      for (Entry e : defs) {
        if (Macro.State.UNDEFINED == e.macro.state
            && Macro.State.UNDEFINED == macro.state
            || Macro.State.FREE == e.macro.state
            && Macro.State.FREE == macro.state
        ) {
          //only allow one entry for UNDEFINED and FREE
          duplicate = e;
        }
        
        if (/*! runtime.test("noDedup")*/ true) {
          //TODO check dedup flag for optimization evaluation
          if (Macro.State.DEFINED == e.macro.state
              && Macro.State.DEFINED == macro.state) {
            if (null == macro.definition && null == e.macro.definition) {
              duplicate = e;
            }
            else if (null != macro.definition && null != e.macro.definition) {
              if (macro.definition.size() == e.macro.definition.size()) {
                boolean isdup;
                
                isdup = true;
                for (int i = 0; i < macro.definition.size(); i++) {
                  Syntax a, b;
                  
                  a = macro.definition.get(i);
                  b = e.macro.definition.get(i);
                  
                  if (! a.getTokenText().equals(b.getTokenText())) {
                    isdup = false;
                  }
                }
                
                if (isdup) {
                  duplicate = e;
                  break;
                }
              }
            }
          }
        }
      }

      if (null != duplicate) {
        Context oldc, newc;
        
        oldc = duplicate.context;
        newc = oldc.or(context);
        oldc.delRef();
        duplicate.context = newc;

      } else {
        //add new entry
        table.get(name).add(new Entry(macro, context));
      }
    }
  }
  
  /**
   * Update the contexts of macros that use a guard macro.  We assume
   * that guard macros for headers are undefined, not free.
   *
   * @param guardMacro The name of the guard macro.
   * @param contextManager The context manager.
   */
  public void rectifyGuard(String guardMacro, ContextManager contextManager) {
    Context var, not;
    Context context;
    
    context = contextManager.reference();
    var = contextManager.new Context(contextManager.getVariableManager()
                                     .getDefinedVariable(guardMacro));
    not = var.not();
    
    for (String name : table.keySet()) {
      List<Entry> entries;
      
      entries = table.get(name);
      for (int i = 0; i < entries.size(); i++) {
        Entry e;
        Context restrictnot;
        
        Context c;
        
        e = entries.get(i);
        
        c = context.and(e.context);


        // Check whether the macro entry's presence condition
        // (e.context) depends on the guard macro (! (defined var)).

        restrictnot = e.context.restrict(not);
        
        if (! restrictnot.equals(e.context)) {
          e.context.delRef();
          
          if (restrictnot.isFalse()) {
            entries.remove(i);
            i--;

          } else {
            e.context = restrictnot;
            e.context.addRef();
          }
        }
        
        restrictnot.delRef();
      }
    }
    
    context.delRef();
    var.delRef();
    not.delRef();
    

    // This following line doesn't check whether the header macro is
    // already defined!  In openHeader, we make sure the header guard
    // is not already contained in the macro table.

    _define(guardMacro, Macro.undefined, contextManager.new Context(true));
  }
  
  /** Undefine a macro under the given context. */
  public void undefine(String name, ContextManager contextManager) {
    define(name, Macro.undefined, contextManager);
  }
  
  /** Gets the macro table entries of the given macro under the
    * given context.  Any entries not valid in the context will
    * not be returned.  The actual table entries are returned, so
    * they shouldn't be modified.
    */
  public List<Entry> get(String name, ContextManager contextManager) {
    List<Entry> all;
    List<Entry> valid;
    Context context;
    
    if (! contains(name)) return null;
    
    context = contextManager.reference();
    
    all = table.get(name);
    valid = new LinkedList<Entry>();
    
    for (Entry e : all) {
      Context and;
      
      and = context.and(e.context);
      
      if (! and.isFalse()) {
        valid.add(new Entry(e.macro, e.context));
      }
      
      and.delRef();
    }
    
    context.delRef();
    
    return valid;
  }
  
  public void free(List<Entry> entries) {
    for (Entry e : entries) {
      e.context.delRef();
    }
  }
  
  /** Whether the table already contains the macro name. */
  public boolean contains(String name) {
    return table.containsKey(name);
  }
  
  /** String representation */
  public String toString() {
    StringBuilder sb;
    
    sb = new StringBuilder();
    for (String name : table.keySet()) {
      sb.append(name);
      sb.append("\n");
      sb.append("-------------------------------------------");
      sb.append("\n");
      for (Entry e : table.get(name)) {
        sb.append(e);
        sb.append("\n");
      }
      sb.append("\n");
    }
    
    return sb.toString();
  }
  
  /** Disable a macro */
  public void disable(String macro) {
    disabled.add(macro);
  }
  
  /** Enable a macro */
  public void enable(String macro) {
    disabled.remove(macro);
  }
  
  /** Test whether a macro is enabled */
  public boolean isEnabled(String macro) {
    return ! disabled.contains(macro);
  }

  /**
   * Count the number of definitions a given macro has.  Does not
   * include FREE or UNDEFINED entries in the macro table.
   *
   * @param name The macro name.
   * @return The number of definitions of the macro.
   */
  public int countDefinitions(String name) {
    if (! table.containsKey(name)) return 0;

    int count = 0;

    for (Entry e : table.get(name)) {
      if (Macro.State.DEFINED == e.macro.state) {
        count++;
      }
    }

    return count;
  }
  

  /** A macro definition */
  public static class Macro {
    /** The macro definition */
    protected List<Syntax> definition;
    
    /** The state of the macro */
    protected State state;
    
    /** An undefined macro */
    public static Macro undefined = new Macro(State.UNDEFINED);
    
    /** A free macro */
    public static Macro free = new Macro(State.FREE);
    
    public enum State {
      FREE, UNDEFINED, DEFINED
    }
    
    /** Only subclasses are instantiable */
    protected Macro(List<Syntax> definition, State state) {
      this.definition = definition;
      this.state = state;
    }
    
    protected Macro(State state) {
      this(null, state);
    }
    
    public boolean isObject() {
      return false;
    }
    
    public boolean isFunction() {
      return false;
    }
    
    public static class Object extends Macro {
      public Object(List<Syntax> definition) {
        super(definition, State.DEFINED);
      }

      public boolean isObject() {
        return true;
      }
    }
    
    public static class Function extends Macro {
      protected List<String> formals;
      protected String variadic;

      public Function(List<String> formals, List<Syntax> definition,
                      String variadic) {
        super(definition, State.DEFINED);
        this.formals = formals;
        this.variadic = variadic;
      }

      public boolean isFunction() {
        return true;
      }
      
      public boolean isVariadic() {
        return null != variadic;
      }
    }
  }

  public static class Entry {
    protected Macro macro;
    protected Context context;
    
    public Entry(Macro macro, Context context) {
      this.macro = macro;
      this.context = context;
      this.context.addRef();
    }
    
    public String toString() {
      StringBuilder sb;
      
      sb = new StringBuilder();
      sb.append(macro.state);
      if (Macro.State.DEFINED == macro.state && null != macro.definition) {
        sb.append("(");
        for (Syntax t : macro.definition) {
          if (t.testFlag(Preprocessor.PREV_WHITE)) {
            sb.append(" ");
          }
          sb.append(t.getTokenText());
        }
        sb.append(")");
      }
      sb.append("\n");
      /*if (! runtime.test("noConditions"))*/ {
        sb.append(context);
      }
      sb.append("\n");
      
      return sb.toString();
    }
  }
}

