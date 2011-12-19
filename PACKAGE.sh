#!/bin/bash

make
jar cf Translator.jar classes
mkdir package
mkdir package/dependencies
mkdir package/out

