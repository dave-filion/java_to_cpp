#!/bin/bash

# Computes static preprocesor usage statistics for a Linux kernel.

# It uses cloc to count source lines of code.  Warn the user if they
# don't have it installed or it's not in the path.

which cloc >/dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "warning: \"cloc\" not found.  It is needed for counting source" 1>&2
    echo "lines of code.  Continuing without it" 1>&1
fi


# Get the set of subdirectories from the Linux source to use.

dirs=`find . -maxdepth 1 -mindepth 1 -type d | grep -v arch`
dirs="$dirs ./arch/x86"


# Print linux version and subdirectories included in stats.

echo "current_dir `basename $PWD`"
for subdir in $dirs; do
    echo "subdirectory $subdir"
done


# File counts

c_h_file_count=`find $dirs -name "*.[c|h]" | wc -l`
c_file_count=`find $dirs -name "*.c" | wc -l`
h_file_count=`find $dirs -name "*.h" | wc -l`

echo "c_h_file_count $c_h_file_count"
echo "c_file_count $c_file_count"
echo "h_file_count $h_file_count"
echo "c_file_pct "$( echo "$c_file_count / $c_h_file_count" | bc -lq )
echo "h_file_pct "$( echo "$h_file_count / $c_h_file_count" | bc -lq )


# Config var counts (using Kconfig files)

echo "config_var_count " `find $dirs -name Kconfig | xargs cat \
    | grep "^config" | awk '{print }' | sort | uniq | wc -l`


# Macro definition

alldefines=`find $dirs -name "*.[c|h]" | xargs cat | grep "^# *define" | wc -l`
defines_c_files=`find $dirs -name "*.[c]" | xargs cat \
    | grep "^# *define" | wc -l`
defines_h_files=`find $dirs -name "*.[h]" | xargs cat \
    | grep "^# *define" | wc -l`

echo "defines_total $alldefines"
echo "defines_c_files $defines_c_files"
echo "defines_h_files $defines_h_files"
echo "defines_c_files_pct "$( echo "$defines_c_files / $alldefines" | bc -lq )
echo "defines_h_files_pct "$( echo "$defines_h_files / $alldefines" | bc -lq )


# Macro usage

find $dirs -name "*.[c|h]" | xargs cat | grep "^# *define" | awk '{print $2}' \
    | awk -F'(' '{print $1}' | sort | uniq > uniquemacros.txt

macros=`cat uniquemacros.txt | wc -l`
macros_c_files=`find $dirs -name "*.c" | xargs cat \
    | grep "^# *define" | awk '{print $2}' | awk -F'(' '{print $1}' \
    | sort | uniq | wc -l`
macros_h_files=`find $dirs -name "*.h" | xargs cat \
    | grep "^# *define" | awk '{print $2}' | awk -F'(' '{print $1}' \
    | sort | uniq | wc -l`

echo "macros_total $macros"
echo "macros_c_files $macros_c_files"
echo "macros_h_files $macros_h_files"
echo "macros_c_files_pct "$( echo "$macros_c_files / $macros" | bc -lq )
echo "macros_h_files_pct "$( echo "$macros_h_files / $macros" | bc -lq )
echo "avg_defs_per_macro " `echo "$alldefines / $macros" | bc -lq`


# Conditionals

start_conditionals_total=`find $dirs -name "*.[c|h]" | xargs cat \
    | grep "^# *if" | wc -l`
start_conditionals_c_files=`find $dirs -name "*.c" | xargs cat \
    | grep "^# *if" | wc -l`
start_conditionals_h_files=`find $dirs -name "*.h" | xargs cat \
    | grep "^# *if" | wc -l`
echo "start_conditionals_total $start_conditionals_total"
echo "start_conditionals_c_files $start_conditionals_c_files"
echo "start_conditionals_h_files $start_conditionals_h_files"
echo "start_conditionals_c_files_pct "$( echo "$start_conditionals_c_files / $start_conditionals_total" | bc -lq )
echo "start_conditionals_h_files_pct "$( echo "$start_conditionals_h_files / $start_conditionals_total" | bc -lq )


# Includes

includes_total=`find $dirs -name "*.[c|h]" | xargs cat \
    | grep "^# *include" | wc -l`
includes_c_files=`find $dirs -name "*.c" | xargs cat \
    | grep "^# *include" | wc -l`
includes_h_files=`find $dirs -name "*.h" | xargs cat \
    | grep "^# *include" | wc -l`

echo "includes_total $includes_total"
echo "includes_c_files $includes_c_files"
echo "includes_h_files $includes_h_files"
echo "includes_c_files_pct "$( echo "$includes_c_files / $includes_total" | bc -lq )
echo "includes_h_files_pct "$( echo "$includes_h_files / $includes_total" | bc -lq )


# All directives

directives_total=`find $dirs -name "*.[c|h]" | xargs cat | egrep \
    "^# *(if|elif|endif|include|define|undef|error|line|warning|pragma)" \
    | wc -l`

directives_c_files=`find $dirs -name "*.c" | xargs cat | egrep \
    "^# *(if|elif|endif|include|define|undef|error|line|warning|pragma)" \
    | wc -l`

directives_h_files=`find $dirs -name "*.h" | xargs cat | egrep \
    "^# *(if|elif|endif|include|define|undef|error|line|warning|pragma)" \
    | wc -l`

echo "directives_total $directives_total"
echo "directives_c_files $directives_c_files"
echo "directives_h_files $directives_h_files"
echo "directives_c_files_pct "$( echo "$directives_c_files / $directives_total" | bc -lq )
echo "directives_h_files_pct "$( echo "$directives_h_files / $directives_total" | bc -lq )

echo ""
echo "[Source Lines of Code]"

cloc $dirs
