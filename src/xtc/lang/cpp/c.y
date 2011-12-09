/* Copyright (C) 1989,1990 James A. Roskind, All rights reserved.
    This grammar was developed  and  written  by  James  A.  Roskind.
    Copying  of  this  grammar  description, as a whole, is permitted
    providing this notice is intact and applicable  in  all  complete
    copies.   Translations as a whole to other parser generator input
    languages  (or  grammar  description  languages)   is   permitted
    provided  that  this  notice is intact and applicable in all such
    copies,  along  with  a  disclaimer  that  the  contents  are   a
    translation.   The reproduction of derived text, such as modified
    versions of this grammar, or the output of parser generators,  is
    permitted,  provided  the  resulting  work includes the copyright
    notice "Portions Copyright (c)  1989,  1990  James  A.  Roskind".
    Derived products, such as compilers, translators, browsers, etc.,
    that  use  this  grammar,  must also provide the notice "Portions
    Copyright  (c)  1989,  1990  James  A.  Roskind"  in   a   manner
    appropriate  to  the  utility,  and in keeping with copyright law
    (e.g.: EITHER displayed when first invoked/executed; OR displayed
    continuously on display terminal; OR via placement in the  object
    code  in  form  readable in a printout, with or near the title of
    the work, or at the end of the file).  No royalties, licenses  or
    commissions  of  any  kind are required to copy this grammar, its
    translations, or derivative products, when the copies are made in
    compliance with this notice. Persons or corporations that do make
    copies in compliance with this notice may charge  whatever  price
    is  agreeable  to  a  buyer, for such copies or derivative works.
    THIS GRAMMAR IS PROVIDED ``AS IS'' AND  WITHOUT  ANY  EXPRESS  OR
    IMPLIED  WARRANTIES,  INCLUDING,  WITHOUT LIMITATION, THE IMPLIED
    WARRANTIES  OF  MERCHANTABILITY  AND  FITNESS  FOR  A  PARTICULAR
    PURPOSE.

    James A. Roskind
    Independent Consultant
    516 Latania Palm Drive
    Indialantic FL, 32903
    (407)729-4348
    jar@ileaf.com

*/

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

/**
 * Definition of C's complete syntactic unit syntax.
 *
 * @version $Revision: 1.44 $
 */

%expect 1

/* keywords */
%token AUTO            DOUBLE          INT             STRUCT
%token BREAK           ELSE            LONG            SWITCH
%token CASE            ENUM            REGISTER        TYPEDEF
%token CHAR            EXTERN          RETURN          UNION
%token CONST           FLOAT           SHORT           UNSIGNED
%token CONTINUE        FOR             SIGNED          VOID
%token DEFAULT         GOTO            SIZEOF          VOLATILE
%token DO              IF              STATIC          WHILE

/* ANSI Grammar suggestions */
%token IDENTIFIER              STRINGliteral
%token FLOATINGconstant        INTEGERconstant        CHARACTERconstant
%token OCTALconstant           HEXconstant

/* New Lexical element, whereas ANSI suggested non-terminal */

%token TYPEDEFname /* Lexer will tell the difference between this and 
    an  Identifier!   An  Identifier  that is CURRENTLY in scope as a 
    typedef name is provided to the parser as a TYPEDEFname.*/

/* Multi-Character operators */
%token  ARROW  /** layout **/            /*    ->                              */
%token  ICR DECR         /*    ++      --                      */
%token  LS RS            /*    <<      >>                      */
%token  LE GE EQ NE      /*    <=      >=      ==      !=      */
%token  ANDAND OROR      /*    &&      ||                      */
%token  ELLIPSIS         /*    ...                             */

/* modifying assignment operators */
%token MULTassign  DIVassign    MODassign   /*   *=      /=      %=      */
%token PLUSassign  MINUSassign              /*   +=      -=              */
%token LSassign    RSassign                 /*   <<=     >>=             */
%token ANDassign   ERassign     ORassign    /*   &=      ^=      |=      */

/* punctuation */
%token LPAREN  /** layout **/
%token RPAREN  /** layout **/
%token COMMA  /** layout **/
%token HASH
%token DHASH
%token LBRACE  /** layout **/
%token RBRACE  /** layout **/
%token LBRACK  /** layout **/
%token RBRACK  /** layout **/
%token DOT  /** layout **/
%token AND
%token STAR
%token PLUS
%token MINUS
%token NEGATE
%token NOT
%token DIV
%token MOD
%token LT
%token GT
%token XOR
%token PIPE
%token QUESTION  /** layout **/
%token COLON  /** layout **/
%token SEMICOLON  /** layout **/
%token ASSIGN  /** layout **/

//tokens for gcc extensions
%token ASMSYM
%token _BOOL
%token _COMPLEX
%token RESTRICT
%token __ALIGNOF
%token __ALIGNOF__
%token ASM
%token __ASM
%token __ASM__
%token AT
%token USD
%token __ATTRIBUTE
%token __ATTRIBUTE__
%token __BUILTIN_OFFSETOF
%token __BUILTIN_TYPES_COMPATIBLE_P
%token __BUILTIN_VA_ARG
%token __BUILTIN_VA_LIST
%token __COMPLEX__
%token __CONST
%token __CONST__
%token __EXTENSION__
%token INLINE
%token __INLINE
%token __INLINE__
%token __LABEL__
%token __RESTRICT
%token __RESTRICT__
%token __SIGNED
%token __SIGNED__
%token __THREAD
%token TYPEOF
%token __TYPEOF
%token __TYPEOF__
%token __VOLATILE
%token __VOLATILE__

