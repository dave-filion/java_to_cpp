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

PACKAGE     = xtc.lang.blink.agent
PACKAGE_LOC = xtc/lang/blink/agent
SOURCE = $(wildcard *.java)
JNI_SOURCE = 
JAR_EXTRAS = 

#***************************************************************************
#
# Include common part of makefile
#
#***************************************************************************

ifdef JAVA_DEV_ROOT
include $(JAVA_DEV_ROOT)/Makerules

JAVAC_OPTIONS  = -target 1.5 -g -Xlint:unchecked -d $(CLASS_DIR) -sourcepath $(SOURCE_DIR)

parsers :

analyzers :

documentation :

endif

# The following is for generating shared library files
ifdef JAVA_HOME
AGENT_DLL=$(JAVA_DEV_ROOT)/bin/$(DLL_PREFIX)jinn$(JNI_DLL_SUFFIX)
AGENT_SOURCES=Agent.java AgentVariable.java
AGENT_CLASS_DIR=$(JAVA_DEV_ROOT)/classes/xtc/lang/blink/agent/
AGENT_CLASSES=$(addprefix $(AGENT_CLASS_DIR),$(patsubst	\
%.java,%.class,$(AGENT_SOURCES)))
AGENT_C_SOURCE_FILES=agent.c agent_main.c common.c state.c \
j2c_proxy.c c2j_proxy.c agent_class.c options.c java_method.c jnicheck.c hashtable.c hashtable_itr.c
OSTYPE = $(shell echo $$OSTYPE)
ifneq (,$(findstring linux,$(OSTYPE)))
  DLL_CC  = gcc -lm  -Wall -g -shared -I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/linux -o 
  DLL_PREFIX = lib
  DLL_SUFFIX = .so
  JNI_DLL_SUFFIX = $(DLL_SUFFIX)
else
ifeq  ($(OSTYPE),cygwin)
  DLL_CC  = gcc -Wall -mno-cygwin -Wall -Wl,--add-stdcall-alias -shared -g -I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/win32 -o 
  DLL_PREFIX = 
  DLL_SUFFIX = .dll
  JNI_DLL_SUFFIX = $(DLL_SUFFIX)
else
ifeq ($(OSTYPE),win32)
  DLL_CC = cl /Zi /LD /I $(JAVA_HOME)/include /I $(JAVA_HOME)/include/win32 /Fe
  DLL_PREFIX =
  DLL_SUFFIX = .dll
  JNI_DLL_SUFFIX = $(DLL_SUFFIX)
endif
endif
endif
endif

ifdef DLL_CC
.PHONY: agent
agent: $(AGENT_DLL)

$(AGENT_DLL): $(AGENT_C_SOURCE_FILES) $(AGENT_CLASSES)
	$(DLL_CC)$@ $(AGENT_C_SOURCE_FILES)

c2j_proxy.c: classes  GenerateJNIFunctionProxy.java
	java -ea xtc.lang.blink.agent.GenerateJNIFunctionProxy -o c2j_proxy.c

agent_class.c: $(AGENT_CLASSES) classes
	java -ea xtc.lang.blink.agent.GenerateHardcodedJavaClass \
	-h agent_class.h -o agent_class.c \
	class_agent $(AGENT_CLASS_DIR)/Agent.class \
	class_agent_variable $(AGENT_CLASS_DIR)/AgentVariable.class \
	class_agent_jni_assertion_failure $(AGENT_CLASS_DIR)/JNIAssertionFailure.class

cleanagent: 
	rm -f $(AGENT_DLL) $(AGENT_CLASSES)
else
agent:
ifdef JAVA_HOME
	@echo agent does not yet support $(OSTYPE)
else
	@echo agent requires setting JAVA_HOME.
endif
endif
