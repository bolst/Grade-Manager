javac -cp lib/ml.jar:lib/json-simple-1.1.1.jar $(find . -name '*.java')

mkdir -p build
mv MANIFEST.MF build/

jar cmf build/MANIFEST.MF GradeManager.jar src/*

mv build/MANIFEST.MF .

mkdir -p build/util
mkdir -p build/front

mv src/*.class build
mv src/util/*.class build/util/
mv src/front/*.class build/front/

java -jar GradeManager.jar
