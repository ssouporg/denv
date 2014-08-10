package org.ssoup.denv.server.containers.docker;

import com.kpelykh.docker.client.DockerClient;
import eu.eee.envs.runtime.AbstractApplicationInstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: ALB
 * Date: 05/09/13 17:39
 */
public class DockerManager extends AbstractApplicationInstanceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerManager.class);

    private String dockerHost;
    private Integer dockerPort;

    private DockerClient dockerClient;

    public DockerManager() {
        this.dockerHost = config.getString("dockerHost");
        this.dockerPort = config.getInteger("dockerPort", 4243);
        String dockerAddress = "http://" + this.dockerHost + ":" + this.dockerPort;
        this.dockerClient = new DockerClient(dockerAddress);
    }

    public void onEvent() {

    }
}
