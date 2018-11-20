all: clean build-frontend build-backend package

clean:
	rm -rf out
	./gradlew clean

build-frontend:
	npm run sass

build-backend:
	./gradlew build

test:
	./gradlew test

package: test
	./gradlew assemble

init: clean
	rm -rf node_modules
	npm install

run: clean build-frontend
	./gradlew bootRun

release: init build-frontend build-backend