# Finds the 50th, 90th, and 100th percentiles from an ascendingly ordered
# list of numbers.

file=$1
item=$2
field=$3

if [[ -z $file || -z $item || -z $field ]]; then
    echo "Please specify a filename, item, and field number."
    exit 1
fi

count=`cat $file | grep $item | wc -l`

echo "Out of $count rows."
for pctl in .5 .9 1; do
    n=`perl -w -e "use POSIX; print ceil($count * $pctl), qq{\n}"`  # ugh
    echo "Percentile $pctl ($n rows)"
    echo `cat $file | grep $item | awk -v col=$field '{ print $col }' \
        | sort -n | head -n $n | tail -n 1`
    echo
done