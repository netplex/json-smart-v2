#!/bin/sh

cd accessors-smart
mvn spotless:check
cd ..

cd json-smart
mvn spotless:check;
cd ..

cd json-smart-action
mvn spotless:check
cd ..
