
/* A Bison parser, made by GNU Bison 2.4.1.  */

/* Skeleton implementation for Bison's Yacc-like parsers in C
   
      Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.
   
   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output.  */
#define YYBISON 1

/* Bison version.  */
#define YYBISON_VERSION "2.4.1"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 0

/* Push parsers.  */
#define YYPUSH 0

/* Pull parsers.  */
#define YYPULL 1

/* Using locations.  */
#define YYLSP_NEEDED 0



/* Copy the first part of user declarations.  */


/* Line 189 of yacc.c  */
#line 73 "c.tab.c"

/* Enabling traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif

/* Enabling verbose error messages.  */
#ifdef YYERROR_VERBOSE
# undef YYERROR_VERBOSE
# define YYERROR_VERBOSE 1
#else
# define YYERROR_VERBOSE 0
#endif

/* Enabling the token table.  */
#ifndef YYTOKEN_TABLE
# define YYTOKEN_TABLE 0
#endif


/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     AUTO = 258,
     DOUBLE = 259,
     INT = 260,
     STRUCT = 261,
     BREAK = 262,
     ELSE = 263,
     LONG = 264,
     SWITCH = 265,
     CASE = 266,
     ENUM = 267,
     REGISTER = 268,
     TYPEDEF = 269,
     CHAR = 270,
     EXTERN = 271,
     RETURN = 272,
     UNION = 273,
     CONST = 274,
     FLOAT = 275,
     SHORT = 276,
     UNSIGNED = 277,
     CONTINUE = 278,
     FOR = 279,
     SIGNED = 280,
     VOID = 281,
     DEFAULT = 282,
     GOTO = 283,
     SIZEOF = 284,
     VOLATILE = 285,
     DO = 286,
     IF = 287,
     STATIC = 288,
     WHILE = 289,
     IDENTIFIER = 290,
     STRINGliteral = 291,
     FLOATINGconstant = 292,
     INTEGERconstant = 293,
     CHARACTERconstant = 294,
     OCTALconstant = 295,
     HEXconstant = 296,
     TYPEDEFname = 297,
     ARROW = 298,
     ICR = 299,
     DECR = 300,
     LS = 301,
     RS = 302,
     LE = 303,
     GE = 304,
     EQ = 305,
     NE = 306,
     ANDAND = 307,
     OROR = 308,
     ELLIPSIS = 309,
     MULTassign = 310,
     DIVassign = 311,
     MODassign = 312,
     PLUSassign = 313,
     MINUSassign = 314,
     LSassign = 315,
     RSassign = 316,
     ANDassign = 317,
     ERassign = 318,
     ORassign = 319,
     LPAREN = 320,
     RPAREN = 321,
     COMMA = 322,
     HASH = 323,
     DHASH = 324,
     LBRACE = 325,
     RBRACE = 326,
     LBRACK = 327,
     RBRACK = 328,
     DOT = 329,
     AND = 330,
     STAR = 331,
     PLUS = 332,
     MINUS = 333,
     NEGATE = 334,
     NOT = 335,
     DIV = 336,
     MOD = 337,
     LT = 338,
     GT = 339,
     XOR = 340,
     PIPE = 341,
     QUESTION = 342,
     COLON = 343,
     SEMICOLON = 344,
     ASSIGN = 345,
     ASMSYM = 346,
     _BOOL = 347,
     _COMPLEX = 348,
     RESTRICT = 349,
     __ALIGNOF = 350,
     __ALIGNOF__ = 351,
     ASM = 352,
     __ASM = 353,
     __ASM__ = 354,
     AT = 355,
     USD = 356,
     __ATTRIBUTE = 357,
     __ATTRIBUTE__ = 358,
     __BUILTIN_OFFSETOF = 359,
     __BUILTIN_TYPES_COMPATIBLE_P = 360,
     __BUILTIN_VA_ARG = 361,
     __BUILTIN_VA_LIST = 362,
     __COMPLEX__ = 363,
     __CONST = 364,
     __CONST__ = 365,
     __EXTENSION__ = 366,
     INLINE = 367,
     __INLINE = 368,
     __INLINE__ = 369,
     __LABEL__ = 370,
     __RESTRICT = 371,
     __RESTRICT__ = 372,
     __SIGNED = 373,
     __SIGNED__ = 374,
     __THREAD = 375,
     TYPEOF = 376,
     __TYPEOF = 377,
     __TYPEOF__ = 378,
     __VOLATILE = 379,
     __VOLATILE__ = 380,
     PPNUM = 381
   };
#endif



#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif


/* Copy the second part of user declarations.  */


/* Line 264 of yacc.c  */
#line 241 "c.tab.c"

#ifdef short
# undef short
#endif

#ifdef YYTYPE_UINT8
typedef YYTYPE_UINT8 yytype_uint8;
#else
typedef unsigned char yytype_uint8;
#endif

#ifdef YYTYPE_INT8
typedef YYTYPE_INT8 yytype_int8;
#elif (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
typedef signed char yytype_int8;
#else
typedef short int yytype_int8;
#endif

#ifdef YYTYPE_UINT16
typedef YYTYPE_UINT16 yytype_uint16;
#else
typedef unsigned short int yytype_uint16;
#endif

#ifdef YYTYPE_INT16
typedef YYTYPE_INT16 yytype_int16;
#else
typedef short int yytype_int16;
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif ! defined YYSIZE_T && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned int
# endif
#endif

#define YYSIZE_MAXIMUM ((YYSIZE_T) -1)

#ifndef YY_
# if YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(msgid) dgettext ("bison-runtime", msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(msgid) msgid
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YYUSE(e) ((void) (e))
#else
# define YYUSE(e) /* empty */
#endif

/* Identity function, used to suppress warnings about constant conditions.  */
#ifndef lint
# define YYID(n) (n)
#else
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static int
YYID (int yyi)
#else
static int
YYID (yyi)
    int yyi;
#endif
{
  return yyi;
}
#endif

