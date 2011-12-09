#!/bin/bash


# Test one linux compilation unit.  First process with SuperC.  Then
# preprocess the SuperC output and the original file with the maximal
# linux configuration.  The output of each should be the same.


# Get options.

while getopts :iS:c: opt; do
    case $opt in
        i)
            saveIntermediate=true
            ;;
        S)
            superc_flags="$OPTARG"
            ;;
        c)
            user_config_file=$OPTARG
            ;;
        \?)
            echo "Invalid argument: -$OPTARG"
            ;;
        :)
            echo "-$OPTARG requires an argument."
            ;;
    esac
done

shift $(($OPTIND - 1))


# Check that a filename was passed.

file=$1

if [ -z $file ]; then
    echo "USAGE: `basename $0` [-i] [-s \"SuperC_args\"] [-c config_file] file"
    echo ""
    echo "FLAGS:"
    echo "  -i            save (i)ntermediate files"
    echo "  -S \"args\"   pass arguments to (S)uperC"
    echo "  -c \"config\" specifies a configuration file to use when the "
    echo "                input file is not part of the x86 kernel."
    exit 0
fi


# Allow the user to specify a configuration file manually instead of
# finding one by filename.

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


# Preprocess the kernel if it isn't already.

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


# The SuperC output.

superc_out=../$preprocessed/$file.super.c


# The SuperC output preprocessed with the maximal linux configuration.

superc_out_E=$superc_out.E


# The original file preprocessed with the maxinal linux configuration.
# This file should already exist.

gcc_E=../$preprocessed/$file.E


# The difference between the preprocessed SuperC output and the
# preprocessed original

superc_diff=../$preprocessed/$file.diff


# The file containing configuration settings for the input file, as
# specified by gcc command-line arguments.

linux_config_file=../$preprocessed/$file.cmdline


# Check that the file is part of the maximal x86 linux configuration
# or the user specified a config file.

if [[ ! (-f $linux_config_file \
    || (! -z $user_config_file && -f $user_config_file)) ]]; then
    echo "This file is not part of the maximal x86 linux configuration.  \
You can specify a config file as the second argument to this command."
    exit 0
fi


# The configuration settings as command-line arguments.  The arguments
# include the filename.  Remove the filename so we can use the
# arguments for the SuperC output.  We use awk to blank out the last
# column, which is the filename.

if [[ ! -z $user_config_file && -f $user_config_file ]]; then
    config_file=$user_config_file
else
    config_file=$linux_config_file
fi

config=`cat $config_file | awk -F' ' '{$(NF) = ""; print $0}'`


# Process the file with SuperC

mkdir -p `dirname $superc_out`  # Ensure directory exists.

run_file_linux.sh -S "$superc_flags" $file > $superc_out


# Preprocess the output of SuperC under the maximal x86 linux
# configuration.

gcc -E $config $superc_out > $superc_out_E


if [ ! -z $user_config_file ]; then
    # If the user specified his own config file and the preprocessed
    # version of the linux file does not exist, create it.

    gcc -E $config $file > $gcc_E 2>/dev/null
fi

# Check that the preprocessed output of SuperC matches the
# preprocessed original file.

java xtc.lang.cpp.cdiff -s $superc_out_E $gcc_E > $superc_diff 2>/dev/null


# Save the exit code from cdiff.  This will be the exit code of this
# program too, since we want to report whether the SuperC output
# matches the original after preprocessing under the maximal x86 linux
# configuration.

retval=$?


if [ -z $saveIntermediate ]; then
    # Clean-up intermediate files.  This is to save space.

    rm $superc_out
    rm $superc_out_E
fi

# Exit and report whether the files matched.

if [ $retval -eq 0 ]; then
    echo "PASSED"
else
    echo "FAILED"
fi

exit $retval
