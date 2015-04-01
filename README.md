denv [![Circle CI](https://circleci.com/gh/ssouporg/denv.svg?style=badge)](https://circleci.com/gh/ssouporg/denv) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.ssoup.denv/denv/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.ssoup.denv/denv) [![Docker Hub](http://dockeri.co/image/alebellu/denv)](https://registry.hub.docker.com/u/alebellu/denv/)
====

A manager for Docker based environments.

# How it works

![Denv L](docs/images/denv_small.jpg "Denv")

# Server Installation

You can choose to install the Denv server as a Docker image or as a Java application running directly on your host.

## Dockerized

Pull the image from the Docker Hub.

    docker pull alebellu/denv

Make sure you have a MongoDB database you can use. For example you can use one from Docker Hub:

    docker pull tutum/mongodb
    docker run -d -p 27017:27017 -p 28017:28017 -e AUTH=no -v /home/synaptiq/data/mongodb:/data/db tutum/mongodb

And run the server (replace the ip address with the one of your Docker and MongoDB host):

    docker run -d --restart=always -e "DOCKER_HOST=172.17.42.1" -e "DOCKER_PORT=4243" -e "spring.data.mongodb.uri=mongodb://172.17.42.1/denv" -p 8090:8080 alebellu/denv /usr/bin/mvn exec:java

## Non-Dockerized

Make sure Java and Maven (http://maven.apache.org/download.cgi) are available on the host, then:

    mkdir denv && cd denv
    wget https://raw.githubusercontent.com/ssouporg/denv/master/pom-standalone.xml -O pom.xml
    mvn clean install

Make sure you have a MongoDB database you can use.
And run the server (replace the ip/port with the one of your Docker and MongoDB hosts):

    /usr/bin/mvn -D "DOCKER_HOST=172.17.42.1" -D "DOCKER_PORT=4243" -D "spring.data.mongodb.uri=mongodb://172.17.42.1/denv" exec:java

# CLI Installation

You can choose to install the Denv Command Line Interface (CLI) as a Docker image or as a Java application running directly on your host.

## Dockerized

Pull the image from the Docker Hub.

    docker pull alebellu/denv

And run the cli (replace the ip/port with those of your Denv server):

    docker run -e "DENV_SERVER_URL=http://172.17.42.1:8090" alebellu/denv /bin/bash /usr/bin/denv envs

For a more convenient short command (replace the ip/port with those of your Denv server):

    alias denv='docker run -e "DENV_SERVER_URL=http://172.17.42.1:8090" alebellu/denv'
    denv envs

## Non-Dockerized

Make sure Java and Maven (http://maven.apache.org/download.cgi) are available on the host, then:

    mkdir denv && cd denv
    wget https://raw.githubusercontent.com/ssouporg/denv/master/pom-cli.xml -O pom-cli.xml
    mvn clean install

Make sure you have a MongoDB database you can use.

Run a CLI command (replace the ip/port with those of your Denv server):

- via Java (faster):

        /usr/bin/mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
        echo -e '#''!'"/bin/bash\njava -cp `cat cp.txt` -D "DENV_SERVER_URL=http://172.17.42.1:8090" org.ssoup.denv.cli.Main \$@" > /usr/bin/denv && chmod +x /usr/bin/denv

      and:

        denv envs

- or via Maven:

        wget https://raw.githubusercontent.com/ssouporg/denv/master/pom-cli.xml -O pom-cli.xml
        /usr/bin/mvn -q -f pom-cli.xml -D "DENV_SERVER_URL=http://172.17.42.1:8090" exec:java -Dexec.args="envs"

# Command Line Interface (CLI)

Commands:

    envs        list the environments
    env         show the details of an environment
    addenv      add a new environment
    rmenv       remove an environment
    clone       clone an environement

    confs       list registered configurations
    addconf     register a new configuration
    conf        show the definition of a configuration
    rmconf      remove a configuration

    vers        list all available versions of an environment configuration
    ver         show the details of an environment configuration version
    addver      adds a new version of an environment
    rmver       remove a version of an environment configuration

# Artifact Deploy (For contributors)

## Snapshots

Change Denv version in all pom.xml files

    mvn versions:set -DnewVersion=0.2-SNAPSHOT

Deploy snapshot to Sonatype

    mvn clean deploy

Anyway snapshots are automatically deployed to Sonatype any time there is a commit on master branch (via Circle CI)

## Releases

Change Denv version in all pom.xml files

    mvn versions:set -DnewVersion=0.2

Deploy to Maven central

    mvn clean deploy -P release -Dhttps.protocols=SSLv3 -Dforce.http.jre.executor=true