#if ! defined yyoverflow || YYERROR_VERBOSE

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#     ifndef _STDLIB_H
#      define _STDLIB_H 1
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's `empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (YYID (0))
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined _STDLIB_H \
       && ! ((defined YYMALLOC || defined malloc) \
	     && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef _STDLIB_H
#    define _STDLIB_H 1
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* ! defined yyoverflow || YYERROR_VERBOSE */


#if (! defined yyoverflow \
     && (! defined __cplusplus \
	 || (defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yytype_int16 yyss_alloc;
  YYSTYPE yyvs_alloc;
};

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (sizeof (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (sizeof (yytype_int16) + sizeof (YYSTYPE)) \
      + YYSTACK_GAP_MAXIMUM)

/* Copy COUNT objects from FROM to TO.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(To, From, Count) \
      __builtin_memcpy (To, From, (Count) * sizeof (*(From)))
#  else
#   define YYCOPY(To, From, Count)		\
      do					\
	{					\
	  YYSIZE_T yyi;				\
	  for (yyi = 0; yyi < (Count); yyi++)	\
	    (To)[yyi] = (From)[yyi];		\
	}					\
      while (YYID (0))
#  endif
# endif

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack_alloc, Stack)				\
    do									\
      {									\
	YYSIZE_T yynewbytes;						\
	YYCOPY (&yyptr->Stack_alloc, Stack, yysize);			\
	Stack = &yyptr->Stack_alloc;					\
	yynewbytes = yystacksize * sizeof (*Stack) + YYSTACK_GAP_MAXIMUM; \
	yyptr += yynewbytes / sizeof (*yyptr);				\
      }									\
    while (YYID (0))

#endif

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  3
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   4653

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  127
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  170
/* YYNRULES -- Number of rules.  */
#define YYNRULES  541
/* YYNRULES -- Number of states.  */
#define YYNSTATES  887

/* YYTRANSLATE(YYLEX) -- Bison symbol number corresponding to YYLEX.  */
#define YYUNDEFTOK  2
#define YYMAXUTOK   381

#define YYTRANSLATE(YYX)						\
  ((unsigned int) (YYX) <= YYMAXUTOK ? yytranslate[YYX] : YYUNDEFTOK)

/* YYTRANSLATE[YYLEX] -- Bison symbol number corresponding to YYLEX.  */
static const yytype_uint8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
      35,    36,    37,    38,    39,    40,    41,    42,    43,    44,
      45,    46,    47,    48,    49,    50,    51,    52,    53,    54,
      55,    56,    57,    58,    59,    60,    61,    62,    63,    64,
      65,    66,    67,    68,    69,    70,    71,    72,    73,    74,
      75,    76,    77,    78,    79,    80,    81,    82,    83,    84,
      85,    86,    87,    88,    89,    90,    91,    92,    93,    94,
      95,    96,    97,    98,    99,   100,   101,   102,   103,   104,
     105,   106,   107,   108,   109,   110,   111,   112,   113,   114,
     115,   116,   117,   118,   119,   120,   121,   122,   123,   124,
     125,   126
};

#if YYDEBUG
/* YYPRHS[YYN] -- Index of the first RHS symbol of rule number YYN in
   YYRHS.  */
static const yytype_uint16 yyprhs[] =
{
       0,     0,     3,     5,     6,     9,    11,    13,    15,    17,
      19,    21,    24,    32,    41,    44,    48,    52,    56,    60,
      63,    67,    71,    75,    79,    82,    86,    90,    94,    98,
     100,   103,   107,   111,   115,   119,   126,   133,   142,   149,
     156,   165,   167,   169,   171,   173,   175,   177,   179,   181,
     183,   185,   187,   190,   193,   195,   198,   200,   202,   204,
     206,   208,   210,   212,   214,   216,   218,   220,   222,   224,
     226,   228,   230,   232,   234,   236,   239,   242,   245,   248,
     250,   253,   256,   259,   262,   265,   268,   270,   273,   276,
     279,   282,   285,   287,   290,   293,   296,   299,   302,   305,
     307,   310,   313,   316,   321,   326,   328,   330,   332,   335,
     338,   341,   344,   346,   349,   352,   355,   357,   359,   361,
     363,   365,   367,   369,   371,   373,   375,   377,   379,   381,
     383,   385,   387,   389,   391,   393,   395,   397,   399,   401,
     403,   410,   418,   421,   429,   438,   442,   444,   446,   447,
     450,   453,   456,   459,   462,   464,   468,   473,   477,   482,
     485,   487,   490,   492,   493,   495,   498,   503,   509,   512,
     518,   525,   531,   538,   542,   549,   557,   559,   563,   567,
     571,   572,   575,   577,   581,   583,   587,   589,   592,   597,
     602,   604,   607,   612,   614,   617,   622,   627,   629,   632,
     637,   639,   643,   646,   648,   650,   652,   655,   657,   660,
     661,   664,   666,   669,   673,   678,   680,   682,   685,   686,
     690,   693,   695,   697,   699,   702,   706,   712,   715,   718,
     722,   728,   731,   733,   735,   737,   739,   741,   743,   746,
     748,   750,   753,   757,   761,   766,   768,   773,   779,   782,
     786,   790,   795,   800,   802,   806,   808,   810,   812,   814,
     817,   821,   824,   828,   833,   835,   839,   841,   843,   846,
     850,   857,   861,   866,   868,   870,   872,   874,   880,   881,
     883,   886,   890,   895,   897,   900,   903,   907,   911,   915,
     919,   924,   926,   928,   930,   932,   934,   936,   938,   943,
     948,   955,   959,   964,   965,   967,   969,   972,   976,   978,
     982,   983,   986,   988,   990,   992,   995,   998,  1004,  1012,
    1018,  1024,  1032,  1042,  1046,  1051,  1054,  1057,  1061,  1063,
    1065,  1067,  1069,  1071,  1073,  1076,  1078,  1080,  1082,  1086,
    1088,  1090,  1092,  1099,  1105,  1107,  1112,  1116,  1121,  1125,
    1129,  1132,  1135,  1137,  1144,  1146,  1150,  1152,  1155,  1158,
    1161,  1164,  1169,  1171,  1173,  1175,  1177,  1179,  1186,  1193,
    1196,  1201,  1204,  1206,  1208,  1211,  1213,  1215,  1217,  1219,
    1221,  1223,  1225,  1230,  1232,  1236,  1240,  1244,  1246,  1250,
    1254,  1256,  1260,  1264,  1266,  1270,  1274,  1278,  1282,  1284,
    1288,  1292,  1294,  1298,  1300,  1304,  1306,  1310,  1312,  1316,
    1318,  1322,  1324,  1330,  1335,  1337,  1341,  1343,  1345,  1347,
    1349,  1351,  1353,  1355,  1357,  1359,  1361,  1363,  1365,  1369,
    1371,  1372,  1374,  1375,  1377,  1379,  1382,  1389,  1391,  1393,
    1394,  1396,  1399,  1404,  1405,  1408,  1412,  1414,  1416,  1418,
    1420,  1422,  1424,  1426,  1428,  1430,  1432,  1434,  1436,  1438,
    1440,  1442,  1444,  1446,  1448,  1450,  1452,  1454,  1456,  1458,
    1460,  1462,  1464,  1466,  1468,  1470,  1472,  1474,  1476,  1478,
    1480,  1482,  1484,  1486,  1488,  1490,  1492,  1494,  1496,  1498,
    1500,  1502,  1504,  1506,  1508,  1510,  1512,  1514,  1516,  1518,
    1520,  1522,  1524,  1526,  1528,  1530,  1532,  1534,  1536,  1538,
    1540,  1542,  1544,  1547,  1552,  1553,  1555,  1561,  1568,  1576,
    1582,  1586,  1588,  1589,  1591,  1593,  1597,  1602,  1610,  1612,
    1616,  1618,  1620,  1622,  1623,  1624,  1625,  1626,  1627,  1628,
    1629,  1630
};

/* YYRHS -- A `-1'-separated list of the rules' RHS.  */
static const yytype_int16 yyrhs[] =
{
     128,     0,    -1,   129,    -1,    -1,   129,   130,    -1,   132,
      -1,   136,    -1,   278,    -1,   131,    -1,    89,    -1,   133,
      -1,   111,   133,    -1,   134,   295,    70,   225,   229,   293,
      71,    -1,   135,   295,   231,    70,   225,   229,   293,    71,
      -1,   208,   290,    -1,   140,   208,   288,    -1,   141,   208,
     288,    -1,   142,   208,   288,    -1,   143,   208,   288,    -1,
     214,   290,    -1,   140,   214,   288,    -1,   141,   214,   288,
      -1,   142,   214,   288,    -1,   143,   214,   288,    -1,   214,
     290,    -1,   140,   214,   288,    -1,   141,   214,   288,    -1,
     142,   214,   288,    -1,   143,   214,   288,    -1,   137,    -1,
     111,   137,    -1,   152,   296,    89,    -1,   153,   296,    89,
      -1,   139,   296,    89,    -1,   138,   296,    89,    -1,   142,
     208,   288,   280,   270,   189,    -1,   143,   208,   288,   280,
     270,   189,    -1,   138,    67,   270,   208,   289,   280,   270,
     189,    -1,   140,   199,   288,   280,   270,   189,    -1,   141,
     199,   288,   280,   270,   189,    -1,   139,    67,   270,   199,
     289,   280,   270,   189,    -1,   150,    -1,   152,    -1,   154,
      -1,   160,    -1,   156,    -1,   151,    -1,   153,    -1,   155,
      -1,   161,    -1,   157,    -1,   163,    -1,   143,   163,    -1,
     142,   144,    -1,   145,    -1,   143,   145,    -1,   145,    -1,
     163,    -1,   146,    -1,   147,    -1,   148,    -1,   272,    -1,
     149,    -1,    19,    -1,   109,    -1,   110,    -1,    30,    -1,
     124,    -1,   125,    -1,    94,    -1,   116,    -1,   117,    -1,
     112,    -1,   113,    -1,   114,    -1,   151,   163,    -1,   142,
     164,    -1,   150,   144,    -1,   150,   164,    -1,   164,    -1,
     143,   164,    -1,   151,   145,    -1,   151,   164,    -1,   153,
     163,    -1,   142,   167,    -1,   152,   144,    -1,   167,    -1,
     143,   167,    -1,   153,   145,    -1,   155,   163,    -1,   142,
      42,    -1,   154,   144,    -1,    42,    -1,   143,    42,    -1,
     155,   145,    -1,   157,   163,    -1,   142,   158,    -1,   156,
     144,    -1,   156,   158,    -1,   158,    -1,   143,   158,    -1,
     157,   145,    -1,   157,   158,    -1,   159,    65,   188,    66,
      -1,   159,    65,   267,    66,    -1,   121,    -1,   122,    -1,
     123,    -1,   161,   163,    -1,   142,   162,    -1,   160,   144,
      -1,   160,   162,    -1,   162,    -1,   143,   162,    -1,   161,
     145,    -1,   161,   162,    -1,   107,    -1,    14,    -1,    16,
      -1,    33,    -1,     3,    -1,    13,    -1,    26,    -1,    15,
      -1,    21,    -1,     5,    -1,     9,    -1,    20,    -1,     4,
      -1,   165,    -1,    22,    -1,    92,    -1,   166,    -1,    25,
      -1,   118,    -1,   119,    -1,    93,    -1,   108,    -1,   168,
      -1,   178,    -1,   169,   292,    70,   170,   293,    71,    -1,
     169,   187,   292,    70,   170,   293,    71,    -1,   169,   187,
      -1,   169,   271,   292,    70,   170,   293,    71,    -1,   169,
     271,   187,   292,    70,   170,   293,    71,    -1,   169,   271,
     187,    -1,     6,    -1,    18,    -1,    -1,   170,   171,    -1,
     173,    89,    -1,   172,    89,    -1,   143,    89,    -1,   141,
      89,    -1,    89,    -1,   143,   175,   270,    -1,   172,    67,
     175,   270,    -1,   141,   174,   270,    -1,   173,    67,   174,
     270,    -1,   199,   176,    -1,   177,    -1,   208,   176,    -1,
     177,    -1,    -1,   177,    -1,    88,   268,    -1,    12,    70,
     179,    71,    -1,    12,   187,    70,   179,    71,    -1,    12,
     187,    -1,    12,    70,   179,    67,    71,    -1,    12,   187,
      70,   179,    67,    71,    -1,    12,   271,    70,   179,    71,
      -1,    12,   271,   187,    70,   179,    71,    -1,    12,   271,
     187,    -1,    12,   271,    70,   179,    67,    71,    -1,    12,
     271,   187,    70,   179,    67,    71,    -1,   180,    -1,   179,
      67,   180,    -1,    35,   291,   181,    -1,    42,   291,   181,
      -1,    -1,    90,   268,    -1,   183,    -1,   183,    67,    54,
      -1,   184,    -1,   183,    67,   184,    -1,   140,    -1,   140,
     216,    -1,   140,   208,   288,   270,    -1,   140,   202,   288,
     270,    -1,   142,    -1,   142,   216,    -1,   142,   208,   288,
     270,    -1,   141,    -1,   141,   216,    -1,   141,   208,   288,
     270,    -1,   141,   202,   288,   270,    -1,   143,    -1,   143,
     216,    -1,   143,   208,   288,   270,    -1,   186,    -1,   185,
      67,   186,    -1,    35,   290,    -1,    35,    -1,    42,    -1,
     141,    -1,   141,   216,    -1,   143,    -1,   143,   216,    -1,
      -1,    90,   190,    -1,   191,    -1,   194,   191,    -1,    70,
     193,    71,    -1,    70,   193,   190,    71,    -1,   265,    -1,
     193,    -1,   193,   190,    -1,    -1,   193,   190,    67,    -1,
     195,    90,    -1,   197,    -1,   198,    -1,   196,    -1,   195,
     196,    -1,    72,   268,    73,    -1,    72,   268,    54,   268,
      73,    -1,    74,    35,    -1,    74,    42,    -1,    72,   268,
      73,    -1,    72,   268,    54,   268,    73,    -1,    35,    88,
      -1,   200,    -1,   208,    -1,   201,    -1,   205,    -1,   202,
      -1,    42,    -1,    42,   217,    -1,   203,    -1,   204,    -1,
      76,   202,    -1,    76,   143,   202,    -1,    65,   203,    66,
      -1,    65,   203,    66,   217,    -1,   206,    -1,    76,    65,
     207,    66,    -1,    76,   143,    65,   207,    66,    -1,    76,
     205,    -1,    76,   143,   205,    -1,    65,   205,    66,    -1,
      65,   207,   217,    66,    -1,    65,   205,    66,   217,    -1,
      42,    -1,    65,   207,    66,    -1,   209,    -1,   210,    -1,
     212,    -1,   211,    -1,    76,   208,    -1,    76,   143,   208,
      -1,   212,   217,    -1,    65,   210,    66,    -1,    65,   210,
      66,   217,    -1,   213,    -1,    65,   212,    66,    -1,    35,
      -1,   215,    -1,    76,   214,    -1,    76,   143,   214,    -1,
     212,    65,   292,   185,   294,    66,    -1,    65,   214,    66,
      -1,    65,   214,    66,   217,    -1,   220,    -1,   221,    -1,
     217,    -1,   219,    -1,    65,   292,   218,   294,    66,    -1,
      -1,   182,    -1,    72,    73,    -1,    72,   268,    73,    -1,
     219,    72,   268,    73,    -1,    76,    -1,    76,   143,    -1,
      76,   216,    -1,    76,   143,   216,    -1,    65,   220,    66,
      -1,    65,   221,    66,    -1,    65,   217,    66,    -1,    65,
     220,    66,   217,    -1,   223,    -1,   224,    -1,   232,    -1,
     233,    -1,   234,    -1,   235,    -1,   281,    -1,   187,    88,
     270,   222,    -1,    11,   268,    88,   222,    -1,    11,   268,
      54,   268,    88,   222,    -1,    27,    88,   222,    -1,    70,
     225,   229,    71,    -1,    -1,   226,    -1,   227,    -1,   226,
     227,    -1,   115,   228,    89,    -1,    35,    -1,   228,    67,
      35,    -1,    -1,   229,   230,    -1,   136,    -1,   222,    -1,
     136,    -1,   231,   136,    -1,   269,    89,    -1,    32,    65,
     267,    66,   222,    -1,    32,    65,   267,    66,   222,     8,
     222,    -1,    10,    65,   267,    66,   222,    -1,    34,    65,
     267,    66,   222,    -1,    31,   222,    34,    65,   267,    66,
      89,    -1,    24,    65,   269,    89,   269,    89,   269,    66,
     222,    -1,    28,   187,    89,    -1,    28,    76,   267,    89,
      -1,    23,    89,    -1,     7,    89,    -1,    17,   269,    89,
      -1,    37,    -1,    38,    -1,    40,    -1,    41,    -1,    39,
      -1,    36,    -1,   237,    36,    -1,   239,    -1,   236,    -1,
     237,    -1,    65,   267,    66,    -1,   241,    -1,   240,    -1,
      35,    -1,   106,    65,   265,    67,   188,    66,    -1,    65,
     292,   224,   293,    66,    -1,   238,    -1,   242,    72,   267,
      73,    -1,   242,    65,    66,    -1,   242,    65,   244,    66,
      -1,   242,    74,   187,    -1,   242,    43,   187,    -1,   242,
      44,    -1,   242,    45,    -1,   243,    -1,    65,   188,    66,
      70,   192,    71,    -1,   265,    -1,   244,    67,   265,    -1,
     242,    -1,    44,   245,    -1,    45,   245,    -1,   252,   253,
      -1,    29,   245,    -1,    29,    65,   188,    66,    -1,   251,
      -1,   249,    -1,   248,    -1,   247,    -1,   246,    -1,   105,
      65,   188,    67,   188,    66,    -1,   104,    65,   188,    67,
     242,    66,    -1,   111,   253,    -1,   250,    65,   188,    66,
      -1,   250,   245,    -1,    96,    -1,    95,    -1,    52,    35,
      -1,    75,    -1,    76,    -1,    77,    -1,    78,    -1,    79,
      -1,    80,    -1,   245,    -1,    65,   188,    66,   253,    -1,
     253,    -1,   254,    76,   253,    -1,   254,    81,   253,    -1,
     254,    82,   253,    -1,   254,    -1,   255,    77,   254,    -1,
     255,    78,   254,    -1,   255,    -1,   256,    46,   255,    -1,
     256,    47,   255,    -1,   256,    -1,   257,    83,   256,    -1,
     257,    84,   256,    -1,   257,    48,   256,    -1,   257,    49,
     256,    -1,   257,    -1,   258,    50,   257,    -1,   258,    51,
     257,    -1,   258,    -1,   259,    75,   258,    -1,   259,    -1,
     260,    85,   259,    -1,   260,    -1,   261,    86,   260,    -1,
     261,    -1,   262,    52,   261,    -1,   262,    -1,   263,    53,
     262,    -1,   263,    -1,   263,    87,   267,    88,   264,    -1,
     263,    87,    88,   264,    -1,   264,    -1,   245,   266,   265,
      -1,    90,    -1,    55,    -1,    56,    -1,    57,    -1,    58,
      -1,    59,    -1,    60,    -1,    61,    -1,    62,    -1,    63,
      -1,    64,    -1,   265,    -1,   267,    67,   265,    -1,   264,
      -1,    -1,   267,    -1,    -1,   271,    -1,   272,    -1,   271,
     272,    -1,   273,    65,    65,   274,    66,    66,    -1,   102,
      -1,   103,    -1,    -1,   275,    -1,   277,   276,    -1,   275,
      67,   277,   276,    -1,    -1,    65,    66,    -1,    65,   244,
      66,    -1,    35,    -1,     3,    -1,     4,    -1,     5,    -1,
       6,    -1,     7,    -1,     8,    -1,     9,    -1,    10,    -1,
      11,    -1,    12,    -1,    13,    -1,    14,    -1,    15,    -1,
      16,    -1,    17,    -1,    18,    -1,    19,    -1,    20,    -1,
      21,    -1,    22,    -1,    23,    -1,    24,    -1,    25,    -1,
      26,    -1,    27,    -1,    28,    -1,    29,    -1,    30,    -1,
      31,    -1,    32,    -1,    33,    -1,    34,    -1,    91,    -1,
      92,    -1,    93,    -1,    94,    -1,    95,    -1,    96,    -1,
      97,    -1,    98,    -1,    99,    -1,   102,    -1,   103,    -1,
     104,    -1,   105,    -1,   106,    -1,   107,    -1,   108,    -1,
     109,    -1,   110,    -1,   111,    -1,   112,    -1,   113,    -1,
     114,    -1,   115,    -1,   116,    -1,   117,    -1,   118,    -1,
     119,    -1,   120,    -1,   121,    -1,   122,    -1,   123,    -1,
     124,    -1,   125,    -1,   279,    89,    -1,   287,    65,   237,
      66,    -1,    -1,   279,    -1,   287,    65,   282,    66,    89,
      -1,   287,   145,    65,   282,    66,    89,    -1,   237,    88,
     283,    88,   283,    88,   286,    -1,   237,    88,   283,    88,
     283,    -1,   237,    88,   283,    -1,   237,    -1,    -1,   284,
      -1,   285,    -1,   284,    67,   285,    -1,   237,    65,   267,
      66,    -1,    72,   277,    73,   237,    65,   267,    66,    -1,
     237,    -1,   286,    67,   237,    -1,    97,    -1,    98,    -1,
      99,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1
};

/* YYRLINE[YYN] -- source line where rule number YYN was defined.  */
static const yytype_uint16 yyrline[] =
{
       0,   176,   176,   181,   183,   187,   188,   189,   190,   194,
     198,   199,   203,   204,   208,   209,   210,   211,   212,   214,
     215,   216,   217,   218,   222,   223,   224,   225,   226,   262,
     263,   267,   268,   269,   270,   277,   278,   279,   283,   284,
     285,   289,   290,   291,   292,   293,   297,   298,   299,   300,
     301,   305,   306,   307,   311,   312,   316,   317,   321,   322,
     323,   324,   325,   329,   330,   331,   335,   336,   337,   341,
     342,   343,   347,   348,   349,   353,   354,   355,   356,   360,
     361,   362,   363,   367,   368,   369,   373,   374,   375,   380,
     381,   382,   386,   387,   388,   392,   393,   394,   395,   399,
     400,   401,   402,   406,   407,   411,   412,   413,   417,   418,
     419,   420,   424,   425,   426,   427,   431,   435,   436,   437,
     438,   439,   443,   444,   445,   446,   447,   448,   449,   450,
     451,   452,   453,   457,   458,   459,   463,   464,   468,   469,
     473,   474,   476,   477,   478,   480,   484,   485,   488,   490,
     494,   495,   496,   497,   498,   502,   503,   507,   508,   513,
     514,   518,   519,   522,   524,   528,   532,   533,   534,   535,
     536,   537,   538,   539,   540,   541,   550,   551,   555,   556,
     559,   561,   565,   566,   570,   571,   575,   576,   577,   578,
     579,   580,   581,   582,   583,   584,   585,   586,   587,   588,
     596,   597,   601,   605,   606,   610,   611,   612,   613,   616,
     618,   622,   623,   632,   633,   634,   638,   639,   642,   643,
     647,   648,   649,   653,   654,   658,   659,   660,   661,   665,
     666,   670,   674,   675,   679,   683,   684,   688,   689,   690,
     697,   698,   699,   703,   704,   711,   712,   713,   715,   716,
     720,   721,   722,   726,   727,   731,   735,   736,   740,   741,
     742,   746,   747,   748,   752,   753,   757,   761,   762,   763,
     767,   768,   769,   773,   774,   775,   779,   780,   782,   784,
     788,   789,   790,   794,   795,   796,   797,   801,   802,   803,
     804,   810,   811,   812,   813,   814,   815,   816,   820,   821,
     822,   823,   834,   837,   839,   843,   844,   848,   852,   853,
     856,   857,   861,   862,   866,   867,   876,   880,   881,   882,
     886,   887,   888,   893,   894,   895,   896,   897,   904,   905,
     909,   910,   911,   916,   917,   923,   924,   925,   926,   927,
     928,   932,   936,   940,   943,   944,   945,   946,   947,   948,
     949,   950,   951,   955,   959,   960,   964,   965,   966,   967,
     968,   969,   970,   971,   972,   973,   974,   978,   982,   986,
     990,   991,   995,   996,  1000,  1004,  1005,  1006,  1007,  1008,
    1009,  1013,  1014,  1018,  1019,  1020,  1021,  1025,  1026,  1027,
    1031,  1032,  1033,  1037,  1038,  1039,  1040,  1041,  1045,  1046,
    1047,  1051,  1052,  1056,  1057,  1061,  1062,  1066,  1067,  1071,
    1072,  1076,  1077,  1079,  1084,  1085,  1089,  1090,  1091,  1092,
    1093,  1094,  1095,  1096,  1097,  1098,  1099,  1103,  1104,  1108,
    1112,  1114,  1117,  1119,  1123,  1124,  1128,  1132,  1133,  1136,
    1138,  1142,  1143,  1146,  1148,  1149,  1153,  1154,  1155,  1156,
    1157,  1158,  1159,  1160,  1161,  1162,  1163,  1164,  1165,  1166,
    1167,  1168,  1169,  1170,  1171,  1172,  1173,  1174,  1175,  1176,
    1177,  1178,  1179,  1180,  1181,  1182,  1183,  1184,  1185,  1186,
    1187,  1188,  1189,  1190,  1191,  1192,  1193,  1194,  1195,  1196,
    1197,  1198,  1199,  1200,  1201,  1202,  1203,  1204,  1205,  1206,
    1207,  1208,  1209,  1210,  1211,  1212,  1213,  1214,  1215,  1216,
    1217,  1218,  1224,  1228,  1231,  1233,  1237,  1238,  1242,  1243,
    1244,  1245,  1248,  1250,  1254,  1255,  1259,  1260,  1264,  1265,
    1269,  1270,  1271,  1280,  1283,  1286,  1289,  1292,  1295,  1298,
    1301,  1304
};
#endif

#if YYDEBUG || YYERROR_VERBOSE || YYTOKEN_TABLE
/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "$end", "error", "$undefined", "AUTO", "DOUBLE", "INT", "STRUCT",
  "BREAK", "ELSE", "LONG", "SWITCH", "CASE", "ENUM", "REGISTER", "TYPEDEF",
  "CHAR", "EXTERN", "RETURN", "UNION", "CONST", "FLOAT", "SHORT",
  "UNSIGNED", "CONTINUE", "FOR", "SIGNED", "VOID", "DEFAULT", "GOTO",
  "SIZEOF", "VOLATILE", "DO", "IF", "STATIC", "WHILE", "IDENTIFIER",
  "STRINGliteral", "FLOATINGconstant", "INTEGERconstant",
  "CHARACTERconstant", "OCTALconstant", "HEXconstant", "TYPEDEFname",
  "ARROW", "ICR", "DECR", "LS", "RS", "LE", "GE", "EQ", "NE", "ANDAND",
  "OROR", "ELLIPSIS", "MULTassign", "DIVassign", "MODassign", "PLUSassign",
  "MINUSassign", "LSassign", "RSassign", "ANDassign", "ERassign",
  "ORassign", "LPAREN", "RPAREN", "COMMA", "HASH", "DHASH", "LBRACE",
  "RBRACE", "LBRACK", "RBRACK", "DOT", "AND", "STAR", "PLUS", "MINUS",
  "NEGATE", "NOT", "DIV", "MOD", "LT", "GT", "XOR", "PIPE", "QUESTION",
  "COLON", "SEMICOLON", "ASSIGN", "ASMSYM", "_BOOL", "_COMPLEX",
  "RESTRICT", "__ALIGNOF", "__ALIGNOF__", "ASM", "__ASM", "__ASM__", "AT",
  "USD", "__ATTRIBUTE", "__ATTRIBUTE__", "__BUILTIN_OFFSETOF",
  "__BUILTIN_TYPES_COMPATIBLE_P", "__BUILTIN_VA_ARG", "__BUILTIN_VA_LIST",
  "__COMPLEX__", "__CONST", "__CONST__", "__EXTENSION__", "INLINE",
  "__INLINE", "__INLINE__", "__LABEL__", "__RESTRICT", "__RESTRICT__",
  "__SIGNED", "__SIGNED__", "__THREAD", "TYPEOF", "__TYPEOF", "__TYPEOF__",
  "__VOLATILE", "__VOLATILE__", "PPNUM", "$accept", "TranslationUnit",
  "ExternalDeclarationList", "ExternalDeclaration", "EmptyDefinition",
  "FunctionDefinitionExtension", "FunctionDefinition",
  "FunctionDeclarator", "FunctionOldPrototype", "DeclarationExtension",
  "Declaration", "DefaultDeclaringList", "DeclaringList",
  "DeclarationSpecifier", "TypeSpecifier", "DeclarationQualifierList",
  "TypeQualifierList", "DeclarationQualifier", "TypeQualifier",
  "ConstQualifier", "VolatileQualifier", "RestrictQualifier",
  "FunctionSpecifier", "BasicDeclarationSpecifier", "BasicTypeSpecifier",
  "SUEDeclarationSpecifier", "SUETypeSpecifier",
  "TypedefDeclarationSpecifier", "TypedefTypeSpecifier",
  "TypeofDeclarationSpecifier", "TypeofTypeSpecifier", "Typeofspecifier",
  "Typeofkeyword", "VarArgDeclarationSpecifier", "VarArgTypeSpecifier",
  "VarArgTypeName", "StorageClass", "BasicTypeName", "SignedKeyword",
  "ComplexKeyword", "ElaboratedTypeName", "StructOrUnionSpecifier",
  "StructOrUnion", "StructDeclarationList", "StructDeclaration",
  "StructDefaultDeclaringList", "StructDeclaringList", "StructDeclarator",
  "StructIdentifierDeclarator", "BitFieldSizeOpt", "BitFieldSize",
  "EnumSpecifier", "EnumeratorList", "Enumerator", "EnumeratorValueOpt",
  "ParameterTypeList", "ParameterList", "ParameterDeclaration",
  "IdentifierList", "Identifier", "IdentifierOrTypedefName", "TypeName",
  "InitializerOpt", "DesignatedInitializer", "Initializer",
  "InitializerList", "MatchedInitializerList", "Designation",
  "DesignatorList", "Designator", "ObsoleteArrayDesignation",
  "ObsoleteFieldDesignation", "Declarator", "TypedefDeclarator",
  "TypedefDeclaratorMain", "ParameterTypedefDeclarator",
  "CleanTypedefDeclarator", "CleanPostfixTypedefDeclarator",
  "ParenTypedefDeclarator", "ParenPostfixTypedefDeclarator",
  "SimpleParenTypedefDeclarator", "IdentifierDeclarator",
  "IdentifierDeclaratorMain", "UnaryIdentifierDeclarator",
  "PostfixIdentifierDeclarator", "ParenIdentifierDeclarator",
  "SimpleDeclarator", "OldFunctionDeclarator",
  "PostfixOldFunctionDeclarator", "AbstractDeclarator",
  "PostfixingAbstractDeclarator", "ParameterTypeListOpt",
  "ArrayAbstractDeclarator", "UnaryAbstractDeclarator",
  "PostfixAbstractDeclarator", "Statement", "LabeledStatement",
  "CompoundStatement", "LocalLabelDeclarationListOpt",
  "LocalLabelDeclarationList", "LocalLabelDeclaration", "LocalLabelList",
  "DeclarationOrStatementList", "DeclarationOrStatement",
  "DeclarationList", "ExpressionStatement", "SelectionStatement",
  "IterationStatement", "JumpStatement", "Constant", "StringLiteralList",
  "PrimaryExpression", "PrimaryIdentifier", "VariableArgumentAccess",
  "StatementAsExpression", "PostfixExpression", "CompoundLiteral",
  "ArgumentExpressionList", "UnaryExpression",
  "TypeCompatibilityExpression", "OffsetofExpression",
  "ExtensionExpression", "AlignofExpression", "Alignofkeyword",
  "LabelAddressExpression", "Unaryoperator", "CastExpression",
  "MultiplicativeExpression", "AdditiveExpression", "ShiftExpression",
  "RelationalExpression", "EqualityExpression", "AndExpression",
  "ExclusiveOrExpression", "InclusiveOrExpression", "LogicalAndExpression",
  "LogicalORExpression", "ConditionalExpression", "AssignmentExpression",
  "AssignmentOperator", "Expression", "ConstantExpression",
  "ExpressionOpt", "AttributeSpecifierListOpt", "AttributeSpecifierList",
  "AttributeSpecifier", "AttributeKeyword", "AttributeListOpt",
  "AttributeList", "AttributeExpressionOpt", "Word", "AssemblyDefinition",
  "AssemblyExpression", "AssemblyExpressionOpt", "AssemblyStatement",
  "Assemblyargument", "AssemblyoperandsOpt", "Assemblyoperands",
  "Assemblyoperand", "Assemblyclobbers", "AsmKeyword", "BindIdentifier",
  "BindIdentifierInList", "BindVar", "BindEnum", "EnterScope", "ExitScope",
  "ExitReentrantScope", "ReenterScope", "KillReentrantScope", 0
};
#endif

# ifdef YYPRINT
/* YYTOKNUM[YYLEX-NUM] -- Internal token number corresponding to
   token YYLEX-NUM.  */
static const yytype_uint16 yytoknum[] =
{
       0,   256,   257,   258,   259,   260,   261,   262,   263,   264,
     265,   266,   267,   268,   269,   270,   271,   272,   273,   274,
     275,   276,   277,   278,   279,   280,   281,   282,   283,   284,
     285,   286,   287,   288,   289,   290,   291,   292,   293,   294,
     295,   296,   297,   298,   299,   300,   301,   302,   303,   304,
     305,   306,   307,   308,   309,   310,   311,   312,   313,   314,
     315,   316,   317,   318,   319,   320,   321,   322,   323,   324,
     325,   326,   327,   328,   329,   330,   331,   332,   333,   334,
     335,   336,   337,   338,   339,   340,   341,   342,   343,   344,
     345,   346,   347,   348,   349,   350,   351,   352,   353,   354,
     355,   356,   357,   358,   359,   360,   361,   362,   363,   364,
     365,   366,   367,   368,   369,   370,   371,   372,   373,   374,
     375,   376,   377,   378,   379,   380,   381
};
# endif

/* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
static const yytype_uint16 yyr1[] =
{
       0,   127,   128,   129,   129,   130,   130,   130,   130,   131,
     132,   132,   133,   133,   134,   134,   134,   134,   134,   134,
     134,   134,   134,   134,   135,   135,   135,   135,   135,   136,
     136,   137,   137,   137,   137,   138,   138,   138,   139,   139,
     139,   140,   140,   140,   140,   140,   141,   141,   141,   141,
     141,   142,   142,   142,   143,   143,   144,   144,   145,   145,
     145,   145,   145,   146,   146,   146,   147,   147,   147,   148,
     148,   148,   149,   149,   149,   150,   150,   150,   150,   151,
     151,   151,   151,   152,   152,   152,   153,   153,   153,   154,
     154,   154,   155,   155,   155,   156,   156,   156,   156,   157,
     157,   157,   157,   158,   158,   159,   159,   159,   160,   160,
     160,   160,   161,   161,   161,   161,   162,   163,   163,   163,
     163,   163,   164,   164,   164,   164,   164,   164,   164,   164,
     164,   164,   164,   165,   165,   165,   166,   166,   167,   167,
     168,   168,   168,   168,   168,   168,   169,   169,   170,   170,
     171,   171,   171,   171,   171,   172,   172,   173,   173,   174,
     174,   175,   175,   176,   176,   177,   178,   178,   178,   178,
     178,   178,   178,   178,   178,   178,   179,   179,   180,   180,
     181,   181,   182,   182,   183,   183,   184,   184,   184,   184,
     184,   184,   184,   184,   184,   184,   184,   184,   184,   184,
     185,   185,   186,   187,   187,   188,   188,   188,   188,   189,
     189,   190,   190,   191,   191,   191,   192,   192,   193,   193,
     194,   194,   194,   195,   195,   196,   196,   196,   196,   197,
     197,   198,   199,   199,   200,   201,   201,   202,   202,   202,
     203,   203,   203,   204,   204,   205,   205,   205,   205,   205,
     206,   206,   206,   207,   207,   208,   209,   209,   210,   210,
     210,   211,   211,   211,   212,   212,   213,   214,   214,   214,
     215,   215,   215,   216,   216,   216,   217,   217,   218,   218,
     219,   219,   219,   220,   220,   220,   220,   221,   221,   221,
     221,   222,   222,   222,   222,   222,   222,   222,   223,   223,
     223,   223,   224,   225,   225,   226,   226,   227,   228,   228,
     229,   229,   230,   230,   231,   231,   232,   233,   233,   233,
     234,   234,   234,   235,   235,   235,   235,   235,   236,   236,
     236,   236,   236,   237,   237,   238,   238,   238,   238,   238,
     238,   239,   240,   241,   242,   242,   242,   242,   242,   242,
     242,   242,   242,   243,   244,   244,   245,   245,   245,   245,
     245,   245,   245,   245,   245,   245,   245,   246,   247,   248,
     249,   249,   250,   250,   251,   252,   252,   252,   252,   252,
     252,   253,   253,   254,   254,   254,   254,   255,   255,   255,
     256,   256,   256,   257,   257,   257,   257,   257,   258,   258,
     258,   259,   259,   260,   260,   261,   261,   262,   262,   263,
     263,   264,   264,   264,   265,   265,   266,   266,   266,   266,
     266,   266,   266,   266,   266,   266,   266,   267,   267,   268,
     269,   269,   270,   270,   271,   271,   272,   273,   273,   274,
     274,   275,   275,   276,   276,   276,   277,   277,   277,   277,
     277,   277,   277,   277,   277,   277,   277,   277,   277,   277,
     277,   277,   277,   277,   277,   277,   277,   277,   277,   277,
     277,   277,   277,   277,   277,   277,   277,   277,   277,   277,
     277,   277,   277,   277,   277,   277,   277,   277,   277,   277,
     277,   277,   277,   277,   277,   277,   277,   277,   277,   277,
     277,   277,   277,   277,   277,   277,   277,   277,   277,   277,
     277,   277,   278,   279,   280,   280,   281,   281,   282,   282,
     282,   282,   283,   283,   284,   284,   285,   285,   286,   286,
     287,   287,   287,   288,   289,   290,   291,   292,   293,   294,
     295,   296
};

/* YYR2[YYN] -- Number of symbols composing right hand side of rule YYN.  */
static const yytype_uint8 yyr2[] =
{
       0,     2,     1,     0,     2,     1,     1,     1,     1,     1,
       1,     2,     7,     8,     2,     3,     3,     3,     3,     2,
       3,     3,     3,     3,     2,     3,     3,     3,     3,     1,
       2,     3,     3,     3,     3,     6,     6,     8,     6,     6,
       8,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     2,     2,     1,     2,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     2,     2,     2,     2,     1,
       2,     2,     2,     2,     2,     2,     1,     2,     2,     2,
       2,     2,     1,     2,     2,     2,     2,     2,     2,     1,
       2,     2,     2,     4,     4,     1,     1,     1,     2,     2,
       2,     2,     1,     2,     2,     2,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       6,     7,     2,     7,     8,     3,     1,     1,     0,     2,
       2,     2,     2,     2,     1,     3,     4,     3,     4,     2,
       1,     2,     1,     0,     1,     2,     4,     5,     2,     5,
       6,     5,     6,     3,     6,     7,     1,     3,     3,     3,
       0,     2,     1,     3,     1,     3,     1,     2,     4,     4,
       1,     2,     4,     1,     2,     4,     4,     1,     2,     4,
       1,     3,     2,     1,     1,     1,     2,     1,     2,     0,
       2,     1,     2,     3,     4,     1,     1,     2,     0,     3,
       2,     1,     1,     1,     2,     3,     5,     2,     2,     3,
       5,     2,     1,     1,     1,     1,     1,     1,     2,     1,
       1,     2,     3,     3,     4,     1,     4,     5,     2,     3,
       3,     4,     4,     1,     3,     1,     1,     1,     1,     2,
       3,     2,     3,     4,     1,     3,     1,     1,     2,     3,
       6,     3,     4,     1,     1,     1,     1,     5,     0,     1,
       2,     3,     4,     1,     2,     2,     3,     3,     3,     3,
       4,     1,     1,     1,     1,     1,     1,     1,     4,     4,
       6,     3,     4,     0,     1,     1,     2,     3,     1,     3,
       0,     2,     1,     1,     1,     2,     2,     5,     7,     5,
       5,     7,     9,     3,     4,     2,     2,     3,     1,     1,
       1,     1,     1,     1,     2,     1,     1,     1,     3,     1,
       1,     1,     6,     5,     1,     4,     3,     4,     3,     3,
       2,     2,     1,     6,     1,     3,     1,     2,     2,     2,
       2,     4,     1,     1,     1,     1,     1,     6,     6,     2,
       4,     2,     1,     1,     2,     1,     1,     1,     1,     1,
       1,     1,     4,     1,     3,     3,     3,     1,     3,     3,
       1,     3,     3,     1,     3,     3,     3,     3,     1,     3,
       3,     1,     3,     1,     3,     1,     3,     1,     3,     1,
       3,     1,     5,     4,     1,     3,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     3,     1,
       0,     1,     0,     1,     1,     2,     6,     1,     1,     0,
       1,     2,     4,     0,     2,     3,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     2,     4,     0,     1,     5,     6,     7,     5,
       3,     1,     0,     1,     1,     3,     4,     7,     1,     3,
       1,     1,     1,     0,     0,     0,     0,     0,     0,     0,
       0,     0
};

/* YYDEFACT[STATE-NAME] -- Default rule to reduce with in state
   STATE-NUM when YYTABLE doesn't specify something else to do.  Zero
   means the default is an error.  */
static const yytype_uint16 yydefact[] =
{
       3,     0,     2,     1,   120,   128,   125,   146,   126,     0,
     121,   117,   123,   118,   147,    63,   127,   124,   130,   133,
     122,    66,   119,   266,    92,     0,     0,     9,   131,   136,
      69,   530,   531,   532,   437,   438,   116,   137,    64,    65,
       0,    72,    73,    74,    70,    71,   134,   135,   105,   106,
     107,    67,    68,     4,     8,     5,    10,   540,   540,     6,
      29,   541,   541,     0,     0,     0,     0,    54,    58,    59,
      60,    62,    41,    46,    42,    47,    43,    48,    45,    50,
      99,     0,    44,    49,   112,    51,    79,   129,   132,    86,
     138,   537,   139,   535,   255,   256,   258,   257,   264,   535,
     267,    61,     0,     7,     0,     0,   203,   204,     0,   168,
       0,   434,     0,     0,     0,     0,   259,   268,    11,    30,
       0,     0,   432,     0,   432,     0,   237,     0,     0,   533,
     232,   234,   236,   239,   240,   235,   245,   233,   533,   533,
     233,   533,    90,    53,    56,    96,   109,    57,    76,    84,
     533,   533,    93,    55,   100,   113,    52,    80,    87,   533,
     533,    77,    78,    81,    75,    82,    85,     0,    88,    83,
       0,    91,    94,    89,    97,    98,   101,   102,    95,     0,
     110,   111,   114,   115,   108,   142,   537,     0,    14,   537,
       0,   261,   276,    24,     0,   512,     0,   536,   536,     0,
     176,     0,     0,   173,   435,   262,   265,   271,   260,   269,
     303,     0,   314,     0,     0,     0,     0,     0,     0,   433,
      34,     0,    33,   537,   238,   253,     0,     0,     0,     0,
       0,     0,   241,   248,   514,    15,    25,   514,    16,    26,
     514,    27,   514,    28,    31,    32,     0,   341,   333,   328,
     329,   332,   330,   331,     0,     0,     0,   537,   375,   376,
     377,   378,   379,   380,   373,   372,     0,     0,     0,     0,
     205,   207,    46,    47,    48,    50,    49,     0,   336,   337,
     344,   335,   340,   339,   356,   352,   381,   366,   365,   364,
     363,     0,   362,     0,   383,   387,   390,   393,   398,   401,
     403,   405,   407,   409,   411,   414,   427,     0,     0,   145,
       0,   148,   278,   280,   381,   429,     0,     0,   439,     0,
     180,   180,     0,   166,     0,     0,     0,   263,   272,     0,
     310,   304,   305,     0,     0,   233,   257,     0,     0,   533,
     533,   303,   315,   534,   534,   278,     0,   243,   250,     0,
       0,     0,   242,   249,   515,   432,   432,   432,   432,   537,
     360,   537,   357,   358,   374,     0,     0,     0,     0,     0,
       0,   369,   537,   283,   206,   275,   273,   274,   208,   103,
     334,     0,   350,   351,     0,     0,     0,   417,   418,   419,
     420,   421,   422,   423,   424,   425,   426,   416,     0,   537,
     371,   359,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,   104,     0,   148,     0,   148,   538,   535,   186,   193,
     190,   197,    42,    47,   279,   182,   184,   539,   200,   539,
     281,     0,   447,   448,   449,   450,   451,   452,   453,   454,
     455,   456,   457,   458,   459,   460,   461,   462,   463,   464,
     465,   466,   467,   468,   469,   470,   471,   472,   473,   474,
     475,   476,   477,   478,   446,   479,   480,   481,   482,   483,
     484,   485,   486,   487,   488,   489,   490,   491,   492,   493,
     494,   495,   496,   497,   498,   499,   500,   501,   502,   503,
     504,   505,   506,   507,   508,   509,   510,   511,     0,   440,
     443,   513,     0,   178,   179,   169,   177,     0,   167,     0,
     171,     0,   308,     0,   430,   306,     0,     0,     0,     0,
       0,   514,   514,   310,   514,   514,   254,   244,   252,   251,
     246,     0,   209,   209,   209,   209,     0,     0,     0,   338,
     303,   538,     0,     0,     0,     0,     0,     0,   284,   285,
     349,   346,     0,   354,     0,   348,   415,     0,   384,   385,
     386,   388,   389,   391,   392,   396,   397,   394,   395,   399,
     400,   402,   404,   406,   408,   410,     0,     0,   428,   538,
     148,   538,   154,     0,     0,   149,     0,     0,     0,   202,
     537,   283,   533,   533,   187,   533,   533,   194,   537,   283,
     533,   191,   533,   198,     0,     0,     0,     0,   282,     0,
       0,     0,   441,   181,   170,   174,     0,   172,     0,   307,
       0,     0,     0,   430,     0,     0,     0,     0,   430,     0,
       0,   341,    92,     0,   312,     0,   313,   291,   292,   311,
     293,   294,   295,   296,   431,     0,   297,     0,     0,     0,
     430,   432,   432,   247,     0,    38,    39,    35,    36,   361,
       0,   218,   382,   310,     0,     0,     0,     0,   289,   287,
     288,   286,   347,     0,   345,   370,   413,     0,     0,   538,
       0,     0,   153,   432,   160,   163,   152,   432,   162,   163,
       0,   151,     0,   150,   140,   284,   432,   432,   432,   432,
     284,   432,   432,   183,   185,   201,   270,   277,   436,   443,
     444,     0,   175,   309,   326,     0,     0,     0,   325,   430,
     430,     0,     0,     0,     0,     0,   432,   316,     0,     0,
      12,     0,   209,   209,   341,   218,     0,     0,   210,   211,
       0,     0,   223,   221,   222,   215,     0,   216,   430,   343,
       0,     0,     0,   290,   355,   412,   141,     0,   143,   165,
     157,   159,   164,   155,   161,   432,   432,   189,   188,   196,
     195,   192,   199,   442,   445,     0,     0,   430,   327,     0,
     301,     0,   323,     0,     0,     0,   430,   521,     0,     0,
      13,    37,    40,   231,     0,     0,   227,   228,   212,     0,
     220,   224,   353,   217,   302,   368,   367,   342,   144,   156,
     158,   430,     0,   299,   430,   324,     0,   430,   430,   298,
     522,     0,     0,   213,     0,     0,   229,     0,   219,   319,
     430,     0,     0,   317,   320,     0,     0,   520,   523,   524,
     516,     0,   214,     0,     0,   225,   300,   430,     0,   430,
       0,     0,   522,     0,   517,   230,     0,     0,   321,   318,
       0,     0,   519,   525,   226,   430,     0,   526,     0,   322,
       0,   528,   518,     0,     0,   527,   529
};

/* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int16 yydefgoto[] =
{
      -1,     1,     2,    53,    54,    55,    56,    57,    58,   644,
      60,    61,    62,   213,   270,   215,   271,   143,    67,    68,
      69,    70,    71,    72,   272,    74,   273,    76,   274,    78,
     275,    80,    81,    82,   276,    84,    85,    86,    87,    88,
      89,    90,    91,   426,   595,   596,   597,   693,   697,   771,
     694,    92,   199,   200,   513,   434,   435,   436,   437,   438,
     645,   277,   665,   748,   749,   756,   757,   750,   751,   752,
     753,   754,   129,   130,   131,   132,   133,   134,   228,   136,
     229,   116,    94,    95,    96,   336,    98,   114,   100,   559,
     375,   439,   192,   376,   377,   646,   647,   648,   330,   331,
     332,   523,   524,   649,   217,   650,   651,   652,   653,   278,
     279,   280,   281,   282,   283,   284,   285,   562,   286,   287,
     288,   289,   290,   291,   292,   293,   294,   295,   296,   297,
     298,   299,   300,   301,   302,   303,   304,   305,   306,   398,
     654,   316,   655,   218,   219,   101,   102,   508,   509,   622,
     510,   103,   354,   357,   656,   798,   847,   848,   849,   882,
     657,   234,   534,   188,   320,   345,   598,   616,   120,   123
};

/* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
#define YYPACT_NINF -724
static const yytype_int16 yypact[] =
{
    -724,    39,  1215,  -724,  -724,  -724,  -724,  -724,  -724,   260,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,   131,  3631,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    2036,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,   -23,    43,   193,   193,  2159,  2282,  -724,  -724,  -724,
    -724,  -724,  2094,  2094,   925,   925,  1273,  1273,   860,   860,
    -724,    18,  1248,  1248,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,    58,  -724,  -724,  -724,  -724,  -724,   233,  -724,  -724,
    -724,  -724,    47,  -724,    53,    85,  -724,  -724,   394,   100,
     674,  -724,    99,   238,   128,  3631,  -724,  -724,  -724,  -724,
     118,  2692,   112,   123,   112,   163,   463,   455,  3522,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,   201,  -724,  -724,
     201,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,   188,  -724,  -724,
     190,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  3014,
    -724,  -724,  -724,  -724,  -724,   224,    58,   241,  -724,  -724,
    4064,  -724,   270,   285,   258,  -724,   330,  -724,  -724,   344,
    -724,   394,   394,   319,  -724,   463,  -724,   463,  -724,  -724,
     291,  2892,  -724,   484,   484,  2405,  2528,  2569,   253,   112,
    -724,   484,  -724,  -724,  -724,  -724,   455,   308,   334,   463,
     455,  3561,  -724,  -724,   547,  -724,   346,   547,  -724,   367,
     349,   375,   543,   385,  -724,  -724,  4364,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  4442,  4442,   433,  3014,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,   405,   408,   419,  4464,
     434,  3258,  1848,  1971,  1971,  1491,  1734,   366,  -724,   457,
    -724,  -724,  -724,  -724,   327,  -724,  1665,  -724,  -724,  -724,
    -724,  4542,  -724,  4464,  -724,   352,   259,   340,    36,   632,
     423,   418,   436,   473,    11,  -724,  -724,   626,   486,   224,
     496,  -724,  2726,  -724,  -724,  -724,   491,  4464,  1544,   184,
     505,   505,   134,  -724,   475,   494,   394,  -724,  -724,   551,
    -724,   291,  -724,   666,  3578,  -724,   463,   253,  3687,  -724,
    -724,   291,  -724,  -724,  -724,  2892,   446,   463,   463,   539,
     449,   455,  -724,  -724,  -724,   112,   112,   112,   112,  3014,
    -724,  3014,  -724,  -724,  -724,   552,   629,   550,  3421,  3421,
    4464,  -724,   434,  3719,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,   508,  -724,  -724,  4086,  4464,   508,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  4464,  3014,
    -724,  -724,  4464,  4464,  4464,  4464,  4464,  4464,  4464,  4464,
    4464,  4464,  4464,  4464,  4464,  4464,  4464,  4464,  4464,  4464,
    4164,  -724,  4464,  -724,   562,  -724,  3299,  -724,    76,    76,
    1790,  1913,  1273,  1273,  -724,   571,  -724,   587,  -724,  -724,
    -724,   588,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,  -724,  -724,   597,   600,
     615,  -724,  4464,  -724,  -724,  -724,  -724,   171,  -724,   225,
    -724,   531,  -724,    60,  1075,  -724,   666,   451,   666,  3663,
    3687,   547,   547,  -724,   547,   547,  -724,  -724,  -724,  -724,
    -724,   538,   596,   596,   596,   596,   651,   658,  4186,  -724,
     291,  -724,   662,   665,   668,   671,   679,   685,  3719,  -724,
    -724,  -724,   638,  -724,    95,  -724,  -724,   688,  -724,  -724,
    -724,   352,   352,   259,   259,   340,   340,   340,   340,    36,
      36,   632,   423,   418,   436,   473,  4464,   -40,  -724,  3299,
    -724,  3299,  -724,   846,  3136,  -724,   173,   242,   691,  -724,
     409,  3460,  -724,  -724,  -724,  -724,  -724,  -724,   437,  3384,
    -724,  -724,  -724,  -724,  2849,   733,   698,   709,  -724,   721,
    1544,  4264,  -724,  -724,  -724,  -724,   251,  -724,   764,  -724,
     713,   740,  4464,  4464,   717,   742,   720,    38,  3830,   749,
     751,   730,   732,  1667,  -724,   734,  -724,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,   750,   735,  -724,  2224,   752,   666,
    1075,   112,   112,  -724,  3986,  -724,  -724,  -724,  -724,   755,
     755,  -724,  -724,  -724,   760,    67,  3421,  3421,  -724,   463,
    -724,  -724,  -724,  4464,  -724,   755,  -724,  4464,   756,  3299,
     761,  4464,  -724,   112,  -724,   747,  -724,   112,  -724,   747,
     362,  -724,   342,  -724,  -724,  3460,   112,   112,   112,   112,
    3384,   112,   112,  -724,  -724,  -724,  -724,  -724,  -724,   615,
    -724,   646,  -724,  -724,  -724,  4464,   105,   753,  -724,  4464,
    3830,  4464,   754,   787,  4464,  4464,   112,  -724,   330,   771,
    -724,   781,   596,   596,   768,  -724,  4464,   516,  -724,  -724,
    4342,   304,  -724,  -724,  -724,  -724,   788,  3986,  1421,  -724,
     796,   794,   798,  -724,  -724,  -724,  -724,   800,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,   112,   112,  -724,  -724,  -724,
    -724,  -724,  -724,  -724,  -724,   673,  4464,  3830,  -724,   778,
    -724,   286,  -724,   804,   690,   693,  3830,    29,   809,   330,
    -724,  -724,  -724,  -724,  3908,    70,  -724,  -724,  -724,  4464,
    -724,  -724,  -724,   810,  -724,  -724,  -724,  -724,  -724,  -724,
    -724,  3830,   790,  -724,  4464,  -724,  4464,  3830,  3830,  -724,
     109,   791,   819,  -724,   544,  4464,   386,   101,  -724,  -724,
    3830,   802,   712,   881,  -724,  1544,    30,   806,   825,  -724,
    -724,   807,  -724,   822,  4464,  -724,  -724,  4464,   811,  3830,
     826,  4464,   109,   109,  -724,   392,   828,   836,  -724,  -724,
     330,   715,   815,  -724,  -724,  3830,    34,  -724,   330,  -724,
    4464,   457,   837,   729,   330,  -724,   457
};

/* YYPGOTO[NTERM-NUM].  */
static const yytype_int16 yypgoto[] =
{
    -724,  -724,  -724,  -724,  -724,  -724,   865,  -724,  -724,    26,
     -36,  -724,  -724,     7,     5,     9,     0,   -41,   772,  -724,
    -724,  -724,  -724,  -724,    12,  -307,    13,  -724,    14,  -724,
      15,    -7,  -724,  -724,    16,   117,   841,   435,  -724,  -724,
     124,  -724,  -724,  -356,  -724,  -724,  -724,   207,   210,   222,
    -570,  -724,  -140,  -230,   604,  -724,  -724,   312,  -724,   315,
      -1,  -215,  -467,  -723,   177,  -724,   186,  -724,  -724,   178,
    -724,  -724,   -63,  -724,  -724,  -115,   -59,  -724,   -34,  -724,
    -220,    23,  -724,    71,  -724,   138,  -724,   377,  -724,  -148,
     536,  -724,  -724,  -353,  -351,  -106,  -724,   565,  -329,  -724,
     602,  -724,  -501,  -724,  -724,  -724,  -724,  -724,  -724,  -724,
    -193,  -724,  -724,  -724,  -724,   261,  -724,   316,   166,  -724,
    -724,  -724,  -724,  -724,  -724,  -724,   -21,   314,   393,   338,
     397,   525,   526,   528,   529,   524,  -724,   -99,  -348,  -724,
    -100,  -242,  -573,   122,    48,    -9,  -724,  -724,  -724,   227,
    -600,  -724,   946,  -191,  -724,   150,    88,  -724,    90,  -724,
     254,   653,   607,   -76,   758,     6,  -126,   518,   901,   216
};

/* YYTABLE[YYPACT[STATE-NUM]].  What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule which
   number is the opposite.  If zero, do what YYDEFACT says.
   If YYTABLE_NINF, syntax error.  */
#define YYTABLE_NINF -542
static const yytype_int16 yytable[] =
{
     111,   139,    66,   319,   119,   432,   346,    64,   109,    63,
     350,    65,   533,   232,    73,    75,    77,    79,    83,   556,
     719,   557,   554,   193,   698,    93,   115,   422,    59,   135,
     135,   161,   660,   166,   813,   171,   563,   174,   432,     3,
      66,   180,   365,   355,   122,    64,   356,    63,   687,    65,
     566,   358,    73,    75,    77,    79,    83,   110,   145,   154,
     727,   324,   325,    93,   419,   380,   380,   589,   227,   591,
     380,   175,   177,   106,   588,   441,   666,   667,   668,   307,
     107,   834,   111,   179,   409,   410,   137,   140,   150,   159,
     185,   315,   516,   106,   233,   861,   112,   187,   420,   880,
     107,   204,   247,   248,   249,   250,   251,   252,   253,   203,
     124,    23,   194,   111,   731,   111,   352,   830,   126,   411,
     412,   216,   374,   378,   835,   772,   214,   628,   231,   772,
     698,   541,   361,    73,    75,    77,    79,    83,   208,   186,
      97,   600,   195,   836,   546,   248,   547,   212,   190,   629,
     196,   139,   601,   552,   553,   854,   789,   366,   344,   786,
      34,    35,   422,   113,    97,   205,    23,   227,   684,   197,
     201,   227,   758,   268,   855,   119,   198,   204,    97,   135,
     135,   845,   146,   155,   567,   309,   521,   135,   210,   149,
     158,   308,   310,   787,   207,   312,    25,   353,   112,   181,
     183,    97,    97,    97,    97,   515,   197,    26,   145,   154,
     204,   216,   220,   198,    34,    35,   214,   216,   315,   232,
     380,   673,   214,    73,    75,    77,    79,    83,    23,    73,
      75,    77,    79,    83,   689,   126,   335,   335,   339,   340,
     700,   343,   624,   342,   335,   860,   221,   556,   371,   557,
     511,   841,   222,    97,   208,   556,   105,   557,   127,   366,
     197,   366,   701,   367,   154,   113,    97,   198,   177,   128,
     623,  -533,   401,   563,   227,   801,   802,   244,   125,   245,
     604,   607,   611,   613,   867,   564,   197,   516,    23,   516,
     167,   170,   227,   198,  -537,   106,   625,   112,   189,   366,
     233,   112,   107,   189,   206,   190,   346,   432,   350,   702,
     190,   311,   431,   602,   605,   424,   755,   429,   337,   428,
     587,   430,   722,   318,    73,   433,    77,    79,    83,   338,
     108,   703,   146,   155,   529,   764,   405,   406,   530,   149,
     158,   358,   317,   661,   662,   431,   111,   111,   111,   111,
     429,   599,   428,   422,   430,   -19,   314,    73,   433,    77,
      79,    83,    34,    35,   113,   367,   248,   367,   113,    97,
     381,   382,   383,   558,   347,   825,   809,    23,   747,    99,
     560,   568,   569,   570,   126,   565,   407,   408,   155,   326,
     726,   166,   384,   183,   810,   158,   516,    23,   658,   385,
     348,   386,   755,   117,   112,   367,   329,   333,   112,   755,
     681,   322,   360,   315,   352,   323,   -20,    99,   334,   -17,
     362,   363,   112,   145,   154,   674,   594,   337,   402,   197,
     691,   593,   379,   403,   404,   314,   198,   -21,   338,   541,
     138,   141,   151,   160,    23,   -22,    31,    32,    33,   769,
     691,   603,   606,   610,   612,   -23,   755,   400,  -225,   314,
    -225,   761,   762,   688,  -226,   690,  -226,   227,   364,   227,
     368,   527,    23,   369,   600,   527,  -225,   542,   543,   544,
     545,   190,  -226,   314,   370,   601,   232,   686,   105,   113,
      23,   105,   209,   380,   105,   353,   105,   225,   415,   372,
     148,   157,   608,   416,   805,   117,   190,   162,   165,   190,
     373,   223,   536,   609,   223,   540,   223,   206,   190,    23,
     226,   190,   417,   190,   216,   418,   126,   672,   223,   214,
     695,   128,   733,   315,   741,   190,    73,    75,    77,    79,
      83,   227,   517,   106,   822,   797,   518,   146,   155,   333,
     107,   806,   208,   208,   149,   158,   423,   681,   807,   135,
     334,   519,   681,   767,   440,   520,   425,   837,   314,   314,
     314,   314,   314,   314,   314,   314,   314,   314,   314,   314,
     314,   314,   314,   314,   314,   314,   522,   154,   765,   594,
     352,   594,   315,   853,   593,   512,   593,   112,   626,   112,
     227,   705,   627,   223,   663,   539,   797,   119,   209,   710,
     190,   838,   866,   -18,   431,   852,   335,   699,   548,   429,
     550,   428,   371,   430,   790,   785,    73,   433,    77,    79,
      83,   791,   590,   191,   794,   795,   732,   846,   614,   695,
      31,    32,    33,   216,    31,    32,    33,   315,   214,   191,
     148,   157,   111,   111,   615,    73,    75,    77,    79,    83,
     216,   618,   224,   619,   527,   214,   527,   620,   135,   846,
     846,   112,    73,    75,    77,    79,    83,   876,   314,   112,
     621,   823,   413,   414,   111,   881,   664,   315,   111,   594,
     829,   886,   421,   422,   593,   549,   422,   111,   111,   111,
     111,    23,   111,   111,   682,   683,   157,   165,   225,   106,
     315,   155,   784,   683,   314,   839,   107,   669,   158,   571,
     572,   843,   844,   699,   670,   335,   842,   111,   208,   675,
     112,   526,   676,   208,   856,   677,   315,   678,   527,   821,
     422,   327,   334,   328,   202,   679,   527,   575,   576,   577,
     578,   680,   314,   869,   685,   315,   827,   422,   216,   828,
     422,   871,   704,   214,   716,   349,   111,   111,   427,   879,
      73,    75,    77,    79,    83,   717,    34,    35,   858,   422,
     883,   877,   422,   742,   743,   105,   105,   718,   105,   105,
     235,   236,   237,   238,   239,   885,   422,   527,   314,   723,
     573,   574,   724,   240,   241,   725,   728,   729,   730,   314,
     579,   580,   242,   243,   734,   770,   735,   422,  -203,   773,
    -204,   793,   736,   740,   737,   671,   759,   766,   777,   778,
     779,   780,   768,   781,   782,   691,   799,   144,   153,   381,
     382,   383,   788,   792,   144,   163,   144,   168,   144,   172,
     144,   176,   800,   314,   144,   182,   803,   314,   796,   812,
     816,   384,   815,     4,   817,   148,   157,   824,   385,   826,
     386,   818,   191,    10,    11,   831,    13,   838,   840,    15,
     850,    23,   349,   537,   538,   851,   349,   153,   126,   859,
      21,   857,   863,    22,   862,   865,   864,   819,   820,   870,
     868,   874,   875,   878,   884,   118,   147,   156,   555,   776,
     775,   333,   314,   147,   164,   147,   169,   147,   173,   147,
     178,   774,   334,   147,   184,   514,   714,   808,     4,   811,
     715,   804,   551,   525,   691,   692,   760,   721,    10,    11,
     581,    13,   582,   585,    15,   583,   783,   584,   104,   832,
     872,   535,   314,   873,    30,    21,   321,   617,    22,   121,
       0,     0,    34,    35,     0,     0,     0,     0,     0,    38,
      39,     0,    41,    42,    43,   314,    44,    45,     0,     0,
       0,    48,    49,    50,    51,    52,     0,   144,   153,     0,
       0,     0,   531,   532,     0,     0,     0,     0,     0,     0,
       0,   314,     0,   153,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,  -541,     0,     0,     0,     0,    30,
     314,     0,     0,     0,     0,     0,     0,    34,    35,   157,
       0,     0,     0,     0,    38,    39,     0,    41,    42,    43,
       0,    44,    45,   153,   163,   168,   172,   176,   182,    51,
      52,     0,     0,     0,     0,     0,   147,   156,     0,     0,
       0,     0,     0,   191,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,   349,     4,     5,
       6,     7,   630,     0,     8,   631,   632,     9,    10,    11,
      12,    13,   633,    14,    15,    16,    17,    18,   634,   635,
      19,    20,   636,   637,   246,    21,   638,   639,    22,   640,
     641,   248,   249,   250,   251,   252,   253,   642,     0,   254,
     255,     0,     0,     0,     0,     0,     0,   256,     0,     0,
       0,     0,     0,     0,     0,     0,   555,     0,     0,     0,
     257,     0,     0,     0,   555,   550,  -538,     0,     0,     0,
     258,   259,   260,   261,   262,   263,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,    28,    29,    30,
     264,   265,    31,    32,    33,     0,     0,    34,    35,   266,
     267,   268,    36,    37,    38,    39,   643,    41,    42,    43,
       0,    44,    45,    46,    47,     0,    48,    49,    50,    51,
      52,     0,   144,   153,   144,   168,     0,     0,     0,     0,
       0,     0,     0,     0,     0,   763,     0,     0,     4,     5,
       6,     7,     0,     0,     8,     0,     0,     9,    10,    11,
      12,    13,     0,    14,    15,    16,    17,    18,     0,     0,
      19,    20,     0,     0,     0,    21,     0,     0,    22,     0,
      23,     4,     0,     0,     0,   706,   707,    24,   708,   709,
       0,    10,    11,   711,    13,   712,     0,    15,     0,     0,
       0,   147,   156,   147,   169,     0,     4,     0,    21,     0,
      25,    22,     0,     0,     0,     0,    10,    11,     0,    13,
       0,    26,    15,     0,     0,     0,     0,     0,     0,     0,
       0,   153,   153,    21,    27,     0,    22,    28,    29,    30,
       0,     0,    31,    32,    33,     0,     0,    34,    35,     0,
       0,     0,    36,    37,    38,    39,    40,    41,    42,    43,
     153,    44,    45,    46,    47,     0,    48,    49,    50,    51,
      52,     0,    30,     0,     0,     0,     0,     0,     0,     0,
      34,    35,     0,     0,     0,    36,     0,    38,    39,     0,
      41,    42,    43,     0,    44,    45,   153,    30,     0,     0,
       0,     0,    51,    52,     0,    34,    35,     0,     0,     0,
       0,     0,    38,    39,     0,    41,    42,    43,     0,    44,
      45,     0,     0,     0,     0,     0,     0,    51,    52,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     4,     5,     6,     7,   630,   739,
       8,   631,   632,     9,    10,    11,    12,    13,   633,    14,
      15,    16,    17,    18,   634,   635,    19,    20,   636,   637,
     246,    21,   638,   639,    22,   640,   641,   248,   249,   250,
     251,   252,   253,   642,     0,   254,   255,     0,     0,     0,
       0,     0,     0,   256,     0,     0,     0,   153,     0,     0,
       0,     0,   153,     0,     0,     0,   257,     0,     0,     0,
       0,   550,   814,     0,     0,     0,   258,   259,   260,   261,
     262,   263,     0,     0,     0,     0,     0,     0,     0,     0,
      15,     0,     0,    28,    29,    30,   264,   265,    31,    32,
      33,    21,     0,    34,    35,   266,   267,   268,    36,    37,
      38,    39,   643,    41,    42,    43,     0,    44,    45,    46,
      47,     0,    48,    49,    50,    51,    52,   442,   443,   444,
     445,   446,   447,   448,   449,   450,   451,   452,   453,   454,
     455,   456,   457,   458,   459,   460,   461,   462,   463,   464,
     465,   466,   467,   468,   469,   470,   471,   472,   473,   474,
       0,     0,     0,     0,     0,    30,     0,     0,     0,     0,
       0,     0,     0,    34,    35,     0,     0,     0,     0,     0,
      38,    39,     0,    41,    42,    43,     0,    44,    45,     0,
       0,     0,    48,    49,    50,    51,    52,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,   475,   476,   477,   478,   479,
     480,   481,   482,   483,     0,     0,   484,   485,   486,   487,
     488,   489,   490,   491,   492,   493,   494,   495,   496,   497,
     498,   499,   500,   501,   502,   503,   504,   505,   506,   507,
       4,     5,     6,     7,     0,     0,     8,     0,     0,     9,
      10,    11,    12,    13,     0,    14,    15,    16,    17,    18,
       0,     0,    19,    20,     0,     0,   246,    21,     0,     0,
      22,     0,   247,   248,   249,   250,   251,   252,   253,    24,
       0,   254,   255,     0,     0,     0,     0,     0,     0,   256,
     387,   388,   389,   390,   391,   392,   393,   394,   395,   396,
       0,     0,   257,     0,     0,     0,     0,     0,     0,     0,
       0,     0,   258,   259,   260,   261,   262,   263,     0,     0,
       0,     0,     0,    15,     0,   397,     0,     0,     0,    28,
      29,    30,   264,   265,    21,     0,     0,     0,     0,    34,
      35,   266,   267,   268,    36,    37,    38,    39,   269,    41,
      42,    43,     0,    44,    45,    46,    47,     0,    48,    49,
      50,    51,    52,     4,     5,     6,     7,     0,     0,     8,
       0,     0,     9,    10,    11,    12,    13,     0,    14,    15,
      16,    17,    18,     0,     0,    19,    20,     0,     0,     0,
      21,     0,     0,    22,     0,    23,     0,     0,    30,     0,
       0,     0,   142,     0,     0,     0,    34,    35,     0,     0,
       0,    36,     0,    38,    39,     0,    41,    42,    43,     0,
      44,    45,     5,     6,     0,   608,     0,     8,    51,    52,
       0,     0,   190,    12,     0,     0,   609,    15,    16,    17,
      18,     0,     0,    19,    20,     0,     0,     0,    21,     0,
       0,     0,    28,    29,    30,     0,     0,     0,     0,     0,
       0,     0,    34,    35,     0,     0,     0,    36,    37,    38,
      39,     0,    41,    42,    43,     0,    44,    45,    46,    47,
       0,    48,    49,    50,    51,    52,     4,     5,     6,     7,
       0,     0,     8,     0,     0,     9,    10,    11,    12,    13,
       0,    14,    15,    16,    17,    18,     0,     0,    19,    20,
      28,    29,    30,    21,     0,     0,    22,     0,    23,     0,
      34,    35,     0,     0,     0,   152,    37,    38,    39,     0,
      41,    42,    43,     0,    44,    45,    46,    47,     0,     0,
       0,     0,    51,    52,     0,     0,     0,     0,   608,     0,
       0,     0,     0,     0,     0,   190,     0,     0,     0,   609,
      15,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,    21,     0,     0,     0,    28,    29,    30,     0,     0,
       0,     0,     0,     0,     0,    34,    35,     0,     0,     0,
      36,    37,    38,    39,     0,    41,    42,    43,     0,    44,
      45,    46,    47,     0,    48,    49,    50,    51,    52,     4,
       5,     6,     7,     0,     0,     8,     0,     0,     9,    10,
      11,    12,    13,     0,    14,    15,    16,    17,    18,     0,
       0,    19,    20,     0,     0,    30,    21,     0,     0,    22,
       0,    23,     0,    34,    35,     0,     0,     0,    24,     0,
      38,    39,     0,    41,    42,    43,     0,    44,    45,     0,
       0,     0,     0,     0,     0,    51,    52,     4,     5,     6,
       0,    25,     0,     8,     0,     0,     0,    10,    11,    12,
      13,     0,    26,    15,    16,    17,    18,     0,     0,    19,
      20,     0,     0,     0,    21,     0,     0,    22,    28,    29,
      30,     0,     0,     0,     0,     0,     0,     0,    34,    35,
       0,     0,     0,    36,    37,    38,    39,     0,    41,    42,
      43,     0,    44,    45,    46,    47,     0,    48,    49,    50,
      51,    52,     4,     5,     6,     7,     0,     0,     8,     0,
       0,     9,    10,    11,    12,    13,     0,    14,    15,    16,
      17,    18,     0,     0,    19,    20,    28,    29,    30,    21,
       0,     0,    22,     0,    23,     0,    34,    35,     0,     0,
       0,   142,    37,    38,    39,     0,    41,    42,    43,     0,
      44,    45,    46,    47,     0,     0,     0,     0,    51,    52,
       0,     0,     0,     0,    25,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,    26,     0,     0,     0,     0,
       0,     0,     0,    15,     0,     0,     0,     0,     0,     0,
       0,    28,    29,    30,    21,     0,     0,     0,     0,     0,
       0,    34,    35,     0,     0,     0,    36,    37,    38,    39,
       0,    41,    42,    43,     0,    44,    45,    46,    47,     0,
      48,    49,    50,    51,    52,     4,     5,     6,     7,   738,
       0,     8,     0,     0,     9,    10,    11,    12,    13,     0,
      14,    15,    16,    17,    18,     0,     0,    19,    20,     0,
       0,     0,    21,     0,     0,    22,     0,    23,    30,     0,
       0,     0,     0,     0,   152,     0,    34,    35,     0,     0,
       0,     0,     0,    38,    39,     0,    41,    42,    43,     0,
      44,    45,     0,     0,     0,     0,     0,    25,    51,    52,
       0,     0,     0,     0,     0,     0,     0,     0,    26,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,    28,    29,    30,     0,     0,     0,
       0,     0,     0,     0,    34,    35,     0,     0,     0,    36,
      37,    38,    39,     0,    41,    42,    43,     0,    44,    45,
      46,    47,     0,    48,    49,    50,    51,    52,     4,     5,
       6,     7,     0,     0,     8,     0,     0,     9,    10,    11,
      12,    13,     0,    14,    15,    16,    17,    18,     0,     0,
      19,    20,     0,     0,     0,    21,     0,     0,    22,     0,
      23,     0,     0,     0,     0,     0,     0,   142,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     337,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,   338,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,    28,    29,    30,
       0,     0,     0,     0,     0,     0,     0,    34,    35,     0,
       0,     0,    36,    37,    38,    39,     0,    41,    42,    43,
       0,    44,    45,    46,    47,     0,    48,    49,    50,    51,
      52,     4,     5,     6,     7,     0,     0,     8,     0,     0,
       9,    10,    11,    12,    13,     0,    14,    15,    16,    17,
      18,     0,     0,    19,    20,     0,     0,     0,    21,     0,
       0,    22,     0,    23,     0,     0,     0,     0,     0,     0,
     152,     0,     4,     5,     6,     7,     0,     0,     8,     0,
       0,     9,    10,    11,    12,    13,     0,    14,    15,    16,
      17,    18,     0,   337,    19,    20,     0,     0,     0,    21,
       0,     0,    22,     0,   338,     0,     0,     0,     0,     0,
       0,    24,     0,     0,     0,     0,     0,     0,     0,     0,
      28,    29,    30,     0,     0,     0,     0,     0,     0,     0,
      34,    35,     0,     0,     0,    36,    37,    38,    39,   341,
      41,    42,    43,     0,    44,    45,    46,    47,     0,    48,
      49,    50,    51,    52,     0,     0,     0,     0,     0,     0,
       0,    28,    29,    30,     0,     0,     0,     0,     0,     0,
       0,    34,    35,     0,     0,     0,    36,    37,    38,    39,
     211,    41,    42,    43,     0,    44,    45,    46,    47,     0,
      48,    49,    50,    51,    52,     4,     5,     6,     7,     0,
       0,     8,     0,     0,     9,    10,    11,    12,    13,     0,
      14,    15,    16,    17,    18,     0,     0,    19,    20,     0,
       0,     0,    21,     0,     0,    22,     0,     0,     0,     4,
       5,     6,     7,     0,    24,     8,     0,     0,     9,    10,
      11,    12,    13,     0,    14,    15,    16,    17,    18,     0,
       0,    19,    20,     0,     0,     0,    21,     0,     0,    22,
       0,   427,     0,     0,     0,     0,     0,     0,    24,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,    28,    29,    30,     0,     0,     0,
       0,     0,     0,     0,    34,    35,     0,     0,     0,    36,
      37,    38,    39,   211,    41,    42,    43,     0,    44,    45,
      46,    47,     0,    48,    49,    50,    51,    52,    28,    29,
      30,     0,     0,     0,     0,     0,     0,     0,    34,    35,
       0,     0,     0,    36,    37,    38,    39,     0,    41,    42,
      43,     0,    44,    45,    46,    47,     0,    48,    49,    50,
      51,    52,     4,     5,     6,     7,     0,     0,     8,     0,
       0,     9,    10,    11,    12,    13,     0,    14,    15,    16,
      17,    18,     0,     0,    19,    20,     0,     0,     0,    21,
       0,     0,    22,     0,     0,     0,     0,     0,     0,     0,
       0,    24,     0,     0,     0,     4,     5,     6,     7,     0,
       0,     8,     0,   713,     9,    10,    11,    12,    13,     0,
      14,    15,    16,    17,    18,     0,     0,    19,    20,     0,
       0,     0,    21,     0,     0,    22,     0,     0,     0,     0,
       0,     0,     0,     0,    24,     0,     0,     0,     0,     0,
       0,    28,    29,    30,     0,     0,     0,     0,     0,     0,
       0,    34,    35,     0,     0,     0,    36,    37,    38,    39,
       0,    41,    42,    43,     0,    44,    45,    46,    47,     0,
      48,    49,    50,    51,    52,     0,     0,     0,     0,     0,
       0,     0,     0,     0,    28,    29,    30,     0,     0,     0,
       0,     0,     0,     0,    34,    35,     0,     0,     0,    36,
      37,    38,    39,     0,    41,    42,    43,     0,    44,    45,
      46,    47,     0,    48,    49,    50,    51,    52,     5,     6,
       7,     0,     0,     8,     0,     0,     9,     0,     0,    12,
       0,     0,    14,    15,    16,    17,    18,     0,     0,    19,
      20,     0,     0,   246,    21,     0,     0,     0,     0,   247,
     248,   249,   250,   251,   252,   253,    24,     0,   254,   255,
       0,     0,     0,     0,     0,     0,   256,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   257,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   258,
     259,   260,   261,   262,   263,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,    28,    29,    30,   264,
     265,     0,     0,     0,     0,     0,    34,    35,   266,   267,
     268,    36,    37,    38,    39,   269,    41,    42,    43,     0,
      44,    45,    46,    47,     0,    48,    49,    50,    51,    52,
       5,     6,     7,     0,     0,     8,     0,     0,     9,     0,
       0,    12,     0,     0,    14,    15,    16,    17,    18,     0,
       0,    19,    20,     0,     0,     0,    21,     0,     0,     0,
       0,    23,     0,     0,     0,     0,     0,     0,   152,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,   337,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,   338,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,   691,   696,     0,     0,    28,    29,
      30,     0,     0,     0,     0,     0,     0,     0,    34,    35,
       0,     0,     0,    36,    37,    38,    39,     0,    41,    42,
      43,     0,    44,    45,    46,    47,     0,    48,    49,    50,
      51,    52,     5,     6,     7,     0,     0,     8,     0,     0,
       9,     0,     0,    12,     0,     0,    14,    15,    16,    17,
      18,     0,     0,    19,    20,     0,     0,     0,    21,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     152,     0,     0,     5,     6,     7,     0,     0,     8,     0,
       0,     9,     0,     0,    12,     0,     0,    14,    15,    16,
      17,    18,     0,   372,    19,    20,     0,     0,     0,    21,
     190,     0,     0,     0,   373,     0,     0,     0,     0,     0,
       0,    24,     0,     0,     0,     0,     0,     0,     0,     0,
      28,    29,    30,     0,     0,     0,     0,     0,     0,     0,
      34,    35,     0,     0,     0,    36,    37,    38,    39,     0,
      41,    42,    43,     0,    44,    45,    46,    47,     0,    48,
      49,    50,    51,    52,     0,     0,     0,     0,   592,     0,
       0,    28,    29,    30,     0,     0,     0,     0,     0,     0,
       0,    34,    35,    15,     0,     0,    36,    37,    38,    39,
       0,    41,    42,    43,    21,    44,    45,    46,    47,    23,
      48,    49,    50,    51,    52,     5,     6,     7,     0,     0,
       8,     0,     0,     9,     0,     0,    12,     0,     0,    14,
      15,    16,    17,    18,     0,     0,    19,    20,     0,   608,
       0,    21,     0,     0,     0,     0,   190,     0,     0,     0,
     609,     0,     0,    24,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,    30,    15,
       0,     0,     0,     0,     0,     0,    34,    35,     0,     0,
      21,     0,     0,    38,    39,    23,    41,    42,    43,     0,
      44,    45,   126,     0,     0,     0,     0,     0,    51,    52,
       0,     0,     0,    28,    29,    30,     0,     0,     0,     0,
       0,     0,     0,    34,    35,   600,     0,     0,    36,    37,
      38,    39,   190,    41,    42,    43,   601,    44,    45,    46,
      47,    15,    48,    49,    50,    51,    52,     0,     0,     0,
       0,     0,    21,     0,    30,     0,     0,    23,     0,     0,
       0,     0,    34,    35,   126,     0,     0,     0,     0,    38,
      39,     0,    41,    42,    43,     0,    44,    45,     0,     0,
      15,     0,     0,     0,    51,    52,     0,   230,     0,     0,
       0,    21,     0,     0,     0,     0,    23,    15,   128,     0,
       0,     0,     0,   126,     0,     0,     0,     0,    21,     0,
       0,     0,     0,    23,     0,     0,    30,     0,     0,     0,
     126,     0,     0,     0,    34,    35,   351,     0,     0,     0,
       0,    38,    39,     0,    41,    42,    43,   128,    44,    45,
       0,     0,     0,   528,     0,     0,    51,    52,     0,     0,
      15,     0,     0,     0,   334,    30,     0,     0,     0,     0,
       0,    21,     0,    34,    35,     0,    23,     0,     0,     0,
      38,    39,    30,    41,    42,    43,     0,    44,    45,     0,
      34,    35,    15,     0,     0,    51,    52,    38,    39,     0,
      41,    42,    43,    21,    44,    45,    25,     0,    23,     0,
       0,     0,    51,    52,     0,   126,    15,    26,     0,     0,
       0,     0,     0,     0,     0,     0,     0,    21,     0,     0,
       0,     0,    23,     0,     0,    30,     0,     0,   659,     0,
       0,     0,     0,    34,    35,     0,     0,     0,    15,   334,
      38,    39,     0,    41,    42,    43,     0,    44,    45,    21,
       0,     0,   337,     0,     0,    51,    52,    30,     0,     0,
       0,     0,     0,   338,     0,    34,    35,     0,     0,     0,
       0,     0,    38,    39,     0,    41,    42,    43,     0,    44,
      45,    30,     0,     0,   372,     0,     0,    51,    52,    34,
      35,   190,     0,     0,     0,   373,    38,    39,     0,    41,
      42,    43,     0,    44,    45,     0,     0,     0,     0,     0,
       0,    51,    52,    30,     0,     0,     0,     0,     0,     0,
       0,    34,    35,     0,     0,     0,     0,     0,    38,    39,
       0,    41,    42,    43,     0,    44,    45,   630,     0,     0,
     631,   632,     0,    51,    52,     0,     0,   633,     0,     0,
       0,     0,     0,   634,   635,     0,     0,   636,   637,   246,
       0,   638,   639,     0,   640,   641,   248,   249,   250,   251,
     252,   253,   107,     0,   254,   255,     0,     0,     0,     0,
       0,     0,   256,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,   257,     0,     0,     0,     0,
     550,     0,     0,     0,     0,   258,   259,   260,   261,   262,
     263,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,   264,   265,    31,    32,    33,
       0,     0,     0,     0,   266,   267,   268,   246,     0,     0,
       0,   269,     0,   744,   248,   249,   250,   251,   252,   253,
       0,     0,   254,   255,     0,     0,     0,     0,     0,     0,
     256,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,   257,     0,     0,     0,     0,   745,   833,
     746,     0,   747,   258,   259,   260,   261,   262,   263,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,   264,   265,     0,     0,     0,     0,     0,
       0,     0,   266,   267,   268,   246,     0,     0,     0,   269,
       0,   744,   248,   249,   250,   251,   252,   253,     0,     0,
     254,   255,     0,     0,     0,     0,     0,     0,   256,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,   257,     0,     0,     0,     0,   745,     0,   746,     0,
     747,   258,   259,   260,   261,   262,   263,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,   264,   265,     0,     0,     0,     0,     0,     0,     0,
     266,   267,   268,   246,     0,     0,     0,   269,     0,   247,
     248,   249,   250,   251,   252,   253,     0,     0,   254,   255,
       0,     0,     0,     0,     0,   246,   256,     0,     0,     0,
       0,   247,   248,   249,   250,   251,   252,   253,     0,   257,
     254,   255,     0,     0,     0,     0,     0,   313,   256,   258,
     259,   260,   261,   262,   263,     0,     0,     0,     0,     0,
       0,   257,   561,     0,     0,     0,     0,     0,     0,   264,
     265,   258,   259,   260,   261,   262,   263,     0,   266,   267,
     268,     0,     0,     0,     0,   269,     0,     0,     0,     0,
       0,   264,   265,     0,     0,     0,     0,     0,     0,     0,
     266,   267,   268,   246,     0,     0,     0,   269,     0,   247,
     248,   249,   250,   251,   252,   253,     0,     0,   254,   255,
       0,     0,     0,     0,     0,   246,   256,     0,     0,     0,
       0,   247,   248,   249,   250,   251,   252,   253,     0,   257,
     254,   255,     0,     0,     0,     0,     0,     0,   256,   258,
     259,   260,   261,   262,   263,     0,     0,     0,     0,     0,
       0,   257,   586,     0,     0,     0,   671,     0,     0,   264,
     265,   258,   259,   260,   261,   262,   263,     0,   266,   267,
     268,     0,     0,     0,     0,   269,     0,     0,     0,     0,
       0,   264,   265,     0,     0,     0,     0,     0,     0,     0,
     266,   267,   268,   246,     0,     0,     0,   269,     0,   247,
     248,   249,   250,   251,   252,   253,     0,     0,   254,   255,
       0,     0,     0,     0,     0,     0,   256,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   257,
     720,     0,     0,     0,     0,     0,     0,     0,     0,   258,
     259,   260,   261,   262,   263,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   264,
     265,     0,     0,     0,     0,     0,     0,     0,   266,   267,
     268,   246,     0,     0,     0,   269,     0,   247,   248,   249,
     250,   251,   252,   253,     0,     0,   254,   255,     0,     0,
       0,     0,     0,   246,   256,     0,     0,     0,     0,   247,
     248,   249,   250,   251,   252,   253,     0,   257,   254,   255,
       0,     0,   745,     0,     0,     0,   256,   258,   259,   260,
     261,   262,   263,     0,     0,     0,     0,     0,     0,   359,
       0,     0,     0,     0,     0,     0,     0,   264,   265,   258,
     259,   260,   261,   262,   263,     0,   266,   267,   268,     0,
       0,     0,     0,   269,     0,     0,     0,     0,     0,   264,
     265,     0,     0,     0,     0,     0,     0,     0,   266,   267,
     268,   246,     0,     0,     0,   269,     0,   247,   248,   249,
     250,   251,   252,   253,     0,     0,   254,   255,     0,     0,
       0,     0,     0,   246,   256,     0,     0,     0,     0,   247,
     248,   249,   250,   251,   252,   253,     0,   361,   254,   255,
       0,     0,     0,     0,     0,     0,   256,   258,   259,   260,
     261,   262,   263,     0,     0,     0,     0,     0,     0,   257,
       0,     0,     0,     0,     0,     0,     0,   264,   265,   258,
     259,   260,   261,   262,   263,     0,   266,   267,   268,     0,
       0,     0,     0,   269,     0,     0,     0,     0,     0,   264,
     265,     0,     0,     0,     0,     0,     0,     0,   266,   267,
     268,   246,     0,     0,     0,   269,     0,   247,   248,   249,
     250,   251,   252,   253,     0,     0,   254,   255,     0,     0,
       0,     0,     0,     0,   256,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,   399,     0,     0,
       0,     0,     0,     0,     0,     0,     0,   258,   259,   260,
     261,   262,   263,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,   264,   265,     0,
       0,     0,     0,     0,     0,     0,   266,   267,   268,     0,
       0,     0,     0,   269
};

static const yytype_int16 yycheck[] =
{
       9,    64,     2,   196,    40,   312,   226,     2,     9,     2,
     230,     2,   341,   128,     2,     2,     2,     2,     2,   372,
     620,   372,   370,    99,   594,     2,    26,    67,     2,    63,
      64,    72,   533,    74,   757,    76,   384,    78,   345,     0,
      40,    82,   257,   234,    67,    40,   237,    40,    88,    40,
     398,   242,    40,    40,    40,    40,    40,     9,    65,    66,
     633,   201,   202,    40,    53,    36,    36,   423,   127,   425,
      36,    78,    79,    35,   422,   317,   543,   544,   545,   179,
      42,   804,    91,    65,    48,    49,    63,    64,    65,    66,
      91,   190,   322,    35,   128,    65,    25,    91,    87,    65,
      42,   110,    35,    36,    37,    38,    39,    40,    41,   110,
      67,    35,    65,   122,    76,   124,   231,    88,    42,    83,
      84,   121,   270,   271,    54,   695,   121,    67,   128,   699,
     700,   351,    65,   121,   121,   121,   121,   121,   115,    91,
       2,    65,    89,    73,   359,    36,   361,   121,    72,    89,
      65,   214,    76,   368,   369,    54,   729,   257,   221,    54,
     102,   103,    67,    25,    26,    66,    35,   226,    73,    35,
      70,   230,   673,   106,    73,   211,    42,   186,    40,   213,
     214,    72,    65,    66,   399,   186,   326,   221,    70,    65,
      66,   185,   186,    88,    66,   189,    65,   231,   127,    82,
      83,    63,    64,    65,    66,    71,    35,    76,   215,   216,
     219,   211,    89,    42,   102,   103,   211,   217,   317,   334,
      36,   550,   217,   211,   211,   211,   211,   211,    35,   217,
     217,   217,   217,   217,   590,    42,   213,   214,   215,   216,
      67,   218,    71,   217,   221,   845,   124,   600,   269,   600,
      66,   824,    89,   115,   231,   608,     2,   608,    65,   359,
      35,   361,    89,   257,   271,   127,   128,    42,   275,    76,
     512,    70,   293,   621,   333,   742,   743,    89,    62,    89,
     428,   429,   430,   431,   857,   385,    35,   517,    35,   519,
      74,    75,   351,    42,    70,    35,    71,   226,    65,   399,
     334,   230,    42,    65,    66,    72,   526,   614,   528,    67,
      72,    70,   312,   428,   429,   309,   664,   312,    65,   312,
     420,   312,    71,    65,   312,   312,   312,   312,   312,    76,
      70,    89,   215,   216,   334,   683,    77,    78,   338,   215,
     216,   532,    72,   534,   535,   345,   355,   356,   357,   358,
     345,   427,   345,    67,   345,    70,   190,   345,   345,   345,
     345,   345,   102,   103,   226,   359,    36,   361,   230,   231,
      43,    44,    45,   373,    66,    89,    72,    35,    74,     2,
     381,   402,   403,   404,    42,   386,    46,    47,   271,    70,
     632,   432,    65,   276,    90,   271,   626,    35,   524,    72,
      66,    74,   750,    26,   333,   399,   115,    65,   337,   757,
     558,    67,   246,   512,   529,    71,    70,    40,    76,    70,
     254,   255,   351,   430,   431,   551,   426,    65,    76,    35,
      88,   426,    66,    81,    82,   269,    42,    70,    76,   659,
      63,    64,    65,    66,    35,    70,    97,    98,    99,   691,
      88,   428,   429,   430,   431,    70,   804,   291,    72,   293,
      74,   676,   677,   589,    72,   591,    74,   526,    35,   528,
      65,   333,    35,    65,    65,   337,    90,   355,   356,   357,
     358,    72,    90,   317,    65,    76,   601,   586,   234,   351,
      35,   237,   115,    36,   240,   529,   242,    42,    75,    65,
      65,    66,    65,    85,   746,   128,    72,    72,    73,    72,
      76,    65,    66,    76,    65,    66,    65,    66,    72,    35,
      65,    72,    86,    72,   524,    52,    42,   548,    65,   524,
     593,    76,   638,   632,   660,    72,   524,   524,   524,   524,
     524,   600,    67,    35,   786,   738,    71,   430,   431,    65,
      42,    35,   529,   530,   430,   431,    70,   705,    42,   593,
      76,    67,   710,   689,    73,    71,    70,   809,   402,   403,
     404,   405,   406,   407,   408,   409,   410,   411,   412,   413,
     414,   415,   416,   417,   418,   419,    35,   594,   687,   589,
     705,   591,   691,   835,   589,    90,   591,   526,    67,   528,
     659,   601,    71,    65,    66,    66,   799,   643,   231,   609,
      72,    67,   854,    70,   614,    71,   593,   594,    66,   614,
      70,   614,   643,   614,   730,   725,   614,   614,   614,   614,
     614,   731,    70,    97,   734,   735,   637,   830,    67,   702,
      97,    98,    99,   643,    97,    98,    99,   746,   643,   113,
     215,   216,   661,   662,    67,   643,   643,   643,   643,   643,
     660,    73,   126,    66,   526,   660,   528,    67,   702,   862,
     863,   600,   660,   660,   660,   660,   660,   870,   512,   608,
      65,   787,    50,    51,   693,   878,    90,   786,   697,   689,
     796,   884,    66,    67,   689,    66,    67,   706,   707,   708,
     709,    35,   711,   712,    66,    67,   271,   272,    42,    35,
     809,   594,    66,    67,   548,   821,    42,    66,   594,   405,
     406,   827,   828,   700,    66,   702,   826,   736,   705,    67,
     659,    65,    67,   710,   840,    67,   835,    66,   600,    66,
      67,   205,    76,   207,    70,    66,   608,   409,   410,   411,
     412,    66,   586,   859,    66,   854,    66,    67,   758,    66,
      67,   861,    71,   758,    66,   229,   775,   776,    35,   875,
     758,   758,   758,   758,   758,    66,   102,   103,    66,    67,
     880,    66,    67,   661,   662,   531,   532,    66,   534,   535,
     137,   138,   139,   140,   141,    66,    67,   659,   632,    35,
     407,   408,    89,   150,   151,    65,    89,    65,    88,   643,
     413,   414,   159,   160,    65,   693,    65,    67,    88,   697,
      88,    34,    88,    71,    89,    70,    66,    71,   706,   707,
     708,   709,    71,   711,   712,    88,    65,    65,    66,    43,
      44,    45,    89,    89,    72,    73,    74,    75,    76,    77,
      78,    79,    71,   687,    82,    83,    88,   691,   736,    71,
      66,    65,    66,     3,    66,   430,   431,    89,    72,    65,
      74,    71,   336,    13,    14,    66,    16,    67,    88,    19,
      89,    35,   346,   347,   348,    66,   350,   115,    42,     8,
      30,    89,    67,    33,    88,    73,    89,   775,   776,    73,
      89,    73,    66,    88,    67,    40,    65,    66,   372,   702,
     700,    65,   746,    72,    73,    74,    75,    76,    77,    78,
      79,   699,    76,    82,    83,   321,   614,   750,     3,   751,
     615,   745,   367,   331,    88,    89,   675,   621,    13,    14,
     415,    16,   416,   419,    19,   417,   719,   418,     2,   799,
     862,   344,   786,   863,    94,    30,   198,   439,    33,    58,
      -1,    -1,   102,   103,    -1,    -1,    -1,    -1,    -1,   109,
     110,    -1,   112,   113,   114,   809,   116,   117,    -1,    -1,
      -1,   121,   122,   123,   124,   125,    -1,   215,   216,    -1,
      -1,    -1,   339,   340,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,   835,    -1,   231,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    89,    -1,    -1,    -1,    -1,    94,
     854,    -1,    -1,    -1,    -1,    -1,    -1,   102,   103,   594,
      -1,    -1,    -1,    -1,   109,   110,    -1,   112,   113,   114,
      -1,   116,   117,   271,   272,   273,   274,   275,   276,   124,
     125,    -1,    -1,    -1,    -1,    -1,   215,   216,    -1,    -1,
      -1,    -1,    -1,   527,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,   541,     3,     4,
       5,     6,     7,    -1,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
      35,    36,    37,    38,    39,    40,    41,    42,    -1,    44,
      45,    -1,    -1,    -1,    -1,    -1,    -1,    52,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,   600,    -1,    -1,    -1,
      65,    -1,    -1,    -1,   608,    70,    71,    -1,    -1,    -1,
      75,    76,    77,    78,    79,    80,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    92,    93,    94,
      95,    96,    97,    98,    99,    -1,    -1,   102,   103,   104,
     105,   106,   107,   108,   109,   110,   111,   112,   113,   114,
      -1,   116,   117,   118,   119,    -1,   121,   122,   123,   124,
     125,    -1,   430,   431,   432,   433,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,   679,    -1,    -1,     3,     4,
       5,     6,    -1,    -1,     9,    -1,    -1,    12,    13,    14,
      15,    16,    -1,    18,    19,    20,    21,    22,    -1,    -1,
      25,    26,    -1,    -1,    -1,    30,    -1,    -1,    33,    -1,
      35,     3,    -1,    -1,    -1,   602,   603,    42,   605,   606,
      -1,    13,    14,   610,    16,   612,    -1,    19,    -1,    -1,
      -1,   430,   431,   432,   433,    -1,     3,    -1,    30,    -1,
      65,    33,    -1,    -1,    -1,    -1,    13,    14,    -1,    16,
      -1,    76,    19,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,   529,   530,    30,    89,    -1,    33,    92,    93,    94,
      -1,    -1,    97,    98,    99,    -1,    -1,   102,   103,    -1,
      -1,    -1,   107,   108,   109,   110,   111,   112,   113,   114,
     558,   116,   117,   118,   119,    -1,   121,   122,   123,   124,
     125,    -1,    94,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
     102,   103,    -1,    -1,    -1,   107,    -1,   109,   110,    -1,
     112,   113,   114,    -1,   116,   117,   594,    94,    -1,    -1,
      -1,    -1,   124,   125,    -1,   102,   103,    -1,    -1,    -1,
      -1,    -1,   109,   110,    -1,   112,   113,   114,    -1,   116,
     117,    -1,    -1,    -1,    -1,    -1,    -1,   124,   125,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,     3,     4,     5,     6,     7,   657,
       9,    10,    11,    12,    13,    14,    15,    16,    17,    18,
      19,    20,    21,    22,    23,    24,    25,    26,    27,    28,
      29,    30,    31,    32,    33,    34,    35,    36,    37,    38,
      39,    40,    41,    42,    -1,    44,    45,    -1,    -1,    -1,
      -1,    -1,    -1,    52,    -1,    -1,    -1,   705,    -1,    -1,
      -1,    -1,   710,    -1,    -1,    -1,    65,    -1,    -1,    -1,
      -1,    70,    71,    -1,    -1,    -1,    75,    76,    77,    78,
      79,    80,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      19,    -1,    -1,    92,    93,    94,    95,    96,    97,    98,
      99,    30,    -1,   102,   103,   104,   105,   106,   107,   108,
     109,   110,   111,   112,   113,   114,    -1,   116,   117,   118,
     119,    -1,   121,   122,   123,   124,   125,     3,     4,     5,
       6,     7,     8,     9,    10,    11,    12,    13,    14,    15,
      16,    17,    18,    19,    20,    21,    22,    23,    24,    25,
      26,    27,    28,    29,    30,    31,    32,    33,    34,    35,
      -1,    -1,    -1,    -1,    -1,    94,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,   102,   103,    -1,    -1,    -1,    -1,    -1,
     109,   110,    -1,   112,   113,   114,    -1,   116,   117,    -1,
      -1,    -1,   121,   122,   123,   124,   125,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    91,    92,    93,    94,    95,
      96,    97,    98,    99,    -1,    -1,   102,   103,   104,   105,
     106,   107,   108,   109,   110,   111,   112,   113,   114,   115,
     116,   117,   118,   119,   120,   121,   122,   123,   124,   125,
       3,     4,     5,     6,    -1,    -1,     9,    -1,    -1,    12,
      13,    14,    15,    16,    -1,    18,    19,    20,    21,    22,
      -1,    -1,    25,    26,    -1,    -1,    29,    30,    -1,    -1,
      33,    -1,    35,    36,    37,    38,    39,    40,    41,    42,
      -1,    44,    45,    -1,    -1,    -1,    -1,    -1,    -1,    52,
      55,    56,    57,    58,    59,    60,    61,    62,    63,    64,
      -1,    -1,    65,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    75,    76,    77,    78,    79,    80,    -1,    -1,
      -1,    -1,    -1,    19,    -1,    90,    -1,    -1,    -1,    92,
      93,    94,    95,    96,    30,    -1,    -1,    -1,    -1,   102,
     103,   104,   105,   106,   107,   108,   109,   110,   111,   112,
     113,   114,    -1,   116,   117,   118,   119,    -1,   121,   122,
     123,   124,   125,     3,     4,     5,     6,    -1,    -1,     9,
      -1,    -1,    12,    13,    14,    15,    16,    -1,    18,    19,
      20,    21,    22,    -1,    -1,    25,    26,    -1,    -1,    -1,
      30,    -1,    -1,    33,    -1,    35,    -1,    -1,    94,    -1,
      -1,    -1,    42,    -1,    -1,    -1,   102,   103,    -1,    -1,
      -1,   107,    -1,   109,   110,    -1,   112,   113,   114,    -1,
     116,   117,     4,     5,    -1,    65,    -1,     9,   124,   125,
      -1,    -1,    72,    15,    -1,    -1,    76,    19,    20,    21,
      22,    -1,    -1,    25,    26,    -1,    -1,    -1,    30,    -1,
      -1,    -1,    92,    93,    94,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,   102,   103,    -1,    -1,    -1,   107,   108,   109,
     110,    -1,   112,   113,   114,    -1,   116,   117,   118,   119,
      -1,   121,   122,   123,   124,   125,     3,     4,     5,     6,
      -1,    -1,     9,    -1,    -1,    12,    13,    14,    15,    16,
      -1,    18,    19,    20,    21,    22,    -1,    -1,    25,    26,
      92,    93,    94,    30,    -1,    -1,    33,    -1,    35,    -1,
     102,   103,    -1,    -1,    -1,    42,   108,   109,   110,    -1,
     112,   113,   114,    -1,   116,   117,   118,   119,    -1,    -1,
      -1,    -1,   124,   125,    -1,    -1,    -1,    -1,    65,    -1,
      -1,    -1,    -1,    -1,    -1,    72,    -1,    -1,    -1,    76,
      19,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    30,    -1,    -1,    -1,    92,    93,    94,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,   102,   103,    -1,    -1,    -1,
     107,   108,   109,   110,    -1,   112,   113,   114,    -1,   116,
     117,   118,   119,    -1,   121,   122,   123,   124,   125,     3,
       4,     5,     6,    -1,    -1,     9,    -1,    -1,    12,    13,
      14,    15,    16,    -1,    18,    19,    20,    21,    22,    -1,
      -1,    25,    26,    -1,    -1,    94,    30,    -1,    -1,    33,
      -1,    35,    -1,   102,   103,    -1,    -1,    -1,    42,    -1,
     109,   110,    -1,   112,   113,   114,    -1,   116,   117,    -1,
      -1,    -1,    -1,    -1,    -1,   124,   125,     3,     4,     5,
      -1,    65,    -1,     9,    -1,    -1,    -1,    13,    14,    15,
      16,    -1,    76,    19,    20,    21,    22,    -1,    -1,    25,
      26,    -1,    -1,    -1,    30,    -1,    -1,    33,    92,    93,
      94,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   102,   103,
      -1,    -1,    -1,   107,   108,   109,   110,    -1,   112,   113,
     114,    -1,   116,   117,   118,   119,    -1,   121,   122,   123,
     124,   125,     3,     4,     5,     6,    -1,    -1,     9,    -1,
      -1,    12,    13,    14,    15,    16,    -1,    18,    19,    20,
      21,    22,    -1,    -1,    25,    26,    92,    93,    94,    30,
      -1,    -1,    33,    -1,    35,    -1,   102,   103,    -1,    -1,
      -1,    42,   108,   109,   110,    -1,   112,   113,   114,    -1,
     116,   117,   118,   119,    -1,    -1,    -1,    -1,   124,   125,
      -1,    -1,    -1,    -1,    65,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    76,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    19,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    92,    93,    94,    30,    -1,    -1,    -1,    -1,    -1,
      -1,   102,   103,    -1,    -1,    -1,   107,   108,   109,   110,
      -1,   112,   113,   114,    -1,   116,   117,   118,   119,    -1,
     121,   122,   123,   124,   125,     3,     4,     5,     6,    65,
      -1,     9,    -1,    -1,    12,    13,    14,    15,    16,    -1,
      18,    19,    20,    21,    22,    -1,    -1,    25,    26,    -1,
      -1,    -1,    30,    -1,    -1,    33,    -1,    35,    94,    -1,
      -1,    -1,    -1,    -1,    42,    -1,   102,   103,    -1,    -1,
      -1,    -1,    -1,   109,   110,    -1,   112,   113,   114,    -1,
     116,   117,    -1,    -1,    -1,    -1,    -1,    65,   124,   125,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    76,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    92,    93,    94,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,   102,   103,    -1,    -1,    -1,   107,
     108,   109,   110,    -1,   112,   113,   114,    -1,   116,   117,
     118,   119,    -1,   121,   122,   123,   124,   125,     3,     4,
       5,     6,    -1,    -1,     9,    -1,    -1,    12,    13,    14,
      15,    16,    -1,    18,    19,    20,    21,    22,    -1,    -1,
      25,    26,    -1,    -1,    -1,    30,    -1,    -1,    33,    -1,
      35,    -1,    -1,    -1,    -1,    -1,    -1,    42,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      65,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    76,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    92,    93,    94,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,   102,   103,    -1,
      -1,    -1,   107,   108,   109,   110,    -1,   112,   113,   114,
      -1,   116,   117,   118,   119,    -1,   121,   122,   123,   124,
     125,     3,     4,     5,     6,    -1,    -1,     9,    -1,    -1,
      12,    13,    14,    15,    16,    -1,    18,    19,    20,    21,
      22,    -1,    -1,    25,    26,    -1,    -1,    -1,    30,    -1,
      -1,    33,    -1,    35,    -1,    -1,    -1,    -1,    -1,    -1,
      42,    -1,     3,     4,     5,     6,    -1,    -1,     9,    -1,
      -1,    12,    13,    14,    15,    16,    -1,    18,    19,    20,
      21,    22,    -1,    65,    25,    26,    -1,    -1,    -1,    30,
      -1,    -1,    33,    -1,    76,    -1,    -1,    -1,    -1,    -1,
      -1,    42,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      92,    93,    94,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
     102,   103,    -1,    -1,    -1,   107,   108,   109,   110,    70,
     112,   113,   114,    -1,   116,   117,   118,   119,    -1,   121,
     122,   123,   124,   125,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    92,    93,    94,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,   102,   103,    -1,    -1,    -1,   107,   108,   109,   110,
     111,   112,   113,   114,    -1,   116,   117,   118,   119,    -1,
     121,   122,   123,   124,   125,     3,     4,     5,     6,    -1,
      -1,     9,    -1,    -1,    12,    13,    14,    15,    16,    -1,
      18,    19,    20,    21,    22,    -1,    -1,    25,    26,    -1,
      -1,    -1,    30,    -1,    -1,    33,    -1,    -1,    -1,     3,
       4,     5,     6,    -1,    42,     9,    -1,    -1,    12,    13,
      14,    15,    16,    -1,    18,    19,    20,    21,    22,    -1,
      -1,    25,    26,    -1,    -1,    -1,    30,    -1,    -1,    33,
      -1,    35,    -1,    -1,    -1,    -1,    -1,    -1,    42,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    92,    93,    94,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,   102,   103,    -1,    -1,    -1,   107,
     108,   109,   110,   111,   112,   113,   114,    -1,   116,   117,
     118,   119,    -1,   121,   122,   123,   124,   125,    92,    93,
      94,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   102,   103,
      -1,    -1,    -1,   107,   108,   109,   110,    -1,   112,   113,
     114,    -1,   116,   117,   118,   119,    -1,   121,   122,   123,
     124,   125,     3,     4,     5,     6,    -1,    -1,     9,    -1,
      -1,    12,    13,    14,    15,    16,    -1,    18,    19,    20,
      21,    22,    -1,    -1,    25,    26,    -1,    -1,    -1,    30,
      -1,    -1,    33,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    42,    -1,    -1,    -1,     3,     4,     5,     6,    -1,
      -1,     9,    -1,    54,    12,    13,    14,    15,    16,    -1,
      18,    19,    20,    21,    22,    -1,    -1,    25,    26,    -1,
      -1,    -1,    30,    -1,    -1,    33,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    42,    -1,    -1,    -1,    -1,    -1,
      -1,    92,    93,    94,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,   102,   103,    -1,    -1,    -1,   107,   108,   109,   110,
      -1,   112,   113,   114,    -1,   116,   117,   118,   119,    -1,
     121,   122,   123,   124,   125,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    92,    93,    94,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,   102,   103,    -1,    -1,    -1,   107,
     108,   109,   110,    -1,   112,   113,   114,    -1,   116,   117,
     118,   119,    -1,   121,   122,   123,   124,   125,     4,     5,
       6,    -1,    -1,     9,    -1,    -1,    12,    -1,    -1,    15,
      -1,    -1,    18,    19,    20,    21,    22,    -1,    -1,    25,
      26,    -1,    -1,    29,    30,    -1,    -1,    -1,    -1,    35,
      36,    37,    38,    39,    40,    41,    42,    -1,    44,    45,
      -1,    -1,    -1,    -1,    -1,    -1,    52,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    65,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    75,
      76,    77,    78,    79,    80,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    92,    93,    94,    95,
      96,    -1,    -1,    -1,    -1,    -1,   102,   103,   104,   105,
     106,   107,   108,   109,   110,   111,   112,   113,   114,    -1,
     116,   117,   118,   119,    -1,   121,   122,   123,   124,   125,
       4,     5,     6,    -1,    -1,     9,    -1,    -1,    12,    -1,
      -1,    15,    -1,    -1,    18,    19,    20,    21,    22,    -1,
      -1,    25,    26,    -1,    -1,    -1,    30,    -1,    -1,    -1,
      -1,    35,    -1,    -1,    -1,    -1,    -1,    -1,    42,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    65,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    76,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    88,    89,    -1,    -1,    92,    93,
      94,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   102,   103,
      -1,    -1,    -1,   107,   108,   109,   110,    -1,   112,   113,
     114,    -1,   116,   117,   118,   119,    -1,   121,   122,   123,
     124,   125,     4,     5,     6,    -1,    -1,     9,    -1,    -1,
      12,    -1,    -1,    15,    -1,    -1,    18,    19,    20,    21,
      22,    -1,    -1,    25,    26,    -1,    -1,    -1,    30,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      42,    -1,    -1,     4,     5,     6,    -1,    -1,     9,    -1,
      -1,    12,    -1,    -1,    15,    -1,    -1,    18,    19,    20,
      21,    22,    -1,    65,    25,    26,    -1,    -1,    -1,    30,
      72,    -1,    -1,    -1,    76,    -1,    -1,    -1,    -1,    -1,
      -1,    42,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      92,    93,    94,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
     102,   103,    -1,    -1,    -1,   107,   108,   109,   110,    -1,
     112,   113,   114,    -1,   116,   117,   118,   119,    -1,   121,
     122,   123,   124,   125,    -1,    -1,    -1,    -1,    89,    -1,
      -1,    92,    93,    94,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,   102,   103,    19,    -1,    -1,   107,   108,   109,   110,
      -1,   112,   113,   114,    30,   116,   117,   118,   119,    35,
     121,   122,   123,   124,   125,     4,     5,     6,    -1,    -1,
       9,    -1,    -1,    12,    -1,    -1,    15,    -1,    -1,    18,
      19,    20,    21,    22,    -1,    -1,    25,    26,    -1,    65,
      -1,    30,    -1,    -1,    -1,    -1,    72,    -1,    -1,    -1,
      76,    -1,    -1,    42,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    94,    19,
      -1,    -1,    -1,    -1,    -1,    -1,   102,   103,    -1,    -1,
      30,    -1,    -1,   109,   110,    35,   112,   113,   114,    -1,
     116,   117,    42,    -1,    -1,    -1,    -1,    -1,   124,   125,
      -1,    -1,    -1,    92,    93,    94,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,   102,   103,    65,    -1,    -1,   107,   108,
     109,   110,    72,   112,   113,   114,    76,   116,   117,   118,
     119,    19,   121,   122,   123,   124,   125,    -1,    -1,    -1,
      -1,    -1,    30,    -1,    94,    -1,    -1,    35,    -1,    -1,
      -1,    -1,   102,   103,    42,    -1,    -1,    -1,    -1,   109,
     110,    -1,   112,   113,   114,    -1,   116,   117,    -1,    -1,
      19,    -1,    -1,    -1,   124,   125,    -1,    65,    -1,    -1,
      -1,    30,    -1,    -1,    -1,    -1,    35,    19,    76,    -1,
      -1,    -1,    -1,    42,    -1,    -1,    -1,    -1,    30,    -1,
      -1,    -1,    -1,    35,    -1,    -1,    94,    -1,    -1,    -1,
      42,    -1,    -1,    -1,   102,   103,    65,    -1,    -1,    -1,
      -1,   109,   110,    -1,   112,   113,   114,    76,   116,   117,
      -1,    -1,    -1,    65,    -1,    -1,   124,   125,    -1,    -1,
      19,    -1,    -1,    -1,    76,    94,    -1,    -1,    -1,    -1,
      -1,    30,    -1,   102,   103,    -1,    35,    -1,    -1,    -1,
     109,   110,    94,   112,   113,   114,    -1,   116,   117,    -1,
     102,   103,    19,    -1,    -1,   124,   125,   109,   110,    -1,
     112,   113,   114,    30,   116,   117,    65,    -1,    35,    -1,
      -1,    -1,   124,   125,    -1,    42,    19,    76,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    30,    -1,    -1,
      -1,    -1,    35,    -1,    -1,    94,    -1,    -1,    65,    -1,
      -1,    -1,    -1,   102,   103,    -1,    -1,    -1,    19,    76,
     109,   110,    -1,   112,   113,   114,    -1,   116,   117,    30,
      -1,    -1,    65,    -1,    -1,   124,   125,    94,    -1,    -1,
      -1,    -1,    -1,    76,    -1,   102,   103,    -1,    -1,    -1,
      -1,    -1,   109,   110,    -1,   112,   113,   114,    -1,   116,
     117,    94,    -1,    -1,    65,    -1,    -1,   124,   125,   102,
     103,    72,    -1,    -1,    -1,    76,   109,   110,    -1,   112,
     113,   114,    -1,   116,   117,    -1,    -1,    -1,    -1,    -1,
      -1,   124,   125,    94,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,   102,   103,    -1,    -1,    -1,    -1,    -1,   109,   110,
      -1,   112,   113,   114,    -1,   116,   117,     7,    -1,    -1,
      10,    11,    -1,   124,   125,    -1,    -1,    17,    -1,    -1,
      -1,    -1,    -1,    23,    24,    -1,    -1,    27,    28,    29,
      -1,    31,    32,    -1,    34,    35,    36,    37,    38,    39,
      40,    41,    42,    -1,    44,    45,    -1,    -1,    -1,    -1,
      -1,    -1,    52,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    65,    -1,    -1,    -1,    -1,
      70,    -1,    -1,    -1,    -1,    75,    76,    77,    78,    79,
      80,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    95,    96,    97,    98,    99,
      -1,    -1,    -1,    -1,   104,   105,   106,    29,    -1,    -1,
      -1,   111,    -1,    35,    36,    37,    38,    39,    40,    41,
      -1,    -1,    44,    45,    -1,    -1,    -1,    -1,    -1,    -1,
      52,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    65,    -1,    -1,    -1,    -1,    70,    71,
      72,    -1,    74,    75,    76,    77,    78,    79,    80,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    95,    96,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,   104,   105,   106,    29,    -1,    -1,    -1,   111,
      -1,    35,    36,    37,    38,    39,    40,    41,    -1,    -1,
      44,    45,    -1,    -1,    -1,    -1,    -1,    -1,    52,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    65,    -1,    -1,    -1,    -1,    70,    -1,    72,    -1,
      74,    75,    76,    77,    78,    79,    80,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    95,    96,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
     104,   105,   106,    29,    -1,    -1,    -1,   111,    -1,    35,
      36,    37,    38,    39,    40,    41,    -1,    -1,    44,    45,
      -1,    -1,    -1,    -1,    -1,    29,    52,    -1,    -1,    -1,
      -1,    35,    36,    37,    38,    39,    40,    41,    -1,    65,
      44,    45,    -1,    -1,    -1,    -1,    -1,    73,    52,    75,
      76,    77,    78,    79,    80,    -1,    -1,    -1,    -1,    -1,
      -1,    65,    66,    -1,    -1,    -1,    -1,    -1,    -1,    95,
      96,    75,    76,    77,    78,    79,    80,    -1,   104,   105,
     106,    -1,    -1,    -1,    -1,   111,    -1,    -1,    -1,    -1,
      -1,    95,    96,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
     104,   105,   106,    29,    -1,    -1,    -1,   111,    -1,    35,
      36,    37,    38,    39,    40,    41,    -1,    -1,    44,    45,
      -1,    -1,    -1,    -1,    -1,    29,    52,    -1,    -1,    -1,
      -1,    35,    36,    37,    38,    39,    40,    41,    -1,    65,
      44,    45,    -1,    -1,    -1,    -1,    -1,    -1,    52,    75,
      76,    77,    78,    79,    80,    -1,    -1,    -1,    -1,    -1,
      -1,    65,    88,    -1,    -1,    -1,    70,    -1,    -1,    95,
      96,    75,    76,    77,    78,    79,    80,    -1,   104,   105,
     106,    -1,    -1,    -1,    -1,   111,    -1,    -1,    -1,    -1,
      -1,    95,    96,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
     104,   105,   106,    29,    -1,    -1,    -1,   111,    -1,    35,
      36,    37,    38,    39,    40,    41,    -1,    -1,    44,    45,
      -1,    -1,    -1,    -1,    -1,    -1,    52,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    65,
      66,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    75,
      76,    77,    78,    79,    80,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    95,
      96,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   104,   105,
     106,    29,    -1,    -1,    -1,   111,    -1,    35,    36,    37,
      38,    39,    40,    41,    -1,    -1,    44,    45,    -1,    -1,
      -1,    -1,    -1,    29,    52,    -1,    -1,    -1,    -1,    35,
      36,    37,    38,    39,    40,    41,    -1,    65,    44,    45,
      -1,    -1,    70,    -1,    -1,    -1,    52,    75,    76,    77,
      78,    79,    80,    -1,    -1,    -1,    -1,    -1,    -1,    65,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    95,    96,    75,
      76,    77,    78,    79,    80,    -1,   104,   105,   106,    -1,
      -1,    -1,    -1,   111,    -1,    -1,    -1,    -1,    -1,    95,
      96,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   104,   105,
     106,    29,    -1,    -1,    -1,   111,    -1,    35,    36,    37,
      38,    39,    40,    41,    -1,    -1,    44,    45,    -1,    -1,
      -1,    -1,    -1,    29,    52,    -1,    -1,    -1,    -1,    35,
      36,    37,    38,    39,    40,    41,    -1,    65,    44,    45,
      -1,    -1,    -1,    -1,    -1,    -1,    52,    75,    76,    77,
      78,    79,    80,    -1,    -1,    -1,    -1,    -1,    -1,    65,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    95,    96,    75,
      76,    77,    78,    79,    80,    -1,   104,   105,   106,    -1,
      -1,    -1,    -1,   111,    -1,    -1,    -1,    -1,    -1,    95,
      96,    -1,    -1,    -1,    -1,    -1,    -1,    -1,   104,   105,
     106,    29,    -1,    -1,    -1,   111,    -1,    35,    36,    37,
      38,    39,    40,    41,    -1,    -1,    44,    45,    -1,    -1,
      -1,    -1,    -1,    -1,    52,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    65,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    75,    76,    77,
      78,    79,    80,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    95,    96,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,   104,   105,   106,    -1,
      -1,    -1,    -1,   111
};

/* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
   symbol of state STATE-NUM.  */
static const yytype_uint16 yystos[] =
{
       0,   128,   129,     0,     3,     4,     5,     6,     9,    12,
      13,    14,    15,    16,    18,    19,    20,    21,    22,    25,
      26,    30,    33,    35,    42,    65,    76,    89,    92,    93,
      94,    97,    98,    99,   102,   103,   107,   108,   109,   110,
     111,   112,   113,   114,   116,   117,   118,   119,   121,   122,
     123,   124,   125,   130,   131,   132,   133,   134,   135,   136,
     137,   138,   139,   140,   141,   142,   143,   145,   146,   147,
     148,   149,   150,   151,   152,   153,   154,   155,   156,   157,
     158,   159,   160,   161,   162,   163,   164,   165,   166,   167,
     168,   169,   178,   208,   209,   210,   211,   212,   213,   214,
     215,   272,   273,   278,   279,   287,    35,    42,    70,   187,
     271,   272,   210,   212,   214,   143,   208,   214,   133,   137,
     295,   295,    67,   296,    67,   296,    42,    65,    76,   199,
     200,   201,   202,   203,   204,   205,   206,   208,   214,   199,
     208,   214,    42,   144,   145,   158,   162,   163,   164,   167,
     208,   214,    42,   145,   158,   162,   163,   164,   167,   208,
     214,   144,   164,   145,   163,   164,   144,   296,   145,   163,
     296,   144,   145,   163,   144,   158,   145,   158,   163,    65,
     144,   162,   145,   162,   163,   187,   271,   292,   290,    65,
      72,   217,   219,   290,    65,    89,    65,    35,    42,   179,
     180,    70,    70,   187,   272,    66,    66,    66,   208,   214,
      70,   111,   136,   140,   141,   142,   143,   231,   270,   271,
      89,   270,    89,    65,   217,    42,    65,   203,   205,   207,
      65,   143,   202,   205,   288,   288,   288,   288,   288,   288,
     288,   288,   288,   288,    89,    89,    29,    35,    36,    37,
      38,    39,    40,    41,    44,    45,    52,    65,    75,    76,
      77,    78,    79,    80,    95,    96,   104,   105,   106,   111,
     141,   143,   151,   153,   155,   157,   161,   188,   236,   237,
     238,   239,   240,   241,   242,   243,   245,   246,   247,   248,
     249,   250,   251,   252,   253,   254,   255,   256,   257,   258,
     259,   260,   261,   262,   263,   264,   265,   267,   292,   187,
     292,    70,   292,    73,   245,   264,   268,    72,    65,   237,
     291,   291,    67,    71,   179,   179,    70,   217,   217,   115,
     225,   226,   227,    65,    76,   208,   212,    65,    76,   208,
     208,    70,   136,   208,   199,   292,   207,    66,    66,   217,
     207,    65,   202,   205,   279,   280,   280,   280,   280,    65,
     245,    65,   245,   245,    35,   188,   267,   292,    65,    65,
      65,   253,    65,    76,   216,   217,   220,   221,   216,    66,
      36,    43,    44,    45,    65,    72,    74,    55,    56,    57,
      58,    59,    60,    61,    62,    63,    64,    90,   266,    65,
     245,   253,    76,    81,    82,    77,    78,    46,    47,    48,
      49,    83,    84,    50,    51,    75,    85,    86,    52,    53,
      87,    66,    67,    70,   292,    70,   170,    35,   140,   141,
     142,   143,   152,   153,   182,   183,   184,   185,   186,   218,
      73,   268,     3,     4,     5,     6,     7,     8,     9,    10,
      11,    12,    13,    14,    15,    16,    17,    18,    19,    20,
      21,    22,    23,    24,    25,    26,    27,    28,    29,    30,
      31,    32,    33,    34,    35,    91,    92,    93,    94,    95,
      96,    97,    98,    99,   102,   103,   104,   105,   106,   107,
     108,   109,   110,   111,   112,   113,   114,   115,   116,   117,
     118,   119,   120,   121,   122,   123,   124,   125,   274,   275,
     277,    66,    90,   181,   181,    71,   180,    67,    71,    67,
      71,   179,    35,   228,   229,   227,    65,   212,    65,   143,
     143,   288,   288,   225,   289,   289,    66,   217,   217,    66,
      66,   207,   270,   270,   270,   270,   188,   188,    66,    66,
      70,   224,   188,   188,   265,   217,   220,   221,   143,   216,
     187,    66,   244,   265,   267,   187,   265,   188,   253,   253,
     253,   254,   254,   255,   255,   256,   256,   256,   256,   257,
     257,   258,   259,   260,   261,   262,    88,   267,   265,   170,
      70,   170,    89,   141,   143,   171,   172,   173,   293,   290,
      65,    76,   202,   208,   216,   202,   208,   216,    65,    76,
     208,   216,   208,   216,    67,    67,   294,   294,    73,    66,
      67,    65,   276,   268,    71,    71,    67,    71,    67,    89,
       7,    10,    11,    17,    23,    24,    27,    28,    31,    32,
      34,    35,    42,   111,   136,   187,   222,   223,   224,   230,
     232,   233,   234,   235,   267,   269,   281,   287,   293,    65,
     229,   280,   280,    66,    90,   189,   189,   189,   189,    66,
      66,    70,   253,   225,   293,    67,    67,    67,    66,    66,
      66,   216,    66,    67,    73,    66,   264,    88,   293,   170,
     293,    88,    89,   174,   177,   199,    89,   175,   177,   208,
      67,    89,    67,    89,    71,   143,   288,   288,   288,   288,
     143,   288,   288,    54,   184,   186,    66,    66,    66,   277,
      66,   244,    71,    35,    89,    65,   268,   269,    89,    65,
      88,    76,   187,   222,    65,    65,    88,    89,    65,   145,
      71,   293,   270,   270,    35,    70,    72,    74,   190,   191,
     194,   195,   196,   197,   198,   265,   192,   193,   229,    66,
     242,   188,   188,   217,   265,   264,    71,   293,    71,   268,
     270,   176,   177,   270,   176,   175,   174,   270,   270,   270,
     270,   270,   270,   276,    66,   267,    54,    88,    89,   269,
     222,   267,    89,    34,   267,   267,   270,   237,   282,    65,
      71,   189,   189,    88,   193,   268,    35,    42,   191,    72,
      90,   196,    71,   190,    71,    66,    66,    66,    71,   270,
     270,    66,   268,   222,    89,    89,    65,    66,    66,   222,
      88,    66,   282,    71,   190,    54,    73,   268,    67,   222,
      88,   269,   267,   222,   222,    72,   237,   283,   284,   285,
      89,    66,    71,   268,    54,    73,   222,    89,    66,     8,
     277,    65,    88,    67,    89,    73,   268,   269,    89,   222,
      73,   267,   283,   285,    73,    66,   237,    66,    88,   222,
      65,   237,   286,   267,    67,    66,   237
};

#define yyerrok		(yyerrstatus = 0)
#define yyclearin	(yychar = YYEMPTY)
#define YYEMPTY		(-2)
#define YYEOF		0

#define YYACCEPT	goto yyacceptlab
#define YYABORT		goto yyabortlab
#define YYERROR		goto yyerrorlab


/* Like YYERROR except do call yyerror.  This remains here temporarily
   to ease the transition to the new meaning of YYERROR, for GCC.
   Once GCC version 2 has supplanted version 1, this can go.  */

#define YYFAIL		goto yyerrlab

#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)					\
do								\
  if (yychar == YYEMPTY && yylen == 1)				\
    {								\
      yychar = (Token);						\
      yylval = (Value);						\
      yytoken = YYTRANSLATE (yychar);				\
      YYPOPSTACK (1);						\
      goto yybackup;						\
    }								\
  else								\
    {								\
      yyerror (YY_("syntax error: cannot back up")); \
      YYERROR;							\
    }								\
while (YYID (0))


#define YYTERROR	1
#define YYERRCODE	256


/* YYLLOC_DEFAULT -- Set CURRENT to span from RHS[1] to RHS[N].
   If N is 0, then set CURRENT to the empty location which ends
   the previous symbol: RHS[0] (always defined).  */

#define YYRHSLOC(Rhs, K) ((Rhs)[K])
#ifndef YYLLOC_DEFAULT
# define YYLLOC_DEFAULT(Current, Rhs, N)				\
    do									\
      if (YYID (N))                                                    \
	{								\
	  (Current).first_line   = YYRHSLOC (Rhs, 1).first_line;	\
	  (Current).first_column = YYRHSLOC (Rhs, 1).first_column;	\
	  (Current).last_line    = YYRHSLOC (Rhs, N).last_line;		\
	  (Current).last_column  = YYRHSLOC (Rhs, N).last_column;	\
	}								\
      else								\
	{								\
	  (Current).first_line   = (Current).last_line   =		\
	    YYRHSLOC (Rhs, 0).last_line;				\
	  (Current).first_column = (Current).last_column =		\
	    YYRHSLOC (Rhs, 0).last_column;				\
	}								\
    while (YYID (0))
#endif


/* YY_LOCATION_PRINT -- Print the location on the stream.
   This macro was not mandated originally: define only if we know
   we won't break user code: when these are the locations we know.  */

#ifndef YY_LOCATION_PRINT
# if YYLTYPE_IS_TRIVIAL
#  define YY_LOCATION_PRINT(File, Loc)			\
     fprintf (File, "%d.%d-%d.%d",			\
	      (Loc).first_line, (Loc).first_column,	\
	      (Loc).last_line,  (Loc).last_column)
# else
#  define YY_LOCATION_PRINT(File, Loc) ((void) 0)
# endif
#endif


/* YYLEX -- calling `yylex' with the right arguments.  */

#ifdef YYLEX_PARAM
# define YYLEX yylex (YYLEX_PARAM)
#else
# define YYLEX yylex ()
#endif

/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)			\
do {						\
  if (yydebug)					\
    YYFPRINTF Args;				\
} while (YYID (0))

