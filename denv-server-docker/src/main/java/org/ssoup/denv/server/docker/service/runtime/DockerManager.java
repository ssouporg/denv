package org.ssoup.denv.server.docker.service.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.runtime.application.AbstractApplicationManager;
import org.ssoup.denv.server.service.runtime.container.ContainerManager;
import org.ssoup.denv.server.service.runtime.container.ImageManager;

/**
 * User: ALB
 * Date: 05/09/13 17:39
 */
@Service
public class DockerManager extends AbstractApplicationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerManager.class);

    @Autowired
    public DockerManager(ImageManager imageManager, ContainerManager containerManager, NamingStrategy namingStrategy) {
        super(imageManager, containerManager, namingStrategy);
        // this.dockerClient = new DockerClient(dockerEnvironmentConfiguration.getDockerAddress());
    }

    public void onEvent() {
    }
}