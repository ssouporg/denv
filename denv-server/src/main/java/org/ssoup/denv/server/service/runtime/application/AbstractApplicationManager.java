package org.ssoup.denv.server.service.runtime.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.application.ImageConfiguration;
import org.ssoup.denv.common.model.application.LinkConfiguration;
import org.ssoup.denv.common.model.application.PortConfiguration;
import org.ssoup.denv.server.domain.runtime.application.Application;
import org.ssoup.denv.server.domain.runtime.application.DenvApplication;
import org.ssoup.denv.server.domain.runtime.container.Container;
import org.ssoup.denv.server.domain.runtime.container.Image;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.ContainerizationException;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.runtime.container.ContainerManager;
import org.ssoup.denv.server.service.runtime.container.ImageManager;

import java.io.InputStream;

/**
 * User: ALB
 * Date: 05/09/13 17:39
 */
public abstract class AbstractApplicationManager implements ApplicationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplicationManager.class);

    private ImageManager imageManager;

    private ContainerManager containerManager;

    private NamingStrategy namingStrategy;

    public AbstractApplicationManager(ImageManager imageManager, ContainerManager containerManager, NamingStrategy namingStrategy) {
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
    public Application createApplication(Environment env) throws DenvException {
        ApplicationConfiguration appConf = env.getApplicationConfiguration();
        Application newApplication = new DenvApplication(appConf.getName(), appConf);
        for (ImageConfiguration imageConf : appConf.getImages()) {
            Image image = imageManager.findOrBuildImage(env, imageConf);
            String command = null; // getCmd(env, serviceConf);
            String containerName = namingStrategy.generateContainerName(env, imageConf.getName());
            Container container = containerManager.createContainer(env, containerName, imageConf, image, command);
            newApplication.registerContainer(imageConf.getName(), container);
        }
        env.registerApp(newApplication);
        return newApplication;
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
        // start all containers in order of dependencies
        boolean allContainersStarted = false;
        while (!allContainersStarted) {
            allContainersStarted = true;
            for (Container container : app.getContainers()) {
                if (container.isRunning()) {
                    continue;
                }

                boolean allLinkedContainersStarted = true;
                ImageConfiguration imageConf = container.getImage().getConf();
                if (imageConf.getLinks() != null) {
                    for (LinkConfiguration link : imageConf.getLinks()) {
                        Container linkedContainer = app.getContainer(link.getService());
                        if (linkedContainer == null) {
                            throw new DenvException("Could not find container for image " + link.getService());
                        }
                        if (!linkedContainer.isRunning()) {
                            allLinkedContainersStarted = false;
                            break;
                        }
                        /*
                        ImageConfiguration linkedImageConf = app.getConf().getImageConfigurationByName(link.getService());
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
    }

    @Override
    public void stopApplication(Environment env, Application app) throws ContainerizationException {
        for (Container container : app.getContainers()) {
            containerManager.stopContainer(env, container);
        }
    }

    @Override
    public void deleteApplication(Environment env, Application application) throws ContainerizationException {
        for (Container container : application.getContainers()) {
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
