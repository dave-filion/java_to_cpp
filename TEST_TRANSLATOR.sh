#!/bin/bash

cd /home/dave/xtc

make -q

java xtc.translator.translation.Translator Hello.java test_dir
