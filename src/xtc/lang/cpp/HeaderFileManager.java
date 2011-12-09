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

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

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

import xtc.lang.cpp.MacroTable.Entry;
import xtc.lang.cpp.MacroTable.Macro;
import xtc.lang.cpp.MacroTable.Macro.State;

import xtc.tree.Location;

/**
 * This class manages the lexing of input files as well as finding
 * headers.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.75 $
 */
public class HeaderFileManager implements Stream {
  /** Quote include directories from the CPP tool */
  private List<String> iquote;
  
  /** I directories from the CPP tool */
  private List<String> I;
  
  /** System directories from the CPP tool */
  private List<String> sysdirs;

  /** The xtc runtime. */
  private Runtime runtime;

  /** The token creator. */
  private TokenCreator tokenCreator;

  /** The current file, main or a header */
  public Include include;
  
  /** The stack of includes */
  public LinkedList<Include> includes;
  
  /** The map of file names to their guard macros */
  private Map<String, String> guards;
  
  /** Whether the output statistics. */
  private final boolean statisticsCollection;

  /** Show errors. */
  private final boolean showErrors;

  /**
   * The names of headers that don't have guards.  This avoids having
   * to recheck a header for a guard when we know it doesn't have one
   */
  private HashSet<String> unguarded;
  
  /**
   * Create a new file manager, given the main file reader, the main
   * file object, and the header search paths.
   */
  public HeaderFileManager(Reader in, File file, List<String> iquote,
                           List<String> I, List<String> sysdirs,
                           Runtime runtime, TokenCreator tokenCreator) {
    this.iquote = iquote;
    this.I = I;
    this.sysdirs = sysdirs;
    this.runtime = runtime;
    this.tokenCreator = tokenCreator;

    this.include = new PFile(file.toString(), file, false);
    ((PFile) this.include).open(in);
    this.includes = new LinkedList<Include>();
    this.guards = new HashMap<String, String>();
    this.unguarded = new HashSet<String>();

    statisticsCollection = runtime.test("statisticsPreprocessor");
    showErrors = runtime.test("showErrors");
  }
  
  /**
   * Get the next token from the base or header file.  If we are in
   * a header a see an EOF, we suppress the EOF and instead pop the
   * header stack and resume reading the last file.
   *
   * @return the next token.
   */
  public Syntax scan() throws IOException {
    if (include.isPFile()) {
      Syntax syntax;
      PFile pfile;
      
      pfile = (PFile) include;
      
      syntax = pfile.scan();

      if (syntax.kind() == Kind.EOF && (! includes.isEmpty())) {

        if (pfile.checkGuard()) {
          if (! guards.containsKey(pfile.file.toString())) {
            guards.put(pfile.file.toString(), pfile.getGuard());
          }
        }

        pfile.close();

        // Don't let the EOF go out, since this is just the end of the
        // included file.  Instead emit a line-marker of where we left
        // off in the parent file and give EOI, end-of-include, flag.

        LinkedList<Language<?>> linemarker = new LinkedList<Language<?>>();
        linemarker.add(tokenCreator.createIntegerConstant(includes.peek()
                                                          .getLocation().line));
        linemarker.getLast().setFlag(Preprocessor.PREV_WHITE);
        linemarker.add(tokenCreator.createStringLiteral("\"" + includes .peek()
                                                        .getLocation().file
                                                        + "\""));
        linemarker.getLast().setFlag(Preprocessor.PREV_WHITE);

        Directive EOI = new Directive(DirectiveTag.LINEMARKER, linemarker);
        EOI.setFlag(Preprocessor.EOI);
        EOI.setLocation(syntax.getLocation());

        syntax = EOI;

        include = includes.pop();

      } else {
        pfile.processGuard(syntax);
      }
      
      return syntax;

    } else if (include.isComputed()) {
      Computed computed;
      Syntax syntax;
      
      computed = (Computed) include;
      
      if (! computed.done()) {
        syntax = computed.scan();

      } else {
        syntax = Preprocessor.EMPTY;

        include = includes.pop();
      }
      
      return syntax;
    }

    // Should never reach here.

    throw new RuntimeException("unchecked include file type");
  }
  
