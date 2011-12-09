#!/bin/bash

# Tests the SuperC C parser on the preprocessed maximal linux
# configuration.


if [ -z "$JAVA_DEV_ROOT" ]
then
    "please run env.sh first"
fi

if [ `basename $PWD` != $LINUXVER ]
then
    echo "Please run from the $LINUXVER directory."
    exit 1
fi

preprocessed=$LINUXVER-preprocessed

if [ ! -d ../$preprocessed ]
then
    echo "Linux has not been preprocessed yet.  Preprocessing it..."
    cd ..
    . preprocesslinux.sh
    if [ ! -d ../$preprocessed ]
    then
        echo "Error: the kernel was not preprocessed"
        exit 1
    fi
    cd $LINUXVER
fi

outfile=linux_torture2.txt

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
    files=`find ../$preprocessed -name "*.c.E" -type f | sort`
fi

echo "Running linux torture test for the C grammar"

if [ -f $outfile ]
then
    rm $outfile
fi
touch $outfile
for i in $files
do
    echo "Parsing $i"

    run_file_linux.sh $i 2>&1 | grep "ACCEPT"

    if [ $? -ne 0 ]; then
        echo "Failed"
        echo $i >> $outfile
    fi
done