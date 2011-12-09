#!/bin/bash

echo "=== SuperC Regression Tester (v0.2) ==="
echo

if [[ $# -ne 2 && $# -ne 3 && $# -ne 4 ]]; then
    if [ $# -gt 0 ]; then
        echo "ERROR: wrong number of arguments"
        echo ""
    fi
    echo "USAGE: `basename $0` action \"files\" [\"flags\"] [\"gcc-flags\"]"
    echo ""
    echo "actions"
    echo "  check           - Run regression tests."
    echo "  update          - Update new tests and existing when different."
    echo "  update-existing - Update only existing tests."
    echo "  update-new      - Add new tests."
    echo "  checkcpp        - Run regression tests for preprocessor."
    echo "  checkcpp-failed - Run checkcpp only for failed tests."
    echo "  checkcpp-passed - Run checkcpp only for passed tests."
    echo ""
    echo "\"files\" - a single file or a quoted list in a single string."
    echo ""
    echo "\"flags\" - optionally set SuperC flags to use in test."
    echo ""
    echo "\"gcc-flags\" - optionally set gcc flags to use in the checkcpp test."

    exit 1
fi

java=java\ -ea

action=$1
files=$2
flags=$3
gccflags=$4

if [[ ! ( $action = "check" || $action = "update-new" \
|| $action = "update-existing" || $action = "update" \
|| $action = "checkcpp" || $action = "checkcpp-failed" \
|| $action = "checkcpp-passed") ]]
then
    echo "Invalid action."
    exit 1
else
    echo "action=$action"
    echo ""
fi 

total=0
expected=0
passed=0
updated=0
errors=0
none=0

if [[ ! -z $flags ]]; then
    echo "flags=$flags"
    echo ""
fi

if [[ ! -z $gccflags ]]; then
    echo "gcc-flags=$gccflags"
    echo ""
fi

for file in $files
do
    # Escape spaces so the flags can be used in the output filenames.
    escflags="`echo $flags | sed 's/ //g' | sed 's/\///g'`"
    if [ -z $escflags ]; then
        fileflags=$file
    else
        fileflags=$file.$escflags
    fi
    total=$(($total + 1))
    if [[ $action = "checkcpp" || $action = "checkcpp-failed" \
        || $action = "checkcpp-passed" ]]; then
        escgccflags="`echo $gccflags | sed 's/ //g' | sed 's/\///g'`"
        if [[ ! -z $escgccflags ]]; then
            escgccflagext=.$escgccflags
        fi
        test=$fileflags.checkcpp$escgccflagext.test
        gcc=$fileflags.checkcpp$escgccflagext.gcc
        diff=$fileflags.checkcpp$escgccflagext.diff
        if [[ $action = "checkcpp" || ($action = "checkcpp-failed" \
            && -f $gcc && -s $diff) || ($action = "checkcpp-passed" \
            && -f $gcc && ! -s $diff) ]]; then
            echo "Testing $file"
            $java xtc.lang.cpp.SuperC -silent -E $flags $file \
                | gcc -E $gccflags - > $test
            gcc -E $gccflags $file > $gcc
            $java xtc.lang.cpp.cdiff $gcc $test > $diff
            if [ $? -ne 0 ]; then
                echo "FAIL"
                errors=$(($errors + 1))
            else
                passed=$(($passed + 1))
            fi
        fi
    else
        test=$fileflags.test
        diff=$fileflags.diff
        valid=$fileflags.valid
        if [ ! -f $valid ]
        then
            if [[ $action = "update-new" || $action = "update" ]]
            then
                echo "Updating $file"
                #SuperC.sh -silent $flags $file > $test
                $java xtc.lang.cpp.SuperC -silent $flags $file > $test
                echo "----------------------------------- Original: $file"
                cat  $file
                echo
                echo "----------------------------------- Processed: $test"
                cat $test
                echo
                echo "----------------------------------- Filename: $file"
                read -p "No correct file found.  Is this test correct? [y/N] "\
                yn
                case $yn in
                    [Yy]* ) mv $test $valid; ;;
                        * ) ;;
                esac
            else
                #echo "No correct file found."
                none=$(($none + 1))
            fi 
        else
            expected=$(($expected + 1))
            if [[ $action != "update-new" ]]; then
                echo "Testing $file"
                #SuperC.sh -silent $flags $file > $test
                $java xtc.lang.cpp.SuperC -silent $flags $file > $test
                diff $valid $test > $diff
                if [ $? -ne 0 ]; then
                    if [[ $action = "update-existing" || $action = "update" ]]
                    then
                        echo "----------------------------------- Original: $file"
                        cat  $file
                        echo
                        echo "----------------------------------- Processed: $file"
                        cat $test
                        echo
                        echo "----------------------------------- Diff: $file"
                        cat $diff
                        echo
                        echo "----------------------------------- Filename: $file"
                        read -p "No longer matches valid file.  Update valid file? [y/N] " yn
                        case $yn in
                            [Yy]* ) mv $test $valid; ;;
                                * ) ;;
                        esac
                    else
                        #cat $diff
                        echo "FAIL"
                        errors=$(($errors + 1))
                    fi
                else
                    #echo "Passed"
                    passed=$(($passed + 1))
                fi
            #else
                #echo "Skip"
            fi
        fi
    fi
done

if [ $action = "check" ]; then
    echo
    echo "# of expected passes                $expected"
    if [ $errors -gt 0 ]; then
        echo "# of passes                         $passed"
        echo "# of failures                       $errors"
    fi
else
    echo "$total file(s)"
    echo "$passed passed"
    echo "$errors failed"
    if [ $none -gt 0 ]
    then
        echo "$none without valid file(s)"
    fi
fi

if [ $errors -gt 0 ]; then
    # Throw an error if any tests failed.
    exit 1
fi