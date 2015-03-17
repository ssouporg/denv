#!/bin/bash -ex

case "$1" in
  pre_machine)
    # have docker bind to both localhost and unix socket
    docker_opts='DOCKER_OPTS="$DOCKER_OPTS -H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock"'
    

    cat /etc/default/docker

    ;;
  dependencies)
    mvn clean install -Dmaven.javadoc.skip=true -DskipTests=true

    ;;
esac
