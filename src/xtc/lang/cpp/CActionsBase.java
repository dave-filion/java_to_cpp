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

import xtc.lang.cpp.ForkMergeParser.Subparser;

/**
 * This class is generated from grammar annotations and provides semantic
 * value and action support.
 */
public abstract class CActionsBase extends Actions {

  public ValueType getValueType(int id) {
    switch (id) {
    case 0: // $end
      return ValueType.NODE;

    case 1: // error
      return ValueType.NODE;

    case 2: // $undefined
      return ValueType.NODE;

    case 3: // AUTO
      return ValueType.NODE;

    case 4: // DOUBLE
      return ValueType.NODE;

    case 5: // INT
      return ValueType.NODE;

    case 6: // STRUCT
      return ValueType.NODE;

    case 7: // BREAK
      return ValueType.NODE;

    case 8: // ELSE
      return ValueType.NODE;

    case 9: // LONG
      return ValueType.NODE;

    case 10: // SWITCH
      return ValueType.NODE;

    case 11: // CASE
      return ValueType.NODE;

    case 12: // ENUM
      return ValueType.NODE;

    case 13: // REGISTER
      return ValueType.NODE;

    case 14: // TYPEDEF
      return ValueType.NODE;

    case 15: // CHAR
      return ValueType.NODE;

    case 16: // EXTERN
      return ValueType.NODE;

    case 17: // RETURN
      return ValueType.NODE;

    case 18: // UNION
      return ValueType.NODE;

    case 19: // CONST
      return ValueType.NODE;

    case 20: // FLOAT
      return ValueType.NODE;

    case 21: // SHORT
      return ValueType.NODE;

    case 22: // UNSIGNED
      return ValueType.NODE;

    case 23: // CONTINUE
      return ValueType.NODE;

    case 24: // FOR
      return ValueType.NODE;

    case 25: // SIGNED
      return ValueType.NODE;

    case 26: // VOID
      return ValueType.NODE;

    case 27: // DEFAULT
      return ValueType.NODE;

    case 28: // GOTO
      return ValueType.NODE;

    case 29: // SIZEOF
      return ValueType.NODE;

    case 30: // VOLATILE
      return ValueType.NODE;

    case 31: // DO
      return ValueType.NODE;

    case 32: // IF
      return ValueType.NODE;

    case 33: // STATIC
      return ValueType.NODE;

    case 34: // WHILE
      return ValueType.NODE;

    case 35: // IDENTIFIER
      return ValueType.NODE;

    case 36: // STRINGliteral
      return ValueType.NODE;

    case 37: // FLOATINGconstant
      return ValueType.NODE;

    case 38: // INTEGERconstant
      return ValueType.NODE;

    case 39: // CHARACTERconstant
      return ValueType.NODE;

    case 40: // OCTALconstant
      return ValueType.NODE;

    case 41: // HEXconstant
      return ValueType.NODE;

    case 42: // TYPEDEFname
      return ValueType.NODE;

    case 43: // ARROW
      return ValueType.NODE;

    case 44: // ICR
      return ValueType.NODE;

    case 45: // DECR
      return ValueType.NODE;

    case 46: // LS
      return ValueType.NODE;

    case 47: // RS
      return ValueType.NODE;

    case 48: // LE
      return ValueType.NODE;

    case 49: // GE
      return ValueType.NODE;

    case 50: // EQ
      return ValueType.NODE;

    case 51: // NE
      return ValueType.NODE;

    case 52: // ANDAND
      return ValueType.NODE;

    case 53: // OROR
      return ValueType.NODE;

    case 54: // ELLIPSIS
      return ValueType.NODE;

    case 55: // MULTassign
      return ValueType.NODE;

    case 56: // DIVassign
      return ValueType.NODE;

    case 57: // MODassign
      return ValueType.NODE;

    case 58: // PLUSassign
      return ValueType.NODE;

    case 59: // MINUSassign
      return ValueType.NODE;

    case 60: // LSassign
      return ValueType.NODE;

    case 61: // RSassign
      return ValueType.NODE;

    case 62: // ANDassign
      return ValueType.NODE;

    case 63: // ERassign
      return ValueType.NODE;

    case 64: // ORassign
      return ValueType.NODE;

    case 65: // LPAREN
      return ValueType.LAYOUT;

    case 66: // RPAREN
      return ValueType.LAYOUT;

    case 67: // COMMA
      return ValueType.LAYOUT;

    case 68: // HASH
      return ValueType.NODE;

    case 69: // DHASH
      return ValueType.NODE;

    case 70: // LBRACE
      return ValueType.LAYOUT;

    case 71: // RBRACE
      return ValueType.LAYOUT;

    case 72: // LBRACK
      return ValueType.LAYOUT;

    case 73: // RBRACK
      return ValueType.LAYOUT;

    case 74: // DOT
      return ValueType.LAYOUT;

    case 75: // AND
      return ValueType.NODE;

    case 76: // STAR
      return ValueType.NODE;

    case 77: // PLUS
      return ValueType.NODE;

    case 78: // MINUS
      return ValueType.NODE;

    case 79: // NEGATE
      return ValueType.NODE;

    case 80: // NOT
      return ValueType.NODE;

    case 81: // DIV
      return ValueType.NODE;

    case 82: // MOD
      return ValueType.NODE;

    case 83: // LT
      return ValueType.NODE;

    case 84: // GT
      return ValueType.NODE;

    case 85: // XOR
      return ValueType.NODE;

    case 86: // PIPE
      return ValueType.NODE;

    case 87: // QUESTION
      return ValueType.LAYOUT;

    case 88: // COLON
      return ValueType.LAYOUT;

    case 89: // SEMICOLON
      return ValueType.LAYOUT;

    case 90: // ASSIGN
      return ValueType.LAYOUT;

    case 91: // ASMSYM
      return ValueType.NODE;

    case 92: // _BOOL
      return ValueType.NODE;

    case 93: // _COMPLEX
      return ValueType.NODE;

    case 94: // RESTRICT
      return ValueType.NODE;

    case 95: // __ALIGNOF
      return ValueType.NODE;

    case 96: // __ALIGNOF__
      return ValueType.NODE;

    case 97: // ASM
      return ValueType.NODE;

    case 98: // __ASM
      return ValueType.NODE;

    case 99: // __ASM__
      return ValueType.NODE;

    case 100: // AT
      return ValueType.NODE;

    case 101: // USD
      return ValueType.NODE;

    case 102: // __ATTRIBUTE
      return ValueType.NODE;

    case 103: // __ATTRIBUTE__
      return ValueType.NODE;

    case 104: // __BUILTIN_OFFSETOF
      return ValueType.NODE;

    case 105: // __BUILTIN_TYPES_COMPATIBLE_P
      return ValueType.NODE;

    case 106: // __BUILTIN_VA_ARG
      return ValueType.NODE;

    case 107: // __BUILTIN_VA_LIST
      return ValueType.NODE;

    case 108: // __COMPLEX__
      return ValueType.NODE;

    case 109: // __CONST
      return ValueType.NODE;

    case 110: // __CONST__
      return ValueType.NODE;

    case 111: // __EXTENSION__
      return ValueType.NODE;

    case 112: // INLINE
      return ValueType.NODE;

    case 113: // __INLINE
      return ValueType.NODE;

    case 114: // __INLINE__
      return ValueType.NODE;

    case 115: // __LABEL__
      return ValueType.NODE;

    case 116: // __RESTRICT
      return ValueType.NODE;

    case 117: // __RESTRICT__
      return ValueType.NODE;

    case 118: // __SIGNED
      return ValueType.NODE;

    case 119: // __SIGNED__
      return ValueType.NODE;

    case 120: // __THREAD
      return ValueType.NODE;

    case 121: // TYPEOF
      return ValueType.NODE;

    case 122: // __TYPEOF
      return ValueType.NODE;

    case 123: // __TYPEOF__
      return ValueType.NODE;

    case 124: // __VOLATILE
      return ValueType.NODE;

    case 125: // __VOLATILE__
      return ValueType.NODE;

    case 126: // PPNUM
      return ValueType.NODE;

    case 127: // $accept
      return ValueType.NODE;

    case 128: // TranslationUnit
      return ValueType.PASS_THROUGH;

    case 129: // ExternalDeclarationList
      return ValueType.LIST;

    case 130: // ExternalDeclaration
      return ValueType.PASS_THROUGH;

    case 131: // EmptyDefinition
      return ValueType.NODE;

    case 132: // FunctionDefinitionExtension
      return ValueType.PASS_THROUGH;

    case 133: // FunctionDefinition
      return ValueType.NODE;

    case 134: // FunctionDeclarator
      return ValueType.NODE;

    case 135: // FunctionOldPrototype
      return ValueType.NODE;

    case 136: // DeclarationExtension
      return ValueType.PASS_THROUGH;

    case 137: // Declaration
      return ValueType.NODE;

    case 138: // DefaultDeclaringList
      return ValueType.NODE;

    case 139: // DeclaringList
      return ValueType.NODE;

    case 140: // DeclarationSpecifier
      return ValueType.PASS_THROUGH;

    case 141: // TypeSpecifier
      return ValueType.PASS_THROUGH;

    case 142: // DeclarationQualifierList
      return ValueType.LIST;

    case 143: // TypeQualifierList
      return ValueType.LIST;

    case 144: // DeclarationQualifier
      return ValueType.NODE;

    case 145: // TypeQualifier
      return ValueType.NODE;

    case 146: // ConstQualifier
      return ValueType.NODE;

    case 147: // VolatileQualifier
      return ValueType.NODE;

    case 148: // RestrictQualifier
      return ValueType.NODE;

    case 149: // FunctionSpecifier
      return ValueType.NODE;

    case 150: // BasicDeclarationSpecifier
      return ValueType.NODE;

    case 151: // BasicTypeSpecifier
      return ValueType.NODE;

    case 152: // SUEDeclarationSpecifier
      return ValueType.NODE;

    case 153: // SUETypeSpecifier
      return ValueType.NODE;

    case 154: // TypedefDeclarationSpecifier
      return ValueType.NODE;

    case 155: // TypedefTypeSpecifier
      return ValueType.NODE;

    case 156: // TypeofDeclarationSpecifier
      return ValueType.NODE;

    case 157: // TypeofTypeSpecifier
      return ValueType.NODE;

    case 158: // Typeofspecifier
      return ValueType.NODE;

    case 159: // Typeofkeyword
      return ValueType.NODE;

    case 160: // VarArgDeclarationSpecifier
      return ValueType.NODE;

    case 161: // VarArgTypeSpecifier
      return ValueType.NODE;

    case 162: // VarArgTypeName
      return ValueType.NODE;

    case 163: // StorageClass
      return ValueType.NODE;

    case 164: // BasicTypeName
      return ValueType.NODE;

    case 165: // SignedKeyword
      return ValueType.NODE;

    case 166: // ComplexKeyword
      return ValueType.NODE;

    case 167: // ElaboratedTypeName
      return ValueType.NODE;

    case 168: // StructOrUnionSpecifier
      return ValueType.NODE;

    case 169: // StructOrUnion
      return ValueType.NODE;

    case 170: // StructDeclarationList
      return ValueType.LIST;

    case 171: // StructDeclaration
      return ValueType.NODE;

    case 172: // StructDefaultDeclaringList
      return ValueType.LIST;

    case 173: // StructDeclaringList
      return ValueType.LIST;

    case 174: // StructDeclarator
      return ValueType.NODE;

    case 175: // StructIdentifierDeclarator
      return ValueType.NODE;

    case 176: // BitFieldSizeOpt
      return ValueType.NODE;

    case 177: // BitFieldSize
      return ValueType.NODE;

    case 178: // EnumSpecifier
      return ValueType.NODE;

    case 179: // EnumeratorList
      return ValueType.LIST;

    case 180: // Enumerator
      return ValueType.NODE;

    case 181: // EnumeratorValueOpt
      return ValueType.NODE;

    case 182: // ParameterTypeList
      return ValueType.NODE;

    case 183: // ParameterList
      return ValueType.LIST;

    case 184: // ParameterDeclaration
      return ValueType.NODE;

    case 185: // IdentifierList
      return ValueType.LIST;

    case 186: // Identifier
      return ValueType.NODE;

    case 187: // IdentifierOrTypedefName
      return ValueType.NODE;

    case 188: // TypeName
      return ValueType.NODE;

    case 189: // InitializerOpt
      return ValueType.NODE;

    case 190: // DesignatedInitializer
      return ValueType.PASS_THROUGH;

    case 191: // Initializer
      return ValueType.NODE;

    case 192: // InitializerList
      return ValueType.NODE;

    case 193: // MatchedInitializerList
      return ValueType.LIST;

    case 194: // Designation
      return ValueType.NODE;

    case 195: // DesignatorList
      return ValueType.LIST;

    case 196: // Designator
      return ValueType.NODE;

    case 197: // ObsoleteArrayDesignation
      return ValueType.NODE;

    case 198: // ObsoleteFieldDesignation
      return ValueType.NODE;

    case 199: // Declarator
      return ValueType.PASS_THROUGH;

    case 200: // TypedefDeclarator
      return ValueType.PASS_THROUGH;

    case 201: // TypedefDeclaratorMain
      return ValueType.PASS_THROUGH;

    case 202: // ParameterTypedefDeclarator
      return ValueType.NODE;

    case 203: // CleanTypedefDeclarator
      return ValueType.NODE;

    case 204: // CleanPostfixTypedefDeclarator
      return ValueType.NODE;

    case 205: // ParenTypedefDeclarator
      return ValueType.PASS_THROUGH;

    case 206: // ParenPostfixTypedefDeclarator
      return ValueType.NODE;

    case 207: // SimpleParenTypedefDeclarator
      return ValueType.NODE;

    case 208: // IdentifierDeclarator
      return ValueType.PASS_THROUGH;

    case 209: // IdentifierDeclaratorMain
      return ValueType.PASS_THROUGH;

    case 210: // UnaryIdentifierDeclarator
      return ValueType.PASS_THROUGH;

    case 211: // PostfixIdentifierDeclarator
      return ValueType.NODE;

    case 212: // ParenIdentifierDeclarator
      return ValueType.PASS_THROUGH;

    case 213: // SimpleDeclarator
      return ValueType.NODE;

    case 214: // OldFunctionDeclarator
      return ValueType.NODE;

    case 215: // PostfixOldFunctionDeclarator
      return ValueType.NODE;

    case 216: // AbstractDeclarator
      return ValueType.NODE;

    case 217: // PostfixingAbstractDeclarator
      return ValueType.NODE;

    case 218: // ParameterTypeListOpt
      return ValueType.NODE;

    case 219: // ArrayAbstractDeclarator
      return ValueType.NODE;

    case 220: // UnaryAbstractDeclarator
      return ValueType.NODE;

    case 221: // PostfixAbstractDeclarator
      return ValueType.NODE;

    case 222: // Statement
      return ValueType.PASS_THROUGH;

    case 223: // LabeledStatement
      return ValueType.NODE;

    case 224: // CompoundStatement
      return ValueType.NODE;

    case 225: // LocalLabelDeclarationListOpt
      return ValueType.NODE;

    case 226: // LocalLabelDeclarationList
      return ValueType.LIST;

    case 227: // LocalLabelDeclaration
      return ValueType.NODE;

    case 228: // LocalLabelList
      return ValueType.LIST;

    case 229: // DeclarationOrStatementList
      return ValueType.LIST;

    case 230: // DeclarationOrStatement
      return ValueType.PASS_THROUGH;

    case 231: // DeclarationList
      return ValueType.LIST;

    case 232: // ExpressionStatement
      return ValueType.NODE;

    case 233: // SelectionStatement
      return ValueType.NODE;

    case 234: // IterationStatement
      return ValueType.NODE;

    case 235: // JumpStatement
      return ValueType.NODE;

    case 236: // Constant
      return ValueType.NODE;

    case 237: // StringLiteralList
      return ValueType.LIST;

    case 238: // PrimaryExpression
      return ValueType.PASS_THROUGH;

    case 239: // PrimaryIdentifier
      return ValueType.NODE;

    case 240: // VariableArgumentAccess
      return ValueType.NODE;

    case 241: // StatementAsExpression
      return ValueType.NODE;

    case 242: // PostfixExpression
      return ValueType.PASS_THROUGH;

    case 243: // CompoundLiteral
      return ValueType.NODE;

    case 244: // ArgumentExpressionList
      return ValueType.LIST;

    case 245: // UnaryExpression
      return ValueType.PASS_THROUGH;

    case 246: // TypeCompatibilityExpression
      return ValueType.NODE;

    case 247: // OffsetofExpression
      return ValueType.NODE;

    case 248: // ExtensionExpression
      return ValueType.NODE;

    case 249: // AlignofExpression
      return ValueType.NODE;

    case 250: // Alignofkeyword
      return ValueType.NODE;

    case 251: // LabelAddressExpression
      return ValueType.NODE;

    case 252: // Unaryoperator
      return ValueType.NODE;

    case 253: // CastExpression
      return ValueType.PASS_THROUGH;

    case 254: // MultiplicativeExpression
      return ValueType.PASS_THROUGH;

    case 255: // AdditiveExpression
      return ValueType.PASS_THROUGH;

    case 256: // ShiftExpression
      return ValueType.PASS_THROUGH;

    case 257: // RelationalExpression
      return ValueType.PASS_THROUGH;

    case 258: // EqualityExpression
      return ValueType.PASS_THROUGH;

    case 259: // AndExpression
      return ValueType.PASS_THROUGH;

    case 260: // ExclusiveOrExpression
      return ValueType.PASS_THROUGH;

    case 261: // InclusiveOrExpression
      return ValueType.PASS_THROUGH;

    case 262: // LogicalAndExpression
      return ValueType.PASS_THROUGH;

    case 263: // LogicalORExpression
      return ValueType.PASS_THROUGH;

    case 264: // ConditionalExpression
      return ValueType.PASS_THROUGH;

    case 265: // AssignmentExpression
      return ValueType.PASS_THROUGH;

    case 266: // AssignmentOperator
      return ValueType.NODE;

    case 267: // Expression
      return ValueType.PASS_THROUGH;

    case 268: // ConstantExpression
      return ValueType.PASS_THROUGH;

    case 269: // ExpressionOpt
      return ValueType.PASS_THROUGH;

    case 270: // AttributeSpecifierListOpt
      return ValueType.NODE;

    case 271: // AttributeSpecifierList
      return ValueType.LIST;

    case 272: // AttributeSpecifier
      return ValueType.NODE;

    case 273: // AttributeKeyword
      return ValueType.NODE;

    case 274: // AttributeListOpt
      return ValueType.NODE;

    case 275: // AttributeList
      return ValueType.LIST;

    case 276: // AttributeExpressionOpt
      return ValueType.NODE;

    case 277: // Word
      return ValueType.NODE;

    case 278: // AssemblyDefinition
      return ValueType.NODE;

    case 279: // AssemblyExpression
      return ValueType.NODE;

    case 280: // AssemblyExpressionOpt
      return ValueType.NODE;

    case 281: // AssemblyStatement
      return ValueType.NODE;

    case 282: // Assemblyargument
      return ValueType.NODE;

    case 283: // AssemblyoperandsOpt
      return ValueType.NODE;

    case 284: // Assemblyoperands
      return ValueType.LIST;

    case 285: // Assemblyoperand
      return ValueType.NODE;

    case 286: // Assemblyclobbers
      return ValueType.NODE;

    case 287: // AsmKeyword
      return ValueType.NODE;

    case 288: // BindIdentifier
      return ValueType.ACTION;

    case 289: // BindIdentifierInList
      return ValueType.ACTION;

    case 290: // BindVar
      return ValueType.ACTION;

    case 291: // BindEnum
      return ValueType.ACTION;

    case 292: // EnterScope
      return ValueType.ACTION;

    case 293: // ExitScope
      return ValueType.ACTION;

    case 294: // ExitReentrantScope
      return ValueType.ACTION;

    case 295: // ReenterScope
      return ValueType.ACTION;

    case 296: // KillReentrantScope
      return ValueType.ACTION;

    default:
      throw new RuntimeException();
    }
  }

