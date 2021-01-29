FROM adoptopenjdk:11-jdk

WORKDIR /mk2

RUN mkdir -p /mk2/config

COPY build/libs/mk2-prototype-0.3.2.jar /mk2/app.jar
COPY docker/runner.sh /
COPY docker/application.yml /mk2/config/application.yml

RUN chmod 755 /runner.sh

EXPOSE 8080
CMD ["/runner.sh"]