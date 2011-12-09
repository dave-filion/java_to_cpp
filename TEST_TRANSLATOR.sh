#!/bin/bash

make -q

java xtc.translator.translation.Translator Hello.java test_dir
