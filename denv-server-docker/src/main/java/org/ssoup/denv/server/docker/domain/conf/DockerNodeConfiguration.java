package org.ssoup.denv.server.docker.domain.conf;

import org.ssoup.denv.server.domain.conf.node.AbstractNodeConfiguration;

/**
 * User: ALB
 * Date: 11/08/14 16:03
 */
public class DockerNodeConfiguration extends AbstractNodeConfiguration {

    private String name;
    private String dockerHost;
    private int dockerPort = 4243;

    public String getDockerHost() {
        return dockerHost;
    }

    public void setDockerHost(String dockerHost) {
        this.dockerHost = dockerHost;
    }

    public int getDockerPort() {
        return dockerPort;
    }

    public void setDockerPort(int dockerPort) {
        this.dockerPort = dockerPort;
    }

    public String getDockerAddress() {
        return "http://" + dockerHost + ":" + dockerPort;
    }

    @Override
    public String getName() {
        return name;
    }
}
