#!/bin/bash

if [ ! -d $LINUXVER ]
then
  read -p "Downloading linux.  Okay? [Y/n]" yn
  case $yn in
    [Nn]* ) echo "Exiting without downloading."; exit;;
    * ) break;;
  esac
  wget http://www.kernel.org/pub/linux/kernel/v2.6/$LINUXVER.tar.bz2
fi

if [ -d $LINUXVER ]
then
  while true
  do
    read -p "Linux directory $LINUXVER exists.  Overwrite? [y/N] " yn
    case $yn in
      [Yy]* ) break;;
      * ) echo "Exiting without overwriting."; exit;;
    esac
  done
fi

echo "Deflating.  Just a moment..."
tar -jxvf $LINUXVER.tar.bz2 > /dev/null

echo "You're all set!"
