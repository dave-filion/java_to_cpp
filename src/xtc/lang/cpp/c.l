#include "token.h"

#ifdef LEXER // Generate the lexer specification.
package xtc.lang.cpp;

import xtc.tree.Location;

import xtc.lang.cpp.Syntax.Language;
import xtc.lang.cpp.Syntax.PreprocessorTag;
import xtc.lang.cpp.Syntax.Text;
import xtc.lang.cpp.Syntax.Layout;
import xtc.lang.cpp.Syntax.EOF;

%%

%{

  /** Used to build strings of comments. */
  private StringBuilder comment = null;

  /** Used to track whether comments have newline characters. */
  private boolean commentnl = false;

  /** The name of the file being lexed. */
  private String fileName = null;

  /** Returns the name of the file being lexed. */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

%}

%eofval{
  EOF syntax = new EOF();

  syntax.setLocation(new Location(fileName, yyline+1, yycolumn));
  return syntax;
%eofval}

%class CLexer
%unicode
%type Syntax

%line
%column

%state COMMENT

/* Uncomment this for a lexer debugging.  See the JFlex manual. */
//%debug

identifier=[a-zA-Z_][0-9a-zA-Z_]*

/* Define floating point number constants. */
exponent_part=([eE]|[pP])[-+]?[0-9]+
fractional_constant=([0-9]*"."[0-9]+)|([0-9]+".")
floating_constant=(({fractional_constant}{exponent_part}?)|([0-9]+{exponent_part}))[FfLl]?

/* Define a catch-all for preprocessor numbers.  These may not
necessarily be valid C numeric constants, but they are valid
preprocessor numbers. */
preprocessing_number="."?[0-9]+([0-9a-zA-Z._]|{exponent_part})*

/* Define numeric constants. */
integer_suffix_opt=([uU]?[lL]?)|([lL][uU])|([uU]("ll"|"LL"))|(("ll"|"LL")[uU]|"LL"|"ll"|"UU"|"uu")
integer_constant=[1-9][0-9]*{integer_suffix_opt}
octal_constant="0"[0-7]*{integer_suffix_opt}
hex_constant="0"[xX][0-9a-fA-F]+{integer_suffix_opt}

