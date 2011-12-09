#!/bin/bash

#RANDOM_NUMBER=$RANDOM
#echo "processing " $RANDOM_NUMBER
#HEAP_DUMP=-XX:+HeapDumpOnOutOfMemoryError\ -XX:HeapDumpPath="./java$RANDOM_NUMBER.hprof"
#GC_LOG=-verbose:gc\ -XX:+PrintGCTimeStamps\ -XX:+PrintGCDetails\ -Xloggc:"./gc$RANDOM_NUMBER.txt"
#debug="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"
#PERFORMANCE=-performance\ -warmupRuns\ 1\ -totalRuns\ 1

#filename=hprof.`basename ${!#}`.`date +%Y%m%d%H%M%S`.txt
filename=hprof.`basename ${!#}`.`date +%s`.txt

#export JAVA_ARGS="-agentlib:hprof=cpu=times,heap=sites,lineno=y,file=$filename"
export JAVA_ARGS="-agentlib:hprof=cpu=samples,interval=3,depth=40,lineno=y,file=$filename"
#export JAVA_ARGS="-agentlib:hprof=cpu=samples,interval=3,heap=sites,lineno=y,file=$filename"

superC.sh $@
