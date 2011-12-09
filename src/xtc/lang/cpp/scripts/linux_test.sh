#!/bin/bash

# This script runs SuperC on a set of Linux files and saves the
# command-line arguments, per-file times, and SuperC output to a file
# named using the current date and time.


default_file_list=$CPPDATA/linux_file_list.txt

# Get options.

# Defaults
args= #empty
plain= #empty
host= #empty
port= #empty
file_list= #empty
test= #empty
out="linux.`date +%Y-%m-%d_%H-%M-%S`.txt"
out_dir= #empty

while getopts :S:rf:o:h:p:td: opt; do
    case $opt in
        S)
            args=$OPTARG
            ;;
        r)
            plain=true
            ;;
        f)
            file_list=$OPTARG
            ;;
        h)
            host=$OPTARG
            ;;
        p)
            port=$OPTARG
            ;;
        o)
            out=$OPTARG
            ;;
        t)
            test=true
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

if [[ -z $args && -z $plain && -z $host ]]; then
    echo "USAGE: `basename $0` [options] -S SuperC_flags"
    echo "       `basename $0` [options] -r"
    echo ""
    echo "  -S SuperC_flags    The SuperC command-line arguments."
    echo "  -r                 Run without SuperC arguments."
    echo ""
    echo "OPTIONS:"
    echo "  -f file_list.txt   Optionally specify a custom list of Linux files"
    echo "                     to run.  Default is $default_file_list."
    echo "  -h file_host       Optionally specify a filename server to get the"
    echo "                     Linux files to run on.  Cannot be used with -f."
    echo "  -p file_host_port  The port for the filename server."
    echo "  -o output_filename The name of the output file. Defaults to"
    echo "                     linux.date_and_time"
    echo "  -d out_dir         Write output to a directory, mirroring the path"
    echo "                     of the given filename in that directory."
    echo "  -t                 Check parameters but don't run SuperC."
    exit 0
fi

if [[ ! -z $args && ! -z $plain ]]; then
    echo "Please specify either -S for command-line arguments or -r for none."
    exit;
fi

if [[ ! -z $file_list && ! -z $host ]]; then
    echo "Cannot specify both a file and a server for the filename list."
    exit;
fi

if [[ ! -z $host && -z $port ]]; then
    echo "Please specify a port (-p port) to connect to at host \"$host\"."
    exit;
fi

if [[ -z $host ]]; then
    if [ -z $file_list ]; then
        file_list=$default_file_list
    fi

    if [ ! -f $file_list ]; then
        echo "The file list \"$file_list\" is not a valid file."
        exit 0
    fi

    length=0;
    for file in `cat $file_list`; do
        files[length]=$file
        length=$length+1
    done
fi

if [[ ! -z $out_dir ]]; then
    out_dir="-d $out_dir"
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

if [ -f $out ]; then
    echo "The output file \"$out\" already exists.  Exiting."
    exit 0
fi

# Check for the calculator program.

which bc >/dev/null 2>&1

if [[ $? -eq 0 ]]; then
    calc="bc -lq"
else
    calc= #empty
    echo "warning: calculator program \"bc\" not found on system, reporting \
start and"
    echo "         end times instead of run-times"
fi


if [[ ! -z $test ]]; then
    echo "Parameters passed tests.  Exiting.  Take out \"-t\" to run for real!"
    exit
fi

echo "Writing output to \"$out\""

echo "# start `date`" >> $out

finalize() {
    echo "# finish `date`" >> $out
    exit
}

if [ ! -z $plain ]; then
    echo "# SuperC was run without any parameters" >> $out
else
    echo "# SuperC was run with the following parameters: $args" >> $out
fi

i=0
while :; do
    before=`date +%s.%N`

    if [ -z $host ]; then
        if [[ $i -eq $length ]]; then
            echo "done";
            finalize;
            exit;
        fi
        file=${files[$i]}
        i=$i+1
    else
        file=`java xtc.lang.cpp.FilenameService -client $host $port`
        errcode=$?
        if [[ $errcode -eq 3 ]]; then
            echo "No more files."
            finalize;
            exit
        elif [[ $errcode -eq 4 ]]; then
            echo "No server available."
            finalize;
            exit
        elif [[ $errcode -ne 0 || -z $file ]]; then
            echo "Unknown file server error."
            finalize;
            exit
        fi
    fi

    echo "Processing $file"
    echo "# Processing $file" >> $out

    run_file_linux.sh -S "$args" $out_dir $file >> $out 2>&1

    after=`date +%s.%N`

    if [[ ! -z $calc ]]; then
        time=`echo "$after - $before" | $calc`
        echo "performance $file $time" >> $out
    else
        # If there is no calculator program on the system, just output
        # the before and after times.
        echo "performance_raw $file $before $after" >> $out
    fi

done

