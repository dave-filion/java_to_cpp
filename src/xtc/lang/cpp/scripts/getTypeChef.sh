#!/bin/bash

if [ ! -d TypeChef ]
then
    echo "Downloading TypeChef"
    git clone git://github.com/ckaestne/TypeChef.git
else
    echo "Already got TypeChef"
fi

echo "Building TypeChef"

cd TypeChef
java -Xmx512M -Xss10m -jar sbt-launch-0.7.5.jar clean update compile
# the scripts refere to version 0.7.4, symlink to avoid editing them
if [ ! -f sbt-launch-0.7.5.jar ]
then
  ln -s sbt-launch-0.7.5.jar sbt-launch-0.7.4.jar
fi
cd ..  
