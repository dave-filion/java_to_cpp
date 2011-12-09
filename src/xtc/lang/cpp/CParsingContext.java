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

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import xtc.tree.Node;

import xtc.util.Pair;
import xtc.util.Runtime;

import xtc.lang.cpp.Syntax.Kind;
import xtc.lang.cpp.Syntax.LanguageTag;
import xtc.lang.cpp.Syntax.ConditionalTag;
import xtc.lang.cpp.Syntax.DirectiveTag;
import xtc.lang.cpp.Syntax.Layout;
import xtc.lang.cpp.Syntax.Language;
import xtc.lang.cpp.Syntax.Text;
import xtc.lang.cpp.Syntax.Directive;
import xtc.lang.cpp.Syntax.Conditional;

import xtc.lang.cpp.ContextManager.Context;

import xtc.lang.cpp.ForkMergeParser.OrderedSyntax;
import xtc.lang.cpp.ForkMergeParser.Lookahead;
import xtc.lang.cpp.ForkMergeParser.StateStack;

/**
 * This class implements the generated CActionsBase class.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.3 $
 */
public class CParsingContext implements Actions.Context {

  /** Output bindings and scope changes. */
  protected static boolean DEBUG = false;

  /** The symbol table for this parsing context. */
  protected SymbolTable symtab;

  /**
   * The parent parsing context, corresponding to the parent
   * scope.
   */
  protected CParsingContext parent;

  /**
   * Whether the scope this parsing context is associated with is
   * reentrant.  This is used to parse function definitions.
   */
  protected boolean reentrant;

  /** The xtc runtime. */
  Runtime runtime;

  /** Whether to display language statistics. */
  boolean languageStatistics;

  /**
   * A three-bit digit.  This is used to capture typedef/var ambiguity
   * when one token can be both.
   */
  public static enum trit {
    TRUE,
    FALSE,
    TRUEFALSE
  }

  /**
   * Create a new initial C parsing contex.
   */
  public CParsingContext(Runtime runtime) {
    this(new SymbolTable(), null, runtime);
  }
  
  /**
   * Create a new C parsing context.
   *
   * @param symtab The symbol table for this parsing context and scope.
   * @param parent The parent parsing context and scope.
   */
  public CParsingContext(SymbolTable symtab, CParsingContext parent,
                         Runtime runtime) {
    this.symtab = symtab;
    this.parent = parent;
    this.runtime = runtime;

    this.reentrant = false;

    languageStatistics = runtime.test("statisticsLanguage");
  }

  /**
   * Copy a C parsing context.  Used for forking the parsing context.
   *
   * @scope The parsing context to copy.
   */
  public CParsingContext(CParsingContext scope, Runtime runtime) {
    this.symtab = scope.symtab.addRef();

    if (scope.parent != null) {
      this.parent = new CParsingContext(scope.parent, runtime);

    } else {
      this.parent = null;
    }

    this.reentrant = scope.reentrant;
    this.runtime = runtime;
  }
  
  public Actions.Context fork() {
    return new CParsingContext(this, runtime);
  }

  public boolean shouldFork(Lookahead n) {
    return true;
  }

