#!/bin/bash

# This script gives the start and end times
# of preprocessing and parsing the entire
# linux source.

if [ -z "$JAVA_DEV_ROOT" ]
then
  "please run env.sh first"
fi

if [ ! -d $LINUXVER ]
then
  echo "No linux downloaded yet.  Getting it..."
  . getlinux.sh
fi

preprocessed=$LINUXVER-preprocessed
outfile=baseline.txt

echo "Baseline test run" >> $outfile

if [ ! -f $LINUXVER/preprocess_linux_source.sh ]
then
  . getlinuxconfiguration.sh
fi

echo "Preprocessing the linux kernel source..."

echo "Time to preprocess linux source" >> $outfile

date >> $outfile
cd $LINUXVER
./preprocess_linux_source.sh ../$preprocessed
cd ..
date >> $outfile

files=`find $preprocessed -name "*.E" -type f | sort`

echo "Running linux torture test for the C grammar"

echo "Time to parse preprocessed linux source" >> $outfile
date >> $outfile

for i in $files
do
echo "Parsing $i"
superC.sh -silent $i 2>&1 | grep "warning: no accepted subparsers"
if [ $? -eq 0 ]; then
  echo $i >> $outfile
fi
done

date >> $outfile

echo "" >> $outfile