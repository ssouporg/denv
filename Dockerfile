FROM java:7u75-jre
MAINTAINER Alessandro Bellucci <alebellu@ssoup.org>

ENV JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64/jre

RUN apt-get update && apt-get -yq install maven

ADD pom-standalone.xml ./pom.xml
ADD pom-cli.xml ./pom-cli.xml
ADD ng /usr/local/bin/ng

RUN mvn clean install
RUN mvn -f pom-cli.xml clean install

RUN mvn dependency:build-classpath -f pom-cli.xml -Dmdep.outputFile=cp.txt
RUN echo '#''!'"/bin/bash\njava -cp `cat cp.txt` com.martiansoftware.nailgun.NGServer \$@" > /usr/bin/denv-ngd && chmod +x /usr/bin/denv-ngd
RUN echo '#''!'"/bin/bash\njava -cp `cat cp.txt` org.ssoup.denv.cli.Main \$@" > /usr/bin/denv && chmod +x /usr/bin/denv

EXPOSE 8080
CMD ["/bin/bash","/usr/bin/denv"]
