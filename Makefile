install:
	./gradlew clean install

run-dist:
	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain

test:
	./gradlew test

build:
	./gradlew clean checkstyleMain test install

.PHONY: build

check-updates:
	./gradlew dependencyUpdates

read-json:
	./build/install/app/bin/app src/test/resources/file1.json src/test/resources/file2.json

act:
	act --secret-file my.secrets
