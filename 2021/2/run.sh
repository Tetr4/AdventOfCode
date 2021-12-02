#!/bin/sh
# Usage: ./run.sh part1.mlm sample.txt

cat $2 |
# Transform input, so it is a valid list of strings in arden syntax, e.g. ("foo", "bar")
sed 's/.*/"&"/' | # Wrap lines with quotes
tr '\n' ',' | # Replace newlines with commas
sed 's/.*/(&)/' | # Add leading and trailing bracket
java -jar arden2bytecode.jar --run $1
