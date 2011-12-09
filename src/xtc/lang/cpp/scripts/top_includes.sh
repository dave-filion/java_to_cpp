#!/bin/bash

# This script finds the top-ten included header files based on the by
# the number of other files that include them.

echo "Current directory: `basename $PWD`"

dirs=`find . -maxdepth 1 -mindepth 1 -type d | grep -v arch`
dirs="$dirs ./arch/x86"

total=`find $dirs -name "*.[c|h]" | wc -l`
num_c=`find $dirs -name "*.c" | wc -l`
num_h=`find $dirs -name "*.h" | wc -l`

echo "top includes in .c and .h"
find $dirs -name "*.[c|h]" | xargs cat | grep "^#include" | awk '{print $2}' | sort | awk -v total=$total 'BEGIN{count=0; file="";}{if ($0 != file) { print count, count / total file; file = $0; count=1; } else { count++ }}' | sort -nr | head
echo

echo "top includes in .c"
find $dirs -name "*.c" | xargs cat | grep "^#include" | awk '{print $2}' | sort | awk -v total=$num_c 'BEGIN{count=0; file="";}{if ($0 != file) { print count, count / total, file; file = $0; count=1; } else { count++ }}' | sort -nr | head
echo

echo "top includes in .h"
find $dirs -name "*.h" | xargs cat | grep "^#include" | awk '{print $2}' | sort | awk -v total=$num_h 'BEGIN{count=0; file="";}{if ($0 != file) { print count, count / total, file; file = $0; count=1; } else { count++ }}' | sort -nr | head

