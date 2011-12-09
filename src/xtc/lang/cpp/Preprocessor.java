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

import java.io.StringReader;
import java.io.IOException;

import java.lang.StringBuilder;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import xtc.util.Pair;
import xtc.util.Runtime;

import xtc.lang.cpp.Syntax.Kind;
import xtc.lang.cpp.Syntax.LanguageTag;
import xtc.lang.cpp.Syntax.PreprocessorTag;
import xtc.lang.cpp.Syntax.ConditionalTag;
import xtc.lang.cpp.Syntax.DirectiveTag;
import xtc.lang.cpp.Syntax.Layout;
import xtc.lang.cpp.Syntax.Language;
import xtc.lang.cpp.Syntax.Text;
import xtc.lang.cpp.Syntax.Directive;
import xtc.lang.cpp.Syntax.Conditional;
import xtc.lang.cpp.Syntax.ConditionalBlock;

import xtc.lang.cpp.MacroTable;
import xtc.lang.cpp.MacroTable.Macro;
import xtc.lang.cpp.MacroTable.Macro.Object;
import xtc.lang.cpp.MacroTable.Macro.Function;
import xtc.lang.cpp.MacroTable.Entry;

import xtc.lang.cpp.ContextManager.Context;

import net.sf.javabdd.BDD;

/**
 * This class expands macros and processes header files
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.112 $
 */
public class Preprocessor implements Stream {
  /** Don't expand the macro (flag). */
  public static int NO_EXPAND = 0;

  /** The token has preceding whitespace (flag). */
  public static int PREV_WHITE = 2;

  /** The token is the left operand of a token-pasting (flag). */
  public static int PASTE_LEFT = 3;

  /**
   * The token is not to be pasted (flag).  Used to prevent incorrect
   * pasting when the right operand is empty.
   */
  public static int AVOID_PASTE = 4;

  /** The token is to be stringified (flag). */
  public static int STRINGIFY_ARG = 5;

  /** The function has already been hoisted (flag). */
  public static int HOISTED_FUNCTION = 6;

  /** The function-like macro does not have a valid invocation. */
  public static int NON_FUNCTION = 7;

  /** The macro has an unknown definition (flag). */
  public static int UNKNOWN_DEF = 8;

  /** The token indicates an end-of-include (flag). */
  public static int EOI = 9;

  /**
   * The token indicates an end-of-expansion (flag).  This flag is
   * used to mark the end of a macro expansion while that expansion is
   * recursively passed to the preprocessor.
   */
  public static int EOE = 10;

  /**
   * An empty token.  Used to replace preprocessor directives and
   * macros that have no contents.
   */
  public static Layout EMPTY = new Layout("");

  /** A space token. */
  public static Layout SPACE = new Layout(" ");

  /**
   * A special token used to avoid incorrect token-pasting when the
   * right operand is empty.
   */
  public static Layout AVOID_PASTE_TOKEN = new Layout("");
  static {
    AVOID_PASTE_TOKEN.setFlag(AVOID_PASTE);
  }

  /**
   * The stream from which the Preprocessor gets tokens and
   * directives
   */
  private Stream stream;
  
  /** The file manager for main file and header streams. */
  private HeaderFileManager fileManager;
  
  /** The macro table. */
  private MacroTable macroTable;
  
  /** The global context. */
  private ContextManager contextManager;

  /** The expression evaluator. */
  private ConditionEvaluator evaluator;

  /** The token creator. */
  private TokenCreator tokenCreator;

  /** The xtc runtime. */
  private Runtime runtime;
  
  /** Whether to gather statistics. */
  private final boolean preprocessorStatistics;

  /** Whether to emit errors to stderr. */
  private final boolean showErrors;

  /**
   * The stack of macro contexts.  Used to keep track of
   * nested macro expansions.
   */
  private LinkedList<TokenBuffer> tcontexts;
  
  /**
   * A flag indicating whether Preprocessor should preprocess tokens
   * or not.  Used to find the open-paren in funlike invocations.
   */
  private int expanding;
  
  /** Whether the preprocessor is currently hoisting a function. */
  private boolean isHoistingFunction;
  
  /** The current list of layout syntax before the next token. */
  Pair<Syntax> layout;

  /** A pointer to the last element in the list of layout syntax. */
  Pair<Syntax> lastLayout;

  /** Stack to store depth and breadth of nested conditionals. */
  LinkedList<Integer> nestedConditionals;

  /** Create a new macro preprocessor */
  public Preprocessor(HeaderFileManager fileManager, MacroTable macroTable,
                      ContextManager contextManager, TokenCreator tokenCreator,
                      Runtime runtime) {
    this.fileManager = fileManager;
    this.macroTable = macroTable;
    this.contextManager = contextManager;
    this.tokenCreator = tokenCreator;
    this.runtime = runtime;

    this.evaluator = new ConditionEvaluator(contextManager, macroTable);
    this.tcontexts = new LinkedList<TokenBuffer>();
    this.expanding = 0;
    this.layout = new Pair<Syntax>(null);
    this.lastLayout = layout;
    this.nestedConditionals = new LinkedList<Integer>();

    preprocessorStatistics = runtime.test("statisticsPreprocessor");
    showErrors = runtime.test("showErrors");
  }
  
  LinkedList<Integer> ss = new LinkedList<Integer>();
  
  /**
   * This class scans the input tokens expanding macros and returns
   * either a Yytoken, whitespace, a directive, or a conditional.
   */
  public Syntax scan() throws IOException {
    Syntax syntax;

    // Get the next token either from the file (base token context)
    // or from a pending macro expansion.

    if (tcontexts.isEmpty()) {

      // Base token context.  The preprocess is pulling tokens from
      // the stream, not from other macro expansions.
        
      syntax = fileManager.scan();

      if (syntax.testFlag(EOI)) {

        // Test to make sure that files contained matched
        // conditional directives.

        int xxx = ss.pop();

        if (contextManager.getDepth() != xxx) {
          System.err.println("JFDKSJAFLKDSA");
          System.err.println(syntax.getLocation());
          System.err.println(contextManager.getDepth());
          System.err.println(xxx);
          assert false;
        }
      }

      /*if (fileManager.includes.size() == 0) {
        if (cfg.roundtrip) {
        this.lastLayout.setTail(new Pair<Syntax>(syntax));
        this.lastLayout = this.lastLayout.tail();
        }
        }*/

    } else {
      // We are inside a macro expansion and preprocessing the
      // tokens of the definition.

      if (tcontexts.peek().done()) {
        // The token context is over.  That is, we reached the end
        // of a macro expansion or parameter prescan.
        syntax = popTokenBuffer();
          
      } else {
        syntax = tcontexts.peek().scan();
        if (syntax.kind() == Kind.CONDITIONAL
            && ( syntax.toConditional().tag() == ConditionalTag.START
                 || syntax.toConditional().tag() == ConditionalTag.NEXT)) {
          syntax.toConditional().context().addRef();
        }
      }
        
      // Handle token-pasting.  Use a while loop because there may
      // be multiple pastes in a row.
      while (syntax.testFlag(PASTE_LEFT)) {
        Syntax next;
          
        // There must be a next token, because we don't allow ## at
        // the beginning or end.
        do {
          next = tcontexts.peek().scan();
        } while (! (next.kind() == Kind.LANGUAGE
                    || next.kind() == Kind.CONDITIONAL_BLOCK
                    || next.testFlag(AVOID_PASTE)));
          
        if (syntax.kind() == Kind.LANGUAGE && next.kind() == Kind.LANGUAGE) {

          Syntax pasted = tokenCreator.pasteTokens(syntax.toLanguage(),
                                                   next.toLanguage());
            
          if (null != pasted) {

            // The paste was successful.

            if (syntax.testFlag(PREV_WHITE)) {
              pasted.setFlag(PREV_WHITE);
            }

            // Use left operand's location for the newly-pasted
            // token.

            pasted.setLocation(syntax.getLocation());
                
            syntax = pasted;
                
            if (next.testFlag(PASTE_LEFT)) {
              syntax.setFlag(PASTE_LEFT);
            }
                
            if (preprocessorStatistics) {
              System.err.format("paste %s %s %s %d\n",
                                "token", "token",
                                getNestedLocation(), 1);
            }

          } else {
            // The paste was unsuccessful.  Add a space between
            // the tokens.
            if (showErrors) {
              System.err.println("error: pasting "
                                 + syntax.getTokenText() + " and "
                                 + next.getTokenText()
                                 + " does not give a valid preprocessing"
                                 + "token");
            }

            // Remove the paste_left flag from the token.

            syntax.clearFlag(PASTE_LEFT);

            pushTokenBuffer(new PlainTokenBuffer(SPACE, next));
          }
              
        } else if (syntax.kind() == Kind.CONDITIONAL_BLOCK
                   || next.kind() == Kind.CONDITIONAL_BLOCK) {
          // One or both token-paste arguments is a conditional.
          // Need to hoist token pasting around it.

          Syntax hoisted;

          hoisted = hoistPasting(syntax, next);
            
          if (null != hoisted) {
            if (preprocessorStatistics) {
              System.err.format("paste %s %s %s %d\n",
                                syntax.kind() == Kind.CONDITIONAL_BLOCK
                                ? "conditional" : "token",
                                next.kind() == Kind.CONDITIONAL_BLOCK
                                ? "conditional" : "token",
                                getNestedLocation(),
                                ((ConditionalBlock) hoisted).branches.size(),
                                1);
            }


            // Return the hoisted pasting.

            syntax = hoisted;

          } else {
            // Just return the original syntax contents, but remove
            // the PASTE_LEFT flag since the pasting was invalid.

            syntax.clearFlag(PASTE_LEFT);

            pushTokenBuffer(new PlainTokenBuffer(SPACE, next));
          }

        } else {
          // syntax will be returned

          pushTokenBuffer(new PlainTokenBuffer(next));
        }
      }
    }

    // Got the next token either from the file or a pending macro
    // expansion.  Now we can expand the token or evaluate the
    // directive.  The method "isExpanding" indicates whether the
    // preprocessor is doing preprocessing or just returning raw
    // tokens from the input (as when collecting function-like macro
    // expansions.)
      
    if (syntax.kind() == Kind.EOF) {

      // Just return EOF.

      return syntax;
        
    } else if (syntax.kind() == Kind.LANGUAGE && isExpanding()
               && ! contextManager.isFalse()) {

      // A regular token.  Check whether it is a macro and expand
      // it.

      return processToken((Language<?>) syntax.toLanguage());
        
    } else if (syntax.kind() == Kind.DIRECTIVE && isExpanding()
               && ! contextManager.isFalse()) {

      // A compound token.  Preprocess it as normal.

      return evaluateDirective(syntax.toDirective());
        
    } else if (syntax.kind() == Kind.DIRECTIVE) {

      // A compound token in a function-like macro invocation. Only
      // conditionals are evaluated.  Otherwise the directive gets
      // collected with the other tokens of the function-like macro
      // invocation.

      Directive directive;
        
      directive = syntax.toDirective();
        
      switch(directive.tag()) {

      case IF:
        // Fall through.
      case IFDEF:
        // Fall through.
      case IFNDEF:
        // Fall through.
      case ELIF:
        // Fall through.
      case ELSE:
        // Fall through.
      case ENDIF:
        return evaluateDirective(directive);

      default:

        // When expansion is off, just return the original directive.
        // During funlike macro prescanning, these will be collected.
        // During argument expansion, these will be evaluated.  For
        // example see
        // cpp_testsuite/cpp/function_arguments_directives.c.

        return directive;
      }

    } else if (syntax.kind() == Kind.CONDITIONAL
               && (isExpanding() || isHoistingFunction)) {
      // A conditional (i.e. the conditional directives generated by
      // and used internally in the preprocessor.  It acts just like
      // a conditional directive.
      if (syntax.kind() == Kind.CONDITIONAL
          && syntax.toConditional().tag() == ConditionalTag.START) {
        Context context;
          
        contextManager.push();
        context = syntax.toConditional().context;
        contextManager.enter(context.getBDD().id());
          
      } else if (syntax.kind() == Kind.CONDITIONAL
                 && syntax.toConditional().tag() == ConditionalTag.NEXT) {
        Context context;
          
        context = syntax.toConditional().context;
        contextManager.enter(context.getBDD().id());
          
      } else if (syntax.kind() == Kind.CONDITIONAL
                 && syntax.toConditional().tag() == ConditionalTag.END) {
        contextManager.pop();
      }
        
      return syntax;

    } else if (syntax.kind() == Kind.CONDITIONAL) {

      // A conditional in a function-like macro invocation.

      return syntax;

    } else if (syntax.kind() == Kind.CONDITIONAL_BLOCK) {
      // A conditional block.  Serialize it into conditionals and
      // regular tokens.
      ConditionalBlock block;
      List<Syntax> serial;
      PlainTokenBuffer stream;
      boolean first;
        
      block = (ConditionalBlock) syntax;
      serial = new LinkedList<Syntax>();
      first = true;
      for (int i = 0; i < block.branches.size(); i++) {
        List<Syntax> branch;
          
        if (first) {
          serial.add(new Conditional(ConditionalTag.START,
                                     block.contexts.get(i)));
          first = false;
        }
        else {
          serial.add(new Conditional(ConditionalTag.NEXT,
                                     block.contexts.get(i)));
        }
        block.contexts.get(i).addRef();
          
        if (null != block.branches.get(i)) {
          // Non-empty branch.
          for (Syntax s : block.branches.get(i)) {
            serial.add(s);
          }
        }
      }
        
      if (! first) {
        serial.add(new Conditional(ConditionalTag.END, null));
      }

      stream = new PlainTokenBuffer(serial);

      pushTokenBuffer(stream);

      return EMPTY;
        
    } else {

      // Any other tokens are just returned.

      return syntax;
    }
  }
  
  public boolean done() {
    if (tcontexts.isEmpty() && fileManager.done()) {
      return true;
    }
    else {
      return false;
    }
  }
  
