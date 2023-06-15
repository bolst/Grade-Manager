@echo off

javac -cp lib\ml.jar -d bin src\*.java

java -cp bin;lib\ml.jar src.GradeManager 