# define YY_SYMBOL_PRINT(Title, Type, Value, Location)			  \
do {									  \
  if (yydebug)								  \
    {									  \
      YYFPRINTF (stderr, "%s ", Title);					  \
      yy_symbol_print (stderr,						  \
		  Type, Value); \
      YYFPRINTF (stderr, "\n");						  \
    }									  \
} while (YYID (0))


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_value_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_value_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (!yyvaluep)
    return;
# ifdef YYPRINT
  if (yytype < YYNTOKENS)
    YYPRINT (yyoutput, yytoknum[yytype], *yyvaluep);
# else
  YYUSE (yyoutput);
# endif
  switch (yytype)
    {
      default:
	break;
    }
}


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (yytype < YYNTOKENS)
    YYFPRINTF (yyoutput, "token %s (", yytname[yytype]);
  else
    YYFPRINTF (yyoutput, "nterm %s (", yytname[yytype]);

  yy_symbol_value_print (yyoutput, yytype, yyvaluep);
  YYFPRINTF (yyoutput, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_stack_print (yytype_int16 *yybottom, yytype_int16 *yytop)
#else
static void
yy_stack_print (yybottom, yytop)
    yytype_int16 *yybottom;
    yytype_int16 *yytop;
#endif
{
  YYFPRINTF (stderr, "Stack now");
  for (; yybottom <= yytop; yybottom++)
    {
      int yybot = *yybottom;
      YYFPRINTF (stderr, " %d", yybot);
    }
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)				\
do {								\
  if (yydebug)							\
    yy_stack_print ((Bottom), (Top));				\
} while (YYID (0))


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_reduce_print (YYSTYPE *yyvsp, int yyrule)
#else
static void
yy_reduce_print (yyvsp, yyrule)
    YYSTYPE *yyvsp;
    int yyrule;
#endif
{
  int yynrhs = yyr2[yyrule];
  int yyi;
  unsigned long int yylno = yyrline[yyrule];
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %lu):\n",
	     yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      YYFPRINTF (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr, yyrhs[yyprhs[yyrule] + yyi],
		       &(yyvsp[(yyi + 1) - (yynrhs)])
		       		       );
      YYFPRINTF (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)		\
do {					\
  if (yydebug)				\
    yy_reduce_print (yyvsp, Rule); \
} while (YYID (0))

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args)
# define YY_SYMBOL_PRINT(Title, Type, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef	YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif



