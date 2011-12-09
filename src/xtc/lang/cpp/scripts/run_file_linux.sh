#!/bin/bash

# Run SuperC with the necessary Linux includes.  It must be run from
# the linux kernel source directory, because "-I include/" is
# relative.


# Get options.

out_dir= #empty

while getopts :S:J:d: opt; do
    case $opt in
        S)
            superc_flags=$OPTARG
            ;;
        J)
            java_flags=$OPTARG
            ;;
	      d)
	          out_dir=$OPTARG
	          ;;
        \?)
            echo "Invalid argument: -$OPTARG"
            exit;
            ;;
        :)
            echo "-$OPTARG requires an argument."
            exit;
            ;;
    esac
done

shift $(($OPTIND - 1))


# Check that a filename was passed.

file=$1


# Check that a filename was passed.

if [ -z $file ]; then
    echo "USAGE: `basename $0` [options] file"
    echo ""
    echo "OPTIONS:"
    echo "  -J Java_args    Pass these arguments to the jvm."
    echo "  -S SuperC_args  Pass these arguments to SuperC."
    echo "  -d out_dir      Write output to a directory, mirroring the path"
    echo "                  of the given filename in that directory."
    exit 0
fi


# Make sure the SuperC environment is set up.

if [ -z "$JAVA_DEV_ROOT" ]
then
    "please run env.sh first"
fi


# Make sure the program is running from the linux directory.  This
# ensures that the __FILE__ macro will have the same path for SuperC.

if [ `basename $PWD` != $LINUXVER ]
then
    echo "Please run from the $LINUXVER directory."
    exit 1
fi

if [[ ! -z $out_dir ]]; then
    mkdir -p $out_dir/`dirname $file`
    java -ea $JAVA_ARGS $java_flags xtc.lang.cpp.SuperC -silent $superc_flags \
	      -include $BUILTINS -isystem $GCCINCLUDEDIR \
	      -I $PWD/arch/x86/include -I include \
	      -include $CPPDATA/nonbooleans.h $file >$out_dir/$file 2>&1
else
    java -ea $JAVA_ARGS $java_flags xtc.lang.cpp.SuperC -silent $superc_flags \
	      -include $BUILTINS -isystem $GCCINCLUDEDIR \
	      -I $PWD/arch/x86/include -I include \
	      -include $CPPDATA/nonbooleans.h $file
fi

