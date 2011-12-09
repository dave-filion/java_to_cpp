# Sums one column of one SuperC statistics row.

item=$1
field=$2

if [[ -z $item || -z $field ]]; then
    echo "Please specify an item and field number."
    exit 1
fi

cat $file | grep $item | awk -v col=$field '
BEGIN{sum=0}
{ sum += $col }
END{print sum}
'