FROM openjdk:8-jre

MAINTAINER Mukesh Joshi <mukesh.bciit@gmail.com>

ARG JAR_FILE

ADD target/${JAR_FILE} /usr/share/myservice/myservice.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/myservice/myservice.jar"]
