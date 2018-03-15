FROM maven:3.5-jdk-8-alpine as BUILD


COPY src /usr/src/song-translation-api/src
COPY pom.xml /usr/src/song-translation-api
RUN mvn -f /usr/src/song-translation-api/pom.xml clean package

FROM tomcat:8.5-jre8-alpine

COPY --from=BUILD /usr/src/song-translation-api/target/song-translation-api-*.war /usr/local/tomcat/webapps/song-translation-api.war