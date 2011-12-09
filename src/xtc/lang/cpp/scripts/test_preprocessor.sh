#!/bin/bash

# Test the SuperC preprocessor by processing the file from the linux
# kernel and comparing the output the GNU preprocessor.

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

outfile=test_preprocessor.txt

if [[ -f $outfile && -s $outfile ]]
then
    while true
    do
        echo "Results from a previous test found."
        read -p "Only test the files that failed? [Y/n] " yn
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
  # Get all linux file compilation units from the x86 version.  This
  # also strips off the beginning "./" so that it won't be in the
  # expansion of the __FILE__ built-in macro.
    files=`get_file_list.sh`
fi

if [ -f $outfile ]
then
    rm $outfile
fi

touch $outfile

for i in $files
do
    echo "Preprocessing and testing $i"

    test_file_linux.sh -S -E $i

    if [ $? -ne 0 ]; then
        echo "Failed"
        echo $i >> $outfile
    fi
done
