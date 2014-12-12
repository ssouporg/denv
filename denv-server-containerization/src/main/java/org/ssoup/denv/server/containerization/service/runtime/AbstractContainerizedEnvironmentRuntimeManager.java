package org.ssoup.denv.server.containerization.service.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssoup.denv.core.containerization.model.runtime.*;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.containerization.exception.ContainerizationException;
import org.ssoup.denv.server.containerization.service.container.ContainerManager;
import org.ssoup.denv.server.containerization.service.container.ImageManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;

import java.io.InputStream;

/**
 * User: ALB
 * Date: 05/09/13 17:39
 */
public abstract class AbstractContainerizedEnvironmentRuntimeManager implements ContainerizedEnvironmentRuntimeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractContainerizedEnvironmentRuntimeManager.class);

    private EnvironmentConfigRepository environmentConfigRepository;

    private ImageManager imageManager;

    private ContainerManager containerManager;

    private NamingStrategy namingStrategy;

    public AbstractContainerizedEnvironmentRuntimeManager(EnvironmentConfigRepository environmentConfigRepository, ImageManager imageManager, ContainerManager containerManager, NamingStrategy namingStrategy) {
        this.environmentConfigRepository = environmentConfigRepository;
        this.imageManager = imageManager;
        this.containerManager = containerManager;
        this.namingStrategy = namingStrategy;
    }

    public ContainerManager getContainerManager() {
        return containerManager;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    /*private String getCmd(Environment env, ImageConfiguration imageConfiguration) {
        String command = imageConfiguration.getCommand();
        if (command != null) {
            command = command.replace("$envId", env.getId());
        }
        return command;
    }*/

    /*public void startApplication(Environment env, Application app) throws DenvException {
        ContainerizedApplication dapp = (ContainerizedApplication)app;
        // start all containers in order of dependencies
        boolean allContainersListening = false;
        while (!allContainersListening) {
            allContainersListening = true;
            for (Container container : dapp.getContainers()) {
                if (!container.canBeStarted()) {
                    continue;
                }

                boolean allLinkedContainersListening = true;
                Image image = container.getImage();
                ImageConfiguration imageConf = image.getConf();
                if (imageConf.getLinks() != null) {
                    for (LinkConfiguration link : imageConf.getLinks()) {
                        Container linkedContainer = dapp.getContainer(link.getService());
                        if (linkedContainer == null) {
                            throw new DenvException("Could not find container for image " + link.getService());
                        }
                        if (linkedContainer.getActualState() != ContainerState.RESPONDING) {
                            allLinkedContainersListening = false;
                            break;
                        }
                        ImageConfiguration linkedImageConf = app.getConf().getImageConfiguration(link.getService());
                        for (PortConfiguration port : linkedImageConf.getPorts()) {
                            if (!containerManager.isContainerListeningOnPort(linkedContainer, port)) {
                                allLinkedContainersListening = false;
                                break;
                            }
                        }
                    }
                }
                if (allLinkedContainersListening) {
                    containerManager.startContainer(env, container);
                    allContainersListening = false;
                }
            }
            if (!allContainersListening) {
                try {
                    Thread.sleep(5000); // allow 5 seconds for dependent containers to start
                } catch (InterruptedException e) {
                }
            }
        }
        dapp.setStarted(true);
    }*/

    /*@Override
    public void stopApplication(Environment env, Application app) throws ContainerizationException {
        ContainerizedApplication capp = (ContainerizedApplication)app;
        for (ContainerInfo containerInfo : capp.getContainersInfo()) {
            containerManager.findContainer(env, );
            containerManager.stopContainer(env, container);
        }
        app.setStarted(false);
    }*/

    @Override
    public InputStream getResource(Container container, String resourcePath) throws ContainerizationException {
        return containerManager.getResource(container, resourcePath);
    }

    @Override
    public InputStream getFolder(Container container, String folderPath) throws ContainerizationException {
        return containerManager.getFolder(container, folderPath);
    }

    public EnvironmentConfigRepository getEnvironmentConfigRepository() {
        return environmentConfigRepository;
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }
}
