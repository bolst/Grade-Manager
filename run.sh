javac -cp lib/ml.jar:lib/json-simple-1.1.1.jar -d bin $(find . -name '*.java')

java -cp bin:lib/ml.jar:lib/json-simple-1.1.1.jar src.GradeManager

rm -r bin
