#!/bin/bash

# This script sets up SuperC environment variables.


########################  User Configuration #################################

JAVA_DEV_ROOT=~/work/java  # Location of xtc suite.

CPPDEV=~/work/SuperC  # Location of SuperC testbed.

LINUXVER=linux-2.6.39  # Linux version.

# Locations of SuperC-specific directories.

CPPDIR=$JAVA_DEV_ROOT/src/xtc/lang/cpp  # Source code.
CPPDATA=$JAVA_DEV_ROOT/data/cpp  # Miscellaneous data directory.
CPPTEST=$JAVA_DEV_ROOT/fonda/cpp_testsuite  # Test suite.

# System-dependent headers and builtin macros.

GCCINCLUDEDIR=$CPPDATA/gcc/x86_64-linux-gnu/4.4.5/include
BUILTINS=$JAVA_DEV_ROOT/data/cpp/builtins.h

# TypeChef linux header settings.

TYPECHEF_LINUXVER=linux-2.6.33.3
TYPECHEF_GCCINCLUDEDIR=/usr/lib/gcc/x86_64-linux-gnu/4.4.5/include

JAVA_ARGS="-Xms2048m -Xmx2048m -Xss128m" # JVM settings.


#######################  System Configuration ################################

# Java classpath and scripts path settings

PATH_SEP=:
CLASSPATH=$CLASSPATH:$JAVA_DEV_ROOT/classes:$JAVA_DEV_ROOT/bin/junit.jar\
:$JAVA_DEV_ROOT/bin/antlr.jar:$JAVA_DEV_ROOT/bin/javabdd.jar
PATH=$PATH:$CPPDIR:$CPPDIR/scripts


#Export the environment vars.

export JAVA_DEV_ROOT CPPDEV LINUXVER GCCINCLUDEDIR BUILTINS \
    TYPECHEF_LINUXVER TYPECHEF_GCCINCLUDEDIR JAVA_ARGS \
    CPPDIR CPPDATA CPPTEST PATH_SEP CLASSPATH PATH
