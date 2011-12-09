#!/bin/bash

cd /home/dave/xtc

make

java xtc.translator.translation.Translator Hello.java test_dir

gedit out/test_dir.hello.Hello.h