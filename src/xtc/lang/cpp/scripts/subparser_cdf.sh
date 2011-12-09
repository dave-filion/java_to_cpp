#!/bin/bash

# This takes the SuperC -parserStatistics output from processing
# several compilation units and creates a cumulative distribution of
# subparser sizes by percentage of parser loop iterations.

files=$@

if [[ -z $files ]]; then
    echo "Please pass the filenames as arguments."
    exit 1
fi

iterations=`cat $files | grep "iterations" \
    | awk 'BEGIN{sum = 0} {sum += $2} END{print sum}'`

cat $files | grep "^subparsers" \
    | awk -v iterations=$iterations '{print $2, ($3 / iterations) }' | sort -n \
    | awk '\
BEGIN{ subparsers=0; sum=0 } \
{if ($1 == subparsers) {
sum += $2;
} else {
print subparsers, sum;
subparsers=$1; sum=$2;
}} \
END{print subparsers, sum;} \
' \
    | awk 'BEGIN{sum = 0} {sum += $2; print $1, sum}'