//preprocessor number catch-all token
%token PPNUM

//%token DOTSTAR
//%token DCOLON

%%

TranslationUnit:  /** complete, passthrough **/
        ExternalDeclarationList
        ;

// ------------------------------------------------------ External definitions

ExternalDeclarationList: /** list, complete **/
        /* empty */  // ADDED gcc allows empty program
        | ExternalDeclarationList ExternalDeclaration
        ;

ExternalDeclaration:  /** passthrough, complete **/
        FunctionDefinitionExtension
        | DeclarationExtension
        | AssemblyDefinition
        | EmptyDefinition
        ;

EmptyDefinition:  /** complete **/
        SEMICOLON
        ;

FunctionDefinitionExtension:  /** passthrough, complete **/  // ADDED
        FunctionDefinition
        | __EXTENSION__ FunctionDefinition
        ;

FunctionDefinition:  /** complete **/ // added scoping
          FunctionDeclarator ReenterScope LBRACE LocalLabelDeclarationListOpt DeclarationOrStatementList ExitScope RBRACE
        | FunctionOldPrototype ReenterScope DeclarationList LBRACE LocalLabelDeclarationListOpt DeclarationOrStatementList ExitScope RBRACE
        ;

FunctionDeclarator:  /** complete **/
                                     IdentifierDeclarator BindVar
        | DeclarationSpecifier      IdentifierDeclarator BindIdentifier
        | TypeSpecifier             IdentifierDeclarator BindIdentifier 
        | DeclarationQualifierList IdentifierDeclarator BindIdentifier 
        | TypeQualifierList        IdentifierDeclarator BindIdentifier 

        |                            OldFunctionDeclarator BindVar
        | DeclarationSpecifier      OldFunctionDeclarator BindIdentifier
        | TypeSpecifier             OldFunctionDeclarator BindIdentifier
        | DeclarationQualifierList OldFunctionDeclarator BindIdentifier
        | TypeQualifierList        OldFunctionDeclarator BindIdentifier
        ;

FunctionOldPrototype:  /** complete **/
                                     OldFunctionDeclarator BindVar
        | DeclarationSpecifier      OldFunctionDeclarator BindIdentifier
        | TypeSpecifier             OldFunctionDeclarator BindIdentifier
        | DeclarationQualifierList OldFunctionDeclarator BindIdentifier
        | TypeQualifierList        OldFunctionDeclarator BindIdentifier
        ;

// -------------------------------------------------------------- Declarations

    /* The following is different from the ANSI C specified  grammar.  
    The  changes  were  made  to  disambiguate  typedef's presence in 
    DeclarationSpecifiers (vs.  in the Declarator for redefinition); 
    to allow struct/union/enum tag declarations without  Declarators, 
    and  to  better  reflect the parsing of declarations (Declarators 
    must be combined with DeclarationSpecifiers ASAP  so  that  they 
    are visible in scope).

    Example  of  typedef  use  as either a DeclarationSpecifier or a 
    Declarator:

      typedef int T;
      struct S { T T;}; /* redefinition of T as member name * /

    Example of legal and illegal Statements detected by this grammar:

      int; /* syntax error: vacuous declaration * /
      struct S;  /* no error: tag is defined or elaborated * /

    Example of result of proper declaration binding:
        
        int a=sizeof(a); /* note that "a" is declared with a type  in 
            the name space BEFORE parsing the Initializer * /

        int b, c[sizeof(b)]; /* Note that the first Declarator "b" is 
             declared  with  a  type  BEFORE the second Declarator is 
             parsed * /

    */

DeclarationExtension:  /** passthrough, complete **/  // ADDED
        Declaration
        | __EXTENSION__ Declaration
        ;

Declaration:  /** complete **/
        SUEDeclarationSpecifier KillReentrantScope SEMICOLON
        | SUETypeSpecifier KillReentrantScope SEMICOLON
        | DeclaringList KillReentrantScope SEMICOLON
        | DefaultDeclaringList KillReentrantScope SEMICOLON
        ;

/* Note that if a typedef were  redeclared,  then  a  declaration 
   specifier must be supplied */

DefaultDeclaringList:  /** complete **/  /* Can't  redeclare typedef names */
        DeclarationQualifierList IdentifierDeclarator BindIdentifier AssemblyExpressionOpt AttributeSpecifierListOpt InitializerOpt
        | TypeQualifierList IdentifierDeclarator BindIdentifier AssemblyExpressionOpt AttributeSpecifierListOpt InitializerOpt
        | DefaultDeclaringList COMMA AttributeSpecifierListOpt IdentifierDeclarator BindIdentifierInList AssemblyExpressionOpt AttributeSpecifierListOpt InitializerOpt
        ;

DeclaringList:  /** complete **/
        DeclarationSpecifier Declarator BindIdentifier AssemblyExpressionOpt AttributeSpecifierListOpt InitializerOpt
        | TypeSpecifier Declarator BindIdentifier AssemblyExpressionOpt AttributeSpecifierListOpt InitializerOpt
        | DeclaringList COMMA AttributeSpecifierListOpt Declarator BindIdentifierInList AssemblyExpressionOpt AttributeSpecifierListOpt InitializerOpt
        ;