  /**
   * Hoist conditionals around a token-pasting.
   *
   * @param left A regular or compound token for the left-hand-side of
   * the token-paste operation.
   * @param right A regular or compound token for the right-hand-side
   * of the token-paste operation.
   * @return the pasted token or null if the paste was invalid.
   */
  private Syntax hoistPasting(Syntax left, Syntax right) throws IOException {
    boolean didPaste;
    ConditionalBlock pastedBlock;
    List<List<Syntax>> leftBranches = null;
    List<Context> leftContexts = null;
    List<List<Syntax>> rightBranches = null;
    List<Context> rightContexts = null;
    
    // First hoist conditionals just around each argument.
    
    if (left.kind() == Kind.CONDITIONAL_BLOCK) {
      ConditionalBlock leftBlock = (ConditionalBlock) left;

      leftBranches = new LinkedList<List<Syntax>>();
      leftContexts = new LinkedList<Context>();
    
      leftBranches.add(new LinkedList<Syntax>());
      leftContexts.add(leftBlock.contexts.get(0));

      hoistConditionals(leftBlock.branches.get(0), leftBranches, leftContexts);
    }

    if (right.kind() == Kind.CONDITIONAL_BLOCK) {
      ConditionalBlock rightBlock = (ConditionalBlock) right;

      rightBranches = new LinkedList<List<Syntax>>();
      rightContexts = new LinkedList<Context>();
    
      rightBranches.add(new LinkedList<Syntax>());
      rightContexts.add(rightBlock.contexts.get(0));

      hoistConditionals(rightBlock.branches.get(0), rightBranches,
                        rightContexts);
    }


    // Then hoist pasting around operands.
      
    didPaste = false;
    pastedBlock = null;
    if (left.kind() == Kind.LANGUAGE) {
      
      for (int i = 0; i < rightBranches.size(); i++) {
        List<Syntax> branch;
        
        branch = rightBranches.get(i);
        if (branch.size() > 0) {
          Syntax first = branch.get(0);
          Language<?> pasted = tokenCreator.pasteTokens(left.toLanguage(),
                                                        first.toLanguage());

          if (null != pasted) {

            // A successful paste.

            branch.remove(0);
            branch.add(0, pasted);

            // Use left operand's location for the newly-pasted
            // token.

            pasted.setLocation(left.getLocation());

            didPaste = true;

          } else {
            // Don't expand tokens of the invalid paste.
            if (showErrors) {
              System.err.println("error: pasting " + left.getTokenText()
                                 + " and " + first.getTokenText()
                                 + " does not give a valid preprocessing "
                                 + "token");
            }
          }
        }
      }
      
      pastedBlock = new ConditionalBlock(rightBranches, rightContexts);
      
    } else if (right.kind() == Kind.LANGUAGE) {

      for (int i = 0; i < leftBranches.size(); i++) {
        List<Syntax> branch;
        
        branch = leftBranches.get(i);
        if (branch.size() > 0) {
          Syntax last = branch.get(branch.size() - 1);
          Language<?> pasted = tokenCreator.pasteTokens(last.toLanguage(),
                                                        right.toLanguage());

          if (null != pasted) {

            // Paste was successful.

            branch.remove(branch.size() - 1);
            branch.add(pasted);

            pasted.setLocation(last.getLocation());

            didPaste = true;

          } else {
            // Don't expand tokens of the invalid paste.
            if (showErrors) {
              System.err.println("error: pasting " + last.getTokenText()
                                 + " and " + left.getTokenText()
                                 + " does not give a valid preprocessing "
                                 + "token");
            }
          }
        }
      }

      pastedBlock = new ConditionalBlock(leftBranches, leftContexts);

    } else {
      throw new RuntimeException("TODO hoist both token-paste args");
    }
    
    // Preserve the PASTE_LEFT flag on the pasted token.  This is
    // necessary since the pasted token may be the argument of another
    // token-paste operation.

    if (right.testFlag(PASTE_LEFT)) {
      pastedBlock.setFlag(PASTE_LEFT);
    } else {
      pastedBlock.clearFlag(PASTE_LEFT);
    }
    
    if (didPaste) {
      return pastedBlock;
    } else {
      return null;
    }
  }
  
  /**
   * Flatten nested conditional blocks.  A conditional block without
   * any nested conditional blocks is returned.
   *
   * @param block The conditional block to flatten.
   * @return The flatten conditional block without any nested
   * conditional blocks.
   */
  private ConditionalBlock flattenConditionalBlock(ConditionalBlock block) {
    ConditionalBlock newBlock;
    List<List<Syntax>> newBranches;
    List<Context> newContexts;
    
    newBranches = new LinkedList<List<Syntax>>();
    newContexts = new LinkedList<Context>();

    for (int i = 0; i < block.branches.size(); i++) {
      List<Syntax> branch;
      Context context;
      List<List<Syntax>> branches;
      List<Context> contexts;
      
      branch = block.branches.get(i);
      context = block.contexts.get(i);

      branches = new LinkedList<List<Syntax>>();
      contexts = new LinkedList<Context>();

      branches.add(new LinkedList<Syntax>());
      contexts.add(context);
      
      flattenBranch(branch, branches, contexts);
      
      newBranches.addAll(branches);
      newContexts.addAll(contexts);
    }
    
    // Trim infeasible branches when presence condition is false.
    // This reduces the number of branches in the flattened
    // conditional.
    for (int i = 0; i < newContexts.size(); i++) {
      Context context;
      
      context = newContexts.get(i);
      
      if (context.isFalse()) {
        context.delRef();
        newContexts.remove(i);
        newBranches.remove(i);
        i--;
      }
    }
    
    newBlock = new ConditionalBlock(newBranches, newContexts);

    // Copy the flags from the old block to the new.
    for (int i = 0; i < Syntax.MAX_FLAGS; i++) {
      if (block.testFlag(i)) {
        newBlock.setFlag(i);
      }
    }

    return newBlock;
  }
  
  /**
   * Takes a list of tokens and conditional blocks and flattens and
   * conditional blocks contained in the branch.
   *
   * @param list The list of tokens.
   * @param branches The flattened branches.
   * @param contexts The presence conditions of the flattened branches
   */
  private void flattenBranch(List<Syntax> list,
                             List<List<Syntax>> branches,
                             List<Context> contexts) {
    for (Syntax syntax : list) {
      if (syntax.kind() == Kind.LANGUAGE || syntax.kind() == Kind.LAYOUT) {
        for (List<Syntax> branch : branches) {
          branch.add(syntax);
        }
      }
      else if (syntax.kind() == Kind.CONDITIONAL_BLOCK) {
        ConditionalBlock block;
        List<List<Syntax>> newBranches;
        List<Context> newContexts;
        
        block = flattenConditionalBlock((ConditionalBlock) syntax);
        
        newBranches = new LinkedList<List<Syntax>>();
        newContexts = new LinkedList<Context>();

        for (int i = 0; i < branches.size(); i++) {
          for (int j = 0; j < block.branches.size(); j++) {
            List<Syntax> newBranch;
            Context newContext;
            
            newBranch = new LinkedList<Syntax>();
            newBranch.addAll(branches.get(i));
            newBranch.addAll(block.branches.get(j));
            
            newContext = contexts.get(i).and(block.contexts.get(j));
            block.contexts.get(j).delRef();
            
            newBranches.add(newBranch);
            newContexts.add(newContext);
          }
          contexts.get(i).delRef();
        }
        
        branches.clear();
        branches.addAll(newBranches);
        
        contexts.clear();
        contexts.addAll(newContexts);
      }
    }
  }
  
  /**
   * Evaluate the directive.  The directives are dispatched to handler
   * functions per type of directive.
   *
   * @param directive The directive to evaluate.
   */
  private Syntax evaluateDirective(Directive directive) throws IOException {
    int s;
    boolean invalid;

    s = 1;

    while (s < directive.size()
           && ((Syntax) directive.get(s)).kind() != Kind.LANGUAGE) {
      s++;
    }

    invalid = false;
    switch (directive.tag()) {
    case IF:
      return ifDirective(directive, s);

    case IFDEF:
      return ifdefDirective(directive, s);

    case IFNDEF:
      return ifndefDirective(directive, s);

    case ELIF:
      return elifDirective(directive, s);

    case ELSE:
      return elseDirective(directive, s);

    case ENDIF:
      return endifDirective(directive, s);

    case INCLUDE:
      return includeDirective(directive, s, false);

    case INCLUDE_NEXT:
      return includeDirective(directive, s, true);

    case DEFINE:

      defineDirective(directive, s);

      return EMPTY;

    case UNDEF:

      undefDirective(directive, s);

      return EMPTY;

    case LINE:

      lineDirective(directive, s);

      return EMPTY;

    case ERROR:

      errorDirective(directive, s);

      return EMPTY;

    case WARNING:

      warningDirective(directive, s);

      return EMPTY;

    case PRAGMA:

      pragmaDirective(directive, s);

      return EMPTY;

    case LINEMARKER:

      // Pass linemarkers through.  Better for debugging.

      return directive;

    default:
      if (showErrors) {
        System.err.println("error: invalid preprocessor directive");
      }

      return EMPTY;
    }

    // Should never reach here.
  }
  
  /**
   * Process if directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   * It passes the conditional expression to a function that
   * evaluates the expression.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private Syntax ifDirective(Directive directive, int s)
    throws IOException {

    // Move past the whitespace after the directive name.

    while (s < directive.size()
           && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;
    
    if (s >= directive.size()) {

      if (showErrors) {
        System.err.println("error: empty if directive");
      }

      return EMPTY;

    } else {
      List<Syntax> tokens;
      BDD bdd;

      tokens = new LinkedList<Syntax>();
      while (s < directive.size()) {
        Syntax syntax;
        
        syntax = (Syntax) directive.get(s);
        if (syntax.kind() == Kind.LANGUAGE) {
          tokens.add(syntax);
        }
        
        s++;
      }

      if (preprocessorStatistics) {
        nestedConditionals.push(1);
      }

      bdd = evaluateExpression(tokens, "if");
      
      contextManager.push();
      contextManager.enter(bdd);

      Conditional conditional = new Conditional(ConditionalTag.START,
                                                contextManager.reference());

      conditional.setLocation(directive.getLocation());

      return conditional;
    }
  }
  
  /**
   * Take expression tokens and return an expanded, completed, parsed,
   * and evaluated expression as a BDD.
   *
   * @param tokens The tokens of the expression to evaluate.
   */
  private BDD evaluateExpression(List<Syntax> tokens, String type)
    throws IOException {
    PlainTokenBuffer scontext;
    List<Syntax> expanded;
    List<List<Syntax>> completed;
    List<Context> contexts;
    List<BDD> terms;
    BDD newBdd;
    int saveExpanding;
    Context global;

    // Add an end-of-expression marker.
    Layout eoe = new Layout("");
    eoe.setFlag(EOE);
    tokens.add(eoe);
    
    // Push a new token context to expand macros in the expression.
    scontext = new PlainTokenBuffer(tokens);
    
    saveExpanding = expanding;
    expanding = 0;
    pushTokenBuffer(scontext);

    // Expand conditional expression macros.
    expanded = new LinkedList<Syntax>();
    for (;;) {
      Syntax syntax;
      
      syntax = scan();

      if (syntax.testFlag(EOE)) {
        break;
      }
      
      expanded.add(syntax);
      
      if (syntax.kind() == Kind.LANGUAGE
          && syntax.getTokenText().equals("defined")) {
        Syntax s;
        // The number of tokens left to collect.
        int collect;
        List<Syntax> defined;
        
        defined = new LinkedList<Syntax>();
        s = null;
        collect = 1;
        for (;;) {
          // TODO fix fonda/macros/defined1.c
          s = tcontexts.peek().scan();
          
          if (s.testFlag(EOE)) {
            break;

          } else if (s.kind() == Kind.LANGUAGE
                     && s.toLanguage().tag().ppTag()
                     == PreprocessorTag.OPEN_PAREN) {
            // Collect two more tokens, the macro and the rparen.
            collect = 2;

          } else if (s.kind() == Kind.LANGUAGE
                     && s.toLanguage().tag().ppTag()
                     == PreprocessorTag.CLOSE_PAREN) {
            collect--;

          } else if (s.kind() == Kind.CONDITIONAL) {
            collect--;
            throw new RuntimeException("NEED DEFINED OPERATOR HOISTING");

          } else if (s.kind() == Kind.LANGUAGE) {
            collect--;

          } else if (s.kind() == Kind.CONDITIONAL_BLOCK) {
            collect--;
            throw new RuntimeException("CONDITIONAL BLOCK IN DEFINED");
          }
          
          defined.add(s);
          
          if (collect == 0 || (s.kind() == Kind.LANGUAGE
                               && s.toLanguage().tag().ppTag()
                               == PreprocessorTag.CLOSE_PAREN)) {
            break;
          }
        }
        
        expanded.addAll(defined);

        if (s.testFlag(EOE)) {
          break;
        }
      }
    }
    
    // Removed because the list may still be on the context
    // stack. Clearing it causes a ConcurrentModificationException.
    // For an example, see macros/conditional_expression_weirdness.c.
    
    //tokens.clear();
    
    popTokenBuffer();
    expanding = saveExpanding;
    
    // Trim leading whitespace.
    while (expanded.size() > 0 && expanded.get(0).kind() == Kind.LAYOUT) {
      expanded.remove(0);
    }
    
    // Collect conditionals into conditional blocks.
    global = contextManager.reference();
    expanded = buildBlocks(expanded, global);
    global.delRef();
    
    completed = new LinkedList<List<Syntax>>();
    contexts = new LinkedList<Context>();
    
    completed.add(new LinkedList<Syntax>());
    contexts.add(contextManager.reference());
    
    // Complete conditional expressions.
    hoistConditionals(expanded, completed, contexts);

    // TODO dedup expressions (put on hold pending evaluation of need
    // for the optimization).
    
    expanded.clear();
    
    // Union of all terms, where Term = Context && CompletedExpression.
    terms = new LinkedList<BDD>();
    for (int i = 0; i < completed.size(); i++) {
      List<Syntax> tokenlist = completed.get(i);
      Context context = contexts.get(i);
      
      if (! context.isFalse()) {
        boolean unknown = false;
        StringBuilder string = new StringBuilder();
        BDD bdd;
        
        for (Syntax token : tokenlist) {

          // FIXME Temporarily disabled this feature (unknown
          // configs).  It seems to be causing incorrectness with
          // CONFIG_NR_CPUS in include/linux/spinlock.h.

          if (true || ! (token.testFlag(UNKNOWN_DEF))) {
            string.append(token.getTokenText());
            string.append(" ");

          } else {
            // Mark presence conditions containing unknown macro defs.
            unknown = true;
            break;
          }
        }

        if (! unknown) {
          bdd = evaluator.evaluate(string.toString());

          if (! bdd.isZero()) {
            terms.add(bdd.and(context.getBDD()));
          }
          
          bdd.free();
        } else {
          // Assume expression is true if it contains an unknown defition.
          terms.add(context.getBDD().id());
        }
      }

      context.delRef();
    }
    
    // Take union of each subexpression term.  Use raw BDD operations
    // for efficiency.
    newBdd = contextManager.getBDDFactory().zero();

    for (BDD term : terms) {
      BDD bdd;
      
      bdd = newBdd.or(term);
      term.free();
      newBdd.free();
      newBdd = bdd;
    }
    
    // TODO: change back to just preprocessorStatistics
    //if (preprocessorStatistics) {
    if (preprocessorStatistics || /*evaluator.sawNonboolean() &&*/ runtime.test("statisticsLanguage")) {

      System.err.format("conditional %s %s %s %d %d\n",
                        type, getNestedLocation(),
                        evaluator.sawNonboolean() ? "nonboolean" : "boolean",
                        nestedConditionals.size() - 1,
                        completed.size());
    }

    return newBdd;
  }
  
  /**
   * Hoist conditionals around a list of tokens that may contain
   * conditionals.  This method returns a list of token-lists and the
   * presence condition of each token-list via parameters.
   *
   * @param list The list of tokens in the expression.
   * @param tokenlists Returns the hoisted expressions.  Intialized by
   * caller.
   * @param contexts Returns the hoisted expressions' presence
   * conditions.  Initialized by caller.
   */
  private void hoistConditionals(List<Syntax> list,
                               List<List<Syntax>> tokenlists,
                               List<Context> contexts) {
    for (Syntax s : list) {
      if (s.kind() == Kind.LANGUAGE) {
        for (List<Syntax> tokenlist : tokenlists) {
          tokenlist.add(s);
        }

      } else if (s.kind() == Kind.CONDITIONAL_BLOCK) {
        ConditionalBlock block;
        List<List<Syntax>> newTokenlists;
        List<Context> newContexts;
        
        block = (ConditionalBlock) s;
        newTokenlists = new LinkedList<List<Syntax>>();
        newContexts = new LinkedList<Context>();
        for (int i = 0; i < block.contexts.size(); i++) {
          List<Syntax> branch;
          Context context;
          List<List<Syntax>> branchTokenlists;
          List<Context> branchContexts;
          
          branch = block.branches.get(i);
          context = block.contexts.get(i);

          branchTokenlists = new LinkedList<List<Syntax>>();
          branchContexts = new LinkedList<Context>();
          branchTokenlists.add(new LinkedList<Syntax>());
          branchContexts.add(context);
          context.addRef();
          
          hoistConditionals(branch, branchTokenlists, branchContexts);
          
          // Combine strings and bdds with newStrings and newBdds.
          for (int a = 0; a < tokenlists.size(); a++) {
            for (int b = 0; b < branchTokenlists.size(); b++) {
              LinkedList<Syntax> tokenlist;
              Context newContext;
              
              tokenlist = new LinkedList<Syntax>();
              tokenlist.addAll(tokenlists.get(a));
              tokenlist.addAll(branchTokenlists.get(b));
              newContext = contexts.get(a).and(branchContexts.get(b));
              if (! newContext.isFalse()) {
                newTokenlists.add(tokenlist);
                newContexts.add(newContext);
              }
              else {
                newContext.delRef();
              }
            }
          }
          
          for (Context c : branchContexts) {
            c.delRef();
          }
        }
        
        tokenlists.clear();
        tokenlists.addAll(newTokenlists);

        for (Context c : contexts) {
          c.delRef();
        }
        contexts.clear();
        contexts.addAll(newContexts);
        
        block.free();
      }
    }
  }
  
