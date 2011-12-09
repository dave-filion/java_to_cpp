#!/bin/bash

# test the configuration-preserving preprocessor
# tests preprocessed output using gcc -E for one kernel configuration

if [ $# -lt 1 ]
then
  echo "USAGE: `basename $0` [args-to-superC] file"
  echo "where file is relative path of source file in kernel"
  exit 1
fi

file=${!#}

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

cmdfile=../$preprocessed/$file.cmdline
normalE=../$preprocessed/$file.E
preprocE=../$preprocessed/$file.preproc.E
preprocC=../$preprocessed/$file.preproc.c
preprocCargs=`cat $cmdfile | sed "s?$file?$preprocC?"`
diff=../$preprocessed/$file.diff

if [ -f $diff ]
then
  contents=`cat $diff`
  if [ "$contents" = "same" ]
  then
    echo "Skipping $file, already passed the test"
    exit
  fi
fi

if [ ! -f $cmdfile ]
then
  echo "No $cmdfile found.  Perhaps this file is not in the configuration."
  exit
fi

extraincludes=
tmp=
for arg in `cat $cmdfile`
do
  if [[ "$tmp" = "-I" || "$tmp" = "-isystem" ]]
  then
    extraincludes="$extraincludes $tmp $arg"
    tmp=
  fi

  if [[ "$arg" = "-I" || "$arg" = "-isystem" ]]
  then
    tmp=$arg
  fi
done

echo "Comparing $file"

echo "timekill.pl 300 $MAINCLASS java -ea -Xms2048m -Xmx2048m -Xss128m \
  $MAINCLASS $extraincludes -silent -E -include $CPPDATA/nonbooleans.h \
  $@ > $preprocC"
gcc -E $preprocCargs > $preprocE
java xtc.lang.cpp.cdiff $normalE $preprocE > $diff
retval=$?
cat $diff

if [ $retval -ne 0 ]; then
  exit 1
fi

exit 0

