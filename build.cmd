@echo off

javac -cp lib\ml.jar;lib\json-simple-1.1.1.jar -d bin src\*.java

java -cp bin;lib\ml.jar;lib\json-simple-1.1.1.jar src.GradeManager 