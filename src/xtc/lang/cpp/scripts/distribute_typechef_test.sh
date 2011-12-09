#!/bin/bash

# This script distributes SuperC runs on a given set of servers.  The
# servers' SuperC installations can be tested first with this script
# as well.  It calls linux_test.sh on the servers remotely.


# Get options.

# Defaults
args= #empty
test= #empty
system= #empty
host= #empty
port= #empty
outbase= #empty
server_list= #empty
unconstrained= #empty

while getopts :a:r:o:s:h:p:uw opt; do
    case $opt in
        a)
            args=$OPTARG
            ;;
        r)
            system=$OPTARG
            ;;
        u)
            unconstrained="-u"
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

if [[ $# -eq 0 && -z $server_list || -z $system || \
    "$system" != "SuperC" && "$system" != "TypeChef" ]]; then
    echo "USAGE: `basename $0` [args] -r SuperC|TypeChef server1 server2 ..."
    echo ""
    echo "  -r SuperC|TypeChef  Run typechef_test.sh on the given servers."
    echo ""
    echo "ARGUMENTS:"
    echo "  -a args          Pass extra args."
    echo "  -u               Run on unconstrained linux kernel."
    echo "  -o output_base   The base name of the output file. Defaults to"
    echo "                   \"subparser_run_\".  This argument is mandatory."
    echo "  -h host_name     The host name of the filename server \
(see notes below)."
    echo "  -p host_port     The port of the filename server (see notes below)."
    echo "  -s server_list   Get the list of servers from a file instead."
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


if [[ -z $host || -z $port ]]; then
    echo "Please specify a host and port name."
    exit
fi

if [[ -z $outbase ]]; then
    echo "Please specify an out_base for the files to write to."
    exit
fi

# Run on all the given servers.

for server in $servers; do
    out=$outbase$server.txt
    ssh $server ". $CPPDIR/scripts/env.sh; cd work/superC/TypeChef/; nohup typechef_test.sh -a \"$args\" $unconstrained -h $host -p $port -o $out $system;" > $outbase$server.program_output.txt 2>&1 &
done
