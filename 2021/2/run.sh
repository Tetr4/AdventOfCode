#!/bin/sh
# Usage: ./run.sh part1.mlm sample.txt

# transform input, so it is a valid list of strings in arden syntax, e.g. ("foo", "bar")
cat $2 |
sed 's/.*/"&"/' | # wrap lines with quotes
tr '\n' ',' | # replace newlines with commas
sed 's/.*/(&)/' | # add leading and trailing bracket
java -jar arden2bytecode.jar --run $1