DeclarationSpecifier:  /** passthrough **/
        BasicDeclarationSpecifier        /* Arithmetic or void */
        | SUEDeclarationSpecifier          /* struct/union/enum */
        | TypedefDeclarationSpecifier      /* typedef*/
        | VarArgDeclarationSpecifier  // ADDED
        | TypeofDeclarationSpecifier // ADDED
        ;

TypeSpecifier:  /** passthrough **/
        BasicTypeSpecifier                 /* Arithmetic or void */
        | SUETypeSpecifier                 /* Struct/Union/Enum */
        | TypedefTypeSpecifier             /* Typedef */
        | VarArgTypeSpecifier  // ADDED
        | TypeofTypeSpecifier // ADDED
        ;

DeclarationQualifierList:  /** list **/  /* const/volatile, AND storage class */
        StorageClass
        | TypeQualifierList StorageClass
        | DeclarationQualifierList DeclarationQualifier
        ;

TypeQualifierList:  /** list **/
        TypeQualifier
        | TypeQualifierList TypeQualifier
        ;

DeclarationQualifier:
        TypeQualifier                  /* const or volatile */
        | StorageClass
        ;

TypeQualifier:  // const, volatile, and restrict can have underscores
        ConstQualifier
        | VolatileQualifier
        | RestrictQualifier
        | AttributeSpecifier // ADDED
        | FunctionSpecifier  // ADDED
        ;

ConstQualifier:  // ADDED
        CONST
        | __CONST
        | __CONST__
        ;

VolatileQualifier:  // ADDED
        VOLATILE
        | __VOLATILE
        | __VOLATILE__
        ;

RestrictQualifier:  // ADDED
        RESTRICT
        | __RESTRICT
        | __RESTRICT__
        ;

FunctionSpecifier: // ADDED
        INLINE
        | __INLINE
        | __INLINE__
        ;

BasicDeclarationSpecifier:      /*StorageClass+Arithmetic or void*/
        BasicTypeSpecifier  StorageClass
        | DeclarationQualifierList BasicTypeName
        | BasicDeclarationSpecifier DeclarationQualifier
        | BasicDeclarationSpecifier BasicTypeName
        ;

BasicTypeSpecifier:
        BasicTypeName            /* Arithmetic or void */
        | TypeQualifierList BasicTypeName
        | BasicTypeSpecifier TypeQualifier
        | BasicTypeSpecifier BasicTypeName
        ;

SUEDeclarationSpecifier:          /* StorageClass + struct/union/enum */
        SUETypeSpecifier StorageClass
        | DeclarationQualifierList ElaboratedTypeName
        | SUEDeclarationSpecifier DeclarationQualifier
        ;

SUETypeSpecifier:
        ElaboratedTypeName              /* struct/union/enum */
        | TypeQualifierList ElaboratedTypeName
        | SUETypeSpecifier TypeQualifier
        ;


TypedefDeclarationSpecifier:       /*Storage Class + typedef types */
        TypedefTypeSpecifier StorageClass
        | DeclarationQualifierList TYPEDEFname
        | TypedefDeclarationSpecifier DeclarationQualifier
        ;

TypedefTypeSpecifier:              /* typedef types */
        TYPEDEFname
        | TypeQualifierList TYPEDEFname
        | TypedefTypeSpecifier TypeQualifier
        ;

TypeofDeclarationSpecifier:      /*StorageClass+Arithmetic or void*/
        TypeofTypeSpecifier  StorageClass
        | DeclarationQualifierList Typeofspecifier
        | TypeofDeclarationSpecifier DeclarationQualifier
        | TypeofDeclarationSpecifier Typeofspecifier
        ;

TypeofTypeSpecifier:  // ADDED
        Typeofspecifier
        | TypeQualifierList Typeofspecifier
        | TypeofTypeSpecifier TypeQualifier
        | TypeofTypeSpecifier Typeofspecifier
        ;

Typeofspecifier:  // ADDED
        Typeofkeyword LPAREN TypeName RPAREN
        | Typeofkeyword LPAREN Expression RPAREN
        ;

Typeofkeyword:  // ADDED
        TYPEOF
        | __TYPEOF
        | __TYPEOF__
        ;

VarArgDeclarationSpecifier:      /*StorageClass+Arithmetic or void*/
        VarArgTypeSpecifier  StorageClass
        | DeclarationQualifierList VarArgTypeName
        | VarArgDeclarationSpecifier DeclarationQualifier
        | VarArgDeclarationSpecifier VarArgTypeName
        ;

VarArgTypeSpecifier:
        VarArgTypeName            /* Arithmetic or void */
        | TypeQualifierList VarArgTypeName
        | VarArgTypeSpecifier TypeQualifier
        | VarArgTypeSpecifier VarArgTypeName
        ;

VarArgTypeName:  // ADDED
        __BUILTIN_VA_LIST
        ;

StorageClass:
        TYPEDEF
        | EXTERN
        | STATIC
        | AUTO
        | REGISTER
        ;

BasicTypeName:
        VOID
        | CHAR
        | SHORT
        | INT
        | LONG
        | FLOAT
        | DOUBLE
        | SignedKeyword
        | UNSIGNED
        | _BOOL  // ADDED
        | ComplexKeyword  // ADDED
        ;

SignedKeyword:
        SIGNED
        | __SIGNED
        | __SIGNED__
        ;

ComplexKeyword:
        _COMPLEX
        | __COMPLEX__
        ;

ElaboratedTypeName:
        StructOrUnionSpecifier
        | EnumSpecifier
        ;

