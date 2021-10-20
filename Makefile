install:
	./gradlew clean install

run-dist:
	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain

build:
	./gradlew clean checkstyleMain install

.PHONY: build

check-updates:
	./gradlew dependencyUpdates

read-json:
	./build/install/app/bin/app file1.json file2.json
#/Users/shokhin_al/Developer/java-project-lvl2/file2.json