  /**
   * Determine whether we are at the end of the stream.
   *
   * @return true if at end of stream.
   */
  public boolean done() {
    if (includes.isEmpty() && include.done()) {
      return true;
    }
    else {
      return false;
    }
  }
  
  /**
   * Enter the given header file.  Use findHeader to get the qualified
   * header.  Also checks for guard macro.
   *
   * @return a start header delimiter if the header exists, otherwise
   * null
   */
  public Syntax includeHeader(String headerName, boolean sysHeader,
                               boolean includeNext,
                               ContextManager contextManager,
                               MacroTable macroTable) throws IOException {
    PFile header;

    header = findHeader(headerName, sysHeader, includeNext);
    
    if (null == header) {

      // Error messages are reported by findHeader already.

      return Preprocessor.EMPTY;

    } else {
      boolean guarded;

      guarded = openHeader(header, contextManager, macroTable);
      
      if (! guarded) {
        includes.push(include);
        include = header;
        
        // Return a line marker for the beginning of the include.
        LinkedList<Language<?>> linemarker = new LinkedList<Language<?>>();
        linemarker.add(tokenCreator.createIntegerConstant(1));
        linemarker.getLast().setFlag(Preprocessor.PREV_WHITE);
        linemarker.add(tokenCreator.createStringLiteral("\"" + headerName
                                                        + "\""));
        linemarker.getLast().setFlag(Preprocessor.PREV_WHITE);

        Directive directive = new Directive(DirectiveTag.LINEMARKER,
                                            linemarker);

        if (statisticsCollection) {
          System.err.format("include %s %s %s %s %s %s %d\n",
                            headerName,
                            includes.peek().getLocation(),
                            guarded ? "guarded" : "unguarded",
                            sysHeader ? "system" : "user",
                            includeNext ? "next" : "normal",
                            "single",
                            includes.size());
        }

        return directive;

      } else {
        return Preprocessor.EMPTY;
      }
    }
  }

  /**
   * Open the header, finding and enforcing header guards.
   *
   * @param header the header descriptor.
   * @param contextManager the presence condition manager.
   * @param macroTable the macro symbol table.
   * @return false if the header was guarded.
   */
  private boolean openHeader(PFile header, ContextManager contextManager,
    MacroTable macroTable) throws IOException {

    // Whether the macro is guarded or not.
    boolean guarded;

    // Whether we need to check the header for a guard macro.
    boolean findGuard;
    
    if (unguarded.contains(header.file.toString())) {

      // We've seen this header file before, and we know it doesn't
      // have a guard macro.

      guarded = false;
      findGuard = false;

    } else if (guards.containsKey(header.file.toString())) {

      // We've seen this header file before and it has a guard macro,
      // so check whether it's guard macro is defined.

      List<Entry> entries;
      String guard;
      
      guard = guards.get(header.file.toString());
      entries = macroTable.get(guard, contextManager);


      // Check whether the header's guard macro is defined in the
      // macro table already for the current presence condition.  If
      // there are any FREE or UNDEFINED entries for the macro, it is
      // not guarded.
      
      guarded = true;

      if (null != entries) {  // Can be null in cppmode.
        for (Entry entry : entries) {
          if (State.DEFINED != entry.macro.state) {
            guarded = false;
            break;
          }
        }
      }

      findGuard = false;

    } else {

      // We have not seen this header before.  Check for a guard
      // macro.

      guarded = false;
      findGuard = true;
    }

    if (guarded) {

      // No need to even open the header file.  It is guarded.

      return true;
    }


    // Open the header file.

    try {

      header.open();

    } catch (FileNotFoundException e) {

      // This should never happen, since findHeader is supposed to
      // have already verified the header's existence.

      throw new RuntimeException();
    }


    // Look for a guard macro for the header, unless we know it
    // doesn't have a guard macro because we've seen it before.
    
    if (findGuard) {
      Syntax syntax;

      // Finding a guard macro requires looking through the whole file
      // first.  We need to check that a single conditional wraps the
      // whole file.
      
      // Disabled the queue.  Now the header is reread instead.

      //header.queue = new LinkedList<Syntax>();

      for(;;) {
        syntax = header.stream.scan();

        //header.queue.offer(syntax);

        if (syntax.kind() == Kind.EOF) {
          break;
        }
        
        header.processGuard(syntax);
      }

      header.close();

      if (header.checkGuard() && ! macroTable.contains(header.getGuard())) {

        if (! guards.containsKey(header.file.toString())) {
          guards.put(header.file.toString(), header.getGuard());
        }

        macroTable.rectifyGuard(header.guardMacro, contextManager);

      } else {
        unguarded.add(header.file.toString());
      }

      header.open();
    }
    
    return false;
  }

