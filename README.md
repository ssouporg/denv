denv [![Circle CI](https://circleci.com/gh/ssouporg/denv.svg?style=badge)](https://circleci.com/gh/ssouporg/denv) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.ssoup.denv/denv/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.ssoup.denv/denv)
====

A manager for Docker based environments.

# Architecture

<a href="http://tinypic.com?ref=35k8njp" target="_blank"><img src="http://i61.tinypic.com/35k8njp.jpg" border="0" alt="Image and video hosting by TinyPic"></a>


# Command Line Interface

Commands:

    list        list the environments
    create      create a new environment
    rm          remove an environment
    clone       clone an environement

    apps        list the registered applications
    add         register a new application
    app         show the definition of a specific application
    rmapp       remove an application

    deploy      deploy an application in a specific environment
    run         run an application in a specific environment
    start       start an application in a specific environment
    stop        stop an application in a specific environment
    kill        kill an application in a specific environment

    ps          list running applications

# Sample use case

Many real work systems are composed by multiple interoperating applications,
just think about microservices architectures.

Now imagine the following three interoperating systems:
- a web application, with its database, managing operational data and exposing a REST api
- a standalone worker application, doing heavy calculations on demand
- an expert system that uses the web app REST api to retrieve operational data from which to inference information

These systems can be run inside containers:
- web app images: ex/web, ex/db
- worker imageForMongo: ex/worker
- expert-system imageForMongo: ex/expert-system

  denv envs

  ENVIRONMENT

  denv create integration
  denv create acceptance
  denv create production

  denv envs

  ENVIRONMENT

  integration
  acceptance
  production

  denv addapp github:alebellu:denv:ex/webapp --name webapp

  denv apps

APPLICATION						ID			NAME

github:alebellu:denv:ex/webapp		a9e8d8e3	webapp

  denv app webapp

IMAGE ex/db
IMAGE ex/web LINK ex/db
EXPOSE ex/web

  denv run webapp -e integration

  docker ps

  CONTAINER   IMAGE     STATUS      NAME

  d8092343    ex/db     running     integration-webapp-ex-db
  a1b28734    ex/web    running     integration-webapp-ex-web







Lets define an application for the worker:

github:alebellu:denv:examples/worker-app.denv :

	IMAGES ex/worker
	EXPOSE worker

we can then add the application to our denv runtime:

denv add github:alebellu:denv:examples/worker-env

github:alebellu:denv:examples/web-env.denv :

	ENV worker-env
	IMAGE ex/db
	IMAGE ex/web LINK db, worker-env
	EXPOSE web

github:alebellu:denv:examples/expert-system-env.denv :

	ENV ex/web-env
	IMAGE ex/expert-system LINK web-env
	EXPOSE ex/expert-system

denv add github:alebellu:ex:ex/expert-system-env

# (For contributors) Artifact Deploy

- Change Denv version in all pom.xml files

mvn versions:set -DnewVersion=0.2-SNAPSHOT

- Deploy snapshot to Sonatype

mvn clean deploy

Anyway snapshots are automatically deployed to Sonatype any time there is a commit on master branch (via Circle CI)

- Deploy to Maven central

mvn clean deploy -P release -Dhttps.protocols=SSLv3 -Dforce.http.jre.executor=true