#if YYERROR_VERBOSE

# ifndef yystrlen
#  if defined __GLIBC__ && defined _STRING_H
#   define yystrlen strlen
#  else
/* Return the length of YYSTR.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static YYSIZE_T
yystrlen (const char *yystr)
#else
static YYSIZE_T
yystrlen (yystr)
    const char *yystr;
#endif
{
  YYSIZE_T yylen;
  for (yylen = 0; yystr[yylen]; yylen++)
    continue;
  return yylen;
}
#  endif
# endif

# ifndef yystpcpy
#  if defined __GLIBC__ && defined _STRING_H && defined _GNU_SOURCE
#   define yystpcpy stpcpy
#  else
/* Copy YYSRC to YYDEST, returning the address of the terminating '\0' in
   YYDEST.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static char *
yystpcpy (char *yydest, const char *yysrc)
#else
static char *
yystpcpy (yydest, yysrc)
    char *yydest;
    const char *yysrc;
#endif
{
  char *yyd = yydest;
  const char *yys = yysrc;

  while ((*yyd++ = *yys++) != '\0')
    continue;

  return yyd - 1;
}
#  endif
# endif

# ifndef yytnamerr
/* Copy to YYRES the contents of YYSTR after stripping away unnecessary
   quotes and backslashes, so that it's suitable for yyerror.  The
   heuristic is that double-quoting is unnecessary unless the string
   contains an apostrophe, a comma, or backslash (other than
   backslash-backslash).  YYSTR is taken from yytname.  If YYRES is
   null, do not copy; instead, return the length of what the result
   would have been.  */