  public Collection<Lookahead> reclassify(Collection<Lookahead> set) {

    // Apply the parsing context to tokens in the follow set. This
    // may reclassify tokens and handle implicit conditionals,
    // e.g. due to C typedef ambiguities.

    // The list of new tokens if there are any from implicit
    // conditionals due to the typedef/var name ambiguity.

    Collection<Lookahead> newTokens = null;

    for (Lookahead n : set) {
      trit isTypedef = trit.FALSE;

      if (n.t.syntax.kind() == Kind.LANGUAGE
          && n.t.syntax.toLanguage().tag() == CTag.IDENTIFIER) { 
          
        isTypedef = isType(n.t.syntax.getTokenText(),
                           n.c);
      }

      // Check for implicit conditional.

      switch (isTypedef) {

      case TRUEFALSE:

        // Find the presence conditions of each token.

        Context typedefContext
          = this.typedefContext(n.t.syntax.getTokenText(), n.c);

        // Update the token's presence condition.

        n.c.delRef();
        n.c = typedefContext;


        // We need to replace the token with two new one because there
        // is a typedef/var amgiguity.

        // Split up the presence conditions.

        Context varContext = typedefContext.not();

        Lookahead identifier = new Lookahead(n.t, varContext); 


        // Add a new token to the list.

        if (null == newTokens) {
          newTokens = new LinkedList<Lookahead>();
        }

        newTokens.add(identifier);


        // Fall through to reclassify the token as a TYPEDEFname.

      case TRUE:

        // Replace the IDENTIFIER with a new one classified as a
        // TYPEDEFname.

        Language<CTag> newToken = new Text<CTag>(CTag.TYPEDEFname,
                                                 n.t.syntax.getTokenText());
        newToken.setLocation(n.t.syntax.getLocation());

        OrderedSyntax newOrdered;
      
        try {
          newOrdered = n.t.copy(newToken);

        } catch (java.io.IOException e) {
          // OrderedSyntax.copy() can throw an IOException because it
          // may have to read from the input stream.  The Actions
          // interface does not throw an IOException, so we throw a
          // RuntimeException instead.

          throw new RuntimeException(e);
        }

        // Update the token part of the next token object.

        n.t = newOrdered;

        break;

      case FALSE:

        // No reclassification necessary.

        break;
      }
    }

    return newTokens;
  }

  /**
   * This method determines whether an identifier is a typedef name,
   * var name, or both by inspecting the symbol table in this scope
   * and any parent scopes.
   *
   * @param ident The identifier.
   * @param context The presence condition.
   */
  public trit isType(String ident, Context context) {
    CParsingContext scope;
    
    scope = this;
      
    while (true) {
      while (scope.reentrant) scope = scope.parent;
        
      if ( scope.symtab.map.containsKey(ident)
           && scope.symtab.map.get(ident).t != null
           ) {
        break;
      }
        
      if (null == scope.parent) {
        return trit.FALSE;
      }
        
      scope = scope.parent;
    }
    
    scope = this;
    
    do {  //find the symbol in local scope or parent scope
      
      while (scope.reentrant) scope = scope.parent;
      
      if (scope.symtab.map.containsKey(ident)) {
        Entry e;
        boolean typedef;
        boolean var;
        
        e = scope.symtab.map.get(ident);
        
        typedef = false;
        var = false;

        //TODO make sure logic is correct and optimize branching
        if (null != e.t) {
          Context and;

          and = e.t.and(context);
          if (! and.isFalse()) {
            typedef = true;
          }
          and.delRef();
        }
        
        if (null != e.f) {
          Context and;

          and = e.f.and(context);
          if (! and.isFalse()) {
            var = true;
          }
          and.delRef();
        }
        
        if (typedef && var) {
          if (DEBUG) System.err.println("isType: " + ident
                                        + " true/false in " /*+ context*/);

          if (languageStatistics) {
            String location = null;  //TODO pass location to this
                                     //function
            runtime.errConsole().pln(String.format("typedef_ambiguity %s %s",
                                                   ident, location)).flush();
          }

          return trit.TRUEFALSE;

        } else if (typedef) {
          if (DEBUG) System.err.println("isType: " + ident
                                        + " true in " /*+ context*/);
          
          return trit.TRUE;

        } else if (var) {
          if (DEBUG) System.err.println("isType: " + ident
                                        + " false in " /*+ context*/);

          return trit.FALSE;
        }
      }
      
      if (null == scope.parent) {
        break;
      }
      
      scope = scope.parent;
    } while (true);
    
    if (DEBUG) System.err.println("isType: " + ident
                                  + " false in " /*+ context*/);
    
    return trit.FALSE;
  }
  
  public boolean mayMerge(Actions.Context other) {
    if (! (other instanceof CParsingContext)) return false;

    return mergeable(this, (CParsingContext) other);
  }