StructOrUnionSpecifier:  // ADDED attributes
        StructOrUnion EnterScope LBRACE StructDeclarationList ExitScope RBRACE
        | StructOrUnion IdentifierOrTypedefName
                EnterScope LBRACE StructDeclarationList ExitScope RBRACE
        | StructOrUnion IdentifierOrTypedefName
        | StructOrUnion AttributeSpecifierList EnterScope LBRACE StructDeclarationList ExitScope RBRACE
        | StructOrUnion AttributeSpecifierList IdentifierOrTypedefName
                EnterScope LBRACE StructDeclarationList ExitScope RBRACE
        | StructOrUnion AttributeSpecifierList IdentifierOrTypedefName
        ;

StructOrUnion:
        STRUCT
        | UNION
        ;

StructDeclarationList:  /** list **/
        /* StructDeclaration */ /* ADDED gcc empty struct */
        | StructDeclarationList StructDeclaration
        ;

StructDeclaration:
        StructDeclaringList SEMICOLON
        | StructDefaultDeclaringList SEMICOLON
        | TypeQualifierList SEMICOLON  // ADDED Declarator is optional
        | TypeSpecifier SEMICOLON  // ADDED Declarator is optional
        | SEMICOLON // ADDED gcc allows empty struct field in declaration
        ;

StructDefaultDeclaringList:  /** list **/        /* doesn't redeclare typedef*/
        TypeQualifierList StructIdentifierDeclarator AttributeSpecifierListOpt
        | StructDefaultDeclaringList COMMA StructIdentifierDeclarator AttributeSpecifierListOpt
        ;

StructDeclaringList:  /** list **/        
        TypeSpecifier StructDeclarator AttributeSpecifierListOpt
        | StructDeclaringList COMMA StructDeclarator AttributeSpecifierListOpt
        ;


StructDeclarator:
        Declarator BitFieldSizeOpt
        | BitFieldSize
        ;

StructIdentifierDeclarator:
        IdentifierDeclarator BitFieldSizeOpt
        | BitFieldSize
        ;

BitFieldSizeOpt:
        /* nothing */
        | BitFieldSize
        ;

BitFieldSize:
        COLON ConstantExpression
        ;

EnumSpecifier:  /* ADDED attributes */
        ENUM LBRACE EnumeratorList RBRACE
        | ENUM IdentifierOrTypedefName LBRACE EnumeratorList RBRACE
        | ENUM IdentifierOrTypedefName
        | ENUM LBRACE EnumeratorList COMMA RBRACE /* ADDED gcc extra comma */ 
        | ENUM IdentifierOrTypedefName LBRACE EnumeratorList COMMA RBRACE /* ADDED gcc extra comma */
        | ENUM AttributeSpecifierList LBRACE EnumeratorList RBRACE
        | ENUM AttributeSpecifierList IdentifierOrTypedefName LBRACE EnumeratorList RBRACE
        | ENUM AttributeSpecifierList IdentifierOrTypedefName
        | ENUM AttributeSpecifierList LBRACE EnumeratorList COMMA RBRACE /* ADDED gcc extra comma */ 
        | ENUM AttributeSpecifierList IdentifierOrTypedefName LBRACE EnumeratorList COMMA RBRACE /* ADDED gcc extra comma */
        ;

/*EnumeratorList:
        IdentifierOrTypedefName EnumeratorValueOpt
        | EnumeratorList COMMA IdentifierOrTypedefName EnumeratorValueOpt
        ;*/

EnumeratorList:  /** list **/  // easier to bind
        Enumerator
        | EnumeratorList COMMA Enumerator
        ;

Enumerator:
        IDENTIFIER BindEnum EnumeratorValueOpt
        | TYPEDEFname BindEnum EnumeratorValueOpt
        ;

EnumeratorValueOpt:
        /* Nothing */
        | ASSIGN ConstantExpression
        ;

ParameterTypeList:  /** complete **/
        ParameterList
        | ParameterList COMMA ELLIPSIS
        ;

ParameterList:  /** list, complete **/
        ParameterDeclaration
        | ParameterList COMMA ParameterDeclaration
        ;

ParameterDeclaration:  /** complete **/
        DeclarationSpecifier
        | DeclarationSpecifier AbstractDeclarator
        | DeclarationSpecifier IdentifierDeclarator BindIdentifier AttributeSpecifierListOpt
        | DeclarationSpecifier ParameterTypedefDeclarator BindIdentifier AttributeSpecifierListOpt
        | DeclarationQualifierList 
        | DeclarationQualifierList AbstractDeclarator
        | DeclarationQualifierList IdentifierDeclarator BindIdentifier AttributeSpecifierListOpt
        | TypeSpecifier
        | TypeSpecifier AbstractDeclarator
        | TypeSpecifier IdentifierDeclarator BindIdentifier AttributeSpecifierListOpt
        | TypeSpecifier ParameterTypedefDeclarator BindIdentifier AttributeSpecifierListOpt
        | TypeQualifierList 
        | TypeQualifierList AbstractDeclarator
        | TypeQualifierList IdentifierDeclarator BindIdentifier AttributeSpecifierListOpt
        ;

    /*  ANSI  C  section  3.7.1  states  "An Identifier declared as a 
    typedef name shall not be redeclared as a Parameter".  Hence  the 
    following is based only on IDENTIFIERs */

IdentifierList:  /** list, complete **/
        Identifier
        | IdentifierList COMMA Identifier
        ;

