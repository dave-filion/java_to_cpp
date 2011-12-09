/**
 * This header provides tool-independent language tag specification.
 * Define the LEXER configuration variable to produce a JFlex lexer
 * specification.  Define the TAG configuration variable to produce an
 * implementation of the Syntax.LanguageTag interface, used to provide
 * information to the SuperC system.
 */

#if defined LEXER && defined TAG
# error May only specify LEXER or TAG output, not both.
#elif ! defined LEXER && ! defined TAG
# error Must specify either LEXER or TAG
#endif

#ifdef LEXER

/**
 * Create a C Language Syntax object.  The "text" parameter must have
 * quotes, since it will be used as a Java string constant.
 */
# define LANGUAGE(token, text)                                        \
  text {                                                              \
    Language<CTag> syntax = new Language<CTag>(CTag.token);           \
                                                                      \
    syntax.setLocation(new Location(fileName, yyline+1, yycolumn+1)); \
                                                                      \
    return syntax;                                                    \
  }

/**
 * Create a C Text Syntax object.
 */
# define TEXT(token, regex, hasName)                                \
  regex {                                                           \
    Text<CTag> syntax = new Text<CTag>(CTag.token, yytext());       \
                                                                    \
    syntax.setLocation(new Location(fileName, yyline+1, yycolumn)); \
                                                                    \
    return syntax;                                                  \
  }

/**
 * Create a preprocessor language token.
 */
# define PREPROCESSOR(token, pptag, text) LANGUAGE(token, text)

#elif defined TAG
# define LANGUAGE(token, text) token(getID(#token), text),
# define TEXT(token, regex, hasName) token(getID(#token), null, hasName),
# define PREPROCESSOR(token, pptag, text) \
  token(getID(#token), text, PreprocessorTag.pptag),
#endif
