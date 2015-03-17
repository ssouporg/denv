#!/bin/bash -ex

case "$1" in
  pre_machine)
    # have docker bind to both localhost and unix socket
    docker_opts='DOCKER_OPTS="$DOCKER_OPTS -H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock"'
    sudo sh -c "echo '$docker_opts' >> /etc/default/docker"

    cat /etc/default/docker

    ;;

esac