  /**
   * A helper method for testing mergeability.
   *
   * @param s The first parsing context.
   * @param t The second parsing context.
   */
  private static boolean mergeable(CParsingContext s, CParsingContext t) {
    if ((null == s) && (null == t)) {
      return true;
    } else if ((null == s) || (null == t)) {
      //System.err.println("TABLES NOT MERGEABLE 1");
      return false;
    } else if (s.symtab == t.symtab) {
      return true;
    } else if (s.reentrant != t.reentrant) {
      //System.err.println("TABLES NOT MERGEABLE 2");
      return false;
    } else {
      return mergeable(s.parent, t.parent);
    }
  }
  
  public Actions.Context merge(Actions.Context other) {
    CParsingContext scope = (CParsingContext) other;

    if (this.symtab == scope.symtab) {
      return this;

    } else {
      symtab.addAll(scope.symtab);

      if (null != parent) {
        return parent.merge(scope.parent);

      } else {
        return null;
      }
    }
  }
  
  public void free() {
    symtab.delRef();

    if (null != parent) {
      parent.free();
    }
  }

  /**
   * Bind an identifier to a typedef or var name for a given presence
   * condition.
   *
   * @param ident The identifier.
   * @param typedef Whether its a typedef name or a var name.
   * @param context The presence condition.
   */
  public void bind(String ident, boolean typedef, Context context) {
    CParsingContext scope;
    
    if (DEBUG) {
      System.err.println("bind: " + ident + " " + typedef);
    }

    scope = this;
    while (scope.reentrant) scope = scope.parent;
    
    scope.symtab.add(ident, typedef, context);
  }

  /**
   * Return the presence condition under which an identifier is a
   * typedef name.
   *
   * @param ident The identifier.
   * @param context The current presence condition.
   */
  public Context typedefContext(String ident, Context context) {
    CParsingContext scope;
    
    scope = this;
    
    do {  //find the symbol in local scope or parent scope

      while (scope.reentrant) scope = scope.parent;

      if (scope.symtab.map.containsKey(ident)) {
        Entry e;
        boolean typedef;
        boolean var;
        Context and;
        
        e = scope.symtab.map.get(ident);

        and = e.t.and(context);
        
        if (! and.isFalse()) {
          return and;
        }
        and.delRef();
      }
      
      if (null == scope.parent) {
        break;
      }
      scope = scope.parent;
    } while (true);
    
    return null;
  }

  /**
   * Enter a new scope.
   *
   * @param context The current presence condition.
   * @return The parsing context of the new scope.
   */
  public CParsingContext enterScope(Context context) {
    CParsingContext scope;
    
    if (DEBUG) System.err.println("enter scope");

    scope = this;
    while (scope.reentrant) {
      scope.symtab.delRef();
      scope.symtab = null;
      scope = scope.parent;
    }
    
    scope = new CParsingContext(new SymbolTable(), new CParsingContext(scope,
                                                                       runtime),
                                runtime);
    
    return scope;
  }
  
  /**
   * Exit the scope.
   *
   * @param context The current presence condition.
   * @return The parsing context of the parent scope.
   */
  public CParsingContext exitScope(Context context) {
    CParsingContext scope;
    
    if (DEBUG) System.err.println("exit scope");

    scope = this;
    while (scope.reentrant) {
      scope.symtab.delRef();
      scope.symtab = null;
      scope = scope.parent;
    }
    
    scope.symtab.delRef();
    scope.symtab = null;
    scope = scope.parent;
    
    return scope;
  }
  
  /**
   * Exit a reentrant scope.
   *
   * @param context The current presence condition.
   * @return The parsing context of the parent scope.
   */
  public CParsingContext exitReentrantScope(Context context) {
    CParsingContext scope;
    
    if (DEBUG) System.err.println("exit reentrant scope");
    
    scope = this;
    while (scope.reentrant) {
      scope.symtab.delRef();
      scope.symtab = null;
      scope = scope.parent;
    }

    scope.reentrant = true;

    return scope;
  }
  
