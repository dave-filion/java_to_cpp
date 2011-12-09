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

import java.lang.StringBuilder;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.IdentityHashMap;

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

import xtc.tree.Node;
import xtc.tree.GNode;

import xtc.Constants;

import xtc.lang.CAnalyzer;
import xtc.lang.CPrinter;
import xtc.lang.CReader;
import xtc.lang.CParser;

import xtc.util.Tool;
import xtc.util.Pair;

import xtc.parser.Result;
import xtc.parser.ParseException;

/**
 * The SuperC configuration-preserving preprocessor and parsing.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.61 $
 */
public class SuperC extends Tool {
  /** The user defined include paths */
  List<String> I;
  
  /** Additional paths for quoted header file names */
  List<String> iquote;
  
  /** Additional paths for system headers */
  List<String> sysdirs;
  
  /** Command-line macros and includes */
  StringReader commandline;

  /** Preprocessor support for token-creation. */
  private final static TokenCreator tokenCreator = new CTokenCreator();

  /** Create a new tool. */
  public SuperC() { /* Nothing to do. */ }

  /**
   * Return the name of this object.
   * 
   * @return The name of this object.
   */
  public String getName() {
    return "SuperC";
  }


  /**
   * Return a copy of the Constants used.
   * 
   * @return The copy of the Constants.
   */
  public String getCopy() {
    return "(C) 2009-2011 Paul Gazzillo, Robert Grimm, and New York\n"
      + "University\nPortions (The C Grammar) Copyright (c) 1989, "
      + "1990 James  A.  Roskind";
  }


  public String getExplanation() {
    return
      "By default, SuperC performs all optimizations besides the " +
      "If one or more " +
      "individual optimizations are specified as command line flags, all " +
      "other optimizations are automatically disabled.";
  }

  public void init() {
    super.init();
    
    runtime.
      // Regular preprocessor arguments.
      word("I", "I", true,
           "Add a directory to the header file search path.").
      word("isystem", "isystem", true,
           "Add a system directory to the header file search path.").
      word("iquote", "iquote", true,
           "Add a quote directory to the header file search path.").
      bool("nostdinc", "nostdinc", false,
           "Don't use the standard include paths.").
      word("D", "D", true, "Define a macro.").
      word("U", "U", true, "Undefine a macro.  Occurs after all -D arguments "
           + "which is a departure from gnu cpp.").
      word("include", "include", true, "Include a header.").
      
      // Extra preprocessor arguments.
      bool("nobuiltins", "nobuiltins", false,
           "Disable gcc built-in macros.").
      bool("nocommandline", "nocommandline", false,
           "Do not process command-line defines (-D), undefines (-U), or " +
           "includes (-include).  Useful for testing the preprocessor.").
      word("mandatory", "mandatory", false,
           "Include the given header file even if nocommandline is on.").
      bool("cppmode", "cppmode", false,
           "Preprocess without preserving configurations.").
      word("TypeChef-x", "TypeChef-x", false,
           "Restricts free macros to those that have the given prefix").

      // SuperC component selection.
      bool("E", "E", false,
           "Just do configuration-preserving preprocessing.").
      bool("lexer", "lexer", false,
           "Just do lexing and print out the tokens.").
      bool("directiveParser", "directiveParser", false,
           "Just do lexing and directive parsing and print out the tokens.").
      bool("preprocessor", "preprocessor", false,
           "Preprocess but don't print.").
      bool("follow-set", "follow-set", false,
           "Compute the FOLLOW sets of each token in the preprocessed input.").

      // Preprocessor optimizations.
      /*bool("Odedup", "optimizeDedup", false,
        "Turn off macro definition deduplication.  Not recommended " +
        "except for analysis.")*/

      // FMLR algorithm optimizations.
      bool("Onone", "doNotOptimize", false,
           "Turn off all optimizations, but still use the follow-set.").
      bool("Oshared", "optimizeSharedReductions", true,
           "Turn on the \"shared reductions\" optimization.").
      bool("Olazy", "optimizeLazyForking", true,
           "Turn on the \"lazy forking\" optimization.").
      bool("Oearly", "optimizeEarlyReduce", true,
           "Turn on the \"early reduce\" optimization.").

      // Platoff ordering has no effect with the other optimizations.
      bool("platoffOrdering", "platoffOrdering", false,
           "Turn on the Platoff ordering optimization.  Off by default.").

      // Other optimizations.
      bool("noFollowCaching", "noFollowCaching", false,
           "Turn off follow-set caching.  On by default.").

      // Naive FMLR.
      bool("naiveFMLR", "naiveFMLR", false,
           "Naive FMLR Turn off all optimizations and don't "
           + "use the follow-set.").

      // Subparser explosion kill switch.
      word("killswitch", "killswitch", false,
           "Stop parsing if subparser set reaches or exceeds the given size. "
           + "This protects against subparser exponential explosion.  An "
           + "error message will be reported.").
      

      // Statistics
      bool("statistics", "allStatistics", false,
           "Collect statistics for the preprocessor and parser.").
      bool("preprocessorStatistics", "statisticsPreprocessor", false,
           "Dynamic analysis of the preprocessor.").
      bool("languageStatistics", "statisticsLanguage", false,
           "Dynamic analysis of the language usage.").
      bool("parserStatistics", "statisticsParser", false,
           "Parser statistics.").

      // Output and debugging
      bool("printAST", "printAST", false,
           "Print the parsed AST.").
      bool("printSource", "printSource", false,
           "Print the parsed AST in C source form.").
      /*bool("showCContext", "showCContext", false,
        "Show scope changes and identifier bindings.").*/
      /*bool("traceIncludes", "traceInclude", false,
        "Show every header entrance and exit.").*/
      bool("showErrors", "showErrors", false,
           "Emit preprocessing and parsing errors to standard err.").
      bool("showActions", "showActions", false,
           "Show all parsing actions.").
      bool("macroTable", "macroTable", false,
           "Show the macro symbol table.")
      ;
  }
  
