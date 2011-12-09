#!/bin/bash

if [ -z "$JAVA_DEV_ROOT" ]
then
    "please run env.sh first"
fi

if [ ! -d $LINUXVER ]
then
    . getlinux.sh
fi

preprocessed=$LINUXVER-preprocessed

if [ -d $preprocessed ]
then
    while true
    do
        echo "The linux kernel appears to have been preprocessed already."
        read -p "Preprocess again? [y/N] " yn
        case $yn in
            [Yy]* ) break; ;;
                * ) exit;;
        esac
    done
fi

if [ ! -f $LINUXVER/preprocess_linux_source.sh ]
then

    while true
    do
        echo "Preprocessing linux requires compiling it too."
        echo "Unless the kernel is compiled already This could take a long time."
        read -p "Continue? [Y/n] " yn
        case $yn in
            [Nn]* ) exit; ;;
                * ) break;;
        esac
    done

    echo "Configuring the linux kernel"

    cd $LINUXVER

    make allyesconfig ARCH=x86

    echo "Gathering preprocessor configurations for each compilation unit..."

    echo "preprocessed=\$1; if [ -z \$preprocessed ]; then echo \"usage: \$0 target-dir\"; exit 1; fi;" > preprocess_linux_source.sh

    chmod 755 preprocess_linux_source.sh

    # Since our own version of sparse (scripts/sparse) is in the
    # search path, it will be called and will write out
    # preprocess_linux.sh
    make C=2

    echo "All done!"

    cd ..

fi

echo "Please run $LINUXVER/preprocessor_linux_source.sh to preprocess the linux kernel."



