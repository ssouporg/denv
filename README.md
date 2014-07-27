denv
====

A manager for container based environments.

# Usage

Commands:

    defs          list the environment definitions
    def <envdef>  list the images used by <envdef>
                       -a, --all: also show details: volumes, linked images
    add		add a new environment definition
    run <envdef>  run a new environment
                       -n, --name <name>: give the new environment the name specified
    envs          list the running environments
    start <envid> start the environment with the id specified
    stop <envid>  stop the environment with the id specified
    rm <envid>    remove the environment with the id specified
    clone <envid> clone an environement

## defs

## def

## add

If no file arguments are specified, the standard input is used.

## run

## envs

## start

## stop

## rm

## clone


# Sample use case

Many real work systems are composed by multiple interoperating applications,
just think about microservices architectures like the ones used by Amazon, Netflix and many others.

Now imagine the following three interoperating systems:
- a web application, with its database, managing operational data and exposing a REST api
- a standalone worker application, doing heavy calculations on demand
- an expert system that uses the web app REST api to retrieve operational data from which to inference information

These systems can be run inside containers:
- web app images: ex/web, ex/db
- worker image: ex/worker
- expert-system image: ex/expert-system

Lets define an environment for the worker to run in:

github:alebellu:denv:examples/worker-env.denv :

	IMAGES ex/worker
	EXPOSE worker

we can then add the environement to our denv runtime:

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

<a href="http://tinypic.com?ref=35mejc9" target="_blank"><img src="http://i59.tinypic.com/35mejc9.jpg" border="0" alt="Image and video hosting by TinyPic"></a>