  /**
   * Get a header descriptor.  If it's from an #include_next directive
   * find the next header after the current one of the same name.
   * This function builds the chain of search paths in the same way as
   * GNU cpp.
   *
   * @param headerName the absolute or relative path of the header.
   * @param sysHeader true if the header is a system header.
   * @param includeNext true if the directive was an #include_next
   * directive, a gcc extension.
   * @return a header descriptor.
   */
  private PFile findHeader(String headerName, boolean sysHeader,
                             boolean includeNext) {
    if (includeNext && includes.isEmpty()) {
      if (showErrors) {
        System.err.println("error: include_next can only be used in "
                           + "header files");
      }
      
      return null;
    } else if (headerName.charAt(0) == '/') {
      File path;
      
      path = new File(headerName);
      
      if (path.exists()) {
        // It's already an absolute path.
        return new PFile(headerName, new File(headerName), false);
      } else {
        return null;
      }
    } else {
      // Ordered list of search paths.
      List<String> chain;
      // Used with include_next to flag current header path.
      boolean foundCurrentHeader;
      
      chain = new LinkedList<String>();
      
      // User header paths.
      if (! sysHeader) {
        chain.add(include.getPath());
        
        for (String s : iquote) {
          chain.add(s);
        }
      }
      
      // -I paths.
      for (String s : I) {
        chain.add(s);
      }
      
      foundCurrentHeader = false;
      
      // Check user header paths.
      for (String s : chain) {
        File path;
        
        path = new File(s, headerName);

        if (includeNext && (! foundCurrentHeader)) {
          if (path.getParent().equals(include.getPath())) {
            // Skip current header if include_next.
            foundCurrentHeader = true;
          }
        } else if (path.exists()) {
          return new PFile(headerName, path, false);
        }
      }
      
      // Check system directories.
      for (String s : sysdirs) {
        File path;
        
        path = new File(s, headerName);
        
        if (includeNext && (! foundCurrentHeader)) {
          if (path.getParent().equals(include.getPath())) {
            foundCurrentHeader = true;
          }
        } else if (path.exists()) {
          return new PFile(headerName, path, true);
        }
      }
      
      if (showErrors) {
        System.err.println("error: header " + headerName + " not found");
      }
      
      return null;
    }
  }
  
