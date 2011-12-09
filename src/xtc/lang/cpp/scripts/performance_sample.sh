#!/bin/bash

# This script generates a sample of files evenly distributed by time
# to run SuperC on the file.

file=$1
size=$2

if [[ -z "$file" || -z "$size" ]]; then
    echo "USAGE: `basename $0` file_to_sample sample_size"
    exit 0
fi

if [ ! -f $file ]; then
    echo "The file doesn't exist: \"$file\""
    exit 0
fi

if [[ ! $size -gt 0 ]]; then
    echo "The sample size should be a non-zero integer: \"$size\""
    exit 0
fi

count=`cat $file | grep "^performance" | wc -l`

cat $file | grep "^performance" | awk '{ print $3, $2 }' | sort -n \
    | awk -v count=$count -v size=$size '
BEGIN{inc=count/size; l=0; i=0; sum=0}
{
if (l == int(i + .5)) {
  print $2
  sum += $1
  i += inc;
}

l++;

}

END{
sum += $1
print "predicted time: " sum > "/dev/stderr";
}
'