Identifier:  /** complete **/
       IDENTIFIER BindVar
       ;

IdentifierOrTypedefName:
        IDENTIFIER
        | TYPEDEFname
        ;

TypeName:
        TypeSpecifier
        | TypeSpecifier AbstractDeclarator
        | TypeQualifierList 
        | TypeQualifierList AbstractDeclarator
        ;

InitializerOpt:
        /* nothing */
        | ASSIGN DesignatedInitializer
        ;

DesignatedInitializer:/** complete, passthrough **/ /* ADDED */
        Initializer
        | Designation Initializer
        ;

/*InitializerStandard:  // ADDED gcc can have empty Initializer lists
        LBRACE InitializerList RBRACE
        | AssignmentExpression
        ;*/

Initializer:  // ADDED gcc can have empty Initializer lists
        LBRACE MatchedInitializerList RBRACE
        | LBRACE MatchedInitializerList DesignatedInitializer RBRACE
        | AssignmentExpression
        ;

InitializerList:  /** complete **/ //modified so that COMMAS are on the right
        MatchedInitializerList
        | MatchedInitializerList DesignatedInitializer
        ;

MatchedInitializerList:  /** list, complete **/
        | MatchedInitializerList DesignatedInitializer COMMA
        ;

Designation:  /* ADDED */
        DesignatorList ASSIGN
        | ObsoleteArrayDesignation
        | ObsoleteFieldDesignation
        ;

DesignatorList:  /** list **/  /* ADDED */
        Designator
        | DesignatorList Designator
        ;

Designator:  /* ADDED */
        LBRACK ConstantExpression RBRACK
        | LBRACK ConstantExpression ELLIPSIS ConstantExpression RBRACK
        | DOT IDENTIFIER //IDENTIFIER
        | DOT TYPEDEFname // ADDED hack to get around using typedef names as struct fields
        ;

ObsoleteArrayDesignation:  /* ADDED */
        LBRACK ConstantExpression RBRACK
        | LBRACK ConstantExpression ELLIPSIS ConstantExpression RBRACK
        ;

ObsoleteFieldDesignation:  /* ADDED */
        IDENTIFIER COLON
        ;

Declarator:  /** complete, passthrough **/
        TypedefDeclarator
        | IdentifierDeclarator
        ;

TypedefDeclarator:  /** passthrough **/  // ADDED
        TypedefDeclaratorMain //AssemblyExpressionOpt AttributeSpecifierListOpt
        ;

TypedefDeclaratorMain:  /** passthrough **/
        ParenTypedefDeclarator  /* would be ambiguous as Parameter*/
        | ParameterTypedefDeclarator   /* not ambiguous as param*/
        ;

ParameterTypedefDeclarator:
        TYPEDEFname 
        | TYPEDEFname PostfixingAbstractDeclarator
        | CleanTypedefDeclarator
        ;

    /*  The  following have at least one STAR. There is no (redundant) 
    LPAREN between the STAR and the TYPEDEFname. */

CleanTypedefDeclarator:
        CleanPostfixTypedefDeclarator
        | STAR ParameterTypedefDeclarator
        | STAR TypeQualifierList ParameterTypedefDeclarator  
        ;

CleanPostfixTypedefDeclarator:
        LPAREN CleanTypedefDeclarator RPAREN
        | LPAREN CleanTypedefDeclarator RPAREN PostfixingAbstractDeclarator
        ;

    /* The following have a redundant LPAREN placed immediately  to  the 
    left of the TYPEDEFname */

ParenTypedefDeclarator:  /** passthrough **/
        ParenPostfixTypedefDeclarator
        | STAR LPAREN SimpleParenTypedefDeclarator RPAREN /* redundant paren */
        | STAR TypeQualifierList  
                LPAREN SimpleParenTypedefDeclarator RPAREN /* redundant paren */
        | STAR ParenTypedefDeclarator
        | STAR TypeQualifierList ParenTypedefDeclarator
        ;
        
ParenPostfixTypedefDeclarator: /* redundant paren to left of tname*/
        LPAREN ParenTypedefDeclarator RPAREN
        | LPAREN SimpleParenTypedefDeclarator PostfixingAbstractDeclarator RPAREN /* redundant paren */
        | LPAREN ParenTypedefDeclarator RPAREN PostfixingAbstractDeclarator
        ;

SimpleParenTypedefDeclarator:
        TYPEDEFname
        | LPAREN SimpleParenTypedefDeclarator RPAREN
        ;

IdentifierDeclarator:  /** passthrough **/
        IdentifierDeclaratorMain //AssemblyExpressionOpt AttributeSpecifierListOpt
        ;

IdentifierDeclaratorMain:  /** passthrough **/
        UnaryIdentifierDeclarator
        | ParenIdentifierDeclarator
        ;

UnaryIdentifierDeclarator: /** passthrough **/
        PostfixIdentifierDeclarator
        | STAR IdentifierDeclarator
        | STAR TypeQualifierList IdentifierDeclarator
        ;
        
PostfixIdentifierDeclarator:
        ParenIdentifierDeclarator PostfixingAbstractDeclarator
        | LPAREN UnaryIdentifierDeclarator RPAREN
        | LPAREN UnaryIdentifierDeclarator RPAREN PostfixingAbstractDeclarator
        ;

ParenIdentifierDeclarator:  /** passthrough **/
        SimpleDeclarator
        | LPAREN ParenIdentifierDeclarator RPAREN
        ;

