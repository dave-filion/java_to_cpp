# The test cases in this directory have either no configuration
# variables, one (A), two (A and B), ..., or five (A, B, C, D, E)
#
# This tester tries every combination of configuration variables to
# test the output of SuperC's parser with the output of gcc under the
# same configuration.

# Get the lists of files that have each configuration variable.  By
# test design, each "hasL" list should be a superset of the next,
# e.g. hasB is a superset of hasC.

hasA=`grep A *.c | awk -F: '{print $1}' | tr '\n' ' '`
hasB=`grep B *.c | awk -F: '{print $1}' | tr '\n' ' '`
hasC=`grep C *.c | awk -F: '{print $1}' | tr '\n' ' '`
hasD=`grep D *.c | awk -F: '{print $1}' | tr '\n' ' '`
hasE=`grep E *.c | awk -F: '{print $1}' | tr '\n' ' '`

# Exit with error code if any test run fails.
checkFail() {
    if [ $? -ne 0 ]; then
        exit 1;
    fi
}

# Check every combination of configuration variables.

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "*.c"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasA" "" "-DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasB" "" "-DB"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasB" "" "-DB -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasC" "" "-DC"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasC" "" "-DC -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasC" "" "-DC -DB"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasC" "" "-DC -DB -DA"

checkFail

# hasD

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasD" "" "-DD"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasD" "" "-DD -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasD" "" "-DD -DB"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasD" "" "-DD -DC"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasD" "" "-DD -DB -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasD" "" "-DD -DC -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasD" "" "-DD -DC -DB"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasD" "" "-DD -DC -DB -DA"

checkFail


# hasE

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DB"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DC"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DD"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DB -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DC -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DD -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DC -DB"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DD -DB"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DD -DC"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DC -DB -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DD -DB -DA"

checkFail

../../../src/xtc/lang/cpp/scripts/regression.sh checkcpp "$hasE" "" "-DE -DD -DC -DB"

checkFail



# All test runs were successful.

exit 0;

