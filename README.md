# Grade Manager

Grade Manager is a Java program that allows users to manage courses and their grade distributions. It provides functionality to add course components (i.e., assignments, tests with weightings, etc), assign grades, and calculate current marks.

## Usage

Clone this repo and run (assuming Java 1.8):

```
javac -cp lib\ml.jar;lib\json-simple-1.1.1.jar -d bin src\*.java

java -cp bin;lib\ml.jar;lib\json-simple-1.1.1.jar src.GradeManager  
```