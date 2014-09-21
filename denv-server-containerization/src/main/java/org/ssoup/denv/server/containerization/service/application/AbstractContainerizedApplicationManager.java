package org.ssoup.denv.server.containerization.service.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfiguration;
import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;
import org.ssoup.denv.core.containerization.domain.conf.application.LinkConfiguration;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.containerization.domain.runtime.Container;
import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplication;
import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplicationImpl;
import org.ssoup.denv.server.containerization.domain.runtime.Image;
import org.ssoup.denv.server.containerization.exception.ContainerizationException;
import org.ssoup.denv.server.containerization.service.container.ContainerManager;
import org.ssoup.denv.server.containerization.service.container.ImageManager;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;

import java.io.InputStream;

/**
 * User: ALB
 * Date: 05/09/13 17:39
 */
public abstract class AbstractContainerizedApplicationManager implements ContainerizedApplicationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractContainerizedApplicationManager.class);

    private ApplicationConfigurationManager applicationConfigurationManager;

    private ImageManager imageManager;

    private ContainerManager containerManager;

    private NamingStrategy namingStrategy;

    public AbstractContainerizedApplicationManager(ApplicationConfigurationManager applicationConfigurationManager, ImageManager imageManager, ContainerManager containerManager, NamingStrategy namingStrategy) {
        this.applicationConfigurationManager = applicationConfigurationManager;
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

    @Override
    public void deployApplication(Environment env, Application app) throws DenvException {
        ContainerizedApplication capp = (ContainerizedApplication)app;
        ContainerizedApplicationConfiguration cappConf = (ContainerizedApplicationConfiguration)applicationConfigurationManager.getApplicationConfiguration(capp.getApplicationConfigurationId());
        for (ImageConfiguration imageConf : cappConf.getImages().values()) {
            Image image = imageManager.findOrBuildImage(env, imageConf);
            String command = null; // getCmd(env, serviceConf);
            String containerName = namingStrategy.generateContainerName(env, imageConf.getId());
            Container container = containerManager.createContainer(env, containerName, imageConf, image, command);
            capp.registerContainer(imageConf.getId(), container);
        }
        app.setDeployed(true);
    }

    /*private String getCmd(Environment env, ImageConfiguration imageConfiguration) {
        String command = imageConfiguration.getCommand();
        if (command != null) {
            command = command.replace("$envId", env.getId());
        }
        return command;
    }*/

    @Override
    public void startApplication(Environment env, Application app) throws DenvException {
        ContainerizedApplication dapp = (ContainerizedApplication)app;
        // start all containers in order of dependencies
        boolean allContainersStarted = false;
        while (!allContainersStarted) {
            allContainersStarted = true;
            for (Container container : dapp.getContainers()) {
                if (container.isRunning()) {
                    continue;
                }

                boolean allLinkedContainersStarted = true;
                Image image = container.getImage();
                ImageConfiguration imageConf = image.getConf();
                if (imageConf.getLinks() != null) {
                    for (LinkConfiguration link : imageConf.getLinks()) {
                        Container linkedContainer = dapp.getContainer(link.getService());
                        if (linkedContainer == null) {
                            throw new DenvException("Could not find container for image " + link.getService());
                        }
                        if (!linkedContainer.isRunning()) {
                            allLinkedContainersStarted = false;
                            break;
                        }
                        /*
                        ImageConfiguration linkedImageConf = app.getConf().getImageConfiguration(link.getService());
                        for (PortConfiguration port : linkedImageConf.getPorts()) {
                            if (!containerManager.isContainerListeningOnPort(linkedContainer, port)) {
                                allLinkedContainersStarted = false;
                                break;
                            }
                        }*/
                    }
                }
                if (allLinkedContainersStarted) {
                    containerManager.startContainer(env, container);
                    allContainersStarted = false;
                }
            }
            if (!allContainersStarted) {
                try {
                    Thread.sleep(5000); // allow 5 seconds for dependent containers to start
                } catch (InterruptedException e) {
                }
            }
        }
        dapp.setStarted(true);
    }

    @Override
    public void stopApplication(Environment env, Application app) throws ContainerizationException {
        ContainerizedApplication capp = (ContainerizedApplication)app;
        for (Container container : capp.getContainers()) {
            containerManager.stopContainer(env, container);
        }
        app.setStarted(false);
    }

    @Override
    public void deleteApplication(Environment env, Application app) throws ContainerizationException {
        ContainerizedApplication capp = (ContainerizedApplication)app;
        for (Container container : capp.getContainers()) {
            containerManager.deleteContainer(env, container);
        }
    }

    @Override
    public InputStream getResource(Container container, String resourcePath) throws ContainerizationException {
        return containerManager.getResource(container, resourcePath);
    }

    @Override
    public InputStream getFolder(Container container, String folderPath) throws ContainerizationException {
        return containerManager.getFolder(container, folderPath);
    }
}
