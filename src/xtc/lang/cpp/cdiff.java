/*
 * xtc - The eXTensible Compiler
 * Copyright (C) 2009-2011 New York University
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
package xtc.lang.cpp;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import xtc.lang.cpp.Syntax.Kind;
import xtc.lang.cpp.Syntax.Language;

/**
 * Token-based diff.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.20 $
 */
public class cdiff {
  /**
   * Print cdiff usage info.
   */
  public static void printUsage() {
    System.err.println(
"cdiff - Compare two C files token-by-token, ignoring directives by default." +
"\n" +
"\n" +
"USAGE:\n" +
"\n" +
"  java xtc.lang.cpp.cdiff [-l|-d] [-s] file1 file2\n" +
"\n" +
"  java xtc.lang.cpp.cdiff -?\n" +
"\n" +
"FLAGS:\n" +
"\n" +
"  -l\tTreat directives as individual tokens.\n" +
"\n" +
"  -d\tParse directives and compare them.\n" +
"\n" +
"  -s\tIgnore whitespace inside of string literals.\n" +
"\n" +
"  -?\tPrint this help message.\n" +
"");
  }

  /** The error code for when the input files are different. */
  public static final int DIFFERENT = 1;

  /** The error code for invalid arguments. */
  public static final int INVALID_ARGUMENTS = 2;

  /** The error code for invalid option flag. */
  public static final int INVALID_FLAG = 3;

  /**
   * Signal an error.  This method terminates cdiff.
   *
   * @param errcode The error code.
   */
  public static void error(int errcode) {
    switch (errcode) {
    case DIFFERENT:
      System.err.println("error: the files are different.");
      break;

    case INVALID_ARGUMENTS:
      System.err.println("error: the arguments are invalid.");
      break;

    case INVALID_FLAG:
      System.err.println("error: the flag is invalid.");
      break;

    default:
      System.err.println("error: unknown error");
      break;
    }

    System.exit(errcode);
  }

  /**
   * Determine whether two files' tokens differ.  Each file is lexed
   * into C tokens and compared token-by-token, ignoring whitespace.
   * If the files are the same, the exit code is 0.  If they differ,
   * the location in each file of the first difference is emitted and
   * the return code is 1.
   */
  public static void main(String[] args) {
    String filename1 = null;
    String filename2 = null;
    boolean pureLexer = false;  // true when directives should be
                                // compared token-by-token.
    boolean directives = false;  // true when directives should be
                                 // parsed and compared.
    boolean ignoreStringWhitespace = false;  // true when whitespace
                                             // in string literals
                                             // should be ignored.

    if (args.length == 0 || args.length == 1 && args[0].equals("-?")) {
      printUsage();

      // Don't display error message because displaying usage with
      // zero arguments is customary.  Still set the right error code
      // though to avoid falsely reporting equality.
      System.exit(INVALID_ARGUMENTS);
    }

    if (args.length != 2 && args.length != 3 && args.length != 4) {
      error(INVALID_ARGUMENTS);
    }


    // Gather command-line arguments.

    int i;

    for (i = 0; i < args.length - 2; i++) {
      String flag = args[i];

      if (flag.length() == 0 || flag.length() > 2 || flag.charAt(0) != '-') {
        error(INVALID_FLAG);

      } else if (flag.charAt(1) == 'l') {
        if (pureLexer || directives) error(INVALID_ARGUMENTS);

        pureLexer = true;

      } else if (flag.charAt(1) == 'd') {
        if (pureLexer || directives) error(INVALID_ARGUMENTS);
        
        directives = true;

      } else if (flag.charAt(1) == 's') {
        ignoreStringWhitespace = true;

      } else {
        error(INVALID_FLAG);
      }
    }

    filename1 = args[i];
    filename2 = args[i + 1];

    try {
      File file1 = new File(filename1);
      File file2 = new File(filename2);
      BufferedReader reader1 = new BufferedReader(new FileReader(file1));
      BufferedReader reader2 = new BufferedReader(new FileReader(file2));
      Stream stream1 = new CLexerStream(reader1, filename1);
      Stream stream2 = new CLexerStream(reader2, filename2);
      boolean end1 = false;
      boolean end2 = false;
      Syntax syntax1 = null;
      Syntax syntax2 = null;

      if ( ! pureLexer) {
        stream1 = new DirectiveParser(stream1, filename1);
        stream2 = new DirectiveParser(stream2, filename2);
      }
      
      while (true) {
        do {
          syntax1 = stream1.scan();
        } while (null == syntax1 || syntax1.kind() != Kind.LANGUAGE
                 && syntax1.kind() != Kind.EOF
                 && (! directives || syntax1.kind() != Kind.DIRECTIVE));
        
        do {
          syntax2 = stream2.scan();
        } while (null == syntax2 || syntax2.kind() != Kind.LANGUAGE
                 && syntax2.kind() != Kind.EOF
                 && (! directives || syntax2.kind() != Kind.DIRECTIVE));
        
        end1 = syntax1.kind() == Kind.EOF;
        end2 = syntax2.kind() == Kind.EOF;

        // FIXME generic string-literal tag.

        if (ignoreStringWhitespace
            && syntax1.kind() == Kind.LANGUAGE
            && syntax2.kind() == Kind.LANGUAGE
            && syntax1.toLanguage().tag() == CTag.STRINGliteral
            && syntax2.toLanguage().tag() == CTag.STRINGliteral
            && syntax1.getTokenText().replaceAll(" ", "")
            .equals(syntax2.getTokenText().replaceAll(" ", ""))) {
          // Don't take into account whitespace when comparing
          // strings.  This is a hack to account for spacing issues
          // in stringification.

          // FIXME.  Compare strings token-by-token instead?

        } else if (! syntax1.getTokenText().equals(syntax2.getTokenText())) {
          System.out.println(syntax1.getLocation());
          System.out.println(syntax1);
          System.out.println();
          System.out.println(syntax2.getLocation());
          System.out.println(syntax2);
          System.exit(DIFFERENT);
        }

        if (end1 || end2) break;
      }
      
      System.exit(0);

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
