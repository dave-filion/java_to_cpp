#!/bin/bash

file=$1

cat $file | awk '
BEGIN{
    defines = 0
    incond = 0
    nesting = 0
    objects=0;
    functions=0;
}

{
if ($1 == "conditional") {
  if ($2 == "if" || $2 == "ifdef" || $2 == "ifndef") {
    nesting++
  }
}
if ($1 == "endif") {
  nesting--;
}
if ($1 == "define") {
  defines++
  if ($3 == "var") {
    objects++
  }
  if ($3 == "fun") {
    functions++
  }
  if (nesting > 0) {
    incond++
  }
}
}

END{
    print "summary_definitions", defines, incond, objects, functions
}
'

defines=`cat $file | grep "^define" | awk '{ print $2 $5 }' | wc -l`
uniqdefs=`cat $file | grep "^define" | awk '{print $2 $5}' | sort | uniq | wc -l`

echo "summary_redefinitions" `echo "$defines - $uniqdefs" | bc -lq`


cat $file | awk '
BEGIN{
  invocations=0
  objects=0
  functions=0
  trimmed=0
}

{
if ($1 == "object") {
  invocations++
  objects++
  if ($6 < $5) {
    trimmed++
  }
}
if ($1 == "function") {
  invocations++
  functions++
  if ($7 < $6) {
    trimmed++
  }
}
}

END{
    print "summary_invocations", invocations, trimmed, objects, functions
}
'

cat $file | grep "^hoist_function" | awk '
BEGIN{
  hoisted=0
}

{
if ($5 > 1) {
  hoisted++
}
}

END{
    print "summary_hoisted_functions", hoisted
}
'

cat $file | awk '
BEGIN{
  nested=0
}

{
if ($1 == "object") {
  print $3
}
if ($1 == "function") {
  print $4
}
}
' | awk -F: '
BEGIN{
  nested=0
}

{
if (NF > 3) {
  nested++
}
}

END{
    print "summary_nested_invocations", nested
}
'

cat $file | grep "^paste" | awk '
BEGIN{
  paste=0
  hoisted=0
  condarg=0
}

{
paste++
if ($5 > 1) {
  hoisted++
}
if ($2 == "conditional" || $3 == "conditional") {
  condarg++
}
}

END{
  print "summary_paste",paste,hoisted,condarg
}
'

cat $file | grep "^stringify" | awk '
BEGIN{
  stringify=0
  hoisted=0
  condarg=0
}

{
stringify++
if ($4 > 1) {
  hoisted++
}
if ($2 == "conditional") {
  condarg++
}
}

END{
  print "summary_stringify",stringify,hoisted,condarg
}
'

cat $file | awk '
BEGIN{
  include=0
  computed=0
  hoisted=0
  valid=0
}

{
if ($1 == "include") {
  include++
}
if ($1 == "computed") {
  if ($5 > 1) {
    hoisted++
  }
}
if ($1 == "end_computed") {
  computed++
  if ($3 > 1) {
    valid++
  }
}
}

END{
  print "summary_include", include, computed, hoisted, valid
}
'

includes=`cat $file | grep "^include" | wc -l`
uniqinc=`cat $file | grep "^include" | awk '{ print $2 }' | sort | uniq | wc -l`

echo "summary_reinclude" `echo "$includes - $uniqinc" | bc -lq`


cat $file | grep "^conditional" | awk '
BEGIN{
  count=0
  nonboolean=0
  hoisted=0
  maxdepth=0
}

{
count++
if ($4 == "nonboolean") {
  nonboolean++
}
if ($5 > maxdepth) {
  maxdepth=$5
}
if ($6 > 1) {
  hoisted++
}
}

END{
  print "summary_conditionals", count, nonboolean, maxdepth, hoisted
}
'

echo "summary_error_directives" `cat $file | grep "^error_directive" | wc -l`

cat $file | grep "c_construct" | awk '
BEGIN{
  count=0
}

{
  if ($2 == "Statement" || $2 == "Declaration" || $2 == "ExternalDeclaration") {
    count++
  }
}

END{
  print "summary_c_statements_and_declarations", count
}
'

echo "summary_typedefs" `cat $file | grep "^typedef" | wc -l`

echo "summary_typedef_ambiguities" `cat $file | grep "^typedef_ambiguity" | wc -l`

