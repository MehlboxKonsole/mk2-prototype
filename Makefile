all: clean build-frontend build-backend package

clean:
	rm -rf out
	./gradlew clean

build-frontend:
	./gradlew generateSass

build-backend:
	./gradlew build

test:
	./gradlew test

package: test
	./gradlew assemble

init: clean
	rm -rf node_modules

run: clean build-frontend
	./gradlew bootRun

docker: clean build-frontend build-backend
	docker build . -t mk2-prototype

release: init build-frontend build-backend