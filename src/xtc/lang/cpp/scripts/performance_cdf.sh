#!/bin/bash

# This takes the performance statistics output from linux_test.sh or
# typechef_test.sh and produces a CDF of per-file times.

files=$@

if [[ -z $files ]]; then
    echo "Please pass a list of files."
    exit 1
fi

total=`cat $files | grep "^performance" \
    | awk 'BEGIN{sum = 0} {sum += 1} END{print sum}'`

cat $files | grep "^performance" | awk '{print $3, $2}' | sort -n \
    | awk -v total=$total 'BEGIN{seconds=0; count=0}
{

if ($1 > seconds) {
print seconds, count / total
  while ($1 > seconds) {
    seconds++
  }
}

count++

}

END{print seconds, count / total}'
