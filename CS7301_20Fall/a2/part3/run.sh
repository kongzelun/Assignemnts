#!/bin/bash

# part3

export CLASSPATH="/usr/local/lib/soot-3.3.0.jar:$CLASSPATH"

javac Example.java Log.java SootConfigure.java TestSootLogging.java TestSootLoggingHeap.java

java TestSootLogging Example

java Example

java TestSootLoggingHeap