  /**
   * Include computed header(s).  Take a list of header names and a
   * list of presence conditions.  Note that because macros can be
   * multiply-defined, a single computed include directive may expand
   * to several possible header filenames.
   *
   * @param completed the list of computed includes.
   * @param contexts the list of presence conditions in which the
   * computed includes are valid.
   * @param includeNext whether the computed include was an
   * #include_next directive.
   * @param contextManager the presence condition manager.
   * @param macroTable the macro symbol table.
   * @return the start include delimiter if the header exists, null
   * otherwise.
   */
  public Syntax includeComputedHeader(List<String> completed,
                                       List<Context> contexts,
                                       boolean includeNext,
                                       ContextManager contextManager,
                                       MacroTable macroTable) {
    includes.push(include);
    
    include = new Computed(completed, contexts, includeNext, contextManager,
                           macroTable);


    if (statisticsCollection) {
      System.err.format("computed %s %s %d %d\n",
                        includes.peek().getLocation(),
                        includeNext ? "next" : "normal",
                        includes.size(),
                        completed.size());
    }

    return Preprocessor.EMPTY;
  }
  
  /**
   * This class abstracts away both a single header and computed
   * headers that have to include several headers in different
   * context.
   */
  public abstract static class Include {
    /** Construct a new instance. */
    private Include() { /* Nothing to do. */ }
    
    /**
     * Determine whether the include is a regular one.
     *
     * @return true if it's a regular include.
     */
    public boolean isPFile() {
      return false;
    }
    
    /**
     * Determine whether the include is a computed one.
     *
     * @return true if it's a computed include.
     */
    public boolean isComputed() {
      return false;
    }
    
    /**
     * Determine whether the stream of tokens of the header is done.
     *
     * @return true when it's done.
     */
    abstract public boolean done();

    /**
     * Get the location of the last-scanned token.
     *
     * @return The location of the last-scanned token.
     */
    abstract public Location getLocation();
    
    /**
     * Get the full path of the header.
     *
     * @return the name of the header.
     */
    abstract public String getName();
    
    /**
     * Get the parent directory of the header.
     *
     * @return the parent directory of the header.
     */
    abstract public String getPath();
  }
  
  /**
   * A struct to encapsulate the data associated with a regular
   * header.
   */
  public class PFile extends Include {
    /** The name of the header file. */
    public final String name;
    
    /** The file object of the header. */
    public final File file;

    /** Whether it's a system header. */
    public final boolean system;

    /** The stream of tokens from the header. */
    private Stream stream;

    /**
     * The macro that guards the header.  null if there is no guard
     * macro.
     */
    private String guardMacro;

    /** The file reader. */
    private BufferedReader fileReader;

    /**
     * The queue is used to stored tokens when looking for a guard
     * macro.
     */
    private Queue<Syntax> queue;
    
    /**
     * A list containing the first and second syntax of the file.
     * Used for checking for a guard macro
     */
    private List<Syntax> guard;
    
    /**
     * Hang onto the previous syntax to find last syntax in the file.
     * Used for checking for a guard macro
     */
    private Syntax buffer;

    /** The location of the last scanned token. */
    private Location location = null;
    
    /** Construct a new header descriptor. */
    public PFile(String name, File file, boolean system) {
      this.name = name;
      this.file = file;
      this.system = system;
      this.guard = new LinkedList<Syntax>();
      this.buffer = null;
      this.guardMacro = null;
    }
    
    /** Open the file reader. */
    public void open() throws FileNotFoundException {

      fileReader = new BufferedReader(new FileReader(file));

      stream = new DirectiveParser(new CLexerStream(fileReader, getName()),
                                   getName());
    }
    
    /**
     * Open the file reader.
     *
     * @param in an already open reader.
     */
    public void open(Reader in) {
      stream = new DirectiveParser(new CLexerStream(in, getName()), getName());
    }
    
    /** Close the reader and streams. */
    public void close() throws IOException {
      if (null != stream) {
        fileReader.close();

        ((DirectiveParser) stream).stream = null;

        stream = null;
      }
    }
    
    /**
     * Get the stream of tokens.
     *
     * @return the stream of tokens.
     */
    public Stream stream() {
      return stream;
    }
    
    /**
     * Get the next token from the stream.
     *
     * @return the next token.
     */
    public Syntax scan() throws IOException {
      Syntax syntax;

      if (null == queue) {
        syntax = stream.scan();
      }
      else {
        syntax = queue.poll();
      }

      location = syntax.getLocation();

      return syntax;
    }
    
