package org.ssoup.denv.server.docker.service.runtime;

import com.github.dockerjava.client.DockerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.domain.container.ContainerManager;
import org.ssoup.denv.server.domain.container.ImageManager;
import org.ssoup.denv.server.domain.application.ApplicationFactory;
import org.ssoup.denv.server.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.runtime.AbstractApplicationManager;

/**
 * User: ALB
 * Date: 05/09/13 17:39
 */
@Service
public class DockerManager extends AbstractApplicationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerManager.class);

    private DockerClient dockerClient;

    @Autowired
    public DockerManager(ImageManager imageManager, ContainerManager containerManager, ApplicationFactory applicationFactory, NamingStrategy namingStrategy) {
        super(imageManager, containerManager, applicationFactory, namingStrategy);
        // this.dockerClient = new DockerClient(dockerEnvironmentConfiguration.getDockerAddress());
    }

    public void onEvent() {
    }
}