  public boolean isComplete(int id) {
    switch(id) {
    case 137: // Declaration
      return true;

    case 136: // DeclarationExtension
      return true;

    case 139: // DeclaringList
      return true;

    case 138: // DefaultDeclaringList
      return true;

    case 279: // AssemblyExpression
      return true;

    case 278: // AssemblyDefinition
      return true;

    case 283: // AssemblyoperandsOpt
      return true;

    case 129: // ExternalDeclarationList
      return true;

    case 282: // Assemblyargument
      return true;

    case 128: // TranslationUnit
      return true;

    case 281: // AssemblyStatement
      return true;

    case 131: // EmptyDefinition
      return true;

    case 280: // AssemblyExpressionOpt
      return true;

    case 130: // ExternalDeclaration
      return true;

    case 133: // FunctionDefinition
      return true;

    case 286: // Assemblyclobbers
      return true;

    case 132: // FunctionDefinitionExtension
      return true;

    case 285: // Assemblyoperand
      return true;

    case 135: // FunctionOldPrototype
      return true;

    case 284: // Assemblyoperands
      return true;

    case 134: // FunctionDeclarator
      return true;

    case 258: // EqualityExpression
      return true;

    case 259: // AndExpression
      return true;

    case 256: // ShiftExpression
      return true;

    case 257: // RelationalExpression
      return true;

    case 262: // LogicalAndExpression
      return true;

    case 263: // LogicalORExpression
      return true;

    case 260: // ExclusiveOrExpression
      return true;

    case 261: // InclusiveOrExpression
      return true;

    case 267: // Expression
      return true;

    case 264: // ConditionalExpression
      return true;

    case 265: // AssignmentExpression
      return true;

    case 268: // ConstantExpression
      return true;

    case 269: // ExpressionOpt
      return true;

    case 288: // BindIdentifier
      return true;

    case 186: // Identifier
      return true;

    case 289: // BindIdentifierInList
      return true;

    case 290: // BindVar
      return true;

    case 184: // ParameterDeclaration
      return true;

    case 291: // BindEnum
      return true;

    case 185: // IdentifierList
      return true;

    case 292: // EnterScope
      return true;

    case 190: // DesignatedInitializer
      return true;

    case 293: // ExitScope
      return true;

    case 294: // ExitReentrantScope
      return true;

    case 295: // ReenterScope
      return true;

    case 296: // KillReentrantScope
      return true;

    case 182: // ParameterTypeList
      return true;

    case 183: // ParameterList
      return true;

    case 199: // Declarator
      return true;

    case 193: // MatchedInitializerList
      return true;

    case 192: // InitializerList
      return true;

    case 222: // Statement
      return true;

    case 223: // LabeledStatement
      return true;

    case 238: // PrimaryExpression
      return true;

    case 237: // StringLiteralList
      return true;

    case 235: // JumpStatement
      return true;

    case 234: // IterationStatement
      return true;

    case 233: // SelectionStatement
      return true;

    case 232: // ExpressionStatement
      return true;

    case 231: // DeclarationList
      return true;

    case 230: // DeclarationOrStatement
      return true;

    case 229: // DeclarationOrStatementList
      return true;

    case 224: // CompoundStatement
      return true;

    case 254: // MultiplicativeExpression
      return true;

    case 255: // AdditiveExpression
      return true;

    case 253: // CastExpression
      return true;

    case 251: // LabelAddressExpression
      return true;

    case 248: // ExtensionExpression
      return true;

    case 249: // AlignofExpression
      return true;

    case 246: // TypeCompatibilityExpression
      return true;

    case 247: // OffsetofExpression
      return true;

    case 244: // ArgumentExpressionList
      return true;

    case 245: // UnaryExpression
      return true;

    case 242: // PostfixExpression
      return true;

    case 243: // CompoundLiteral
      return true;

    case 240: // VariableArgumentAccess
      return true;

    case 241: // StatementAsExpression
      return true;

    default:
      return false;
    }
  }

  public void dispatch(int id, Subparser subparser) {
    switch(id) {
    case 288:
      BindIdentifier(subparser);
      break;

    case 289:
      BindIdentifierInList(subparser);
      break;

    case 290:
      BindVar(subparser);
      break;

    case 291:
      BindEnum(subparser);
      break;

    case 292:
      EnterScope(subparser);
      break;

    case 293:
      ExitScope(subparser);
      break;

    case 294:
      ExitReentrantScope(subparser);
      break;

    case 295:
      ReenterScope(subparser);
      break;

    case 296:
      KillReentrantScope(subparser);
      break;

    default:
      // Do nothing
      break;
    }
  }

  public abstract void BindIdentifier(Subparser subparser);

  public abstract void BindIdentifierInList(Subparser subparser);

  public abstract void BindVar(Subparser subparser);

  public abstract void BindEnum(Subparser subparser);

  public abstract void EnterScope(Subparser subparser);

  public abstract void ExitScope(Subparser subparser);

  public abstract void ExitReentrantScope(Subparser subparser);

  public abstract void ReenterScope(Subparser subparser);

  public abstract void KillReentrantScope(Subparser subparser);

}
