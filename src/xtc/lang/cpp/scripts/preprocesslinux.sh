#!/bin/bash

if [ -z "$JAVA_DEV_ROOT" ]
then
  "please run env.sh first"
fi

. getlinuxconfiguration.sh

cd $LINUXVER

echo "Preprocessing the linux kernel source..."
. ./preprocess_linux_source.sh ../$preprocessed

cd ..

echo "All done!"



