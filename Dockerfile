FROM java:7u75-jre
MAINTAINER Alessandro Bellucci <alebellu@ssoup.org>

ENV JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64/jre

RUN apt-get update && apt-get -yq install maven

ADD pom-standalone.xml ./pom.xml
ADD pom-cli.xml ./pom-cli.xml

RUN mvn clean install

EXPOSE 8080
CMD ["/usr/bin/mvn" "-f" "pom-cli.xml" "exec:java"]
