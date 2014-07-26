denv
====

A manager for container based environments.

 *   denv defs          list the environment definitions
 *   denv def <envdef>  list the images used by <envdef>
 *                      -a, --all: also show details: volumes, linked images
 *   denv add		add a new environment definition
 *   denv run <envdef>  run a new environment
 *                      -n, --name <name>: give the new environment the name specified
 *   denv envs          list the running environments
 *   denv start <envid> start the environment with the id specified
 *   denv stop <envid>  stop the environment with the id specified
 *   denv rm <envid>    remove the environment with the id specified
 *   denv clone <envid> clone an environement

github:alebellu:denv:examples/worker-env.denv :

	IMAGES ex/worker
	EXPOSE worker

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

