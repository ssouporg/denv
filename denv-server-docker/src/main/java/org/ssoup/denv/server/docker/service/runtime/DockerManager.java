package org.ssoup.denv.server.docker.service.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.containerization.service.application.AbstractContainerizedApplicationManager;
import org.ssoup.denv.server.containerization.service.container.ContainerManager;
import org.ssoup.denv.server.containerization.service.container.ImageManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;

/**
 * User: ALB
 * Date: 05/09/13 17:39
 */
@Service
public class DockerManager extends AbstractContainerizedApplicationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerManager.class);

    @Autowired
    public DockerManager(ApplicationConfigurationManager applicationConfigurationManager, ImageManager imageManager, ContainerManager containerManager, NamingStrategy namingStrategy) {
        super(applicationConfigurationManager, imageManager, containerManager, namingStrategy);
        // this.dockerClient = new DockerClient(dockerEnvironmentConfiguration.getDockerAddress());
    }

    public void onEvent() {
    }
}