SimpleDeclarator:
        IDENTIFIER  /* bind */
        ;

OldFunctionDeclarator:
        PostfixOldFunctionDeclarator
        | STAR OldFunctionDeclarator
        | STAR TypeQualifierList OldFunctionDeclarator
        ;

PostfixOldFunctionDeclarator:
        ParenIdentifierDeclarator LPAREN EnterScope IdentifierList ExitReentrantScope RPAREN
        | LPAREN OldFunctionDeclarator RPAREN
        | LPAREN OldFunctionDeclarator RPAREN PostfixingAbstractDeclarator
        ;

AbstractDeclarator:
        UnaryAbstractDeclarator
        | PostfixAbstractDeclarator
        | PostfixingAbstractDeclarator
        ;

PostfixingAbstractDeclarator:
        ArrayAbstractDeclarator
        | LPAREN EnterScope ParameterTypeListOpt ExitReentrantScope RPAREN
        ;
ParameterTypeListOpt:
        /* empty */
        | ParameterTypeList
        ;

ArrayAbstractDeclarator:
        LBRACK RBRACK
        | LBRACK ConstantExpression RBRACK
        | ArrayAbstractDeclarator LBRACK ConstantExpression RBRACK
        ;

UnaryAbstractDeclarator:
        STAR 
        | STAR TypeQualifierList 
        | STAR AbstractDeclarator
        | STAR TypeQualifierList AbstractDeclarator
        ;

PostfixAbstractDeclarator:
        LPAREN UnaryAbstractDeclarator RPAREN
        | LPAREN PostfixAbstractDeclarator RPAREN
        | LPAREN PostfixingAbstractDeclarator RPAREN
        | LPAREN UnaryAbstractDeclarator RPAREN PostfixingAbstractDeclarator
        ;

// ---------------------------------------------------------------- Statements

Statement:  /** passthrough, complete **/
        LabeledStatement
        | CompoundStatement
        | ExpressionStatement
        | SelectionStatement
        | IterationStatement
        | JumpStatement
        | AssemblyStatement  // ADDED
        ;

LabeledStatement:  /** complete **/  // ADDED attributes
        IdentifierOrTypedefName COLON AttributeSpecifierListOpt Statement
        | CASE ConstantExpression COLON Statement
        | CASE ConstantExpression ELLIPSIS ConstantExpression COLON Statement  // ADDED case range
        | DEFAULT COLON Statement
        ;

/*CompoundStatement:
        LBRACE RBRACE
        | LBRACE DeclarationList RBRACE
        | LBRACE StatementList RBRACE
        | LBRACE DeclarationList StatementList RBRACE
        ;*/

CompoundStatement:  /** complete **/  /* ADDED */
        LBRACE LocalLabelDeclarationListOpt DeclarationOrStatementList RBRACE
        ;

LocalLabelDeclarationListOpt:
        /* empty */
        | LocalLabelDeclarationList
        ;

LocalLabelDeclarationList:  /** list **/
        LocalLabelDeclaration
        | LocalLabelDeclarationList LocalLabelDeclaration
        ;

LocalLabelDeclaration:  /* ADDED */
        __LABEL__ LocalLabelList SEMICOLON
        ;

LocalLabelList:  /** list **/  // ADDED
        IDENTIFIER
        | LocalLabelList COMMA IDENTIFIER
        ;

DeclarationOrStatementList:  /** list, complete **/  /* ADDED */
        | DeclarationOrStatementList DeclarationOrStatement
        ;

DeclarationOrStatement: /** passthrough, complete **/  /* ADDED */
        DeclarationExtension
        | Statement
        ;

DeclarationList:  /** list, complete **/
        DeclarationExtension
        | DeclarationList DeclarationExtension
        ;

/*StatementList:
        Statement
        | StatementList Statement
        ;*/

ExpressionStatement:  /** complete **/
        ExpressionOpt SEMICOLON
        ;

SelectionStatement:  /** complete **/
        IF LPAREN Expression RPAREN Statement
        | IF LPAREN Expression RPAREN Statement ELSE Statement
        | SWITCH LPAREN Expression RPAREN Statement
        ;

IterationStatement:  /** complete **/
        WHILE LPAREN Expression RPAREN Statement
        | DO Statement WHILE LPAREN Expression RPAREN SEMICOLON
        | FOR LPAREN ExpressionOpt SEMICOLON ExpressionOpt SEMICOLON
                ExpressionOpt RPAREN Statement
        ;

JumpStatement:  /** complete **/
        GOTO IdentifierOrTypedefName SEMICOLON
        | GOTO STAR Expression SEMICOLON  // ADDED
        | CONTINUE SEMICOLON
        | BREAK SEMICOLON
        | RETURN ExpressionOpt SEMICOLON
        ;

// --------------------------------------------------------------- Expressions

/* CONSTANTS */
Constant:
        FLOATINGconstant
        | INTEGERconstant
        /* We are not including ENUMERATIONConstant here  because  we 
        are  treating  it like a variable with a type of "enumeration 
        Constant".  */
        | OCTALconstant
        | HEXconstant
        | CHARACTERconstant
        ;

/* STRING LITERALS */
StringLiteralList:  /** list, complete **/
                STRINGliteral
                | StringLiteralList STRINGliteral
                ;