static YYSIZE_T
yytnamerr (char *yyres, const char *yystr)
{
  if (*yystr == '"')
    {
      YYSIZE_T yyn = 0;
      char const *yyp = yystr;

      for (;;)
	switch (*++yyp)
	  {
	  case '\'':
	  case ',':
	    goto do_not_strip_quotes;

	  case '\\':
	    if (*++yyp != '\\')
	      goto do_not_strip_quotes;
	    /* Fall through.  */
	  default:
	    if (yyres)
	      yyres[yyn] = *yyp;
	    yyn++;
	    break;

	  case '"':
	    if (yyres)
	      yyres[yyn] = '\0';
	    return yyn;
	  }
    do_not_strip_quotes: ;
    }

  if (! yyres)
    return yystrlen (yystr);

  return yystpcpy (yyres, yystr) - yyres;
}
# endif

/* Copy into YYRESULT an error message about the unexpected token
   YYCHAR while in state YYSTATE.  Return the number of bytes copied,
   including the terminating null byte.  If YYRESULT is null, do not
   copy anything; just return the number of bytes that would be
   copied.  As a special case, return 0 if an ordinary "syntax error"
   message will do.  Return YYSIZE_MAXIMUM if overflow occurs during
   size calculation.  */
static YYSIZE_T
yysyntax_error (char *yyresult, int yystate, int yychar)
{
  int yyn = yypact[yystate];

  if (! (YYPACT_NINF < yyn && yyn <= YYLAST))
    return 0;
  else
    {
      int yytype = YYTRANSLATE (yychar);
      YYSIZE_T yysize0 = yytnamerr (0, yytname[yytype]);
      YYSIZE_T yysize = yysize0;
      YYSIZE_T yysize1;
      int yysize_overflow = 0;
      enum { YYERROR_VERBOSE_ARGS_MAXIMUM = 5 };
      char const *yyarg[YYERROR_VERBOSE_ARGS_MAXIMUM];
      int yyx;

# if 0
      /* This is so xgettext sees the translatable formats that are
	 constructed on the fly.  */
      YY_("syntax error, unexpected %s");
      YY_("syntax error, unexpected %s, expecting %s");
      YY_("syntax error, unexpected %s, expecting %s or %s");
      YY_("syntax error, unexpected %s, expecting %s or %s or %s");
      YY_("syntax error, unexpected %s, expecting %s or %s or %s or %s");
# endif
      char *yyfmt;
      char const *yyf;
      static char const yyunexpected[] = "syntax error, unexpected %s";
      static char const yyexpecting[] = ", expecting %s";
      static char const yyor[] = " or %s";
      char yyformat[sizeof yyunexpected
		    + sizeof yyexpecting - 1
		    + ((YYERROR_VERBOSE_ARGS_MAXIMUM - 2)
		       * (sizeof yyor - 1))];
      char const *yyprefix = yyexpecting;

      /* Start YYX at -YYN if negative to avoid negative indexes in
	 YYCHECK.  */
      int yyxbegin = yyn < 0 ? -yyn : 0;

      /* Stay within bounds of both yycheck and yytname.  */
      int yychecklim = YYLAST - yyn + 1;
      int yyxend = yychecklim < YYNTOKENS ? yychecklim : YYNTOKENS;
      int yycount = 1;

      yyarg[0] = yytname[yytype];
      yyfmt = yystpcpy (yyformat, yyunexpected);

      for (yyx = yyxbegin; yyx < yyxend; ++yyx)
	if (yycheck[yyx + yyn] == yyx && yyx != YYTERROR)
	  {
	    if (yycount == YYERROR_VERBOSE_ARGS_MAXIMUM)
	      {
		yycount = 1;
		yysize = yysize0;
		yyformat[sizeof yyunexpected - 1] = '\0';
		break;
	      }
	    yyarg[yycount++] = yytname[yyx];
	    yysize1 = yysize + yytnamerr (0, yytname[yyx]);
	    yysize_overflow |= (yysize1 < yysize);
	    yysize = yysize1;
	    yyfmt = yystpcpy (yyfmt, yyprefix);
	    yyprefix = yyor;
	  }

      yyf = YY_(yyformat);
      yysize1 = yysize + yystrlen (yyf);
      yysize_overflow |= (yysize1 < yysize);
      yysize = yysize1;

      if (yysize_overflow)
	return YYSIZE_MAXIMUM;

      if (yyresult)
	{
	  /* Avoid sprintf, as that infringes on the user's name space.
	     Don't have undefined behavior even if the translation
	     produced a string with the wrong number of "%s"s.  */
	  char *yyp = yyresult;
	  int yyi = 0;
	  while ((*yyp = *yyf) != '\0')
	    {
	      if (*yyp == '%' && yyf[1] == 's' && yyi < yycount)
		{
		  yyp += yytnamerr (yyp, yyarg[yyi++]);
		  yyf += 2;
		}
	      else
		{
		  yyp++;
		  yyf++;
		}
	    }
	}
      return yysize;
    }
}
#endif /* YYERROR_VERBOSE */


