#!/bin/bash

# test the configuration-preserving preprocessor
# tests preprocessed output using gcc -E for one kernel configuration

if [ -z "$JAVA_DEV_ROOT" ]
then
  "please run env.sh first"
fi

if [ `basename $PWD` != $LINUXVER ]
then
  echo "Please run from the $LINUXVER directory."
  exit 1
fi

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

outfile=cppcmplinux.txt

if [[ -f $outfile && -s $outfile ]]
then
  while true
  do
    echo "Results from a previous test found."
    read -p "Only test the files that failed? [Y/n] " yn
    case $yn in
      [Nn]* )
        files=
        break
        ;;
      * )
        files=`cat $outfile | sort`
        break
        ;;
    esac
  done
fi

if [[ -f $outfile && ! -s $outfile ]]
then
  while true
  do
    echo "The last test appears to have no failures."
    read -p "Run again? [y/N] " yn
    case $yn in
      [Yy]* )
        break
        ;;
      * )
        exit
        ;;
    esac
  done
fi

if [ -z $files ]
then
  #get all linux files, strip off beginning "./", don't include other archs
  files=`find . -name "*.c" | sed 's/^\.\/\(.*\)$/\1/' \
    | egrep -v "^arch/([^x]|x[^8])" | sort`
fi

if [ -f $outfile ]
then
  rm $outfile
fi
touch $outfile
for file in $files
do
  cppcmp.sh $file

  if [ $? -ne 0 ]
  then
    echo $file >> $outfile
  fi
done
