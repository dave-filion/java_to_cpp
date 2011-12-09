file=$1

# This script finds the maximum subparsers used by file.  It expects
# the output of SuperC's -parserStatistics option ("subparsers size
# count") and from subparser_cdf.sh ("# Processing filename")

if [[ -z $file || ! -f $file ]]; then
  echo "Please specify a filename."
fi

cat $file | egrep "^# Processing|^subparsers" \
    | awk 'BEGIN{file=""; max=0;}
{
if ($2 == "Processing") {
  if ($file != "") {
    print max, file;
  }
  file = $3;
  max = 0;
} else if ($1 == "subparsers") {
  if ($2 > max) {
    max = $2;
  }
}

}

END{print max, file}'
