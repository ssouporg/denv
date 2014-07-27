denv
====

A manager for container based environments.

# Usage

Commands:

    envs          list the environment definitions
    env		  show the definition of a specific environment
    add		  add a new environment definition
    apps	  list the applications
    app		  show the definition of a specific application
    run  	  run an application in a specific environment
    ps            list the running applications for one or all environments
    start	  start an environment
    stop	  stop an environment
    rm  	  remove an environment
    clone	  clone an environement

## defs

List the environment definitions

## def

def <envdef>  list the images used by <envdef>
                       -a, --all: also show details: volumes, linked images

## add

If no file arguments are specified, the standard input is used.

## run

    run <envdef>  run a new environment
                       -n, --name <name>: give the new environment the name specified

## envs

## start

    start <envid> start the environment with the id specified

## stop

    stop <envid>  stop the environment with the id specified

## rm

    rm <envid>    remove the environment with the id specified

## clone

    clone <envid> clone an environement


# Sample use case

Many real work systems are composed by multiple interoperating applications,
just think about microservices architectures.

Now imagine the following three interoperating systems:
- a web application, with its database, managing operational data and exposing a REST api
- a standalone worker application, doing heavy calculations on demand
- an expert system that uses the web app REST api to retrieve operational data from which to inference information

These systems can be run inside containers:
- web app images: ex/web, ex/db
- worker image: ex/worker
- expert-system image: ex/expert-system

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

# Architecture

<a href="http://tinypic.com?ref=35k8njp" target="_blank"><img src="http://i61.tinypic.com/35k8njp.jpg" border="0" alt="Image and video hosting by TinyPic"></a>

