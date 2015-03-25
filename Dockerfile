FROM java:7u75-jre
MAINTAINER Alessandro Bellucci <alebellu@ssoup.org>

RUN apt-get update && apt-get -yq install maven

ADD pom-docker.xml ./pom.xml

RUN mvn clean install

EXPOSE 8080
CMD ["/usr/bin/mvn exec:java"]