  /**
   * Prepare for file processing.  Build header search paths.
   * Include command-line headers. Process command-line and built-in macros.
   */
  public void prepare() {

    // Configure optimizations options.

    boolean explicitOptimizations = runtime.hasPrefixValue("optimize");
    boolean doNotOptimize
      = runtime.hasValue("doNotOptimize")
      && runtime.test("doNotOptimize");
    boolean naiveFMLR
      = runtime.hasValue("naiveFMLR")
      && runtime.test("naiveFMLR");

    // Check optimization options.
    if (explicitOptimizations && doNotOptimize) { 
      runtime.error("no optimizations incompatible with explicitly specified " +
                    "optimizations");
    }
    
    if (naiveFMLR && (explicitOptimizations || doNotOptimize)) {
      runtime.error("naive FMLR is incompatible with all optimizations and "
                    + "with Onone because it still uses the follow.set");
    }


    // Now, fill in the defaults.
    if (explicitOptimizations || doNotOptimize || naiveFMLR) {
      runtime.initFlags("optimize", false);
    }


    // Configure statistics options.

    if (runtime.hasValue("allStatistics") && runtime.test("allStatistics")) {
      runtime.initFlags("statistics", true);
    }


    // Set the command-line argument defaults.

    runtime.initDefaultValues();



    // Use the Java implementation of JavaBDD. Setting it here means
    // the user doesn't have to set it on the commandline.

    System.setProperty("bdd", "java");
    

    // Get preprocessor settings.

    iquote = new LinkedList<String>();
    I = new LinkedList<String>();
    sysdirs = new LinkedList<String>();

    // The following shows which command-line options add to ""
    // headers and which add to <> headers.  Additionally, only
    // -isystem are considered system headers.  System headers have a
    // special marker to cpp, but SuperC does not need to use this.

    // currentheaderdirectory iquote I    isystem standardsystem
    // ""                     ""     ""   ""     ""
    //                               <>   <>     <>
    //                                    marked system headers 
    
    if (!runtime.test("nostdinc")) {
      for (int i = 0; i < Builtins.sysdirs.length; i++) {
        sysdirs.add(Builtins.sysdirs[i]);
      }
    }
    
    for (Object o : runtime.getList("isystem")) {
      if (o instanceof String) {
        String s;
        
        s = (String) o;
        if (sysdirs.indexOf(s) < 0) {
          sysdirs.add(s);
        }
      }
    }

    for (Object o : runtime.getList("I")) {
      if (o instanceof String) {
        String s;

        s = (String) o;

        // Ignore I if already a system path.
        if (sysdirs.indexOf(s) < 0) {
          I.add(s);
        }
      }
    }
    
    for (Object o : runtime.getList("iquote")) {
      if (o instanceof String) {
        String s;
        
        s = (String) o;
        // cpp permits bracket and quote search chains to have
        // duplicate dirs.
        if (iquote.indexOf(s) < 0) {
          iquote.add(s);
        }
      }
    }

    // Make one large file for command-line/builtin stuff.
    StringBuilder commandlinesb;

    commandlinesb = new StringBuilder();
    
    if (! runtime.test("nobuiltins")) {
      commandlinesb.append(Builtins.builtin);
    }
    
    if (! runtime.test("nocommandline")) {
      for (Object o : runtime.getList("D")) {
        if (o instanceof String) {
          String s, name, definition;
          
          s = (String) o;
          
          // Truncate at first newline according to gcc spec.
          if (s.indexOf("\n") >= 0) {
            s = s.substring(0, s.indexOf("\n"));
          }
          if (s.indexOf("=") >= 0) {
            name = s.substring(0, s.indexOf("="));
            definition = s.substring(s.indexOf("=") + 1);
          }
          else {
            name = s;
            // The default for command-line defined guard macros.
            definition = "1";
          }
          commandlinesb.append("#define " + name + " " + definition + "\n");
        }
      }
      
      for (Object o : runtime.getList("U")) {
        if (o instanceof String) {
          String s, name, definition;
          
          s = (String) o;
          // Truncate at first newline according to gcc spec.
          if (s.indexOf("\n") >= 0) {
            s = s.substring(0, s.indexOf("\n"));
          }
          name = s;
          commandlinesb.append("#undef " + name + "\n");
        }
      }
      
      for (Object o : runtime.getList("include")) {
        if (o instanceof String) {
          String filename;
          
          filename = (String) o;
          commandlinesb.append("#include \"" + filename + "\"\n");
        }
      }
    }
    
    if (null != runtime.getString("mandatory")
        && runtime.getString("mandatory").length() > 0) {
      commandlinesb.append("#include \"" + runtime.getString("mandatory")
                           + "\"\n");
    }
    
    if (commandlinesb.length() > 0) {
      commandline = new StringReader(commandlinesb.toString());

    } else {
      commandline = null;
    }
  }

