#!/bin/bash

cd ~/xtc

make

java xtc.translator.translation.Translator Hello.java test_dir


