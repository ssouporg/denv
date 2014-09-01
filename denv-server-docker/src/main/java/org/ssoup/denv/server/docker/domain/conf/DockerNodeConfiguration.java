package org.ssoup.denv.server.docker.domain.conf;

import org.ssoup.denv.common.model.node.AbstractNodeConfiguration;

/**
 * User: ALB
 * Date: 11/08/14 16:03
 */
public class DockerNodeConfiguration extends AbstractNodeConfiguration {

    private String name;
    private String dockerHost;
    private int dockerPort = 4243;

    public DockerNodeConfiguration(String name, String dockerHost, int dockerPort) {
        this.name = name;
        this.dockerHost = dockerHost;
        this.dockerPort = dockerPort;
    }

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
