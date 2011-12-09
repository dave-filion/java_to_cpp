#!/bin/bash

# This script distributes SuperC runs on a given set of servers.  The
# servers' SuperC installations can be tested first with this script
# as well.  It calls linux_test.sh on the servers remotely.


# Get options.

# Defaults
SuperC_args= #empty
test= #empty
verify= #empty
run= #empty
host= #empty
port= #empty
outbase= #empty
server_list= #empty
out_dir= #empty

while getopts :S:tvro:s:h:p:d: opt; do
    case $opt in
        S)
            SuperC_args=$OPTARG
            ;;
        t)
            test=-t
            ;;
        v)
            verify=true
            ;;
        r)
            run=true
            ;;
        o)
            outbase=$OPTARG
            ;;
        s)
            server_list=$OPTARG
            ;;
        h)
            host=$OPTARG
            ;;
        p)
            port=$OPTARG
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

if [[ $# -eq 0 && -z $server_list ]]; then
    echo "USAGE: `basename $0` [args] -v server1 server2 ..."
    echo "       `basename $0` [args] -r server1 server2 ..."
    echo ""
    echo "  -v   Verify the SuperC installation on the servers."
    echo "  -r   Run linux_test.sh on the given servers."
    echo ""
    echo "REQUIRED ARGUMENTS:"
    echo "  -h host_name     The host name of the filename server \
(see notes below)."
    echo "  -p host_port     The port of the filename server (see notes below)."
    echo "  -o output_base   The base name of the output file. Defaults to"
    echo "                   \"subparser_run_\".  This argument is mandatory."
    echo ""
    echo "OPTIONS:"
    echo "  -s server_list   Get the list of servers from a file instead."
    echo "  -S SuperC_args   The arguments to SuperC."
    echo "  -d out_dir       Write output to a directory, mirroring the path"
    echo "                   of the given filename in that directory."
    echo "  -t               Check the parameters before running."
    echo ""
    echo "NOTE: For the servers to know what files to process, run an"
    echo "      instance of \"java xtc.lang.cpp.FilenameService\" and"
    echo "      provide the hostname and port."
    exit 0
fi

if [ ! -z $server_list ]; then
    if [ ! -f $server_list ]; then
        echo "Invalid server list file \"$server_list\"."
        exit;
    fi
    servers=`cat $server_list`
else
    servers=$@
fi

if [[ ! -z $out_dir ]]; then
    out_dir="-d $out_dir"
fi

if [ ! -z $verify ]; then
    # Test the SuperC installation on all the given servers.

    for server in $servers; do
        echo "Testing $server"
        ssh $server ". $CPPDIR/scripts/env.sh; cd work/superC/linux-2.6.39/; test_file_linux.sh -S \"-printSource\" arch/x86/kernel/acpi/realmode/regs.c"
        if [ $? -ne 0 ]; then
            echo "SERVER $server TEST FAILED"
            exit
        fi
    done;
else
    # Run the subparser data collection on all the given servers.

    if [[ -z $host || -z $port ]]; then
        echo "Please specify a host and port name."
        exit
    fi

    if [[ -z $outbase ]]; then
        echo "Please specify an out_base for the files to write to."
        exit
    fi

    for server in $servers; do
        out=$outbase$server.txt
        ssh $server ". $CPPDIR/scripts/env.sh; cd work/superC/\$LINUXVER; nohup linux_test.sh -S \"$SuperC_args\" -h $host -p $port -o $out $out_dir $test;" > $outbase$server.program_output.txt 2>&1 &
    done

fi
