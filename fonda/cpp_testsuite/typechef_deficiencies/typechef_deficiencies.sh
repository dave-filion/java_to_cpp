# This uses the Linux test case runner to run TypeChef, which is why
# you will get an error message if you don't run it from the TypeChef
# directory and have the test case setup.  Just install TypeChef and
# run prepareLinuxTestCase.sh

for i in $CPPTEST/typechef_deficiencies/*.c; do
    typechef_run_file.sh -t TypeChef -a "-printSource" -r $i > $i.out 2>&1;
done