    /**
     * This routine collects the first, second, and last syntax of the
     * file to check for a guard macro
     *
     * @param syntax The current token.
     */
    public void processGuard(Syntax syntax) {
      if (syntax.kind() == Kind.LANGUAGE
          || syntax.kind() == Kind.DIRECTIVE) {
        //save first two syntax
        if (guard.size() == 0) {
          guard.add(syntax);
        }
        else if (guard.size() == 1) {
          guard.add(syntax);
        }

        //save last syntax
        buffer = syntax;
      }
    }
    
    /**
     * Check for guard macro.  Get the guard macro name with
     * getGuard().
     *
     * @return true if the header has a guard.
     */
    public boolean checkGuard() {
      for (;;) {
        if (guard.size() == 2 && null != buffer) {
          Directive first, second, last;
          String ifndef, define;
          int s;
          
          if (guard.get(0).kind() != Kind.DIRECTIVE
              || guard.get(1).kind() != Kind.DIRECTIVE
              || buffer.kind() != Kind.DIRECTIVE) {
            break;
          }
          
          first = guard.get(0).toDirective();
          second = guard.get(1).toDirective();
          last = buffer.toDirective();
          
          if (first.tag() != DirectiveTag.IFNDEF
              || second.tag() != DirectiveTag.DEFINE
              || last.tag() != DirectiveTag.ENDIF) {
            break;
          }
          
          s = 1;
          
          // Move past the whitespace after the directive name.
          while (s < first.size()
                 && ((Syntax) first.get(s)).kind() == Kind.LAYOUT) s++;
          
          if (s >= first.size()) {
            break;
          }

          if (! (((Syntax) first.get(s)).kind() == Kind.LANGUAGE
                 && ((Syntax) first.get(s)).toLanguage().tag().hasName())) {
            break;
          }
          
          ifndef = ((Syntax) first.get(s)).getTokenText();
          
          s = 1;
          
          // Move past the whitespace after the directive name.
          while (s < second.size()
                 && ((Syntax) second.get(s)).kind() == Kind.LAYOUT) s++;

          if (s >= second.size()) {
            break;
          }

          if (! (((Syntax) second.get(s)).kind() == Kind.LANGUAGE
                 && ((Syntax) second.get(s)).toLanguage().tag().hasName())) {
            break;
          }
          
          define = ((Syntax) second.get(s)).getTokenText();

          if (! ifndef.equals(define)) {
            break;
          }
          
          this.guardMacro = define;
          
          return true;
        }
        
        break;
      }
      
      return false;
    }
    
    /**
     * Return the guard macro name if it has one.  Must be called after
     * checkGuard.
     *
     * @return the guard macro name, null otherwise.
     */
    public String getGuard() {
      return guardMacro;
    }

    public boolean isPFile() {
      return true;
    }
    
    public boolean done() {
      return stream.done();
    }

    public Location getLocation() {
      return location;
    }
    
    public String getName() {
      return file.toString();
    }
    
    public String getPath() {
      return file.getParent();
    }
  }
  
  /**
   * The descriptor for a computed header.  Because macros can be
   * implicitly conditional, a computed header may expand to multiple
   * header file names.
   */
  private class Computed extends Include {
    /** The list of header file names. */
    private List<String> completed;

    /** The presence condition of each header file name. */
    private List<Context> contexts;

    /**
     * Whether the computed header was from an #include_next
     * directive.
     */
    private boolean includeNext;

    /** A pointer to the presence condition manager. */
    private ContextManager contextManager;
    
    /** A pointer to the macro symbol table. */
    private MacroTable macroTable;

    /** Whether we are at the end of all headers. */
    private boolean end;

    /** The current header descriptor. */
    private PFile pfile;

    /** The index of the current header file name. */
    private int i;

    /** The number of valid headers. */
    private int nvalid;
    
