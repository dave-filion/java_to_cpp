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

if [ -z $files ]
then
  # Get all linux file compilation units from the x86 version.  This
  # also strips off the beginning "./" so that it won't be in the
  # expansion of the __FILE__ built-in macro.
    files=`find . -name "*.c" | sed 's/^\.\/\(.*\)$/\1/' \
        | egrep -v "^arch/([^x]|x[^8])" | sort`
fi

for file in $files
do
    linux_config_file=../$preprocessed/$file.cmdline

    if [ -f $linux_config_file ]; then
        # Then it is a compilation unit.
        echo $file
    fi
done
