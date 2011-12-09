#!/bin/bash

# Test the SuperC directive parser by processing the files from the
# linux kernel and comparing the output against the original.

if [ -z "$JAVA_DEV_ROOT" ]
then
    "please run env.sh first"
fi

if [ ! -d $LINUXVER ]
then
    echo "No linux downloaded yet.  Getting it..."
    . getlinux.sh
fi

outfile=test_directiveParser.txt

if [[ -f $outfile && -s $outfile ]]
then
    while true
    do
        echo "Results from a previous test found."
        read -p "Only parse the files that failed? [Y/n] " yn
        case $yn in
            [Nn]* )
                files=
                break
                ;;
            * )
                files=`cat $outfile | sort`
                break
                ;;
        esac
    done
fi

if [[ -f $outfile && ! -s $outfile ]]
then
    while true
    do
        echo "The last test appears to have no failures."
        read -p "Run again? [y/N] " yn
        case $yn in
            [Yy]* )
                break
                ;;
            * )
                exit
                ;;
        esac
    done
fi

if [ -z $files ]
then
    files=`find $LINUXVER -name "*.[c|h]" -type f | sort`
fi

echo "Testing the directive parser on the linux kernel."

if [ -f $outfile ]
then
    rm $outfile
fi

touch $outfile

for i in $files
do
    echo "Lexing, directive parsing, and testing $i"

    # Lex the file with SuperC's lexer
    java xtc.lang.cpp.SuperC -silent -directiveParsing $i > $i.tmp 2>/dev/null

    java xtc.lang.cpp.cdiff $i $i.tmp >/dev/null 2>/dev/null

    if [ $? -ne 0 ]; then
        echo "Failed"
        echo $i >> $outfile
    fi

    # Clean-up temp file.
    rm $i.tmp
done
