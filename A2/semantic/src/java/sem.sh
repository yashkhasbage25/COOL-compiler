#!/bin/bash
# java -cp /usr/local/lib/antlr-4.5-complete.jar:../java java.cool.SemanticTest ../test_cases/$1
cd cool
make
cd ..
java -cp /usr/local/lib/antlr-4.5-complete.jar:../java cool.SemanticTest ../test_cases/$1