    /** The current stream of tokens. */
    public Stream stream;

    /** The location of the last-scanned token. */
    public Location location = null;
    
    /** Construct a new computed header. */
    public Computed(List<String> completed, List<Context> contexts,
                    boolean includeNext, ContextManager contextManager,
                    MacroTable macroTable) {
      this.completed = completed;
      this.contexts = contexts;
      this.includeNext = includeNext;
      this.contextManager = contextManager;
      this.macroTable = macroTable;
      this.end = false;
      this.pfile = null;
      this.stream = null;
      this.i = -1; // We increment before checking the filename.
      this.nvalid = 0;
    }
    
    /**
     * Get the next token from the stream.
     *
     * @return the next token.
     */
    public Syntax scan() throws IOException {
      if (null == pfile) {
        pfile = null;
        
        // Keep trying until we get a valid header.
        while (null == pfile && i < (completed.size() - 1)) {
          char first, last;
          String headerName = null;
          boolean sysHeader;
          String str;
          
          // Get the next header file.
          i++;
          
          str = completed.get(i);
          
          first = str.charAt(0);
          last = str.charAt(str.length() - 1);
          
          for (;;) {
            sysHeader = false;
            if ('<' == first && '>' == last) {
              sysHeader = true;
            }
            else if ('"' == first && '"' == last) {
              // A user header.
            }
            else {
              // Error.  Each header file name either needs quotes or
              // brackets.
              break;
            }
            
            headerName = str.substring(1, str.length() - 1);
            pfile = findHeader(headerName, sysHeader, includeNext);


            break;
          }
          
          if (null == pfile) {
            if (showErrors) {
              System.err.println("error: invalid header from computed "
                                 + "include, " + str);
            }
            // File does not exist or invalid header string.  Try the
            // next header file name.
            continue;

          } else {
            // Open the file or guard it.
            boolean guarded;
            
            guarded = openHeader(pfile, contextManager, macroTable);

            nvalid++;
            
            if (statisticsCollection) {
              System.err.format("include %s %s %s %s %s %s %d\n",
                                headerName,
                                includes.peek().getLocation(),
                                guarded ? "guarded" : "unguarded",
                                sysHeader ? "system" : "user",
                                includeNext ? "next" : "normal",
                                "computed",
                                includes.size());
            }

            if (guarded) {
              pfile = null;
              
              // Guarded, so move to next one.
              continue;

            } else {
              return new Conditional(ConditionalTag.START, contexts.get(i));
            }
          }
        }  // While invalid pfile.
        
        // When we get here, there are no more valid headers.
        end = true;
        
        if (statisticsCollection) {
          System.err.format("end_computed %s %d\n",
                            includes.peek().getLocation(),
                            nvalid);
        }

        return Preprocessor.EMPTY;

      } else {
        Syntax syntax;
      
        syntax = pfile.scan();
    
        if (syntax.kind() == Kind.EOF && (! includes.isEmpty())) {
          if (pfile.checkGuard()) {
            if (! guards.containsKey(pfile.file.toString())) {
              guards.put(pfile.file.toString(), pfile.getGuard());
            }
          }
          pfile.close();
          // Make it null so we can move on in the next scan.
          pfile = null;
          
          syntax = new Conditional(ConditionalTag.END, null);

        } else {
          pfile.processGuard(syntax);
        }
       
        location = syntax.getLocation();
 
        return syntax;
      }
    }
    
    public boolean isComputed() {
      return true;
    }
    
    public boolean done() {
      return end;
    }

    public Location getLocation() {
      return location;
    }
    
    public String getName() {
      if (null != pfile) {
        return pfile.getName();
      }
      else {
        return "";
      }
    }
    
    public String getPath() {
      if (null == pfile) {
        // The subheaders use the current directory in which the
        // computed include lives.
        return includes.peek().getPath();
      }
      else {
        return pfile.file.getParent();
      }
    }
  }
}