/* EXPRESSIONS */
PrimaryExpression:  /** complete, passthrough **/
        PrimaryIdentifier
        | Constant
        | StringLiteralList
        | LPAREN Expression RPAREN
        | StatementAsExpression  // ADDED
        | VariableArgumentAccess  // ADDED
        ;

PrimaryIdentifier:
        IDENTIFIER  /* We cannot use a typedef name as a variable */
        ;

VariableArgumentAccess:  /** complete **/  // ADDED
        __BUILTIN_VA_ARG LPAREN AssignmentExpression COMMA TypeName RPAREN
        ;

StatementAsExpression:  /** complete **/  //ADDED
        LPAREN EnterScope CompoundStatement ExitScope RPAREN

PostfixExpression:  /** passthrough, complete **/
        PrimaryExpression
        | PostfixExpression LBRACK Expression RBRACK
        | PostfixExpression LPAREN RPAREN
        | PostfixExpression LPAREN ArgumentExpressionList RPAREN
        | PostfixExpression DOT IdentifierOrTypedefName
        | PostfixExpression ARROW IdentifierOrTypedefName
        | PostfixExpression ICR
        | PostfixExpression DECR
        | CompoundLiteral  /* ADDED */
        ;

CompoundLiteral:  /** complete **/  /* ADDED */
        LPAREN TypeName RPAREN LBRACE InitializerList RBRACE
        ;

ArgumentExpressionList:  /** list, complete **/
        AssignmentExpression
        | ArgumentExpressionList COMMA AssignmentExpression
        ;

UnaryExpression:  /** passthrough, complete **/
        PostfixExpression
        | ICR UnaryExpression
        | DECR UnaryExpression
        | Unaryoperator CastExpression
        | SIZEOF UnaryExpression
        | SIZEOF LPAREN TypeName RPAREN
        | LabelAddressExpression  // ADDED
        | AlignofExpression // ADDED
        | ExtensionExpression // ADDED
        | OffsetofExpression // ADDED
        | TypeCompatibilityExpression  // ADEED
        ;

TypeCompatibilityExpression:  /** complete **/
        __BUILTIN_TYPES_COMPATIBLE_P LPAREN TypeName COMMA TypeName RPAREN
        ;

OffsetofExpression:  /** complete **/
        __BUILTIN_OFFSETOF LPAREN TypeName COMMA PostfixExpression RPAREN
        ;

ExtensionExpression:  /** complete **/
        __EXTENSION__ CastExpression
        ;

AlignofExpression:  /** complete **/
        Alignofkeyword LPAREN TypeName RPAREN
        | Alignofkeyword UnaryExpression
        ;

Alignofkeyword:
        __ALIGNOF__
        | __ALIGNOF
        ;

LabelAddressExpression:  /** complete **/  // ADDED
        ANDAND IDENTIFIER;
        ;

Unaryoperator:
        AND
        | STAR
        | PLUS
        | MINUS
        | NEGATE
        | NOT
        ;

CastExpression:  /** passthrough, complete **/
        UnaryExpression
        | LPAREN TypeName RPAREN CastExpression
        ;

MultiplicativeExpression:  /** passthrough, complete **/
        CastExpression
        | MultiplicativeExpression STAR CastExpression
        | MultiplicativeExpression DIV CastExpression
        | MultiplicativeExpression MOD CastExpression
        ;

AdditiveExpression:  /** passthrough, complete **/
        MultiplicativeExpression
        | AdditiveExpression PLUS MultiplicativeExpression
        | AdditiveExpression MINUS MultiplicativeExpression
        ;

ShiftExpression:  /** passthrough, complete **/
        AdditiveExpression
        | ShiftExpression LS AdditiveExpression
        | ShiftExpression RS AdditiveExpression
        ;

RelationalExpression:  /** passthrough, complete **/
        ShiftExpression
        | RelationalExpression LT ShiftExpression
        | RelationalExpression GT ShiftExpression
        | RelationalExpression LE ShiftExpression
        | RelationalExpression GE ShiftExpression
        ;

EqualityExpression:  /** passthrough, complete **/
        RelationalExpression
        | EqualityExpression EQ RelationalExpression
        | EqualityExpression NE RelationalExpression
        ;

AndExpression:  /** passthrough, complete **/
        EqualityExpression
        | AndExpression AND EqualityExpression
        ;

ExclusiveOrExpression:  /** passthrough, complete **/
        AndExpression
        | ExclusiveOrExpression XOR AndExpression
        ;

InclusiveOrExpression:  /** passthrough, complete **/
        ExclusiveOrExpression
        | InclusiveOrExpression PIPE ExclusiveOrExpression
        ;

LogicalAndExpression:  /** passthrough, complete **/
        InclusiveOrExpression
        | LogicalAndExpression ANDAND InclusiveOrExpression
        ;

LogicalORExpression:  /** passthrough, complete **/
        LogicalAndExpression
        | LogicalORExpression OROR LogicalAndExpression
        ;

ConditionalExpression:  /** passthrough, complete **/
        LogicalORExpression
        | LogicalORExpression QUESTION Expression COLON
                ConditionalExpression
        | LogicalORExpression QUESTION COLON  // ADDED gcc incomplete conditional
                ConditionalExpression
        ;

AssignmentExpression:  /** passthrough, complete **/
        ConditionalExpression
        | UnaryExpression AssignmentOperator AssignmentExpression
        ;

AssignmentOperator:
        ASSIGN
        | MULTassign
        | DIVassign
        | MODassign
        | PLUSassign
        | MINUSassign
        | LSassign
        | RSassign
        | ANDassign
        | ERassign
        | ORassign
        ;

