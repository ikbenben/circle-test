#!/bin/bash

if [ -z "$1" ]
  then
    echo "Please supply a path to a filename"
    exit
fi

./sbt run -Dconfig.linesfile=$1
