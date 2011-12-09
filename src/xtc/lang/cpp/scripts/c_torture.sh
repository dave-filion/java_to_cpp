#!/bin/bash

# Test the SuperC C parser on the gcc C torture test suite.  Only
# checks whether the parsing passed or failed.

if [ -z "$JAVA_DEV_ROOT" ]
then
    "please run env.sh first"
fi

outfile=c_torture.txt
tmp=tmpfile.c
rm $outfile
touch $outfile
for i in `find $JAVA_DEV_ROOT/fonda/xtc_testsuite/gcc.c-torture/ -name "*.c" -type f | sort`
do
    echo "Processing $i" 1>&2
    gcc -E $i > $tmp

    java -ea xtc.lang.cpp.SuperC -silent $tmp 2>&1 | grep "ACCEPT"

    if [ $? -ne 0 ]; then
        echo $i >> $outfile
    fi
done
rm $tmp

if [ ! -s $outfile ]
then
    echo "Passed c torture test."
else
    echo "Failed.  Check $outfile for which files it failed on."
fi
