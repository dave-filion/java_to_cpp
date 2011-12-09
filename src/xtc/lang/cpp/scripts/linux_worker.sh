#!/bin/bash

# Run SuperC on linux files given certain flags.  It takes a server
# and port to retrieve the linux file names to process for distributed
# operation.

# Get options.

if [[ -z $1 || -z $2 ]]; then
    echo "USAGE: `basename $0` [-S SuperC_args] host port"
    echo "touch ofdeath to stop processing."
    exit 0
fi

while getopts :S: opt; do
    case $opt in
        S)
            superc_flags=$OPTARG
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

server=$1
port=$2

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

if [ -f ofdeath ]; then
    echo "Please remove the ofdeath file."
    exit
fi


while : ; do
    file=`java xtc.lang.cpp.FilenameService -client $server $port`
    if [[ -z $file || -f ofdeath ]]; then
        echo "done"
        exit;
    fi

    echo "Processing $file, with $superc_flags, on $server:$port"

    data_file=data$port/$file.data
    tar_file=data$port/$file.data.tar.gz

    mkdir -p `dirname $data_file`

    run_file_linux.sh -S "$superc_flags" $file > $data_file 2>&1

    tar -C `dirname $data_file` -zcf $tar_file `basename $data_file`
    rm $data_file

done
