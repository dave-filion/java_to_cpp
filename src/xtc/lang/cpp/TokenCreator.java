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

import java.io.IOException;

import xtc.lang.cpp.Syntax.Language;

/**
 * Token creation methods
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.1 $
 */
interface TokenCreator {

  /**
   * Create a string literal language token.
   *
   * @param str The string.
   * @return The language token.
   */
  public Language<?> createStringLiteral(String str);

  /**
   * Create an integer constant language token.
   *
   * @param i The integer.
   * @return The language token.
   */
  public Language<?> createIntegerConstant(int i);

  /**
   * Create an identifier language token.
   *
   * @param ident The text of the identifier.
   * @return The language token.
   */
  public Language<?> createIdentifier(String ident);

  /**
   * Paste two language tokens into a single token.  Return null if
   * they cannot be pasted.
   *
   * @param t1 The first token.
   * @param t2 The second token.
   * @return The pasted token or null.
   * @throws IOException since pasting may use a lexer.
   */
  public Language<?> pasteTokens(Language<?> t1, Language<?> t2)
    throws IOException;
}