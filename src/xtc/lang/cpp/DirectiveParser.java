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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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

import xtc.tree.Location;

/**
 * Parses directives into compound tokens.
 *
 * @author Paul Gazzillo
 * @version $Revision: 1.11 $
 */
public class DirectiveParser implements Stream {
  /** The input stream of tokens. */
  Stream stream;
    
  // TODO need to handle saving the filename better.
  /** The filename. */
  String filename;

  /** We are at the beginning of a newline. */
  protected boolean newline;
   
  /** Create a new directive parser stream. */
  public DirectiveParser(Stream stream, String filename) {
    this.stream = stream;
    this.filename = filename;
    this.newline = true;
  }
    
  /**
   * This function parses preprocessor directives.  The directive must
   * occur at the beginning of the new line, which is why we must keep
   * a flag indicating whether this is so.
   *
   * The function returns either the next Yytoken
   * from the lexer or a Directive which the function has parsed.
   *
   * The directive's location is taken from the location of the hash
   * symbol.
   *
   * @return the next token or compound token.
   */
  public Syntax scan() throws java.io.IOException {
    Syntax syntax = stream.scan();
      
    // Parse the directive.
    if (newline && syntax.kind() == Kind.LANGUAGE
        && syntax.toLanguage().tag().ppTag() == PreprocessorTag.HASH) {
      List<Language<?>> list;
      String directiveName;
      DirectiveTag tag;
      boolean prevWhite = false;  // Flag for preserving whitespace.
      Location location = syntax.getLocation();
        
      list = new ArrayList<Language<?>>();
        
      // TODO if the directive does not have a newline character and
      // it's the last thing in the file, the EOF character gets
      // dropped, even though the EOF character is necessary for the
      // LR parser to finish.

      do { // Skip the whitespace after the #.
        syntax = stream.scan();

        if (syntax.kind() == Kind.LAYOUT
            && ((Layout) syntax).hasNewline()) {
          break;

        } else if (syntax.kind() == Kind.LANGUAGE) {
          break;
        }
      } while (true);

      if (syntax.kind() == Kind.LAYOUT && ((Layout) syntax).hasNewline()
          || syntax.kind() == Kind.EOF) {

        // It's an empty line marker.

        Directive empty = new Directive(DirectiveTag.LINEMARKER, list);
        empty.setLocation(location);

        return empty;
      }

      directiveName = syntax.toLanguage().getTokenText();

      if (tagMap.containsKey(directiveName)) {
        tag = tagMap.get(directiveName);

      } else {
        tag = DirectiveTag.LINEMARKER;
        list.add(syntax.toLanguage());
      }

      do { // Skip the whitespace after the directive name.
        syntax = stream.scan();

        if (syntax.kind() == Kind.LAYOUT
            && ((Layout) syntax).hasNewline()) {
          break;

        } else if (syntax.kind() == Kind.LANGUAGE) {
          break;
        }
      } while (true);

      if (syntax.kind() == Kind.LAYOUT && ((Layout) syntax).hasNewline()
          || syntax.kind() == Kind.EOF) {

        // It's an empty directive.

        Directive empty = new Directive(tag, list);
        empty.setLocation(location);

        return empty;
      }

      list.add(syntax.toLanguage());

      do { // Collect the tokens in the directive.
        syntax = stream.scan();

        if (syntax.kind() == Kind.EOF) {
          break;

        } else if (syntax.kind() == Kind.LANGUAGE) {
          if (prevWhite) {
            syntax.setFlag(Preprocessor.PREV_WHITE);
            prevWhite = false;
          }
          list.add(syntax.toLanguage());

        } else if (syntax.kind() == Kind.LAYOUT
            && ((Layout) syntax).hasNewline()) {
          break;

        } else if (syntax.kind() == Kind.LAYOUT) {
          // Set the PREV_WHITE flag when a token is preceded by
          // whitespace.  This is used to preserve spacing in expanded
          // macros.
          if (syntax.getTokenText().length() > 0) {
            prevWhite = true;
          }
        }
      } while (true);

      newline = true;

      Directive directive = new Directive(tag, list);
      directive.setLocation(location);

      return directive;

    } else {
      // Check whether there is a newline.
      newline = (syntax.kind() == Kind.LAYOUT)
        && ((Layout) syntax).hasNewline();

      return syntax;
    }
  }
    
  public boolean done() {
    return stream.done();
  }

  private static final HashMap<String, DirectiveTag> tagMap
    = new HashMap<String, DirectiveTag>();

  static {
    tagMap.put("if", DirectiveTag.IF);
    tagMap.put("ifdef", DirectiveTag.IFDEF);
    tagMap.put("ifndef", DirectiveTag.IFNDEF);
    tagMap.put("elif", DirectiveTag.ELIF);
    tagMap.put("else", DirectiveTag.ELSE);
    tagMap.put("endif", DirectiveTag.ENDIF);
    tagMap.put("include", DirectiveTag.INCLUDE);
    tagMap.put("include_next", DirectiveTag.INCLUDE_NEXT);
    tagMap.put("define", DirectiveTag.DEFINE);
    tagMap.put("undef", DirectiveTag.UNDEF);
    tagMap.put("line", DirectiveTag.LINE);
    tagMap.put("error", DirectiveTag.ERROR);
    tagMap.put("warning", DirectiveTag.WARNING);
    tagMap.put("pragma", DirectiveTag.PRAGMA);
  }
}
