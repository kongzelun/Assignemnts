#!/bin/bash

# part2&3

export CLASSPATH="/usr/local/lib/antlr-4.7-complete.jar:$CLASSPATH"

alias antlr4='java -jar /usr/local/lib/antlr-4.7-complete.jar'
alias grun='java org.antlr.v4.runtime.misc.TestRig'

# wget https://raw.githubusercontent.com/antlr/codebuff/master/grammars/org/antlr/codebuff/Java8.g4

java -jar /usr/local/lib/antlr-4.7-complete.jar Java8.g4

javac Java8BaseListener.java  Java8Lexer.java  Java8Listener.java  Java8Parser.java  JavaParserTest.java
java JavaParserTest Test.java
