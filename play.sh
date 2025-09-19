#!/bin/bash
JAR_FILE=$(find target -name "wordle-cli-*-jar-with-dependencies.jar")
java -jar $JAR_FILE "$@"
