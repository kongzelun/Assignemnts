#!/bin/bash

# part1

export CLASSPATH="/usr/local/lib/soot-3.3.0.jar:$CLASSPATH"

javac DominatorFinder.java  GCD.java  TestDominatorFinder.java

java TestDominatorFinder
