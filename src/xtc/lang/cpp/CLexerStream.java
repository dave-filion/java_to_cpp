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

import java.io.Reader;

import java.util.Iterator;

import xtc.lang.cpp.Syntax.Kind;
import xtc.lang.cpp.Syntax.LanguageTag;
import xtc.lang.cpp.Syntax.ConditionalTag;
import xtc.lang.cpp.Syntax.DirectiveTag;
import xtc.lang.cpp.Syntax.Layout;
import xtc.lang.cpp.Syntax.Language;
import xtc.lang.cpp.Syntax.Text;
import xtc.lang.cpp.Syntax.Directive;
import xtc.lang.cpp.Syntax.Conditional;

/**
 * A stream of tokens from the C lexer.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.7 $
 */
public class CLexerStream implements Stream {
  /** The lexer. */
  CLexer scanner;

  /** The last element read. */
  Syntax syntax;
    
  /** Create a new stream. */
  public CLexerStream(Reader in, String fileName) {
    this.scanner = new CLexer(in);
    this.scanner.setFileName(fileName);
    this.syntax = null;
  }
    
  public Syntax scan() throws java.io.IOException {
    syntax = scanner.yylex();
          
    return syntax;
  }
    
  public boolean done() {
    return syntax.kind() == Kind.EOF;
  }
}
