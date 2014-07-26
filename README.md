denv
====

A manager for container based environments.

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

<a href="http://tinypic.com?ref=35mejc9" target="_blank"><img src="http://i59.tinypic.com/35mejc9.jpg" border="0" alt="Image and video hosting by TinyPic"></a>
