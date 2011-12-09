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

import xtc.tree.Node;
import xtc.tree.GNode;
import xtc.tree.Location;

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

import xtc.lang.cpp.ForkMergeParser.StateStack;
import xtc.lang.cpp.ForkMergeParser.Subparser;

/**
 * This class implements the generated CActionsBase class.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.13 $
 */
public class CActions extends CActionsBase {

  /** The initial parsing context. */
  CParsingContext initialParsingContext;

  /** The xtc runtime. */
  Runtime runtime;

  /** Whether to display language statistics. */
  boolean languageStatistics;

  public CActions(Runtime runtime) {
    this.runtime = runtime;
    this.initialParsingContext = new CParsingContext(runtime);

    languageStatistics = runtime.test("statisticsLanguage");
  }

  public boolean hasContext() {
    return true;
  }

  public Actions.Context getInitialContext() {
    return initialParsingContext;
  }

  public Object getValue(int id, String name, Pair<Object> values) {
    Object value;

    if (values == Pair.<Object>empty()) {
      value = null;

    } else {
      value = GNode.createFromPair(name, values.head(), values.tail());
    }

    if (languageStatistics) {
      if (name.equals("Statement")
          || name.equals("Declaration")
          || name.equals("ExternalDeclaration")) {

        // Get the location of the production.

        Location location = getProductionLocation(value);


        // Emit the marker.

        runtime.errConsole().pln(String.format("c_construct %s %s",
                                               name, location)).flush();
      }
    }

    return value;
  }

  /**
   * Get  location of a production given its value.
   *
   * @param value The value of the production.
   * @return The location.
   */
  private static Location getProductionLocation(Object value) {
    if (value instanceof Node) {
      for (Object o : (Node) value) {
        Location location = getProductionLocation(o);

        if (null != location) {
          return location;
        }
      }

      return ((Node) value).getLocation();
    } else {
      return null;
    }
  }

  public void BindIdentifier(Subparser subparser) {
    StateStack stack = subparser.s;
    ContextManager.Context context = subparser.getContext();
    CParsingContext scope = (CParsingContext) subparser.scope;
    boolean typedef;
    Language ident;
      
    //get nodes containing the type and the declarator
    Object a = stack.get(3).value;
    Object b = stack.get(2).value;

    Language t;

    /** Assume the typedef keyword is the first token of a typedef */
    while (true) {
      if (a instanceof Node && ! (a instanceof Syntax)) {
        Node n = (Node) a;
        if (n.hasName(ForkMergeParser.CHOICE_NODE_NAME)) {
          // When it's a conditional node, the first child is a
          // presence condition, the second is the first AST child.
          a = n.get(1);
        } else {
          a = n.get(0);
        }
      } else if (a instanceof Pair) {
        a = ((Pair) a).head();
      } else {
        break;
      }
    }
      
    t = (Language) a;

    ident = getident(b);

    if (CTag.TYPEDEF == t.tag()) {
      typedef = true;

      if (languageStatistics) {
        if (typedef) {
          String location = null;  //TODO pass location to this function

          runtime.errConsole().pln(String.format("typedef %s %s",
                                                 ident, location)).flush();
        }
      }

    } else {
      typedef = false;

      // TODO statistics collect bind var name
    }
      
    scope.bind(ident.getTokenText(), typedef, context);
  }
    
  public void BindIdentifierInList(Subparser subparser) {
    StateStack stack = subparser.s;
    ContextManager.Context context = subparser.getContext();
    CParsingContext scope = (CParsingContext) subparser.scope;
      
    boolean typedef;
    Language ident;
      
    //get nodes containing the type and the declarator
    Object a = stack.get(5).value;
    Object b = stack.get(2).value;
      
    Language t;
      
    while (true) {
      if (a instanceof Node && ! (a instanceof Syntax)) {
        Node n = (Node) a;
        if (n.hasName(ForkMergeParser.CHOICE_NODE_NAME)) {
          // When it's a conditional node, the first child is a
          // presence condition, the second is the first AST child.
          a = n.get(1);
        } else {
          a = n.get(0);
        }
      } else if (a instanceof Pair) {
        a = ((Pair) a).head();
      } else {
        break;
      }
    }
      
    t = (Language) a;

    if (CTag.TYPEDEF == t.tag()) {
      typedef = true;

      // TODO statistics collect bind typedef

    } else {
      typedef = false;

      // TODO statistics collect bind var name
    }
      
    ident = getident(b);

    scope.bind(ident.getTokenText(), typedef, context);
  }
    
  public void BindVar(Subparser subparser) {
    StateStack stack = subparser.s;
    ContextManager.Context context = subparser.getContext();
    CParsingContext scope = (CParsingContext) subparser.scope;
      
    Language ident;
      
    Object b = stack.get(2).value;
      
    ident = getident(b);
      
    scope.bind(ident.getTokenText(), false, context);

    // TODO statistics collect bind var name
  }
    
  public void BindEnum(Subparser subparser) {
    StateStack stack  = subparser.s;
    ContextManager.Context context = subparser.getContext();
    CParsingContext scope = (CParsingContext) subparser.scope;
      
    String ident;
      
    //must occur after an identifier.or.typedef.name token

    Object b = stack.get(2).value;

    ident = getident(b).getTokenText();

    scope.bind(ident, false, context);
      
    // TODO statistics collect bind var name
  }
    
  public void EnterScope(Subparser subparser) {
    ContextManager.Context context = subparser.getContext();
      
    subparser.scope = ((CParsingContext) subparser.scope).enterScope(context);
  }
    
  public void ExitScope(Subparser subparser) {
    ContextManager.Context context = subparser.getContext();
      
    subparser.scope = ((CParsingContext) subparser.scope).exitScope(context);
  }

  public void ExitReentrantScope(Subparser subparser) {
    ContextManager.Context context = subparser.getContext();
      
    subparser.scope
      = ((CParsingContext) subparser.scope).exitReentrantScope(context);
  }
    
  public void ReenterScope(Subparser subparser) {
    ContextManager.Context context = subparser.getContext();
      
    subparser.scope
      = ((CParsingContext) subparser.scope).reenterScope(context);
  }
    
  public void KillReentrantScope(Subparser subparser) {
    ContextManager.Context context = subparser.getContext();
      
    subparser.scope
      = ((CParsingContext) subparser.scope).killReentrantScope(context);
  }
    
  /**
   * Find the identifier or typedef name in a declarator.  Assume
   * the first identifier in the subtree is the var or typedef name.
   *
   * @param o The semantic value.
   * @return The first identifier in the subtree or null if there is
   * none.
   */
  private static Language getident(Object o) {
    if (o instanceof Language) {
      Language token = ((Language) o);
        
      if (CTag.IDENTIFIER == token.tag()
          || CTag.TYPEDEFname == token.tag()) {
        return token;

      } else {
        return null;
      }

    } else if (o instanceof Pair) {
      Pair<?> b = (Pair<?>) o;
        
      while (b != Pair.empty()) {
        Object child = b.head();
          
        if (null != child) {
          Language ident = getident(child);
            
          if (null != ident) {
            return ident;
          }
        }
          
        b = b.tail();
      }
        
      return null;
    } else if (o instanceof ContextManager.Context) {
      return null;
    } else {
      Node b = (Node) o;
        
      for (int i = 0; i < b.size(); i++) {
        Object child = b.get(i);
          
        if (null != child) {
          Language ident = getident(child);
            
          if (null != ident) {
            return ident;
          }
        }
      }
        
      return null;
    }
  }

}
