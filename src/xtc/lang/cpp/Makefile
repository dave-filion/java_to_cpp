# Makefile 2 
#***************************************************************************
# 
# Java development root directory.
# 
# (C) 1999 Jacob Dreyer - Geotechnical Software Services
# jacob.dreyer@geosoft.no - http://geosoft.no
#
# Modifications Copyright (C) 2001, 2004 Robert Grimm 
# rgrimm@alum.mit.edu
#
# Modifications Copyright (C) 2007 New York University
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
#
#***************************************************************************




#***************************************************************************
#
# This section describes the current package.
#
# o PACKAGE     - The complete package name. 
# o PACKAGE_LOC - Same as PACKAGE but with "/"s instead of "."s.
# o SOURCE      - List of the source files. Remember extension.
# o JNI_SOURCE  - Files from SOURCE that are to be built with the JAVAH 
#                 compiler.
# o JAR_EXTRAS  - None-class files and directories that are to be bundled
#                 into the jar archive.
#
#***************************************************************************

PACKAGE     = xtc.lang.cpp
PACKAGE_LOC = xtc/lang/cpp

SOURCE = \
	BuiltinsGenerator.java \
	Builtins.java \
	ForkMergeParserTables.java \
	ContextManager.java \
	Syntax.java \
	CTag.java \
	CLexer.java \
	ConditionParser.java \
	Stream.java \
	CLexerStream.java \
	DirectiveParser.java \
	HeaderFileManager.java \
	MacroTable.java \
	ConditionEvaluator.java \
	CTokenCreator.java \
	Preprocessor.java \
	ActionGenerator.java \
	Actions.java \
	CActionsBase.java \
	CActions.java \
	CParsingContext.java \
	ForkMergeParser.java \
	TokenFilter.java \
	FilenameService.java \
	cdiff.java \
	SuperC.java


JNI_SOURCE =

JAR_EXTRAS =


#***************************************************************************
#
# Include common part of makefile
#
#***************************************************************************

ifdef JAVA_DEV_ROOT
include $(JAVA_DEV_ROOT)/Makerules

manual:
	make manual.pdf

manual.pdf: manual.tex
	pdflatex manual.tex
	pdflatex manual.tex

configure:
	if [ -f Builtins.java ]; then rm Builtins.java; fi
	make BuiltinsGenerator.class
	make Builtins.java
	rm -f ${JAVA_DEV_ROOT}/classes/xtc/lang/cpp/SuperC.class
	make Builtins.class
	make SuperC.class
	$(JAVA) xtc.lang.cpp.Builtins > builtins.h

Builtins.java:
	cpp -v < empty.c 2> sysdirs > /dev/null
	cpp -dM < empty.c > builtins 2> /dev/null
	$(JAVA) xtc.lang.cpp.BuiltinsGenerator sysdirs builtins > Builtins.java
	rm sysdirs
	rm builtins

parsers:
	# parser
	bison -o c.tab.c c.y
	gcc -DYYDEBUG -DYYPRINT\(a,b,c\) -o gen_tables gen_tables.c
	./gen_tables > ForkMergeParserTables.java
	rm gen_tables
	make ForkMergeParserTables.class
	# ctags
	gcc -E -DTAG - < c.l | grep -v "^#" > CTag.java
	make CTag.class
	# lexer
	#   filter out line directives for java compilation
	gcc -E -DLEXER - < c.l | grep -v "^#" > c.l.lexer
	$(JAVA) -jar $(JAVA_DEV_ROOT)/bin/JFlex.jar c.l.lexer
	rm c.l.lexer
	# semantic actions
	make ActionGenerator.class
	gen_actions.pl < c.y | $(JAVA) xtc.lang.cpp.ActionGenerator CActionsBase > CActionsBase.java
	make CActionsBase.java
	$(RATS) -option 'parser(xtc.lang.cpp.ConditionParser)' Condition.rats

clobber-parsers :
	rm -f CLexer.java
	rm -f c.tab.c
	rm -f ForkMergeParserTables.java
	rm -f CActionsBase.java
	rm -f ConditionParser.java

endif