/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yydestruct (const char *yymsg, int yytype, YYSTYPE *yyvaluep)
#else
static void
yydestruct (yymsg, yytype, yyvaluep)
    const char *yymsg;
    int yytype;
    YYSTYPE *yyvaluep;
#endif
{
  YYUSE (yyvaluep);

  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yytype, yyvaluep, yylocationp);

  switch (yytype)
    {

      default:
	break;
    }
}

/* Prevent warnings from -Wmissing-prototypes.  */
#ifdef YYPARSE_PARAM
#if defined __STDC__ || defined __cplusplus
int yyparse (void *YYPARSE_PARAM);
#else
int yyparse ();
#endif
#else /* ! YYPARSE_PARAM */
#if defined __STDC__ || defined __cplusplus
int yyparse (void);
#else
int yyparse ();
#endif
#endif /* ! YYPARSE_PARAM */


/* The lookahead symbol.  */
int yychar;

/* The semantic value of the lookahead symbol.  */
YYSTYPE yylval;

/* Number of syntax errors so far.  */
int yynerrs;



/*-------------------------.
| yyparse or yypush_parse.  |
`-------------------------*/

#ifdef YYPARSE_PARAM
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void *YYPARSE_PARAM)
#else
int
yyparse (YYPARSE_PARAM)
    void *YYPARSE_PARAM;
