#!/bin/sh

cd json-smart
mvn spotless:check;
cd ..

cd accessors-smart
mvn spotless:check
cd ..

cd json-smart-action
mvn spotless:check
cd ..