/* Define escape sequences. */
simple_escape=[abfnrtv\'\"?\\]
octal_escape=[0-7]{1,3}
hex_escape="x"[0-9a-fA-F]+
escape_sequence=[\\]({simple_escape}|{octal_escape}|{hex_escape})

/* Define character and string literals.  A single character in
literals is a normal character (without newlines or quotes), an escape
sequence, or a line continuation.  The continuation is preserved to
preserve layout.  */
c_char=[^\'\\\n]|{escape_sequence}|\\\n
s_char=[^\"\\\n]|{escape_sequence}|\\\n

//TODO need to lexer \n without quote as in ~/Work/java/fonda/cpp_testsuite/cpp/stringification_strings.c


// Define whitespace characters.
h_tab=[\011]
form_feed=[\014]
v_tab=[\013]
c_return=[\015]
horizontal_white=[ ]|{h_tab}
newline=\r|\n|\r\n
//comment=[/][*]([^*/]|[^*][/]|[*][^/])*[*][/]
linecomment="//"[^\r\n]*{newline}
continuation="\\\n"
whitespace=({horizontal_white})+|({v_tab}|{c_return}|{form_feed})+|{continuation}

//whitespace=({horizontal_white})+|({v_tab}|{c_return}|{form_feed})+|{continuation}|{comment}
//whitespace=({horizontal_white})+|({v_tab}|{c_return}|{form_feed})+|({horizontal_white}|{v_tab}|{c_return}|{form_feed})*"\n"|{continuation}|{comment}

%%

<YYINITIAL> {

// yyline + 1, yychar, yychar + yylength()

{whitespace}+
{ return new Layout(yytext(), false); }

{whitespace}*({newline}|{linecomment})+{whitespace}*
{ return new Layout(yytext(), true); }


#elif defined TAG // Generate the CTag enumeration.
package xtc.lang.cpp;

import xtc.lang.cpp.Syntax.Layout;
import xtc.lang.cpp.Syntax.PreprocessorTag;
import xtc.lang.cpp.Syntax.Language;

public enum CTag implements xtc.lang.cpp.Syntax.LanguageTag {
#endif

LANGUAGE(AUTO,"auto")
LANGUAGE(BREAK,"break")
LANGUAGE(CASE,"case")
LANGUAGE(CHAR,"char")
LANGUAGE(CONST,"const")
LANGUAGE(CONTINUE,"continue")
LANGUAGE(DEFAULT,"default")
LANGUAGE(DO,"do")
LANGUAGE(DOUBLE,"double")
LANGUAGE(ELSE,"else")
LANGUAGE(ENUM,"enum")
LANGUAGE(EXTERN,"extern")
LANGUAGE(FLOAT,"float")
LANGUAGE(FOR,"for")
LANGUAGE(GOTO,"goto")
LANGUAGE(IF,"if")
LANGUAGE(INT,"int")
LANGUAGE(LONG,"long")
LANGUAGE(REGISTER,"register")
LANGUAGE(RETURN,"return")
LANGUAGE(SHORT,"short")
LANGUAGE(SIGNED,"signed")
LANGUAGE(SIZEOF,"sizeof")
LANGUAGE(STATIC,"static")
LANGUAGE(STRUCT,"struct")
LANGUAGE(SWITCH,"switch")
LANGUAGE(TYPEDEF,"typedef")
LANGUAGE(UNION,"union")
LANGUAGE(UNSIGNED,"unsigned")
LANGUAGE(VOID,"void")
LANGUAGE(VOLATILE,"volatile")
LANGUAGE(WHILE,"while")

//C99
LANGUAGE(_BOOL,"_Bool")
LANGUAGE(_COMPLEX,"_Complex")
LANGUAGE(INLINE,"inline")
LANGUAGE(RESTRICT,"restrict")

//GCC 
LANGUAGE(__ALIGNOF,"__alignof")
LANGUAGE(__ALIGNOF__,"__alignof__")
LANGUAGE(ASM,"asm")
LANGUAGE(__ASM,"__asm")
LANGUAGE(__ASM__,"__asm__")
LANGUAGE(__ATTRIBUTE,"__attribute")
LANGUAGE(__ATTRIBUTE__,"__attribute__")
LANGUAGE(__BUILTIN_OFFSETOF,"__builtin_offsetof")
LANGUAGE(__BUILTIN_TYPES_COMPATIBLE_P,"__builtin_types_compatible_p")
LANGUAGE(__BUILTIN_VA_ARG,"__builtin_va_arg")
LANGUAGE(__BUILTIN_VA_LIST,"__builtin_va_list")
LANGUAGE(__COMPLEX__,"__complex__")
LANGUAGE(__CONST,"__const")
LANGUAGE(__CONST__,"__const__")
LANGUAGE(__EXTENSION__,"__extension__")
LANGUAGE(__INLINE,"__inline")
LANGUAGE(__INLINE__,"__inline__")
LANGUAGE(__LABEL__,"__label__")
LANGUAGE(__RESTRICT,"__restrict")
LANGUAGE(__RESTRICT__,"__restrict__")
LANGUAGE(__SIGNED,"__signed")
LANGUAGE(__SIGNED__,"__signed__")
LANGUAGE(__THREAD,"__thread")
LANGUAGE(TYPEOF,"typeof")
LANGUAGE(__TYPEOF,"__typeof")
LANGUAGE(__TYPEOF__,"__typeof__")
LANGUAGE(__VOLATILE,"__volatile")
LANGUAGE(__VOLATILE__,"__volatile__")

TEXT(IDENTIFIER,{identifier},true)
TEXT(INTEGERconstant,{integer_constant},false)
TEXT(OCTALconstant,{octal_constant},false)
TEXT(HEXconstant,{hex_constant},false)
TEXT(FLOATINGconstant,{floating_constant},false)
TEXT(PPNUM,{preprocessing_number},false)

/* The \' doesn't mesh with the function-like invocation.  Returns an
error that there is an unmatched ' in the parameter-list.  Circumvent
the issue by putting the offending regular expression in a macro.  */

#define CHARACTERconstant_regex "L"?\'{c_char}+\'
#define STRINGliteral_regex "L"?\"{s_char}*\"

TEXT(CHARACTERconstant,CHARACTERconstant_regex,false)
TEXT(STRINGliteral,STRINGliteral_regex,false)

LANGUAGE(ARROW,"->")
LANGUAGE(ICR,"++")
LANGUAGE(DECR,"--")
LANGUAGE(LS,"<<")
LANGUAGE(RS,">>")
LANGUAGE(LE,"<=")
LANGUAGE(GE,">=")
LANGUAGE(EQ,"==")
LANGUAGE(NE,"!=")
LANGUAGE(ANDAND,"&&")
LANGUAGE(OROR,"||")
LANGUAGE(PLUSassign,"+=")
LANGUAGE(MINUSassign,"-=")
LANGUAGE(MULTassign,"*=")
LANGUAGE(DIVassign,"/=")
LANGUAGE(MODassign,"%=")
LANGUAGE(LSassign,"<<=")
LANGUAGE(RSassign,">>=")
LANGUAGE(ANDassign,"&=")
LANGUAGE(ERassign,"^=")
LANGUAGE(ORassign,"|=")

PREPROCESSOR(LPAREN,OPEN_PAREN,"(")
PREPROCESSOR(RPAREN,CLOSE_PAREN,")")
PREPROCESSOR(COMMA,COMMA,",")
PREPROCESSOR(HASH,HASH,"#")
PREPROCESSOR(DHASH,DOUBLE_HASH,"##")
PREPROCESSOR(ELLIPSIS,ELLIPSIS,"...")

LANGUAGE(LBRACE,"{")
LANGUAGE(RBRACE,"}")
LANGUAGE(LBRACK,"[")
LANGUAGE(RBRACK,"]")
LANGUAGE(DOT,".")
LANGUAGE(AND,"&")
LANGUAGE(STAR,"*")
LANGUAGE(PLUS,"+")
LANGUAGE(MINUS,"-")
LANGUAGE(NEGATE,"~")
LANGUAGE(NOT,"!")
LANGUAGE(DIV,"/")
LANGUAGE(MOD,"%")
LANGUAGE(LT,"<")
LANGUAGE(GT,">")
LANGUAGE(XOR,"^")
LANGUAGE(PIPE,"|")
LANGUAGE(QUESTION,"?")
LANGUAGE(COLON,":")
LANGUAGE(SEMICOLON,";")
LANGUAGE(ASSIGN,"=")

// TODO find out what @ and @function are, something to do with assembly, e.g. in linux-2.6.36/include/linux/linkage.h
LANGUAGE(AT,"@")
LANGUAGE(USD,"$")

// For c++
//LANGUAGE(DOTSTAR,".*")
//LANGUAGE(DCOLON,"::")
//LANGUAGE(ARROWSTAR,"->*")

#ifdef LEXER

"/*" {
yybegin(COMMENT);
comment = new StringBuilder();
comment.append(yytext());
commentnl = false;
}
}

<COMMENT> {
"*/" {
comment.append(yytext()); yybegin(YYINITIAL);
return new Layout(comment.toString(), false);
}
[^*\n]+ {comment.append(yytext());}
"*" {comment.append(yytext());}
\n {comment.append(yytext()); commentnl = true;}
}

#elif defined TAG
TYPEDEFname(getID("TYPEDEFname"), null, true);

  private final int id;
  private final String text;
  private final boolean hasName;
  private final PreprocessorTag ppTag;

  CTag(int id, String text, boolean hasName, PreprocessorTag ppTag) {
    this.id = id;
    this.text = text;
    this.hasName = hasName;
    this.ppTag = ppTag;
  }

  CTag(int id, String text, boolean hasName) {
    this(id, text, hasName, PreprocessorTag.NONE);
  }

  CTag(int id, String text) {
    this(id, text, isName(text), PreprocessorTag.NONE);
  }

  CTag(int id, String text, PreprocessorTag ppTag) {
    this(id, text, isName(text), ppTag);
  }

  public int getID() {
    return id;
  }

  public String getText() {
    return text;
  }

  public boolean hasName() {
    return hasName;
  }

  public PreprocessorTag ppTag() {
    return ppTag;
  }

  static boolean isName(String name) {
    if (null == name || name.length() == 0) return false;

    return Character.isLetter(name.charAt(0)) || '_' == name.charAt(0);
  }

  static int getID(String token) {
    for (int id = 0; id < ForkMergeParserTables.YYNTOKENS; id++) {
      if (ForkMergeParserTables.yytname.table[id].equals(token)) {
        return id;
      }
    }

    // TODO use exceptions
    System.err.println("error: invalid token name");
    System.exit(1);

    return -1;
  }

}
#endif