#endif
#else /* ! YYPARSE_PARAM */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void)
#else
int
yyparse ()

#endif
#endif
{


    int yystate;
    /* Number of tokens to shift before error messages enabled.  */
    int yyerrstatus;

    /* The stacks and their tools:
       `yyss': related to states.
       `yyvs': related to semantic values.

       Refer to the stacks thru separate pointers, to allow yyoverflow
       to reallocate them elsewhere.  */

    /* The state stack.  */
    yytype_int16 yyssa[YYINITDEPTH];
    yytype_int16 *yyss;
    yytype_int16 *yyssp;

    /* The semantic value stack.  */
    YYSTYPE yyvsa[YYINITDEPTH];
    YYSTYPE *yyvs;
    YYSTYPE *yyvsp;

    YYSIZE_T yystacksize;

  int yyn;
  int yyresult;
  /* Lookahead token as an internal (translated) token number.  */
  int yytoken;
  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;

#if YYERROR_VERBOSE
  /* Buffer for error messages, and its allocated size.  */
  char yymsgbuf[128];
  char *yymsg = yymsgbuf;
  YYSIZE_T yymsg_alloc = sizeof yymsgbuf;
#endif

#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N))

  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  yytoken = 0;
  yyss = yyssa;
  yyvs = yyvsa;
  yystacksize = YYINITDEPTH;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY; /* Cause a token to be read.  */

  /* Initialize stack pointers.
     Waste one element of value and location stack
     so that they stay on the same level as the state stack.
     The wasted elements are never initialized.  */
  yyssp = yyss;
  yyvsp = yyvs;

  goto yysetstate;

/*------------------------------------------------------------.
| yynewstate -- Push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
 yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;

 yysetstate:
  *yyssp = yystate;

  if (yyss + yystacksize - 1 <= yyssp)
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYSIZE_T yysize = yyssp - yyss + 1;

#ifdef yyoverflow
      {
	/* Give user a chance to reallocate the stack.  Use copies of
	   these so that the &'s don't force the real ones into
	   memory.  */
	YYSTYPE *yyvs1 = yyvs;
	yytype_int16 *yyss1 = yyss;

	/* Each stack pointer address is followed by the size of the
	   data in use in that stack, in bytes.  This used to be a
	   conditional around just the two extra args, but that might
	   be undefined if yyoverflow is a macro.  */
	yyoverflow (YY_("memory exhausted"),
		    &yyss1, yysize * sizeof (*yyssp),
		    &yyvs1, yysize * sizeof (*yyvsp),
		    &yystacksize);

	yyss = yyss1;
	yyvs = yyvs1;
      }