  /**
   * Reenter a reentrant scope.
   *
   * @param context The current presence condition.
   * @return The parsing context of the reentered scope.
   */
  public CParsingContext reenterScope(Context context) {
    if (DEBUG) System.err.println("reenter scope");
    
    if (! reentrant) {
      //if (cfg.errordetail) {
        //System.err.println("NOT REENTRANT");
      //}
    }
    else {
      reentrant = false;
    }
    
    return this;
  }
  
  /**
   * Kill a reentrant scope.
   *
   * @param context The current presence condition.
   * @return The parsing context of the non-reentrant parent scope.
   */
  public CParsingContext killReentrantScope(Context context) {
    CParsingContext scope;
    
    if (DEBUG) System.err.println("kill reentrant scope");
    
    scope = this;
    while (scope.reentrant) {
      scope.symtab.delRef();
      scope.symtab = null;
      scope = scope.parent;
    }

    return scope;
  }
  
  /** The symbol table that store parsing context symbols. */
  private static class SymbolTable {

    /** The symbol table */
    public HashMap<String, Entry> map;
    
    /** The reference count for cleaning up the table BDDs */
    public int refs;
    
    /** New table */
    public SymbolTable() {
      this.map = new HashMap<String, Entry>();
      this.refs = 1;
    }
    
    public SymbolTable addRef() {
      refs++;
      
      return this;
    }
  
    public void delRef() {
      refs--;
      if (0 == refs) {  //clean up symbol table
        for (String str : this.map.keySet()) {
          Entry e = this.map.get(str);
  
          if (null != e.t) {
            e.t.delRef();
          }
          if (null != e.f) {
            e.f.delRef();
          }
        }
      }
    }
    
    public void add(String ident, boolean typedef, Context context) {
      if (! map.containsKey(ident)) {
        map.put(ident,
                new Entry(typedef ? context : null, typedef ? null : context));
        context.addRef();
      }
      else {
        Entry e;
        
        e = map.get(ident);
        
        if (typedef) {
          if (null == e.t) {
            e.t = context;
            context.addRef();
          }
          else {
            Context or;
            
            or = e.t.or(context);
            e.t.delRef();
            e.t = or;
          }
        }
        else {
          if (null == e.f) {
            e.f = context;
            context.addRef();
          }
          else {
            Context or;
            
            or = e.f.or(context);
            e.f.delRef();
            e.f = or;
          }
        }
      }
    }

    public void addAll(SymbolTable symtab) {
      for (String str : symtab.map.keySet()) {
        if (! map.containsKey(str)) {
          Entry e = symtab.map.get(str);
          
          map.put(str, new Entry(e.t, e.f));
          
          if (null != e.t) {
            e.t.addRef();
          }
          
          if (null != e.f) {
            e.f.addRef();
          }
        }
        else {
          Entry d = map.get(str);
          Entry e = symtab.map.get(str);
          
          if (null != e.t) {
            if (null == d.t) {
              d.t = e.t;
              e.t.addRef();
            }
            else {
              Context or;
              
              or = d.t.or(e.t);
              d.t.delRef();
              d.t = or;
            }
          }
          
          if (null != e.f) {
            if (null == d.f) {
              d.f = e.f;
              e.f.addRef();
            }
            else {
              Context or;
              
              or = d.f.or(e.f);
              d.f.delRef();
              d.f = or;
            }
          }
        }
      }
    }
  }

  /** An entry in the symbol table. */
  private static class Entry {
    /** The presence condition when the symbol is a typedef name. */
    Context t;

    /** The presence condition when the symbol is a var name. */
    Context f;
    
    /** Create a new entry.
     *
     * @param t The typedef name presence condition.
     * @param f The var name presence condition.
     */
    public Entry(Context t, Context f) {
      this.t = t;
      this.f = f;
    }
  }  

}

