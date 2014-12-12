package org.ssoup.denv.server.docker.service.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.Container;
import org.ssoup.denv.core.containerization.model.runtime.ContainerRuntimeInfo;
import org.ssoup.denv.core.containerization.model.runtime.ContainerizedEnvironmentRuntimeInfo;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.containerization.service.runtime.AbstractContainerizedEnvironmentRuntimeManager;
import org.ssoup.denv.server.containerization.service.container.ContainerManager;
import org.ssoup.denv.server.containerization.service.container.ImageManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;

/**
 * User: ALB
 * Date: 05/09/13 17:39
 */
@Service
public class DockerManager extends AbstractContainerizedEnvironmentRuntimeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerManager.class);

    @Autowired
    public DockerManager(EnvironmentConfigRepository environmentConfigRepository, ImageManager imageManager, ContainerManager containerManager, NamingStrategy namingStrategy) {
        super(environmentConfigRepository, imageManager, containerManager, namingStrategy);
        // this.dockerClient = new DockerClient(dockerEnvironmentConfiguration.getDockerAddress());
    }

    public void onEvent() {
    }

    @Override
    public void saveSnapshot(Environment env, String snapshotName) throws DenvException {
        if (!env.getActualState().canCreateSnapshot()) {
            throw new DenvException("Cannot create snapshot for environment " + env.getId() + ": the environment is in state " + env.getActualState());
        }
        ContainerizedEnvironmentConfiguration envConf = (ContainerizedEnvironmentConfiguration)
                getEnvironmentConfigRepository().findOne(env.getEnvironmentConfigurationId());
        ContainerizedEnvironmentRuntimeInfo runtimeInfo = (ContainerizedEnvironmentRuntimeInfo) env.getRuntimeInfo();
        for (ImageConfiguration imageConf : envConf.getImages().values()) {
            ContainerRuntimeInfo containerRuntimeInfo = runtimeInfo.getContainerRuntimeInfo(imageConf.getId());
            Container container = getContainerManager().findContainerById(env, imageConf, containerRuntimeInfo.getId());
            String snapshotImageName = getNamingStrategy().generateImageName(env, imageConf, snapshotName);
            getContainerManager().saveContainer(env, container, snapshotImageName);
        }
    }
}
