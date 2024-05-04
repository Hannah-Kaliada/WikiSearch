FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
COPY target/wiki-0.0.1-SNAPSHOT.jar wiki.jar
ENTRYPOINT ["java","-jar","/wiki.jar"]