  /**
   * Process ifdef directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private Syntax ifdefDirective(Directive directive, int s) {

    // Move past the whitespace after the directive name.

    while (s < directive.size()
           && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;
    
    if (s >= directive.size()) {

      if (showErrors) {
        System.err.println("error: empty ifdef directive");
      }

      return EMPTY;

    } else {
      String str;
      BDD bdd;
      
      if (((Syntax) directive.get(s)).kind() == Kind.LANGUAGE
          && ((Syntax) directive.get(s)).toLanguage().tag().hasName()) {

        // Valid macro name.

      } else {

        if (showErrors) {
          System.err.println("error: invalid macro name in ifdef");
        }

        return EMPTY;
      }
      
      str = contextManager.getVariableManager()
        .createDefinedVariable(((Syntax) directive.get(s)).getTokenText());
      
      bdd = evaluator.evaluate(str);
      
      contextManager.push();
      contextManager.enter(bdd);
      
      if (preprocessorStatistics) {
        System.err.format("conditional %s %s %s %d %d\n",
                          "ifdef", getNestedLocation(),
                          "boolean",
                          nestedConditionals.size(), 1);
        nestedConditionals.push(1);
      }

      Conditional conditional = new Conditional(ConditionalTag.START,
                                                contextManager.reference());

      conditional.setLocation(directive.getLocation());

      return conditional;
    }
  }
  
  /**
   * Process ifndef directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private Syntax ifndefDirective(Directive directive, int s) {

    // Move past the whitespace after the directive name.

    while (s < directive.size()
           && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;
    
    if (s >= directive.size()) {

      if (showErrors) {
        System.err.println("error: empty ifndef directive");
      }

      return EMPTY;

    } else {
      String str;
      BDD bdd;
      
      if (((Syntax) directive.get(s)).toLanguage().tag().hasName()) {

        // Valid macro name.

      } else {

        if (showErrors) {
          System.err.println("error: invalid macro name in ifdef");
        }

        return EMPTY;
      }

      str = contextManager.getVariableManager()
        .createNotDefinedVariable(((Syntax) directive.get(s)).getTokenText());
      
      bdd = evaluator.evaluate(str);

      contextManager.push();
      contextManager.enter(bdd);

      if (preprocessorStatistics) {
        System.err.format("conditional %s %s %s %d %d\n",
                          "ifndef", getNestedLocation(),
                          "boolean",
                          nestedConditionals.size(), 1);
        nestedConditionals.push(1);
      }

      Conditional conditional = new Conditional(ConditionalTag.START,
                                                contextManager.reference());

      conditional.setLocation(directive.getLocation());

      return conditional;
    }
  }
  
  /**
   * Process elif directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private Syntax elifDirective(Directive directive, int s)
    throws IOException {

    // Move past the whitespace after the directive name.

    while (s < directive.size()
           && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;
    
    if (s >= directive.size()) {

      if (showErrors) {
        System.err.println("error: empty if directive");
      }

      return EMPTY;

    } else {
      List<Syntax> tokens;
      BDD bdd;

      tokens = new LinkedList<Syntax>();
      while (s < directive.size()) {
        Syntax syntax;
        
        syntax = (Syntax) directive.get(s);
        if (syntax.kind() == Kind.LANGUAGE) {
          tokens.add(syntax);
        }
        
        s++;
      }
      
      contextManager.enterElse();
      
      if (preprocessorStatistics) {
        nestedConditionals.push(nestedConditionals.pop() + 1);
      }

      bdd = evaluateExpression(tokens, "elif");
      
      contextManager.enterElif(bdd);

      Conditional conditional = new Conditional(ConditionalTag.NEXT,
                                                contextManager.reference());

      conditional.setLocation(directive.getLocation());

      return conditional;
    }
  }
  
  /**
   * Process else directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private Syntax elseDirective(Directive directive, int s) {

    contextManager.enterElse();

    if (preprocessorStatistics) {
      System.err.format("conditional %s %s %s %d %d\n",
                        "else", getNestedLocation(),
                        "boolean",
                        nestedConditionals.size() - 1, 1);
      nestedConditionals.push(nestedConditionals.pop() + 1);
    }

    Conditional conditional = new Conditional(ConditionalTag.NEXT,
                                              contextManager.reference());

    conditional.setLocation(directive.getLocation());

    return conditional;
  }
  
  /**
   * Process endif directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private Syntax endifDirective(Directive directive, int s) {

    try {
      contextManager.pop();

      if (preprocessorStatistics) {
        int breadth = nestedConditionals.pop();

        System.err.format("endif %s %s %d\n",
                          getNestedLocation(),
                          nestedConditionals.size(),
                          breadth);
      }

    } catch (Exception e) {
      throw new RuntimeException("unmatched #endif found");
    }

    Conditional conditional = new Conditional(ConditionalTag.END, null);

    conditional.setLocation(directive.getLocation());

    return conditional;
  }
  
  /**
   * Process include directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   * Computed macros are evaluated.  If the macro is multiply-defined,
   * we generate multiple includes that are wrapped in conditional
   * objects.  The preprocessor needs 
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   * @param includeNext Whether the include directive was an
   * #include_next directive.
   */
  private Syntax includeDirective(Directive directive, int s,
                                  boolean includeNext) throws IOException {
    StringBuilder sb;
    String str;
    LinkedList<Syntax> tokens;

    // Move past the whitespace after the directive name.
    while (s < directive.size()

           && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;
    
    sb = new StringBuilder();
    
    // Combine all tokens before next whitespace.
    tokens = new LinkedList<Syntax>();
    while (s < directive.size()
           && (((Syntax) directive.get(s)).kind() != Kind.LAYOUT)) {
      sb.append(((Syntax) directive.get(s)).getTokenText());
      tokens.add((Syntax) directive.get(s));
      s++;
    }
    
    while (s < directive.size()) {
      tokens.add((Syntax) directive.get(s));
      s++;
    }
    
    while (tokens.getLast().kind() == Kind.LAYOUT) {
      tokens.removeLast();
    }
    
    str = sb.toString();

    if (str.length() == 0) {
      if (showErrors) {
        System.err.println("error: empty include directive");
      }

      return EMPTY;

    } else {
      char first, last;
      String headerName;
      boolean sysHeader;
      
      first = str.charAt(0);
      last = str.charAt(str.length() - 1);
      
      sysHeader = false;
      if ('<' == first && '>' == last) {

        // System header.

        sysHeader = true;

      } else if ('"' == first && '"' == last) {

        // User header.

      } else {

        // Computed header.

        List<Syntax> computed, blocks;
        PlainTokenBuffer sc;
        List<List<Syntax>> completed;
        List<String> completedStrings;
        List<Context> contexts;
        Context global;
          
        // Add an end-of-expansion marker.
        Layout eoe = new Layout("");
        eoe.setFlag(EOE);
        tokens.add(eoe);

        sc = new PlainTokenBuffer(tokens);
        pushTokenBuffer(sc);
          
        computed = new LinkedList<Syntax>();
        for (;;) {
          Syntax syntax;
            
          syntax = scan();
            
          if (syntax.testFlag(EOE)) {
            break;
          }
            
          computed.add(syntax);
        }
          
        popTokenBuffer();
          
        global = contextManager.reference();

        // Build conditional blocks.  Then hoistConditionals is used
        // to hoist the conditionals around the include's
        // expression.
        blocks = buildBlocks(computed, global);
        global.delRef();
          
        completed = new LinkedList<List<Syntax>>();
        contexts = new LinkedList<Context>();
          
        completed.add(new LinkedList<Syntax>());
        contexts.add(contextManager.reference());
          
        // Make all combinations.
        hoistConditionals(blocks, completed, contexts);
          
        // Build strings and trim those using macros with unknown
        // definitions.
        completedStrings = new LinkedList<String>();
        for (int i = 0; i < completed.size(); i++) {
          List<Syntax> tokenlist = completed.get(i);
          Context context = contexts.get(i);
          StringBuilder string = new StringBuilder();
          boolean unknown = false;
            
          for (Syntax token : tokenlist) {
            if (! (token.testFlag(UNKNOWN_DEF))) {
              string.append(token.getTokenText());
            }
            else {
              // Mark those containing unknown definitions.
              unknown = true;
              string.delete(0, string.length());
              string.append(token.getTokenText());
              break;
            }
          }
            
          if (! unknown) {
            completedStrings.add(string.toString());
          }
          else {
            if (showErrors) {
              System.err.println("warning: computed header used unknown " +
                                 "definition(s): " + string.toString());
            }
            completed.remove(i);
            context.delRef();
            contexts.remove(i);
            i--;
          }
        }
          
        return fileManager.includeComputedHeader(completedStrings, contexts,
                                                 includeNext, contextManager,
                                                 macroTable);
      }

      // It is not a computed header.  Include the file normally.
        
      headerName = str.substring(1, str.length() - 1);

      Syntax linemarker
        = fileManager.includeHeader(headerName, sysHeader, includeNext,
                                    contextManager, macroTable);
        
      if (EMPTY != linemarker) {
        ss.push(contextManager.getDepth());
      }

      return linemarker;
    }
  }

  /**
   * Process define directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   * This function parses the macro, determining whether its function-
   * or object-like and adds a new table entry given the current context.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private void defineDirective(Directive directive, int s) {

    // Move past the whitespace after the directive name.

    while (s < directive.size()
           && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;

    if (s >= directive.size()) {
      if (showErrors) {
        System.err.println("error: empty define directive");
      }
      return;

    } else if (! ((Syntax) directive.get(s)).toLanguage().tag().hasName()) {
      if (showErrors) {
        System.err.println("error: defining a non-identifier token");
      }

    } else {
      String name;
      List<String> formals;
      LinkedList<Syntax> definition;
      boolean isFunctionlike;
      Macro macro;
      Context context;
      String variadic;
      
      name = ((Syntax) directive.get(s)).getTokenText();
      formals = null;
      definition = null;
      variadic = null;
      
      // Move past the macro name.
      s++;
      isFunctionlike = false;
      if (s < directive.size()) {
        
        // Check if macro is function-like.  If so, we need to parse
        // the macros formal arguments.  To be a function-like macro,
        // the macro name, e.g. "F", must be followed immediately by
        // an open paren, e.g. "F()".  "F ()" is an object-like macro
        // as in cpp_testsuite/cpp/function_false_function.c.
        
        if (((Syntax) directive.get(s)).kind() == Kind.LANGUAGE
            && ((Syntax) directive.get(s)).toLanguage().tag().ppTag()
            == PreprocessorTag.OPEN_PAREN
            && ! ((Syntax) directive.get(s)).toLanguage()
            .testFlag(PREV_WHITE)) {
          // Move past paren.
          s++;
          
          do {
            // Move past whitespace.
            while (s < directive.size()
                   && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;
            
            if (((Syntax) directive.get(s)).kind() == Kind.LANGUAGE
                && ((Syntax) directive.get(s)).toLanguage().tag().hasName()) {
              // We are on a formal argument name.

              if (formals == null) {
                formals = new LinkedList<String>();
              }
              
              // Check for named variadic.
              if (s < (directive.size() - 1)
                  && ((Syntax) directive.get(s + 1)).kind() == Kind.LANGUAGE
                  && ((Syntax) directive.get(s + 1)).toLanguage()
                  .tag().ppTag() == PreprocessorTag.ELLIPSIS) {
                if (null != variadic) {
                  if (showErrors) {
                    System.err.println("error: no args allowed after " +
                                       "variadic");
                  }
                  return;
                }
                variadic = ((Syntax) directive.get(s)).getTokenText();
                s++;

              } else {
                formals.add(((Syntax) directive.get(s)).getTokenText());
              }

            } else if (((Syntax) directive.get(s)).kind() == Kind.LANGUAGE
                       && ((Syntax) directive.get(s)).toLanguage()
                       .tag().ppTag() == PreprocessorTag.ELLIPSIS) {
              // The formal argument is variadic.
              if (null != variadic) {
                if (showErrors) {
                  System.err.println("error: no args allowed after variadic");
                }
                return;
              }

              // The default name of the variadic argument.
              variadic = "__VA_ARGS__";

            } else if (((Syntax) directive.get(s)).kind() == Kind.LANGUAGE
                       && ((Syntax) directive.get(s)).toLanguage()
                       .tag().ppTag() == PreprocessorTag.CLOSE_PAREN
                       && null == formals) {
              // Function-like macro with no arguments.  Done looking
              // for formals.
              s++;
              break;
            } else {
              if (showErrors) {
                System.err.println("error: parameter name missing");
              }
              return;
            }
            
            s++;
            
            //move past whitespace
            while (s < directive.size()
                   && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;

            if (s >= directive.size()) {
              if (showErrors) {
                System.err.println("error: missing end parenthesis");
              }
              return;
            }
            
            if (((Syntax) directive.get(s)).kind() == Kind.LANGUAGE
                && ((Syntax) directive.get(s)).toLanguage().tag().ppTag()
                == PreprocessorTag.COMMA) {
              // Comma-separated formal arguments.
              s++;

            } else if (((Syntax) directive.get(s)).kind() == Kind.LANGUAGE
                       && ((Syntax) directive.get(s)).toLanguage()
                       .tag().ppTag() == PreprocessorTag.CLOSE_PAREN) {
              // Done looking for formals.
              s++;
              break;

            } else {
              if (showErrors) {
                System.err.println("error: missing end parenthesis or comma");
              }
              return;
            }
          } while (true);

          isFunctionlike = true;
        }
        
        // Move past the whitespace after the macro name.
        while (s < directive.size()
               && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;
        
        if (s >= directive.size() ) {
          // Empty macro.

        } else {

          // Read in the macro definition, checking token-paste and
          // stringify operations.  The operators are removed and
          // instead the operands of pasting and stringification are
          // flagged as such.

          boolean followingPasteOp = false;
          boolean followingStringify = false;
          boolean prevWhite = false;
          final String pasteError
            = "'##' cannot appear at either end of a macro expansion";
          
          do {
            Syntax syntax;
            
            syntax = (Syntax) directive.get(s);
            
            if (isFunctionlike && syntax.kind() == Kind.LANGUAGE
                && syntax.toLanguage().tag().ppTag()
                == PreprocessorTag.HASH) {
              // Stringifification operator.
              Syntax next;
              int ss;
              boolean valid;
              
              // Stringification can only be done on macro arguments.
              // The following code checks for that.
              ss = s + 1;
              valid = false;
              while (ss < directive.size()) {
                next = (Syntax) directive.get(ss);
                if (next.kind() == Kind.LANGUAGE) {
                  if (null != formals
                      && formals.contains(next.getTokenText())) {
                    valid = true;

                  } else if (null != variadic
                             && next.getTokenText().equals(variadic)) {
                    valid = true;
                  }
                  break;
                }
                ss++;
              }
              
              if (! valid) {
                if (showErrors) {
                  System.err.println("'#' is not followed by a macro " +
                                     "parameter");
                }
              }

            } else if (syntax.kind() == Kind.LANGUAGE
                       && syntax.toLanguage().tag().ppTag()
                       == PreprocessorTag.DOUBLE_HASH) {
              // Token-paste operator.

              // The token-paste operator is binary, so it can't
              // be the first token of the definition.
              if (null == definition) {
                if (showErrors) {
                  System.err.println(pasteError);
                }
                return;
              }

              // Flag the previous token as the left operand of a
              // token-pasting.
              definition.getLast().setFlag(PASTE_LEFT);
              
            } else if (syntax.kind() == Kind.LANGUAGE) {
              // A regular token.
              Language<?> token;
              
              token = (Language<?>) syntax;
              
              if (null == definition) {
                definition = new LinkedList<Syntax>();
              }
              
              if (prevWhite) {
                // Flat the token as having whitespace before it.
                // Whitespace tokens are removed from macro
                // definitions.
                token.setFlag(PREV_WHITE);
              }
              
              if (followingStringify) {
                // Flag the token as a stringification argument.
                token.setFlag(STRINGIFY_ARG);
              }
              
              definition.add(token);
              
            } else if (syntax.kind() == Kind.LAYOUT
                       && (! followingStringify)) {
              // Whitespace.
              if (null != definition) {
                prevWhite = true;
              }
            }
            
            if (syntax.kind() == Kind.LANGUAGE) {
              followingPasteOp = syntax.kind() == Kind.LANGUAGE &&
                syntax.toLanguage().tag().ppTag()
                == PreprocessorTag.DOUBLE_HASH;
              followingStringify = syntax.kind() == Kind.LANGUAGE &&
                syntax.toLanguage().tag().ppTag() == PreprocessorTag.HASH;
              
              if (! followingStringify) {
                // If the stringification operator has whitespace
                // before it, flag the stringification argument
                // instead, since we remove the operator.
                prevWhite = false;
              }
            }
            
            s++;
          } while (s < directive.size());
          
          if (followingPasteOp) {
            // The token-pasting operator can't appear at the end of a
            // definition since it's a binary operator.
            if (showErrors) {
              System.err.println(pasteError);
            }
            return;
          }
        }
      }
      
      // Create and store the macro definitions in the macro symbol
      // table.

      if (isFunctionlike) {
        macro = new Macro.Function(formals, definition, variadic);

      } else {
        macro = new Macro.Object(definition);
      }
      
      macroTable.define(name, macro, contextManager);

      if (preprocessorStatistics) {
        System.err.format("define %s %s %s %d\n",
                          name, isFunctionlike ? "fun" : "var",
                          directive.getLocation(),
                          macroTable.countDefinitions(name));
      }
    }
  }
  
  /**
   * Process undef directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private void undefDirective(Directive directive, int s) {

    // Move past the whitespace after the directive name.

    while (s < directive.size()
           && ((Syntax) directive.get(s)).kind() == Kind.LAYOUT) s++;
    
    if (s >= directive.size()) {
      if (showErrors) {
        System.err.println("error: empty undef directive");
      }
      return;

    } else {
      String name;
      Context context;
      
      name = ((Syntax) directive.get(s)).getTokenText();

      // TODO should probably check that name is an identifier or
      // keyword.
      
      macroTable.undefine(name, contextManager);

      if (preprocessorStatistics) {
        System.err.format("undef %s %s %d\n",
                          name, directive.getLocation(),
                          macroTable.countDefinitions(name));
      }
    }
  }
  
  /**
   * Process line directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private void lineDirective(Directive directive, int s) {

    // TODO implement the line directive so it updates the internal
    // line counter and filename.  This could also interact with
    // conditionals, so we would need an interal filename and line
    // counter for each differeing presence condition.

    if (preprocessorStatistics) {
      System.err.format("line_directive %s", getNestedLocation());
    }

  }
  
  /**
   * Process error directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private void errorDirective(Directive directive, int s) {

    // Just look for the original error directive.

    if (preprocessorStatistics) {
      System.err.format("error_directive %s", getNestedLocation());
    }

  }
  
  /**
   * Process warning directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private void warningDirective(Directive directive, int s) {

    // TODO output the warning message when showErrors is on.

    if (preprocessorStatistics) {
      System.err.format("warning_directive %s", getNestedLocation());
    }

  }
  
  /**
   * Process pragma directive.  This takes the list of syntactic units
   * and the position of the first syntax after the directive name.
   *
   * @param directive The tokens of the directive.
   * @param s The number of tokens after the directive name.
   */
  private void pragmaDirective(Directive directive, int s) {

    // Pragma directives are compiler dependent.  TODO implement gcc's
    // pragma once by exploiting the existing guard macro
    // implementation.

    if (preprocessorStatistics) {
      System.err.format("pragma_directive %s", getNestedLocation());
    }

  }
  
  /**
   * Check a token to see if it's a defined macro and expand if necessary.
   * Multiply-defined macros are expanded to all definitions, but
   * wrapped with conditionals.  The Preprocessor must check for conditional
   * objects to update the context and also to normalize token-pasting
   * and stringification that involve conditionals
   *
   * @param token The token to try to expand.
   */
  private Syntax processToken(Language<?> token) throws IOException {
    String name;
      
    name = token.getTokenText();

    // Process built-in macros.
      
    if (name.equals("__FILE__")) {

      // Emit the current filename.

      List<Syntax> list;
      String fileName = fileManager.include.getLocation().file;
        
      list = new LinkedList<Syntax>();
      list.add(tokenCreator.createStringLiteral("\"" + fileName + "\""));
        
      pushTokenBuffer(new SingleExpansionBuffer(name, list));

      if (preprocessorStatistics) {
        System.err.format("object %s %s %d %d %d\n",
                          name, getNestedLocation(),
                          getMacroNestingDepth(), 1, 1);
      }

      return createMacroLineMarker(name);

    } else if (name.equals("__LINE__")) {

      // Emit the current line number.

      List<Syntax> list;
      int lineNumber = fileManager.include.getLocation().line;
        
      list = new LinkedList<Syntax>();
      list.add(tokenCreator.createIntegerConstant(lineNumber));
        
      pushTokenBuffer(new SingleExpansionBuffer(name, list));
        
      if (preprocessorStatistics) {
        System.err.format("object %s %s %d %d %d\n",
                          name, getNestedLocation(),
                          getMacroNestingDepth(), 1, 1);
      }

      return createMacroLineMarker(name);
    }


    // Check whether the token is eligible for expansion.

    if (! token.tag().hasName()  // Not a preprocessor identifier.
        || (! macroTable.contains(name))  // Not in the macro table.
        || token.testFlag(NO_EXPAND)) {  //  Has the no expand flag.

      // No expansion possible.

      return token;
    } else if (! macroTable.isEnabled(name)) {  // Is disabled.

      // No expansion possible.

      token = (Language<?>) token.copy();
      token.setFlag(NO_EXPAND);

      return token;
    }
      
    List<Entry> entries;
    boolean hasDefinition;
    boolean hasFunction;
        
    entries = macroTable.get(name, contextManager);
        
    hasDefinition = false;
    hasFunction = false;
    for (Entry e : entries) {
      if (Macro.State.DEFINED == e.macro.state) {
        if (e.macro.isFunction()) {
          if (token.testFlag(NON_FUNCTION)) {
            // Do not count the function definitions if this is not a
            // valid function-like invocation.
          } else {
            hasDefinition = true;
            hasFunction = true;
          }
        } else {
          hasDefinition = true;
        }
      }
          
    }

    if (! hasDefinition) {
      // Macro has no definition in this presence condition.

      return token;
    }

    // Expand the macro to it's definition and push a new token
    // context for the preprocessor to pull tokens from.  This
    // is to preprocess the macro expansion.

    TokenBuffer macroContext;


    if (hasFunction) {

      // Function-like macro.  At least one definition of the
      // macro is function-like.  First we hoist the
      // function-like macro invocation around conditionals that
      // interfere with the syntax.  Then we expand each hoisted
      // invocation.

      if (! token.testFlag(HOISTED_FUNCTION)) {

        // Hoist the function before invoking it.

        return hoistFunction(token);

      } else {

        // Invoke the function.

        return funlikeInvocation(token, entries);
      }

    } else {

      // Object-like macro expansion.

      // Check whether we need to output the definition in a
      // conditional.  We don't need to when the definition's presence
      // condition is the same as the current one.

      boolean needConditional;
            
      needConditional = true;
      if (entries.size() == 1) {
        Context context;
        Context and;
              
        context = contextManager.reference();
        and = context.and(entries.get(0).context);
        context.delRef();
        
        needConditional = ! contextManager.is(and);
        and.delRef();
      }


      if (preprocessorStatistics) {
        int nused = 0;

        for (Entry e : entries) {
          if (Macro.State.DEFINED == e.macro.state) {
            nused++;
          }
        }

        System.err.format("object %s %s %d %d %d\n",
                          name, getNestedLocation(),
                          getMacroNestingDepth(),
                          macroTable.countDefinitions(name),
                          nused);
      }


      // Expand the object-like macro.
            
      if (entries.size() == 1
          && contextManager.is(entries.get(0).context)) {

        // Don't bother outputting conditionals when there is only one
        // possible macro definition in the current presence
        // condition.

        Directive linemarker = createMacroLineMarker(name);

        if (token.testFlag(PREV_WHITE)) {
          // Preserve the PREV_WHITE flag.
          
          linemarker.setFlag(PREV_WHITE);
        }

        pushTokenBuffer(new SingleExpansionBuffer(name, entries.get(0)
                                                  .macro.definition));
        macroTable.free(entries);

        return linemarker;
              
      } else {

        // Expand all possible macro definitions in the current
        // presence condition.

        List<Syntax> original;
        List<List<Syntax>> lists;
        List<Context> contexts;
              
        original = new LinkedList<Syntax>();
        original.add(token);

        lists = new LinkedList<List<Syntax>>();
        contexts = new LinkedList<Context>();
        for (Entry e : entries) {
          contexts.add(e.context);
                
          switch (e.macro.state) {
          case DEFINED:
            lists.add(e.macro.definition);
            break;
          case UNDEFINED:
          case FREE:
            List<Syntax> replacement;
                    
            replacement = new LinkedList<Syntax>();
            replacement.add(token);
            lists.add(replacement);
            break;
          }
        }
              
        pushTokenBuffer(new MultipleExpansionBuffer(name,lists,
                                                    contexts));

        return createMacroLineMarker(name);
      }
    }
  }

  /**
   * This class recognizes a function-like macro invocation in a given
   * configuration and reports when the invocation needs to fork.
   */
  private static class Invocation {
    Context startContext;

    /**
     * The state of the parser.
     * -3 means it hasn't start parsing the argument list
     * -2 means invalid invocation (no starting paren)
     * -1 means finished parsing the invocation (seen last paren)
     * 0 means it has read the right paren of the arg list,
     *   but there are no nested parens
     * >0 is the nesting depth of parentheses
     */
    int state;
    
    public Invocation(Context startContext) {
      this.startContext = startContext;
      this.state = -3;
    }
    
    public void parse(ConditionalSyntax csyntax) {
      Syntax syntax;
      Context context;
      Context and;
      
      if (done()) return;
      
      syntax = csyntax.syntax;
      context = csyntax.context;
      
      /*if (syntax.kind() == Kind.DIRECTIVE &&
        (syntax.toDirective().tag() == DirectiveTag.WARNING
        || syntax.toDirective().tag() == DirectiveTag.INCLUDE)) {
        state = -2;
        return;
        }*/
      
      if (syntax.kind() != Kind.LANGUAGE) {
        if (-3 == state
            && ((Syntax) syntax).kind() == Kind.CONDITIONAL
            && ( syntax.toConditional().tag() == ConditionalTag.START
                 || syntax.toConditional().tag() == ConditionalTag.NEXT)) {
          // The GCC preprocessor dictates that an open parenthesis
          // must follow the function-like macro name, only separated
          // by space.  See macros/function_if_weird_paren3.c.
          state = -2;
        }

        return;
      }
      
      and = startContext.and(context);
      
      if (and.isFalse()) {
        and.delRef();
        return;
      }
      else {
        and.delRef();
      }
      
      if (-3 == state) {
        // Haven't started parsing the parameter list.

        if (syntax.kind() == Kind.LANGUAGE) {
          if (syntax.kind() == Kind.LANGUAGE &&
              syntax.toLanguage().tag().ppTag()
              == PreprocessorTag.OPEN_PAREN) {
            // Start parsing the argument list.
            state = 0;
            
          } else {
            // The first token was not an open parenthesis, so it's
            // not a valid function-like macro invocation.  (It may
            // still be an object-like invocation though.)
            state = -2;
          }
        }

      } else {
        // We are in the middle of parsing an argument list.

        // Track the nesting depth of parentheses.
        if (syntax.kind() == Kind.LANGUAGE) {
          switch (syntax.toLanguage().tag().ppTag()) {
          case OPEN_PAREN:
            state++;
            break;
          case CLOSE_PAREN:
            state--;
            break;
          }
        }
      }
    }
    
    /**
     * Done when we have finished parsing or seen an invalid
     * function-like macro invocation invocation.
     */
    public boolean done() {
      return -1 == state || -2 == state;
    }
    
    public boolean invalid() {
      return -2 == state;
    }
    
    public boolean started() {
      return state > -2;
    }
    
    /**
     * Indicates that we need to hoist conditionals around the
     * function-like macro invocation.  Hoisting is necessary when a
     * parenthesis or comma is inside a condition.  Specifically,
     * there are three situations when hoisting is necessary: (1) the
     * starting paren has a different presence condition; (2) a comma
     * in state 0 has a different presence condition; and (3) the
     * ending paren has a different presence condition.
     *
     * @param csyntax The (possibly compound) token and it's presence
     * condition.
     * @return true when the preprocessor needs to hoist conditionals
     * around the function-like macro invocation.
     */
    public boolean needFork(ConditionalSyntax csyntax) {
      Syntax syntax;
      Context context;
      Context and;
      
      //need to fork in the following cases
      
      syntax = csyntax.syntax;
      context = csyntax.context;
      
      if (syntax.kind() == Kind.CONDITIONAL) return false;
      
      if (syntax.kind() != Kind.LANGUAGE) return false;

      if (startContext.is(context)) return false;

      and = startContext.and(context);
      
      if (and.isFalse()) {
        and.delRef();
        return false;
      }
      else {
        and.delRef();
      }
      
      if (null != syntax) {
        if (syntax.kind() == Kind.LANGUAGE) {
          switch (syntax.toLanguage().tag().ppTag()) {
          case OPEN_PAREN:
          case COMMA:
            return 0 == state;
          case CLOSE_PAREN:
            return -1 == state;
          }
        }
      }
      
      return false;
    }
  }
  
  /** An object containing a token and it's presence condition. */
  private static class ConditionalSyntax {
    public final Syntax syntax;
    public final Context context;
    
    public ConditionalSyntax(Syntax syntax, Context context) {
      this.syntax = syntax;
      this.context = context;
    }

    public String toString() {
      return syntax.toString();
    }
  }
  
  /**
   * Parse a function-like macro invocation and hoist conditionals
   * around it if necessary.  This function pulls the tokens of the
   * invocation directly from the preprocessor's token-stream via the
   * scan() function.
   *
   * @param token The token containing the macro name.
   */
  private Syntax hoistFunction(Language<?> token) throws IOException {
    Syntax syntax;
    Language<?> flagged;
    Language<?> nonfunction;
    List<Invocation> invocations;
    List<ConditionalSyntax> buffer;
    Context union;
    List<Syntax> hoisted;
    ContextManager savedManager;
    boolean needConditional;
    Syntax eofOrInclude;
    
    invocations = new LinkedList<Invocation>();
    invocations.add(new Invocation(contextManager.reference()));

    buffer = new LinkedList<ConditionalSyntax>();

    // Copy the contextManager.  This is necessary to back out of the
    // changes in presence condition due to conditionals inside of the
    // function-like macro invocation.
    savedManager = contextManager;
    contextManager = new ContextManager(contextManager);
    
    // Turn of macro expansion.  This is done because the preprocessor
    // needs to first read and parse the raw, unexpanded tokens of the
    // function-like macro invocation.
    expansionOff();
    isHoistingFunction = true;

    eofOrInclude = null;
    for (;;) {
      // This is the main loop that parsing the function-like macro
      // invocation and checks for conditionals.
      ConditionalSyntax csyntax;
      Context context;
      boolean done;

      syntax = scan();

      if (preprocessorStatistics) {
        // TODO collect prescan directives.
      }

      context = contextManager.reference();

      // Only certain tokens are allowed in function-like macro
      // invocations.  The following are allowed: regular tokens,
      // layout, defines, undefines, errors, warnings, and
      // conditionals.  The following tests reflects this, except for
      // conditionals.  The main preprocessor "scan()" method updates
      // the presence condition given conditionals as usual, even
      // though expansion is turned off with expansionOff().
      if (syntax.kind() == Kind.LANGUAGE || syntax.kind() == Kind.LAYOUT
          || (syntax.kind() == Kind.DIRECTIVE
              && (syntax.toDirective().tag() == DirectiveTag.DEFINE
                  || syntax.toDirective().tag() == DirectiveTag.UNDEF
                  || syntax.toDirective().tag() == DirectiveTag.INCLUDE
                  || syntax.toDirective().tag() == DirectiveTag.INCLUDE_NEXT
                  || syntax.toDirective().tag() == DirectiveTag.ERROR
                  || syntax.toDirective().tag() == DirectiveTag.WARNING))) {

        // Parse the regular tokens of the function-like macro
        // invocation.

        if (! context.isFalse()) {
          csyntax = new ConditionalSyntax(syntax, context);
          
          buffer.add(csyntax);
          
          for (Invocation inv : invocations) {
            inv.parse(csyntax);
          }
          
          for (int i = 0; i < invocations.size(); i++) {
            Invocation inv;
            
            inv = invocations.get(i);
            
            if (inv.needFork(csyntax)) {
              List<Invocation> forked;
              
              forked = forkInvocation(inv, csyntax.context, buffer);
              invocations.addAll(i + 1, forked);
              i = i + forked.size();
            }
          }

        } else {
          context.delRef();
        }

      } else {
        // Pass through any tokens that aren't regular language
        // tokens.
        buffer.add(new ConditionalSyntax(syntax, null));
      }
      
      done = true;
      for (Invocation inv : invocations) {
        if (! inv.done()) {
          done = false;
          break;
        }
      }
      
      if (done) {
        break;
      }
      
      if (syntax.kind() == Kind.EOF || syntax.testFlag(EOE)
          /*|| syntax.kind() == Kind.DIRECTIVE
            && syntax.toDirective().kind() == Directive.Kind.INCLUDE*/) {
        // Include directives are not permitted in function-like macro
        // invocations.  Stop parsing once we see one, an EOF, or an
        // end-of-expansion.  All signal an incomplete invocation.
        // The token is saved so it can be processed by the
        // preprocessor. (Right now expansion is off.)
        eofOrInclude = syntax;
        break;
      }
    }

    isHoistingFunction = false;
    expansionOn();
    
    // TODO optimization: detect invalid function invocations
    // (e.g. due to invalid number of arguments) and remove them from
    // the list of invocations so that the preprocessor doesn't have
    // to bother processing them and throwing an error.
    
    hoisted = new LinkedList<Syntax>();

    flagged = (Language<?>) token.copy();
    flagged.setFlag(HOISTED_FUNCTION);

    nonfunction = (Language<?>) token.copy();
    nonfunction.setFlag(NON_FUNCTION);
    
    if (invocations.size() == 1
        && savedManager.is(invocations.get(0).startContext)) {
      // If there was only one invocation, no need to hoist
      // conditionals.
      needConditional = false;

    } else {
      // Need to hoist conditionals around the invocation.
      needConditional = true;
    }

    for (int i = 0; i < buffer.size(); i++) {
      ConditionalSyntax cs = buffer.get(i);

      if (cs.syntax.kind() == Kind.CONDITIONAL) {
        //hoisted.add(cs.syntax);
        //buffer.remove(i);
        //i--;
      } else if (cs.syntax.kind() == Kind.DIRECTIVE &&
                 (cs.syntax.toDirective().tag() == DirectiveTag.IF
                  || cs.syntax.toDirective().tag() == DirectiveTag.ELIF
                  || cs.syntax.toDirective().tag() == DirectiveTag.ELSE
                  || cs.syntax.toDirective().tag() == DirectiveTag.ENDIF)) {
        //hoisted.add(cs.syntax);
        //buffer.remove(i);
        //i--;
      }
    }

    // Hoist conditionals around the function-like macro invocations.
    for (Invocation inv : invocations) {
      Context last;
      List<Syntax> invTokens = new LinkedList<Syntax>();
      boolean hasInclude = false;
      boolean seenLParen = false;
      boolean brokenLParen = false;
      DirectiveTag brokenByKind = null;

      if (needConditional) {
        if (invocations.get(0) == inv) {
          hoisted.add(new Conditional(ConditionalTag.START, inv.startContext));
          
        } else {
          hoisted.add(new Conditional(ConditionalTag.NEXT, inv.startContext));
        }
      }

      last = null;
      for (ConditionalSyntax cs : buffer) {
        Syntax s;
        Context c;
        
        s = cs.syntax;
        c = cs.context;
        
        if (s.kind() == Kind.LANGUAGE || s.kind() == Kind.LAYOUT
            || (s.kind() == Kind.DIRECTIVE
                && (s.toDirective().tag() == DirectiveTag.DEFINE
                    || s.toDirective().tag() == DirectiveTag.UNDEF
                    || s.toDirective().tag() == DirectiveTag.INCLUDE
                    || s.toDirective().tag() == DirectiveTag.INCLUDE_NEXT
                    || s.toDirective().tag() == DirectiveTag.ERROR
                    || s.toDirective().tag() == DirectiveTag.WARNING))
            ) {
          Context and;

          if (s.kind() == Kind.LANGUAGE &&
              s.toLanguage().tag().ppTag() == PreprocessorTag.OPEN_PAREN) {
            seenLParen = true;
          }

          if (s.kind() == Kind.DIRECTIVE &&
              (s.toDirective().tag() == DirectiveTag.DEFINE
               || s.toDirective().tag() == DirectiveTag.INCLUDE)
              && ! seenLParen) {
            brokenLParen = true;
            brokenByKind = s.toDirective().tag();

          } else if (s.kind() == Kind.DIRECTIVE &&
                     s.toDirective().tag() == DirectiveTag.INCLUDE) {
            hasInclude = true;
          }

          and = inv.startContext.and(c);
          if (! and.isFalse()) {
            if (null != last && ! last.is(c)) {
              invTokens.add(new Conditional(ConditionalTag.END, null));
              last = null;
            }
            
            if (! inv.startContext.is(and)) {
              if (null == last) {
                invTokens.add(new Conditional(ConditionalTag.START, c));
                last = c;
              }
            }
            invTokens.add(s);
          }
          and.delRef();
        }
      }

      /*if (-3 == inv.state) {

        // This function-like macro did not have an open parenthesis,
        // so it isn't a valid invocation.

        hoisted.add(nonfunction);
        hoisted.addAll(invTokens);

        } else*/ if (! brokenLParen && (! hasInclude || isAppleGCC())) {

        // Function-like macros with include directives in them are
        // not allowed.  The GCC preprocessor actually removes the
        // tokens from the entire invocation.  However, the Apple
        // build of gcc allows the directive to go through.  See
        // macros/function_if_weird_paren4.

        hoisted.add(flagged);
        hoisted.addAll(invTokens);
        
      } else {

        // Function-like macros with a define (or possibly any other
        // directive) before the left parenthesis breaks the
        // invocation.  The GCC Preprocessor outputs only the
        // function-like macro name, but not the argument list.  See
        // macros/function_parenthesis.c for an example.

        //TODO This behaves like funlikeInvocation when it sees a
        //directive before the parenthesis.  Should these be combined
        //in one function?

        if (brokenByKind == DirectiveTag.DEFINE) {
          hoisted.add(nonfunction);
          
        } else if (brokenByKind == DirectiveTag.INCLUDE) {
          hoisted.add(nonfunction);
          hoisted.addAll(invTokens);
        }
      }

      if (null != last) {
        hoisted.add(new Conditional(ConditionalTag.END, null));
      }
    }
    
    if (needConditional && invocations.size() > 0) {
      hoisted.add(new Conditional(ConditionalTag.END, null));
    }


    // Save the tokens that were read in, but weren't part of a
    // function-like macro invocation.

    union = contextManager.new Context(false);
    for (Invocation inv : invocations) {
      Context tmp;
      
      tmp = union.or(inv.startContext);
      union.delRef();
      union = tmp;
    }

    Context current;
    
    current = null;
    for (ConditionalSyntax cs : buffer) {
      Syntax s;
      Context c;
      
      s = cs.syntax;
      c = cs.context;
      
      if (s.kind() == Kind.LANGUAGE || s.kind() == Kind.LAYOUT
          || (s.kind() == Kind.DIRECTIVE
              && (s.toDirective().tag() == DirectiveTag.DEFINE
                  || s.toDirective().tag() == DirectiveTag.UNDEF
                  || s.toDirective().tag() == DirectiveTag.INCLUDE
                  || s.toDirective().tag() == DirectiveTag.INCLUDE_NEXT
                  || s.toDirective().tag() == DirectiveTag.ERROR
                  || s.toDirective().tag() == DirectiveTag.WARNING))) {
        Context tmp;
        
        tmp = c.andNot(union);
        
        if (! tmp.isFalse()) {
          if (null == current) {
            hoisted.add(new Conditional(ConditionalTag.START, tmp));
            current = tmp;
            
          } else if (! current.is(tmp)) {
            hoisted.add(new Conditional(ConditionalTag.END, null));
            hoisted.add(new Conditional(ConditionalTag.START, tmp));
            current = tmp;
          }
          
          hoisted.add(s);
          
        } else {
          tmp.delRef();
        }
        
        c.delRef();
        
      } else {
        if (null != current) {
          hoisted.add(new Conditional(ConditionalTag.END, null));
          current = null;
        }
        hoisted.add(s);
      }
    }

    union.delRef();
    
    if (null != current) {
      hoisted.add(new Conditional(ConditionalTag.END, null));
    }
    
    if (null != eofOrInclude) {
      hoisted.add(eofOrInclude);
    }


    // Restore the contextManager.

    contextManager.free();
    contextManager = savedManager;


    //assert checkMatchingConditionals(hoisted);

    pushTokenBuffer(new PlainTokenBuffer(hoisted));

    if (preprocessorStatistics) {
      int nhoisted;
      
      nhoisted = 0;

      for (Invocation inv : invocations) {
        if (inv.done()) {
          nhoisted++;
        }
      }

      System.err.format("hoist_function %s %s %d %d\n",
                        token.getTokenText(),
                        getNestedLocation(),
                        getMacroNestingDepth(),
                        nhoisted);
    }

    return EMPTY;
  }

  /**
   * Check that conditionals match in a list of tokens.  This only
   * checks whether START and END condition tokens are present; NEXT
   * is ignored.
   *
   * @param list The list of tokens to check.
   * @return true If the conditionals match.
   */
  private boolean checkMatchingConditionals(List<Syntax> list) {
    int nesting = 0;

    for (Syntax s : list) {
      if (s.kind() == Kind.CONDITIONAL
          && s.toConditional().tag() == ConditionalTag.START) {
        nesting++;

      } else if (s.kind() == Kind.CONDITIONAL
                 && s.toConditional().tag() == ConditionalTag.END) {
        nesting--;
      }
    }

    if (0 == nesting) {
      return true;

    } else {
      return false;
    }
  }

  /**
   * Fork a function-like macro invocation.
   *
   * @param inv the current invocation.
   * @param context The presence condition of the invocation.
   * @param list The tokens of the invocations so far.
   * @return the resulting forked invocations.
   */
  private static List<Invocation> forkInvocation(Invocation inv,
                                                 Context context,
                                                 List<ConditionalSyntax>
                                                 list) {
    Context new1, new2;
    List<Invocation> invocations;
    
    new1 = inv.startContext.and(context);
    new2 = inv.startContext.andNot(context);
    
    inv.startContext.delRef();
    inv.startContext = new1;
    
    invocations = new LinkedList<Invocation>();
    
    if (! new2.isFalse()) {
      Invocation newinv;
      
      newinv = new Invocation(new2);
      invocations.add(newinv);
      
      for (ConditionalSyntax s : list) {
        if (newinv.done()) break;
        
        newinv.parse(s);
        
        if (newinv.needFork(s)) {
          List<Invocation> forked;
          
          forked = forkInvocation(newinv, s.context, list);
          invocations.addAll(forked);
        }
      }
    }
    else {
      new2.delRef();
    }
    
    return invocations;
  }
  
  /**
   * Expand a function-like macro invocation.  This method assumes
   * that any conditionals that break the invocation have already been
   * expanded by hoistFunction.  This parses the parameters, expands
   * them, and replaces the formal parameters with the actuals in the
   * macro definition.
   * 
   * This method follows the gcc preprocessor implementation.  The
   * pseudo-code for this algorithm is the following:<br>
   * <pre>
   * if (funlike)
   *  fun funlike_invocation_p
   *    check for paren
   *    fun collect_args
   *      for each argument
   *        call cpp_get_token
   *        while tracking parens and commas
   *          commas must be nesting == 0
   *          don't forget to capture variadics!
   *
   *  fun replace_args
   *    loop through tokens in func macro def
   *      if we encounter an arg
   *        stringify the arg
   *        expand the arg
   *          fun expand_arg
   *            call push_ptoken_context of the args tokens
   *            call cpp_get_token
   *              buffer resulting tokens and store for arg replacement
   *            call _cpp_pop_context
   *    now we replace the args in the func macro def, loop through
   *      replace expanded, stringified, and do pasting
   *      swallow the comma on variadic arg
   *
   * call _cpp_push_token_context of the macro's definition (w/args replaced)
   * </pre>
   *
   * @param token The macro name token.
   * @param entries The definitions of the macro from the macro table.
   * @return A line marker or an empty layout token.
   */
  private Syntax funlikeInvocation(Syntax token,
                                   List<Entry> entries)
    throws IOException {
    Syntax syntax;
    LinkedList<Syntax> buffer;
    boolean hasVariadic;
    
    buffer = new LinkedList<Syntax>();
    
    // Skip the whitespace before the open parenthesis.
    expansionOff();
    
    do {
      syntax = scan();
      
      buffer.add(syntax);
    } while (syntax.kind() == Kind.LAYOUT
             && ! (syntax.kind() == Kind.EOF || syntax.testFlag(EOE)));

    expansionOn();

    // Check whether any definitions have a variadic argument.
    hasVariadic = false;
    for (Entry e : entries) {
      if (e.macro.isFunction()) {
        if (((Function) e.macro).isVariadic()) {
          hasVariadic = true;
          break;
        }
      }
    }
    
    if (syntax.kind() == Kind.LANGUAGE &&
        syntax.toLanguage().tag().ppTag() == PreprocessorTag.OPEN_PAREN) {
      // The next token is an open parenthesis.  Start parsing the
      // arguments.
      int argc;
      LinkedList<LinkedList<Syntax>> args;
      LinkedList<LinkedList<Syntax>> rawargs = null;
      
      // Add the function name to the buffer to preserve original
      // source in the macro delimiter.
      buffer.addFirst(token);
      
      // Initialize the list of parsed arguments.
      argc = 0;
      args = new LinkedList<LinkedList<Syntax>>();
      
      if (hasVariadic) {
        // Also save arguments with their whitespace and commas, so
        // that the variadic argument can be stringified.
        rawargs = new LinkedList<LinkedList<Syntax>>();
      }
      
      // Don't expand macros while collect arguments.  Instead,
      // arguments are prescanned (expanded by the preprocessor)
      // before substituting them for their formal arguments in the
      // definition.
      expansionOff();
      
      // Collect macros arguments until we see the closing parenthesis
      // or EOF.
      do {
        int parenDepth = 0;
        
        // Initialize the next argument.
        argc++;
        args.add(null);
        if (hasVariadic) {
          rawargs.add(new LinkedList<Syntax>());
        }
        
        for (;;) {
          boolean conditionalDirective;
          
          syntax = scan();
          
          buffer.add(syntax);

          // Check for conditional directives in the invocation.
          conditionalDirective = false;
          if (syntax.kind() == Kind.DIRECTIVE) {
            switch(syntax.toDirective().tag()) {
            case IF:
            case IFDEF:
            case IFNDEF:
            case ELIF:
            case ELSE:
            case ENDIF:
              conditionalDirective = true;
              break;
            }
          }
          
          // Drop leading whitespace.
          if ( (syntax.kind() == Kind.LAYOUT /* TODO || syntax.isDelimiter() */)
               && args.getLast() == null) {
            
            if (hasVariadic && argc > 1) {
              rawargs.get(rawargs.size() - 2).add(syntax);
            }
            
            continue;
            
          } else if (conditionalDirective) {
            // This should never happen, since conditional directives
            // are hoisted around invocations.
            throw new RuntimeException("Function-like macro was not hoisted");

          } else if (syntax.kind() == Kind.LANGUAGE &&
                     syntax.toLanguage().tag().ppTag()
                     == PreprocessorTag.OPEN_PAREN) {
            // Track nesting depth of parentheses.
            parenDepth++;
            
          } else if (syntax.kind() == Kind.LANGUAGE &&
                     syntax.toLanguage().tag().ppTag()
                     == PreprocessorTag.CLOSE_PAREN) {
            // Track nesting depth of parentheses.
            if (parenDepth-- == 0) {
              break;
            }

          } else if (syntax.kind() == Kind.LANGUAGE &&
                     syntax.toLanguage().tag().ppTag()
                     == PreprocessorTag.COMMA) {
            if (0 == parenDepth) {
              // Only the commas outside of parentheses separate
              // arguments.

              if (hasVariadic) {
                rawargs.getLast().add(syntax);
              }

              // Saw one complete argument.
              break;
            }

          } else if (syntax.kind() == Kind.EOF || syntax.testFlag(EOE)) {
            // Just in case we encounter EOF while collecting
            // arguments.  This means there was no closing parenthesis
            // in the invocation.
            break;
          }
          
          // Function-like macro invocations can have regular tokens,
          // internal conditional object instances, and other
          // directives: define, undef, error, and warning.
          if (syntax.kind() == Kind.LANGUAGE || syntax.kind() == Kind.LAYOUT
              || syntax.kind() == Kind.CONDITIONAL
              || (syntax.kind() == Kind.DIRECTIVE
                  && (syntax.toDirective().tag() == DirectiveTag.DEFINE
                      || syntax.toDirective().tag() == DirectiveTag.UNDEF
                      || syntax.toDirective().tag() == DirectiveTag.ERROR
                      || syntax.toDirective().tag() == DirectiveTag.WARNING)
                  )) {
            // Arguments are made of regular tokens and conditionals.
            // Also save layout for stringifying the variadic
            // argument.

            if (args.getLast() == null) {
              args.removeLast();
              args.add(new LinkedList<Syntax>());
            }

            if (syntax.kind() == Kind.LANGUAGE && args.getLast().size() == 0
                && syntax.testFlag(PREV_WHITE)) {
              // Remove the leading whitespace caused by an argument
              // that contains a nested function-like macro expansion.
              // For example: #define _ASM_ALIGN __ASM_SEL(.balign 4,
              // .balign 8) from
              // linux-2.6.38/arch/x86/include/asm/asm.h:22.  The
              // token ".balign" should not have leading whitespace
              // when "__ASM_SEL" is expanded.

              syntax = syntax.copy();

              // TODO I changed this because it seemed like an error.
              // It needs to be tested.
              /* syntax.clearFlag(PASTE_LEFT); */
              syntax.clearFlag(PREV_WHITE);
            }
            
            // Save the token in the current argument.
            args.getLast().add(syntax);
            if (hasVariadic) {
              rawargs.getLast().add(syntax);
            }
          }
        }
        
        // Drop trailing padding.
        if (args.getLast() != null) {
          while (args.getLast().getLast().kind() == Kind.LAYOUT) {
            args.getLast().removeLast();
          }
          
          if (args.getLast().size() == 0) {
            args.removeLast();
            args.add(null);
          }
        }
        
      } while (! (syntax.kind() == Kind.LANGUAGE &&
                  syntax.toLanguage().tag().ppTag()
                  == PreprocessorTag.CLOSE_PAREN
                  || syntax.kind() == Kind.EOF || syntax.testFlag(EOE)));

      expansionOn();

      if (syntax.kind() == Kind.EOF || syntax.testFlag(EOE)) {

        if (showErrors) {
          System.err.println("error: unterminated argument list " +
                             "invoking macro " + token.getTokenText());
        }
        
        // The GNU preprocessor leaves only the macro name token and
        // not the incomplete parameter list when a function-like
        // macro invocation has an incomplete parameter list.  See
        // macros/conditional_expression_weirdness.c for an example.


        // Backup to EOF (which is now in "syntax")

        pushTokenBuffer(new PlainTokenBuffer(syntax));


        // Emit only the uninvoked function-like macro name.

        return token;

      } else {
        // We have seen the closing parenthesis of the complete
        // invocation.

        pushTokenBuffer(replaceArgs(token, args, rawargs,
                                    entries, buffer));

        if (preprocessorStatistics) {
          int nused = 0;

          for (Entry e : entries) {
            if (Macro.State.DEFINED == e.macro.state) {
              nused++;
            }
          }

          System.err.format("function %s %d %s %d %d %d\n",
                            token.getTokenText(),
                            args.size(),
                            getNestedLocation(),
                            getMacroNestingDepth(),
                            macroTable.countDefinitions(token.getTokenText()),
                            nused);
        }

        return createMacroLineMarker(token.getTokenText());
      }

      
    } else {
      // This is not a function-like macro invocation since we did not
      // see an opening parenthesis.
      
      // TODO if there any any definitions the macro that are
      // object-like, need to expand and push a context for these

      // TODO when "buffer" contains an #include, it is not processed
      // until the rest of the "buffer"'s tokens are processed.  So in
      // $CPPTEST/cpp/function_parenthesis.c, the fourth invocation,
      // the ")" is emitted after the contents of
      // $CPPTEST/cpp/function_parenthesis.h.

      // Back out of the invocation and reprocess the whitespace and
      // token we read while searching for the open parenthesis.

      // Back up the tokens we read by creating a token buffer.

      pushTokenBuffer(new PlainTokenBuffer(buffer));


      // Return the uninvoked function name.

      return token;
    }
  }
  
  /**
   * Substitute the formal parameters in a macro definition with the
   * actual parameters from the macro invocation.
   *
   * @param token The name of the function-like macro.
   * @param args The actual parameters.
   * @param rawargs The actual parameters including commas and
   * whitespace.
   * @param entries The macro definitions from the macro symbol table.
   * @param buffer The raw tokens of the macro invocation.
   */
  private TokenBuffer
    replaceArgs(Syntax token,
                LinkedList<LinkedList<Syntax>> args,
                LinkedList<LinkedList<Syntax>> rawargs,
                List<Entry> entries,
                LinkedList<Syntax> buffer)
    throws IOException {
    String name = token.getTokenText();
    List<List<Syntax>> stringified;
    List<List<Syntax>> blockArgs;
    List<List<Syntax>> expanded;
    
    expanded = null;
    blockArgs = null;
    stringified = null;
    
    // Remove tokens whose presence conditions are false under the
    // current presence condition.
    for (List<Syntax> arg : args) {
      Context c;
      
      c = contextManager.reference();
      trimInfeasible(arg, c);
      c.delRef();
    }

    // Prescan (expand) and stringify arguments for each function
    // definition.  Each definition may have a different number and
    // different names of formal arguments.  Arguments are expanded
    // and stringified only if they are actually used in a definition.
    for (Entry e : entries) {
      if (e.macro.isFunction()) {
        Function f;
        
        // Skip empty definitions.
        if (null == e.macro.definition) continue;
        
        // TODO verify number of arguments which can be different for
        // each definition.  Because of variadics, one invocation can
        // be valid for definitions with differing numbers of
        // arguments.
        
        f = (Function) e.macro;

        // Skip definitions without arguments.
        if (null == f.formals) continue;

        // Expand and stringify arguments.  Delay expanding and
        // stringifying variadics, since the number of arguments in
        // the variadic depends on the definition.
        for (int i = 0; i < f.definition.size(); i++ ) {
          Syntax t;
          int indexOfFormal;
          
          t = f.definition.get(i);
          
          if (! (t.kind() == Kind.LANGUAGE
                 && t.toLanguage().tag().hasName())) continue;
          
          indexOfFormal = f.formals.indexOf(t.getTokenText());
          
          // TODO can remove this after verifying correct number of
          // actual arguments.
          if (indexOfFormal >= args.size()) {
            indexOfFormal = -1;
          }
          
          if (indexOfFormal >= 0) {
            // The token is a formal argument for the current
            // definition.
            
            if (t.testFlag(STRINGIFY_ARG)) {
              // Stringify the argument.

              if (null == stringified) {
                stringified = new ArrayList<List<Syntax>>();
                for (int init = 0; init < args.size(); init++) {
                  stringified.add(null);
                }
              }

              if (null == stringified.get(indexOfFormal)) {
                Context global;
                
                global = contextManager.reference();
                if (null == blockArgs) {
                  blockArgs = new ArrayList<List<Syntax>>(args.size());
                  for (int init = 0; init < args.size(); init++) {
                    blockArgs.add(null);
                  }
                }
                if (null == blockArgs.get(indexOfFormal)) {
                  blockArgs.set(indexOfFormal,
                                buildBlocks(args.get(indexOfFormal), global));
                }
                stringified.set(indexOfFormal,
                                stringifyArg(blockArgs.get(indexOfFormal),
                                             global));
                global.delRef();
              }

            } else if (t.testFlag(PASTE_LEFT)
                       || (i > 0 && f.definition.get(i - 1)
                           .testFlag(PASTE_LEFT))) {
              // Operands to the token-paste operator are _not_
              // expanded.

              if (null == blockArgs) {
                blockArgs = new ArrayList<List<Syntax>>(args.size());
                for (int init = 0; init < args.size(); init++) {
                  blockArgs.add(null);
                }
              }

              if (null == blockArgs.get(indexOfFormal)) {
                Context global;
                
                global = contextManager.reference();
                blockArgs.set(indexOfFormal,
                              buildBlocks(args.get(indexOfFormal), global));

                global.delRef();
              }

            } else {
              // Expand the argument (i.e. prescan.)

              if (null == expanded) {
                expanded = new ArrayList<List<Syntax>>();
                for (int init = 0; init < args.size(); init++) {
                  expanded.add(null);
                }
              }

              if (null == expanded.get(indexOfFormal)) {
                expanded.set(indexOfFormal,
                             expandArg(args.get(indexOfFormal)));
                
                Context c;
                
                c = contextManager.reference();
                trimInfeasible(expanded.get(indexOfFormal), c);
                c.delRef();
              }
            }
          }
        } // For each token in the definition.
      }
    } // For each definition.
    
    
    List<List<Syntax>> lists
      = new LinkedList<List<Syntax>>();;
    List<Context> contexts = new LinkedList<Context>();;

    // Subsitute the arguments for each definition.  The result is a
    // list of tokens for each definition containing.
    for (Entry e : entries) {
      contexts.add(e.context);
      
      if (e.macro.isFunction()) {
        List<Syntax> replaced;
        Function f;
        boolean argsCheck;
        
        f = (Function) e.macro;
        
        // Check that the number of formal arguments matches the
        // number of actuals.  Because of variadics, the number of
        // actuals may legally be greater than the number of formals.

        argsCheck = false;

        if (null == f.formals || f.formals.size() == 0) {
          if (! f.isVariadic()) {
            argsCheck = null == args
              || args.size() == 0
              || args.size() == 1
              && (null == args.get(0) || args.get(0).size() == 0);

            // Determine whether the invocation has no
            // arguments.  It has no arguments if there is only one
            // argument and it contains no regular tokens.
            if (args.size() == 1 && null != args.get(0)
                && args.get(0).size() > 0) {
              argsCheck = true;

              for (Syntax s : args.get(0)) {
                if (s.kind() == Kind.LANGUAGE) {
                  argsCheck = false;
                  break;
                }
              }
            }
            
          } else {
            argsCheck = true;
          }

        } else {
          if (null != args && args.size() == f.formals.size()) {
            argsCheck = true;

          } else if (f.isVariadic() && args.size() >= (f.formals.size() + 1)) {
            argsCheck = true;
          }
        }

        if (! argsCheck) {
          // The number of arguments does not match the number of
          // formal arguments.

          if (showErrors) {
            System.err.println("error: macro \"" + name + "\" passed "
                               + (null == args ? "0" : args.size())
                               + " arguments, but takes just "
                               + (null == f.formals ? "0" : f.formals.size()));
          }

          // The GNU preprocessor does not expand the macro.  All it
          // does is output the original macro name without the formal
          // parameter list.
          Language<?> newToken = (Language<?>) token.copy();
          newToken.setFlag(NO_EXPAND);
          replaced = new LinkedList<Syntax>();
          replaced.add(newToken);
          
        } else if (null == f.definition) {
          // The definition was empty, so it expands to nothing.
          replaced = null;

        } else {
          // Substitute the formal arguments with the actual
          // arguments for each definition.

          LinkedList<Syntax> varArg = null;
          List<Syntax> varStr = null;
          List<Syntax> varBlock = null;
          LinkedList<Syntax> varExp = null;
          
          replaced = new LinkedList<Syntax>();
          
          //Expand and stringify the variadic argument if the current
          //definition has one.
          for (int i = 0; i < f.definition.size(); i++ ) {
            Syntax t;
            int indexOfFormal;
            
            t = f.definition.get(i);
            
            if (! (t.kind() == Kind.LANGUAGE
                   && t.toLanguage().tag().hasName())) continue;

            if (f.isVariadic() && t.getTokenText().equals(f.variadic)) {
              if (null == varArg) {
                // Construct the variadic argument out of several actual
                // arguments.

                varArg = new LinkedList<Syntax>();
                
                for (int argi = null == f.formals ? 0 : f.formals.size();
                     argi < rawargs.size(); argi++) {
                  if (null != rawargs.get(argi)) {
                    for (Syntax s : rawargs.get(argi)) {
                      varArg.add(s);
                    }
                  }
                }
                
                // Remove trailing padding.
                while (varArg.size() > 0
                       && varArg.getLast().kind() == Kind.LAYOUT) {
                  varArg.removeLast();
                }
              }
              
              if (t.testFlag(STRINGIFY_ARG)) {
                // Stringify the variadic argument.

                if (null == varStr) {
                  Context global;
                  
                  global = contextManager.reference();
                  if (null == varBlock) {
                    varBlock = buildBlocks(varArg, global);
                  }
                  varStr = stringifyArg(varBlock, global);
                  global.delRef();
                }

              } else if (t.testFlag(PASTE_LEFT)
                         || (i > 0 && f.definition.get(i - 1)
                             .testFlag(PASTE_LEFT))) {
                // Operands to token-pasting are _not_ expanded.  Save
                // the unexpanded variadic argument.

                if (null == varBlock) {
                  Context global;
                  
                  global = contextManager.reference();
                  varBlock = buildBlocks(varArg, global);
                  global.delRef();
                }

              } else {
                // Expand the variadic argument.
                
                if (null == varExp) {
                  varExp = expandArg(varArg);
                }
              }
            }
          }

          // Finally, substitute the formals with the actuals.
          for (int i = 0; i < f.definition.size(); i++) {
            Syntax t;
            boolean variadic;
            int indexOfFormal;
            
            t = f.definition.get(i);
            variadic = false;
            
            if (null != f.formals) {
              indexOfFormal = f.formals.indexOf(t.getTokenText());
            }
            else {
              indexOfFormal = -1;
            }
            
            if (f.isVariadic() && t.getTokenText().equals(f.variadic)) {
              variadic = true;
            }

            if (indexOfFormal < 0 && ! variadic) {
              if ( (i < f.definition.size() - 1)
                   && null != f.variadic
                   && f.variadic.equals(f.definition.get(i + 1).getTokenText())
                   && t.kind() == Kind.LANGUAGE
                   && t.toLanguage().tag().ppTag() == PreprocessorTag.COMMA
                   && t.testFlag(PASTE_LEFT)
                   ) {
                // The following implements a GCC preprocessor
                // feature.  When an empty variaidic argument is
                // pasted with a comma, the comma is removed.  If the
                // variadic is not empty, no pasting occurs.

                if (args.size() == f.formals.size()) {
                  // Swallow the comma (don't add it to the expanded
                  // definition.)  Then skip the variadic argument
                  // since we know it's empty.
                  i++;

                } else {
                  // Don't attempt to paste the comma with the
                  // variadic.  Even though there are no tokens that
                  // can be pasted with a comma, this avoids the error
                  // message that would be emitted.
                  Language<?> newcomma;
                  
                  // TODO check cpp/function_variadic_paste.c, need to
                  // get rid of space between "," and the empty variadic.
                  
                  newcomma = (Language<?>) t.copy();
                  newcomma.clearFlag(PASTE_LEFT);
                  replaced.add(newcomma);
                }
                
              } else {
                // It's not a formal argument, so just pass it
                // through.
                replaced.add(t);
              }

            } else {
              
              // We found a formal argument.  Substitute it with the
              // actual argument.

              List<Syntax> argArg = null;
              List<Syntax> argStr = null;
              List<Syntax> argBlock = null;
              List<Syntax> argExp = null;

              if (variadic) {
                argArg = varArg;
                argStr = varStr;
                argBlock = varBlock;
                argExp = varExp;

              } else {
                argArg = args.get(indexOfFormal);
                if (null != stringified) {
                  argStr = stringified.get(indexOfFormal);
                }
                if (null != blockArgs) {
                  argBlock = blockArgs.get(indexOfFormal);
                }
                if (null != expanded) {
                  argExp = expanded.get(indexOfFormal);
                }
              }
              
              // TODO check whether adding space for tokens with
              // PREV_WHITE is even necessary. Shouldn't stringifyArg,
              // etc, be checking for PREV_WHITE and adding space
              // instead?
              if (t.testFlag(STRINGIFY_ARG)) {
                if (t.testFlag(PREV_WHITE)
                    && argStr != null && argStr.size() > 0) {
                  replaced.add(SPACE);
                }
                replaced.addAll(argStr);

              } else if (t.testFlag(PASTE_LEFT)) {
                // Expand a macro argument that is the left operand of
                // a token-paste operation.  Only the _last_ token of
                // the actual argument gets pasted, so add the
                // PASTE_LEFT flag to it.
                List<Syntax> arg;
                Syntax last;
                
                arg = argBlock;
                if (arg.size() > 0) {
                  last = (Syntax) arg.get(arg.size() - 1);
    
                  if (null != arg) {
                    if (t.testFlag(PREV_WHITE)) {
                      replaced.add(SPACE);
                    }

                    for (Syntax a : arg) {
                      if (a != last) {
                        replaced.add(a);
                      }
                    }
                  
                    // Copy the token that will received the
                    // PASTE_LEFT token.  This is necessary since the
                    // actual argument may be substituted elsewhere,
                    // i.e. not as the left operand of a
                    // token-pasting.

                    last = (Syntax) last.copy();
                    last.setFlag(PASTE_LEFT);
                    replaced.add(last);
                  }
                }

              } else if ( i > 0 && f.definition.get(i - 1)
                          .testFlag(PASTE_LEFT)) {
                // Expand a macro argument that is the right operand
                // of a token-paste operation.  The only thing we need
                // to do special here is check for an empty argument.
                List<Syntax> arg;
                
                arg = argBlock;
                if (null != arg && arg.size() > 0) {
                  if (t.testFlag(PREV_WHITE)) {
                    replaced.add(SPACE);
                  }

                  for (Syntax a : arg) {
                    replaced.add(a);
                  }
                  
                } else {
                  // The argument is empty, so add a special token to
                  // avoid the token-paste.
                  replaced.add(AVOID_PASTE_TOKEN);
                }

              } else if (null != argExp) {
                // Substitute a formal argument with an actual.

                if (t.testFlag(PREV_WHITE) && argExp != null
                    && argExp.size() > 0) {
                  replaced.add(SPACE);
                }

                for (Syntax a : argExp) {
                  replaced.add(a);
                }

              }
            }
          }
        }
        
        lists.add(replaced);

      } else {
        // The entry is an object-like definition, free, or undefined.
        List<Syntax> replaced;

        replaced = new LinkedList<Syntax>();
        
        if (Macro.State.DEFINED == e.macro.state) {
          if (null != e.macro.definition) {
            replaced.addAll(e.macro.definition);
          }

          for (Syntax s : buffer) {
            if (buffer.getFirst() != s) {
              replaced.add(s);
            }
          }

        } else {
          replaced.addAll(buffer);
        }
        
        lists.add(replaced);
      }
    }
    
    macroTable.free(entries);
    
    // Check whether we need a conditional for the expansion.  When
    // there is only one definition and it is from the same presence
    // condition, we don't need a conditional around the expanded
    // definition(s).

    boolean needConditional;
    
    needConditional = true;
    if (lists.size() == 1) {
      Context context;
      Context and;
      
      context = contextManager.reference();
      and = context.and(contexts.get(0));
      context.delRef();

      needConditional = ! contextManager.is(and);
      and.delRef();
    }
    
    // Return the expanded macros so they can be preprocessed.

    if (needConditional) {
      return new MultipleExpansionBuffer(name, lists, contexts);
    }
    else {
      return new SingleExpansionBuffer(name, lists.get(0));
    }
  }
  
  /**
   * Trim tokens whose presence condition is infeasible under a given
   * presence condition.  The passed list of tokens is modified.
   *
   * @param list The list of tokens.
   * @param context The presence condition to test for feasibility
   * under.
   */
  private static void trimInfeasible(List<Syntax> list,
                                     Context context) {
    int nesting;
    boolean feasible;
    
    if (null == list) return;
    
    nesting = 0;
    feasible = true;
    for (int i = 0; i < list.size(); i++) {
      Syntax s;
      
      s = list.get(i);
  
      if (! feasible && nesting == 0) {
        if (s.kind() == Kind.CONDITIONAL
            && (s.toConditional().tag() == ConditionalTag.NEXT
                || s.toConditional().tag() == ConditionalTag.END)) {
          feasible = true;
          continue;
        }
      }
      
      if (! feasible) {
        if (s.kind() == Kind.CONDITIONAL
            && s.toConditional().tag() == ConditionalTag.START) {
          nesting++;
        }
        if (s.kind() == Kind.CONDITIONAL
            && s.toConditional().tag() == ConditionalTag.END) {
          nesting--;
        }
      }
      
      if (feasible) {
        // Leave the token alone.

      } else {
        // Remove infeasible tokens.
        list.remove(i);
        i--;
        continue;
      }
      
      if (feasible) {
        if (s.kind() == Kind.CONDITIONAL
            && (s.toConditional().tag() == ConditionalTag.START
                || s.toConditional().tag() == ConditionalTag.NEXT)) {
          Context and;
          
          and = context.and(s.toConditional().context);
          if (and.isFalse()) {
            feasible = false;
            nesting = 0;
          }
          and.delRef();
        }
      }
    }
  }

  /**
   * Stringify a macro argument.  Because of conditionals, there may a
   * different stringified argument by presence condition.  This
   * method hoists the conditional around stringification and returns
   * a list of string literals.
   *
   * @param arg The list of tokens of the macro argument.
   * @param global The current presence condition.  Used to trim
   * infeasible tokens
   * @return The list of string literals.
   */
  private List<Syntax> stringifyArg(List<Syntax> arg,
                                              Context global) {
    if (null == arg) {
      // An empty argument just becomes "".
      List<Syntax> list;
      
      list = new LinkedList<Syntax>();
      list.add(tokenCreator.createStringLiteral(""));

      if (preprocessorStatistics) {
        System.err.format("stringify %s %s %d\n",
                          "token", getNestedLocation(), 1);
      }
      
      return list;
      
    } else {
      boolean hoist;
      
      // Check whether we need to hoist a conditional around the
      // stringification.  Hoisting is necessary when the argument
      // contains conditionals.
      hoist = false;
      for (Syntax s : arg) {
        if (s.kind() == Kind.CONDITIONAL_BLOCK) {
          hoist = true;
          break;
        }
      }
      
      if (hoist) {
        // Hoist conditionals around stringification.
        List<StringBuilder> strings;
        List<Context> contexts;
        List<Syntax> list;
        boolean first;
        
        strings = new LinkedList<StringBuilder>();
        contexts = new LinkedList<Context>();

        strings.add(new StringBuilder());
        contexts.add(global);
        global.addRef();
        hoistStringification(arg, strings, contexts, global);
        
        list = new LinkedList<Syntax>();
        first = true;
        for (int i = 0; i < strings.size(); i++) {
          String str;
          BDD bdd;
          
          if (! contexts.get(i).isFalse()) {
            str = escapeString(strings.get(i).toString());
            
            if (first) {
              list.add(new Conditional(ConditionalTag.START, contexts.get(i)));
              first = false;
            }
            else {
              list.add(new Conditional(ConditionalTag.NEXT, contexts.get(i)));
            }
            
            list.add(tokenCreator.createStringLiteral(str));
          }
          else {
            contexts.get(i).delRef();
          }
        }
        
        if (! first) {
          list.add(new Conditional(ConditionalTag.END, null));
        }

        if (preprocessorStatistics) {
          System.err.format("stringify %s %s %d\n",
                            "conditional", getNestedLocation(),
                            strings.size());
        }

        return list;
        
      } else {
        // Stringify the argument.
        StringBuilder sb;
        String str;
        Language<?> stringified;
        List<Syntax> list;
        
        sb = new StringBuilder();
        
        for (Syntax s : arg) {
          if (s.kind() == Kind.LANGUAGE) {
            if (s.testFlag(PREV_WHITE)) {
              sb.append(' ');
            }
            sb.append(s.getTokenText());
          }
          else if (s.kind() == Kind.LAYOUT) {
            sb.append(' ');
          }
        }
        
        str = escapeString(sb.toString());
        
        stringified = tokenCreator.createStringLiteral(str);
        
        list = new LinkedList<Syntax>();
        list.add(stringified);
        
        if (preprocessorStatistics) {
          System.err.format("stringify %s %s %d\n",
                            "token", getNestedLocation(), 1);
        }

        return list;
      }
    }
  }
  
  /**
   * Take a list of tokens and conditionals and group the conditionals
   * together into a structured conditional block object.
   *
   * @param list The tokens to build the conditional blocks from.
   * @param global The current presence condition, used to trim
   * infeasible conditional branches.
   */
  private List<Syntax> buildBlocks(List<Syntax> list,
                                             Context global)
    throws IOException {

    List<Syntax> newList;
    PlainTokenBuffer tcontext;
    Syntax syntax;
    boolean hasConditional = false;
    
    newList = new LinkedList<Syntax>();
    tcontext = new PlainTokenBuffer(list);
    
    syntax = tcontext.scan();
    while (null != syntax) {
      if (syntax.kind() == Kind.CONDITIONAL) {
        newList.add(buildConditionalBlock(syntax.toConditional(), tcontext,
                                          global));
        hasConditional = true;
      }
      else {
        newList.add(syntax);
      }
      
      syntax = tcontext.scan();
    }

    if (hasConditional && newList.size() > 1) {
      // If conditional blocks were created and there are several
      // tokens in the list, wrap them in a single block.  This is for
      // arguments to stringification and token-pasting that should
      // have a single token for each operand, not a list.

      return wrapBlock(newList);
    } else {
      return newList;
    }
  }
  
  /**
   * Build one conditional block out of a list of tokens.
   *
   * @param start The starting "if" compound token of the conditional.
   * @param streamin The remaining tokens of the conditional.
   * @param global The current presence conditional used to trim
   * infeasible conditional branches.
   * @return The conditional block.
   */
  private ConditionalBlock
    buildConditionalBlock(Conditional start, Stream streamin, Context global)
    throws IOException {
    List<Syntax> branch;
    List<List<Syntax>> branches;
    List<Context> contexts;
    ConditionalBlock block;
    Syntax syntax;
    
    branches = new LinkedList<List<Syntax>>();
    contexts = new LinkedList<Context>();

    branch = new LinkedList<Syntax>();
    branches.add(branch);
    
    contexts.add(start.context);
    start.context.addRef();
    
    syntax = streamin.scan();
    
    while (null != syntax) {
      
      if (syntax.kind() == Kind.CONDITIONAL
          && syntax.toConditional().tag() == ConditionalTag.START) {
        branch.add(buildConditionalBlock(syntax.toConditional(),
                                         streamin, global));
      }
      else if (syntax.kind() == Kind.CONDITIONAL
               && syntax.toConditional().tag() == ConditionalTag.NEXT) {
        branch = new LinkedList<Syntax>();
        branches.add(branch);
        contexts.add(syntax.toConditional().context);
        syntax.toConditional().context.addRef();
      }
      else if (syntax.kind() == Kind.CONDITIONAL
               && syntax.toConditional().tag() == ConditionalTag.END) {
        break;
      }
      else {
        branch.add(syntax);
      }
      
      syntax = streamin.scan();
    }
    
    // Trim infeasible branches.
    for (int i = 0; i < branches.size(); i++) {
      List<Syntax> list;
      Context local;
      
      list = branches.get(i);

      local = global.and(contexts.get(i));
      
      if (local.isFalse()) {
        branches.remove(i);
        contexts.remove(i);
      }
      
      local.delRef();
    }
    
    // Add implicit else to the block if necessary.
    Context union, notUnion, implicitElse;
    
    union = contexts.get(0);
    union.addRef();
    
    for (int i = 1; i < contexts.size(); i++) {
      Context tmp;
      
      tmp = union.or(contexts.get(i));
      union.delRef();
      union = tmp;
    }
    
    notUnion = union.not();
    union.delRef();
    
    implicitElse = global.and(notUnion);
    
    notUnion.delRef();

    if (! implicitElse.isFalse()) {
      contexts.add(implicitElse);
      branches.add(new LinkedList<Syntax>());
    }
    else {
      implicitElse.delRef();
    }
    
    block = new ConditionalBlock(branches, contexts);
    
    // Copy the flags from the old block to the new.
    for (int i = 0; i < Syntax.MAX_FLAGS; i++) {
      if (start.testFlag(i)) {
        block.setFlag(i);
      }
    }
    
    return block;
  }

  /**
   * Wrap a list of tokens in a single conditional block.  The
   * resulting block will have one branch containing the list of
   * tokens and will have a presence condition of TRUE.  This is to
   * create a single token out of several for hoisting token-pasting
   * and stringification around conditionals.
   *
   * @param tokens The list of tokens to wrap.
   * @return A list containing the conditional block with one
   * branch.
   */
  private List<Syntax> wrapBlock(List<Syntax> tokens) {
    List<List<Syntax>> branches
      = new LinkedList<List<Syntax>>();
    List<Context> contexts = new LinkedList<Context>();

    branches.add(tokens);
    contexts.add(contextManager.new Context(true));

    LinkedList<Syntax> list = new LinkedList<Syntax>();

    list.add(new ConditionalBlock(branches, contexts));

    return list;
  }
  
  /**
   * Escape quotes in the string and add quotes.
   *
   * @param str The string to escape.
   * @return The escaped string.
   */
  private static String escapeString(String str) {
    str = str.replace("\\", "\\\\");
    str = str.replace("\"", "\\\"");
    str = "\"" + str + "\"";
    
    return str;
  }
  
  /**
   * Hoist stringification around all conditionals in a list of
   * tokens.  This completes the conditionals by taking all
   * combinations of their branches, resulting in multiplicative
   * explosion in the number of strings.
   *
   * @param list The list of tokens containing conditionals to hoist.
   * @param strings The stringified strings.
   * @param contexts The presence conditions of the hoisted
   * conditionals around each string.
   */
  private static void hoistStringification(List<Syntax> list,
                                           List<StringBuilder> strings,
                                           List<Context> contexts,
                                           Context global) {
    // TODO unit test this by having nested conditionals passed as
    // stringify args, verify optimal bdd freeing.

    for (Syntax s : list) {
      if (s.kind() == Kind.LANGUAGE) {
        for (StringBuilder sb : strings) {
          sb.append(s.getTokenText());
        }

      } else if (s.kind() == Kind.LAYOUT) {
        if (s.getTokenText().length() > 0) {
          for (StringBuilder sb : strings) {
            if (sb.charAt(sb.length() - 1) != ' ') {
              sb.append(' ');
            }
          }
        }

      } else if (s.kind() == Kind.CONDITIONAL_BLOCK) {
        ConditionalBlock block;
        List<StringBuilder> newStrings;
        List<Context> newContexts;
        
        block = (ConditionalBlock) s;
        newStrings = new LinkedList<StringBuilder>();
        newContexts = new LinkedList<Context>();
        for (int i = 0; i < block.contexts.size(); i++) {
          List<Syntax> branch;
          Context context;
          List<StringBuilder> branchStrings;
          List<Context> branchContexts;
          
          branch = block.branches.get(i);
          context = block.contexts.get(i);

          branchStrings = new LinkedList<StringBuilder>();
          branchContexts = new LinkedList<Context>();
          branchStrings.add(new StringBuilder());
          branchContexts.add(context);
          context.addRef();
          
          hoistStringification(branch, branchStrings, branchContexts, global);
          
          // Combine strings and contexts with newStrings and newContexts.
          for (int a = 0; a < strings.size(); a++) {
            for (int b = 0; b < branchStrings.size(); b++) {
              StringBuilder sb;
              Context tmp, newContext;
              
              tmp = contexts.get(a).and(branchContexts.get(b));
              newContext = global.and(tmp);
              tmp.delRef();

              if (newContext.isFalse()) {
                newContext.delRef();
              }
              else {
                sb = new StringBuilder();
                sb.append(strings.get(a));
                sb.append(branchStrings.get(b));
                newStrings.add(sb);
                newContexts.add(newContext);
              }
            }
          }
          
          for (Context c : branchContexts) {
            c.delRef();
          }
        }
        
        strings.clear();
        strings.addAll(newStrings);

        for (Context c : contexts) {
          c.delRef();
        }
        contexts.clear();
        contexts.addAll(newContexts);
      }
    }
  }
  
  /**
   * Expand an argument by pushing a token context and using
   * Preprocessor.
   *
   * @param arg The argument to expand.
   * @return The expanded argument.
   */
  private LinkedList<Syntax>
                               expandArg(LinkedList<Syntax> arg)
    throws IOException {
    PlainTokenBuffer scontext;
    LinkedList<Syntax> expanded;
    LinkedList<Syntax> argEOE;

    // Empty argument.
    if (null == arg) return null;
    
    argEOE = new LinkedList<Syntax>();
    argEOE.addAll(arg);

    // Add an end-of-expansion marker.
    Layout eoe = new Layout("");
    eoe.setFlag(EOE);
    argEOE.add(eoe);

    scontext = new PlainTokenBuffer(argEOE);
    
    pushTokenBuffer(scontext);
    
    expanded = new LinkedList<Syntax>();
    for (;;) {
      Syntax syntax;
      
      syntax = scan();

      if (syntax.testFlag(EOE)) {
        break;

      } else if (syntax.kind() == Kind.EOF) {
        if (showErrors) {
          System.err.println("real EOF in argument expansion");
        }
        break;
      }

      expanded.add(syntax);
    }

    popTokenBuffer();
    
    return expanded;
  }
  
  /**
   * Turn expansion off.  The preprocessor will not preprocess any
   * tokens in this state.
   */
  private void expansionOff() {
    expanding++;
  }

  /**
   * Turn expansion on.  The preprocessor will preprocess tokens in
   * this state.
   */
  private void expansionOn() {
    expanding--;
  }
  
  /**
   * Check if preprocessor should preprocess tokens or not.
   *
   * @return true if the preprocessor should be expanding macros.
   */
  private boolean isExpanding() {
    return expanding == 0;
  }

  //process different directives, i.e. definitions
  //update conditional context
  //expand macros in program text and in conditional expressions
  //parse function-like macros
  //complete function-like macros
  
  /**
   * Push a token context.  A token context is a list of tokens that
   * are pending preprocessing.  They are used for preprocessing macro
   * definitions after expansion.  Since macros can be nested, the
   * token contexts are stored in a stack.
   *
   * @param tcontext The token context.
   */
  private void pushTokenBuffer(TokenBuffer tcontext) {
    tcontexts.push(tcontext);

    if (tcontext.hasMacroName()) {

      // Disable the macro so it can't be recursively expanded.

      macroTable.disable(tcontext.getMacroName());
    }
  }
  
  /**
   * Pop a token context.  Returns a macro expansion delimiter.
   *
   * @return A delimiter that signifies the end of a macro expansion.
   */
  private Syntax popTokenBuffer() {
    TokenBuffer tcontext;
    
    tcontext = tcontexts.pop();
    
    if (tcontext.hasMacroName()) {
      macroTable.enable(tcontext.getMacroName());
    }

    return EMPTY;
  }

  /**
   * Find the current macro invocation nesting depth.  This is used
   * for statistics collection.  The stack of token buffers is
   * searched for macro invocations each is counted.
   *
   * @return The macro invocation nesting depth.
   */
  private int getMacroNestingDepth() {
    if (tcontexts.size() == 0) return 0;

    int count = 0;

    for (TokenBuffer t : tcontexts) {
      if (t.hasMacroName()) {
        count++;
      }
    }

    return count;
  }

  /**
   * Get a special location string that also indicates what macro, if
   * any, is currently being expanded.
   *
   * @return The location string including nested macro.
   */
  private String getNestedLocation() {
    // The current location string.

    String location = fileManager.include.getLocation().toString();


    // Find the macro that is being expanded if there is any.

    String macro = null;


    // This assumes, as the Java documentation verifies, that the top
    // of the stack is at the front of the list.  See
    // download.oracle.com/javase/6/docs/api/java/util/LinkedList.html#pop%28%29

    for (TokenBuffer t : tcontexts) {
      if (t.hasMacroName()) {
        macro = t.getMacroName();
        break;
      }
    }


    if (null == macro) {
      return location;
    } else {
      return String.format("%s:%s", location, macro);
    }
  }
  
  /**
   * A token buffer.  This buffer is used to support nested macro
   * expansion.  It is also used when the preprocessor needs to emit
   * more than one token at a time, e.g. after a failed token-pasting.
   */
  private static abstract class TokenBuffer implements Stream {
    /**
     * Whether or not this buffer holds a macro expansion.
     *
     * @return true is this buffer is a macro expansion.
     */
    public boolean hasMacroName() {
      return false;
    }

    /**
     * Return the name of the macro that this buffer holds the
     * expansion of.
     *
     * @return The name of the macro.
     * @throws UnsupportedOperationException if hasMacroName() is false.
     */
    public String getMacroName() {
      throw new UnsupportedOperationException();
    }
  }
  
  /**
   * A plain token buffer.  This buffer is used when the preprocessor
   * needs to oput more than one token at a time, e.g. after a failed
   * token-paste.
   */
  private static class PlainTokenBuffer extends TokenBuffer {

    /** An iterator over the buffer's tokens. */
    private ListIterator<Syntax> iterator;
    
    /**
     * Create a new buffer from a list of tokens.
     *
     * @param list A list of tokens.
     */
    public PlainTokenBuffer(List<Syntax> list) {
      if (null != list) {
        this.iterator = list.listIterator();
      }
      else {
        this.iterator = null;
      }
    }

    /**
     * Create a new token buffer of just two tokens. TODO optimize!
     *
     * @param A first token.
     * @param The second token.
     */
    public PlainTokenBuffer(Syntax a, Syntax b) {
      LinkedList<Syntax> list = new LinkedList<Syntax>();

      list.add(a);
      list.add(b);

      iterator = list.listIterator();
    }
                                          
    /**
     * Create a new token buffer of just one token. TODO optimize!
     *
     * @param The token.
     */
    public PlainTokenBuffer(Syntax a) {
      LinkedList<Syntax> list = new LinkedList<Syntax>();

      list.add(a);

      iterator = list.listIterator();
    }
                                          
    public Syntax scan() {
      if (null == iterator) {
        return null;
      }
      
      if (iterator.hasNext()) {
        return iterator.next();
      }
      else {
        return null;
      }
    }
    
    public boolean done() {
      return ! iterator.hasNext();
    }

    public boolean hasMacroName() {
      return false;
    }

    public String getMacroName() {
      throw new UnsupportedOperationException();
    }
  }
  
  /** A token buffer for a singly-defined macro expansion. */
  private static class SingleExpansionBuffer extends TokenBuffer {

    /** The name of the macro being expanded. */
    private String name;

    /**
     * An iterator over the list of tokens in the macro expansion.
     */
    private ListIterator<Syntax> iterator;

    /**
     * Create a token buffer for the macro expansion.
     *
     * @param name The name of the macro being expanded.
     * @param expansion The tokens of the macro expansion.
     */
    public SingleExpansionBuffer(String name,
                                 List<Syntax> expansion) {
      this.name = name;

      if (null == expansion) {
        this.iterator = null;

      } else {
        this.iterator = expansion.listIterator();
      }
    }
    
    public Syntax scan() {
      if (null == iterator) {
        return null;
      }
      
      if (iterator.hasNext()) {
        return iterator.next();

      } else {
        return null;
      }
    }
    
    public boolean done() {
      return iterator == null || ! iterator.hasNext();
    }

    public boolean hasMacroName() {
      return true;
    }

    public String getMacroName() {
      return name;
    }
  }
  
  /**
   * A token buffer for a multiply-defined macro expansion.
   */
  private static class MultipleExpansionBuffer extends TokenBuffer {
    
    /** The name of the macro being expanded. */
    private String name;

    /** The set of expansions. */
    private List<List<Syntax>> lists;
    
    /** The presence conditions of the expansions. */
    private List<Context> contexts;
    
    /** The current expansion. */
    private int list;
    
    /** The position within an expansion. */
    private int i;

    /**
     * Create a new MultipleExpansionBuffer.
     *
     * @param name The name of the macro being expanded.
     * @param lists The expansions of the macro.
     * @param contexts The presence conditions of the expansions.
     */
    public MultipleExpansionBuffer(String name,
                                   List<List<Syntax>> lists,
                                   List<Context> contexts) {
      this.name = name;
      this.lists = lists;
      this.contexts = contexts;
      this.list = -1;
      this.i = 0;
      
      for (int listi = 0; listi < lists.size(); listi++) {
        trimInfeasible(lists.get(listi), contexts.get(listi));
      }
    }

    public Syntax scan() {
      if (-1 == list) {
        list = 0;
        i = 0;
        contexts.get(list).addRef();
        
        return new Conditional(ConditionalTag.START, contexts.get(list));

      } else if (list < lists.size()) {
        if (null != lists.get(list) && i < lists.get(list).size()) {
          return lists.get(list).get(i++);

        } else {
          list++;
          i = 0;
          if (list < lists.size()) {
            contexts.get(list).addRef();
            
            return new Conditional(ConditionalTag.NEXT, contexts.get(list));

          } else {
            return new Conditional(ConditionalTag.END, null);
          }
        }

      } else {
        System.err.println("called!");
        freeContext();

        return null;
      }
    }

    public boolean done() {
      return list >= lists.size();
    }
    
    public boolean hasMacroName() {
      return true;
    }

    public String getMacroName() {
      return name;
    }

    /**
     * Free the presence condition data structures.
     */
    private void freeContext() {
      for (Context context : contexts) {
        context.delRef();
      }
    }
  }

  /**
   * Create a special linemarker the marks the beginning of a macro
   * expansion.  This is useful for debugging.
   */
  private Directive createMacroLineMarker(String name) {

    // Create the individual tokens comprising the line marker.

    LinkedList<Language<?>> list = new LinkedList<Language<?>>();
    list.add(tokenCreator.createIntegerConstant(fileManager.include
                                                .getLocation().line));
    list.getLast().setFlag(Preprocessor.PREV_WHITE);
    list.add(tokenCreator.createStringLiteral("\"" + fileManager.include
                                              .getLocation().file
                                              + ":" + name + "\""));
    list.getLast().setFlag(Preprocessor.PREV_WHITE);


    Directive linemarker = new Directive(DirectiveTag.LINEMARKER,
                                         list);
    linemarker.setLocation(fileManager.include.getLocation());

    return linemarker;
  }

  /**
   * This method determines whether we are on a system that uses the
   * Apple build of gcc, because there is (at least one) minor
   * difference in this version.
   *
   * @return true if the system uses the Apple build of gcc.
   */
  private static boolean isAppleGCC() {
    return xtc.Limits.COMPILER_VERSION.indexOf("Apple") >= 0;
  }
}