Expression:  /** passthrough, complete **/
        AssignmentExpression
        | Expression COMMA AssignmentExpression
        ;

ConstantExpression: /** passthrough, complete **/
        ConditionalExpression
        ;

    /* The following was used for clarity */
ExpressionOpt:  /** passthrough, complete **/
        /* Nothing */
        | Expression
        ;

AttributeSpecifierListOpt:  // ADDED
        /* empty */
        | AttributeSpecifierList
        ;

AttributeSpecifierList:  /** list **/  // ADDED
        AttributeSpecifier
        | AttributeSpecifierList AttributeSpecifier
        ;

AttributeSpecifier:  // ADDED
        AttributeKeyword LPAREN LPAREN AttributeListOpt RPAREN RPAREN
        ;

AttributeKeyword:  // ADDED
        __ATTRIBUTE
        | __ATTRIBUTE__
        ;

AttributeListOpt:  // ADDED
        /* empty */
        | AttributeList
        ;

AttributeList:  /** list **/  // ADDED
        Word AttributeExpressionOpt
        | AttributeList COMMA Word AttributeExpressionOpt
        ;

AttributeExpressionOpt:  // ADDED
        /* empty */
        | LPAREN RPAREN
        | LPAREN ArgumentExpressionList RPAREN
        ;

Word:  // ADDED
        IDENTIFIER
        | AUTO
        | DOUBLE
        | INT
        | STRUCT
        | BREAK
        | ELSE
        | LONG
        | SWITCH
        | CASE
        | ENUM
        | REGISTER
        | TYPEDEF
        | CHAR
        | EXTERN
        | RETURN
        | UNION
        | CONST
        | FLOAT
        | SHORT
        | UNSIGNED
        | CONTINUE
        | FOR
        | SIGNED
        | VOID
        | DEFAULT
        | GOTO
        | SIZEOF
        | VOLATILE
        | DO
        | IF
        | STATIC
        | WHILE
        | ASMSYM
        | _BOOL
        | _COMPLEX
        | RESTRICT
        | __ALIGNOF
        | __ALIGNOF__
        | ASM
        | __ASM
        | __ASM__
        | __ATTRIBUTE
        | __ATTRIBUTE__
        | __BUILTIN_OFFSETOF
        | __BUILTIN_TYPES_COMPATIBLE_P
        | __BUILTIN_VA_ARG
        | __BUILTIN_VA_LIST
        | __COMPLEX__
        | __CONST
        | __CONST__
        | __EXTENSION__
        | INLINE
        | __INLINE
        | __INLINE__
        | __LABEL__
        | __RESTRICT
        | __RESTRICT__
        | __SIGNED
        | __SIGNED__
        | __THREAD
        | TYPEOF
        | __TYPEOF
        | __TYPEOF__
        | __VOLATILE
        | __VOLATILE__
        ;

// ------------------------------------------------------------------ Assembly

AssemblyDefinition:  /** complete **/
        AssemblyExpression SEMICOLON
        ;

AssemblyExpression:  /** complete **/
        AsmKeyword LPAREN StringLiteralList RPAREN
        ;

AssemblyExpressionOpt:  /** complete **/
        /* empty */
        | AssemblyExpression
        ;

AssemblyStatement:   /** complete **/ // ADDED 
        AsmKeyword LPAREN Assemblyargument RPAREN SEMICOLON
        | AsmKeyword TypeQualifier LPAREN Assemblyargument RPAREN SEMICOLON
        ;

Assemblyargument:  /** complete **/  // ADDED
        StringLiteralList COLON AssemblyoperandsOpt COLON AssemblyoperandsOpt COLON Assemblyclobbers
        | StringLiteralList COLON AssemblyoperandsOpt COLON AssemblyoperandsOpt
        | StringLiteralList COLON AssemblyoperandsOpt
        | StringLiteralList
        ;

AssemblyoperandsOpt:  /** complete **/  // ADDED
        /* empty */
        | Assemblyoperands
        ;

Assemblyoperands:  /** list, complete **/  // ADDED
        Assemblyoperand
        | Assemblyoperands COMMA Assemblyoperand
        ;

Assemblyoperand:  /** complete **/  // ADDED
                             StringLiteralList LPAREN Expression RPAREN
        | LBRACK Word RBRACK StringLiteralList LPAREN Expression RPAREN
        ;

Assemblyclobbers:  /** complete **/  // ADDED
        StringLiteralList
        | Assemblyclobbers COMMA StringLiteralList
        ;

AsmKeyword:  // ADDED
        ASM
        | __ASM
        | __ASM__
        ;

// ----------------------------------------------------------- Semantic Actions

/** The following productions are markers the parser uses for binding
  * Identifiers and managing parser context
  */

BindIdentifier:  /** action, complete **/
        /* empty */ ;

BindIdentifierInList:  /** action, complete **/
        /* empty */ ;

BindVar:  /** action, complete **/
        /* empty */ ;

BindEnum:  /** action, complete **/
        /* empty */ ;

EnterScope:  /** action, complete **/
        /* empty */ ;

ExitScope:  /** action, complete **/
        /* empty */ ;

ExitReentrantScope:  /** action, complete **/
        /* empty */ ;

ReenterScope:  /** action, complete **/
        /* empty */ ;

KillReentrantScope:  /** action, complete **/
        /* empty */ ;