#else /* no yyoverflow */
# ifndef YYSTACK_RELOCATE
      goto yyexhaustedlab;
# else
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
	goto yyexhaustedlab;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
	yystacksize = YYMAXDEPTH;

      {
	yytype_int16 *yyss1 = yyss;
	union yyalloc *yyptr =
	  (union yyalloc *) YYSTACK_ALLOC (YYSTACK_BYTES (yystacksize));
	if (! yyptr)
	  goto yyexhaustedlab;
	YYSTACK_RELOCATE (yyss_alloc, yyss);
	YYSTACK_RELOCATE (yyvs_alloc, yyvs);
#  undef YYSTACK_RELOCATE
	if (yyss1 != yyssa)
	  YYSTACK_FREE (yyss1);
      }
# endif
#endif /* no yyoverflow */

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;

      YYDPRINTF ((stderr, "Stack size increased to %lu\n",
		  (unsigned long int) yystacksize));

      if (yyss + yystacksize - 1 <= yyssp)
	YYABORT;
    }

  YYDPRINTF ((stderr, "Entering state %d\n", yystate));

  if (yystate == YYFINAL)
    YYACCEPT;

  goto yybackup;

/*-----------.
| yybackup.  |
`-----------*/
yybackup:

  /* Do appropriate processing given the current state.  Read a
     lookahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to lookahead token.  */
  yyn = yypact[yystate];
  if (yyn == YYPACT_NINF)
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* YYCHAR is either YYEMPTY or YYEOF or a valid lookahead symbol.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token: "));
      yychar = YYLEX;
    }

  if (yychar <= YYEOF)
    {
      yychar = yytoken = YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yyn == 0 || yyn == YYTABLE_NINF)
	goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the lookahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);

  /* Discard the shifted token.  */
  yychar = YYEMPTY;

  yystate = yyn;
  *++yyvsp = yylval;

  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- Do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     `$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];


  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
      

/* Line 1455 of yacc.c  */
#line 3126 "c.tab.c"
      default: break;
    }
  YY_SYMBOL_PRINT ("-> $$ =", yyr1[yyn], &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);

  *++yyvsp = yyval;

  /* Now `shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTOKENS] + *yyssp;
  if (0 <= yystate && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTOKENS];

  goto yynewstate;


/*------------------------------------.
| yyerrlab -- here on detecting error |
`------------------------------------*/
yyerrlab:
  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
#if ! YYERROR_VERBOSE
      yyerror (YY_("syntax error"));
#else
      {
	YYSIZE_T yysize = yysyntax_error (0, yystate, yychar);
	if (yymsg_alloc < yysize && yymsg_alloc < YYSTACK_ALLOC_MAXIMUM)
	  {
	    YYSIZE_T yyalloc = 2 * yysize;
	    if (! (yysize <= yyalloc && yyalloc <= YYSTACK_ALLOC_MAXIMUM))
	      yyalloc = YYSTACK_ALLOC_MAXIMUM;
	    if (yymsg != yymsgbuf)
	      YYSTACK_FREE (yymsg);
	    yymsg = (char *) YYSTACK_ALLOC (yyalloc);
	    if (yymsg)
	      yymsg_alloc = yyalloc;
	    else
	      {
		yymsg = yymsgbuf;
		yymsg_alloc = sizeof yymsgbuf;
	      }
	  }

	if (0 < yysize && yysize <= yymsg_alloc)
	  {
	    (void) yysyntax_error (yymsg, yystate, yychar);
	    yyerror (yymsg);
	  }
	else
	  {
	    yyerror (YY_("syntax error"));
	    if (yysize != 0)
	      goto yyexhaustedlab;
	  }
      }
#endif
    }



  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse lookahead token after an
	 error, discard it.  */

      if (yychar <= YYEOF)
	{
	  /* Return failure if at end of input.  */
	  if (yychar == YYEOF)
	    YYABORT;
	}
      else
	{
	  yydestruct ("Error: discarding",
		      yytoken, &yylval);
	  yychar = YYEMPTY;
	}
    }

  /* Else will try to reuse lookahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:

  /* Pacify compilers like GCC when the user code never invokes
     YYERROR and the label yyerrorlab therefore never appears in user
     code.  */
  if (/*CONSTCOND*/ 0)
     goto yyerrorlab;

  /* Do not reclaim the symbols of the rule which action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;	/* Each real token shifted decrements this.  */

  for (;;)
    {
      yyn = yypact[yystate];
      if (yyn != YYPACT_NINF)
	{
	  yyn += YYTERROR;
	  if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYTERROR)
	    {
	      yyn = yytable[yyn];
	      if (0 < yyn)
		break;
	    }
	}

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
	YYABORT;


      yydestruct ("Error: popping",
		  yystos[yystate], yyvsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  *++yyvsp = yylval;


  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", yystos[yyn], yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturn;

/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturn;

#if !defined(yyoverflow) || YYERROR_VERBOSE
/*-------------------------------------------------.
| yyexhaustedlab -- memory exhaustion comes here.  |
`-------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  /* Fall through.  */
#endif

yyreturn:
  if (yychar != YYEMPTY)
     yydestruct ("Cleanup: discarding lookahead",
		 yytoken, &yylval);
  /* Do not reclaim the symbols of the rule which action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
		  yystos[*yyssp], yyvsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif
#if YYERROR_VERBOSE
  if (yymsg != yymsgbuf)
    YYSTACK_FREE (yymsg);
#endif
  /* Make sure YYID is used.  */
  return YYID (yyresult);
}



