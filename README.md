denv [![Circle CI](https://circleci.com/gh/ssouporg/denv.svg?style=badge)](https://circleci.com/gh/ssouporg/denv) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.ssoup.denv/denv/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.ssoup.denv/denv) [![Docker Hub](http://dockeri.co/image/alebellu/denv)](https://registry.hub.docker.com/u/alebellu/denv/)
====

A manager for Docker based environments.

# How it works

![Denv L](docs/images/denv_small.jpg "Denv")

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