  public Node parse(Reader in, File file) throws IOException, ParseException {
    HeaderFileManager fileManager;
    MacroTable macroTable;
    ContextManager contextManager;
    Stream preprocessor;
    Node result = null;

    if (runtime.test("lexer") || runtime.test("directiveParser")) {

      // Just do lexing and/or directive parsing, print the tokens and
      // quit if these options are selected.

      Stream stream;
      Syntax syntax;

      stream = new CLexerStream(in, file.getName());

      if (runtime.test("directiveParser")) {
        stream = new DirectiveParser(stream, file.getName());
      }

      syntax = stream.scan();

      while (syntax.kind() != Kind.EOF) {
        System.out.print(syntax.toString());
        syntax = stream.scan();
      }

      return null;
    }


    // Initialize the preprocessor with built-ins and command-line
    // macros and includes.
    
    macroTable = new MacroTable(runtime, tokenCreator);
    contextManager = new ContextManager();

    if (null != commandline) {
      Syntax syntax;
      
      try {
        commandline.reset();
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      fileManager = new HeaderFileManager(commandline,
                                          new File("<command-line>"),
                                          iquote, I, sysdirs, runtime,
                                          tokenCreator);
      preprocessor = new Preprocessor(fileManager, macroTable, contextManager,
                                      tokenCreator, runtime);
      
      do {
        syntax = preprocessor.scan();
      } while (syntax.kind() != Kind.EOF);

      commandline = null;
    }
    
    fileManager = new HeaderFileManager(in, file, iquote, I, sysdirs, runtime,
                                        tokenCreator);

    preprocessor = new Preprocessor(fileManager, macroTable, contextManager,
                                    tokenCreator, runtime);
    

    // Run SuperC.

    if (runtime.test("follow-set")) {

      // Compute the follow-set of each token of the preprocessed
      // input.
      

      // Initialize a parser to use it's follow-set method and ordered
      // syntax class.

      ForkMergeParser parser;
      parser = new ForkMergeParser(preprocessor, contextManager,
                                   new CActions(runtime), runtime);


      // Initialize the the token stream.  Only pass ordinary tokens
      // and conditionals to the follow-set.

      preprocessor = new TokenFilter(preprocessor);

      ForkMergeParser.OrderedSyntax ordered
        = parser.new OrderedSyntax(preprocessor);


      // A stack of presence conditions.  Used to store the presence
      // conditions of nested conditionals.

      LinkedList<Context> contexts = new LinkedList<Context>();

      contexts.addLast(contextManager.new Context(true));


      // Read each token from the token stream until EOF.
      
      while (true) {

        ordered = ordered.getNext();
        Syntax syntax = ordered.syntax;


        // The presence condition of the current token.  For
        // conditionals, it the presence condition of their parent
        // conditional branch (or true if they are at the top-level of
        // the source code.

        Context presenceCondition;


        // Print the token.

        System.out.print("SYNTAX " + syntax.toString().trim());


        if (syntax.kind() == Kind.CONDITIONAL) {

          // Update the presence condition.

          Conditional conditional = syntax.toConditional();

          switch (conditional.tag()) {
          case START:
            presenceCondition = contexts.getLast();
            contexts.addLast(conditional.context);
            break;

          case NEXT:
            contexts.removeLast();
            presenceCondition = contexts.getLast();
            contexts.addLast(conditional.context);
            break;

          case END:
            contexts.removeLast();
            presenceCondition = contexts.getLast();
            break;

          default:
            throw new UnsupportedOperationException();
          }
        } else {

          // Print the presence condition.

          presenceCondition = contexts.getLast();
          System.out.print(" ::: " + contexts.getLast().toString());
        }

        System.out.print("\n");


        // Print the follow set.

        if (syntax.kind() == Kind.LANGUAGE
            || syntax.kind() == Kind.EOF
            || syntax.kind() == Kind.CONDITIONAL
            && syntax.toConditional().tag() == ConditionalTag.START) {

          Map<Integer, ForkMergeParser.Lookahead> follow
            = parser.follow(ordered, presenceCondition.addRef());

          presenceCondition.delRef();

          System.out.print("FOLLOW [\n");
          for (Integer i : follow.keySet()) {
            ForkMergeParser.Lookahead l = follow.get(i);

            System.out.println("  " + l.t.syntax.toString() + " ::: " + l.c);
          }
          System.out.print("]\n\n");
        } else {
          System.out.print("\n");
        }


        if (syntax.kind() == Kind.EOF) break;
      }


    } else if (runtime.test("E") || runtime.test("preprocessor")) {

      // Run the SuperC preprocessor only.

      Syntax syntax;
      boolean seenNewline = true;
      
      syntax = preprocessor.scan();

      while (syntax.kind() != Kind.EOF) {
        if (! runtime.test("statisticsPreprocessor")
            && ! runtime.test("preprocessor")) {
          if (syntax.kind() == Kind.LANGUAGE
              || syntax.kind() == Kind.LAYOUT
              || syntax.kind() == Kind.CONDITIONAL
              || (syntax.kind() == Kind.DIRECTIVE
                  && syntax.toDirective().tag() == DirectiveTag.LINEMARKER)) {

            // Add a newline before a conditional directive or a
            // linemarker to mimic correct preprocessor directive
            // usage.
            if ((syntax.kind() == Kind.CONDITIONAL
                 || syntax.kind() == Kind.DIRECTIVE)
                && ! seenNewline) {
              System.out.print("\n");
              seenNewline = true;
            }

            if (syntax.testFlag(Preprocessor.PREV_WHITE)) {
              System.out.print(" ");
            }

            System.out.print(syntax);
            
            // Keep track of whether we have seen a newline already.
            if (syntax.kind() == Kind.LAYOUT && ((Layout) syntax).hasNewline()
                && syntax.getTokenText().endsWith("\n")) {
              seenNewline = true;

            } else if (syntax.kind() == Kind.CONDITIONAL
                       || syntax.kind() == Kind.DIRECTIVE) {
              System.out.print("\n");
              seenNewline = true;

            } else {
              seenNewline = false;
            }
          }
        }
        
        if (syntax.kind() == Kind.CONDITIONAL
            && ( syntax.toConditional().tag == ConditionalTag.START
                 || syntax.toConditional().tag == ConditionalTag.NEXT)) {
          syntax.toConditional().context.delRef();
        }

        syntax = preprocessor.scan();
      }


    } else {

      // Run the SuperC preprocessor and parser.

      ForkMergeParser parser;
      Object translationUnit;
      
      // Only pass ordinary tokens and conditionals to the parser.
      preprocessor = new TokenFilter(preprocessor);

      parser = new ForkMergeParser(preprocessor, contextManager,
                                   new CActions(runtime), runtime);

      if (runtime.test("naiveFMLR")) {
        translationUnit = parser.parseNaively();
      } else {
        translationUnit = parser.parse();
      }

      if (runtime.test("printAST")) {
        runtime.console().format((Node) translationUnit).pln().flush();
      }

      if (runtime.test("printSource")) {
        OutputStreamWriter writer = new OutputStreamWriter(System.out);

        System.err.println("Print Source");

        printSource((Node) translationUnit,
                    contextManager.new Context(true),
                    writer);

        writer.flush();
      }

      if (runtime.test("statisticsParser")) {
        // runtime.errConsole()
        //   .pln(String.format("ast_nodes %d",
        //                      astNodeCount((Node) translationUnit)));
        runtime.errConsole()
          .pln(String.format("dag_nodes %d",
                             dagNodeCount((Node) translationUnit,
                                          new IdentityHashMap<Object, Boolean>()
                                          )));
        runtime.errConsole().flush();
      }

      result = (Node) translationUnit;
    }
    

    // Print optional statistics and debugging information.

    if (runtime.test("macroTable")) {
      System.err.println("Macro Table");
      System.err.println(macroTable);
    }


    return result;
  }

  /**
   * Print an AST (or a subtree of it) in C source form.
   *
   * @param n An AST or a subtree.
   * @param presenceCondition The current nested presence condition.
   * @param writer The writer.
   * @throws IOException Because it writes to output. 
   */
  private static void printSource(Node n, Context presenceCondition,
                                  OutputStreamWriter writer)
    throws IOException {
    if (n.isToken()) {
      writer.write(n.getTokenText());
      writer.write(" ");

    } else if (n instanceof Node) {

      if (n instanceof GNode
          && ((GNode) n).hasName(ForkMergeParser.CHOICE_NODE_NAME)) {

        boolean seenIf = false;
        Context branchCondition = null;

        for (Object bo : n) {
          if (bo instanceof Context) {
            if (! seenIf) {
              writer.write("\n#if ");
              seenIf = true;
            } else {
              writer.write("\n#elif ");
            }

            branchCondition = (Context) bo;

            branchCondition.print(writer);

            writer.write("\n");
          } else if (bo instanceof Node) {
            printSource((Node) bo, branchCondition, writer);
          }
        }
        writer.write("\n#endif\n");

      } else {
        for (Object o : n) {
          printSource((Node) o, presenceCondition, writer);
        }
      }

    } else {
      throw new UnsupportedOperationException("unexpected type");
    } 
  }

  /**
   * Count the number of AST nodes.
   *
   * @param n An AST or a subtree.
   */
  private static int astNodeCount(Node n) {
    if (n.isToken()) {
      return 1;

    } else if (n instanceof Node) {
      int count = 0;

      for (Object bo : n) {
        if (bo instanceof Node) {
          count += astNodeCount((Node) bo);
        }
      }

      return count;

    } else {
      throw new UnsupportedOperationException("unexpected type");
    } 
  }
  
  /**
   * Count the number of DAG nodes.
   *
   * @param n A DAG or a subtree.
   * @param seen A hash map to store the seen nodes.
   */
  private static int dagNodeCount(Node n,
                                  IdentityHashMap<Object, Boolean> seen) {
    if (seen.containsKey(n)) return 0;

    seen.put(n, true);

    if (n.isToken()) {
      return 1;

    } else if (n instanceof Node) {
      int count = 0;

      for (Object bo : n) {
        if (bo instanceof Node) {
          count += dagNodeCount((Node) bo, seen);
        }
      }

      return count;

    } else {
      throw new UnsupportedOperationException("unexpected type");
    } 
  }
  
  /**
   * Preprocess the given CPP CST.
   * 
   * @param node The CPP CST
   * @return The preprocessed CPP CST
   */
  public Node preprocess(Node node) {
    return null;
  }

  /**
   * Run the tool with the specified command line arguments.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    new SuperC().run(args);
  }
}
