#!/bin/bash

# part0

export CLASSPATH="/usr/local/lib/soot-3.3.0.jar:$CLASSPATH"

javac Example.java  TestSootCallGraph.java

java TestSootCallGraph Example
