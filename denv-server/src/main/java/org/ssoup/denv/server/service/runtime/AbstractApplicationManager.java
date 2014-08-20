package org.ssoup.denv.server.service.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.conf.application.ServiceConfiguration;
import org.ssoup.denv.server.domain.container.ContainerizationException;
import org.ssoup.denv.server.domain.container.Container;
import org.ssoup.denv.server.domain.container.ContainerManager;
import org.ssoup.denv.server.domain.container.Image;
import org.ssoup.denv.server.domain.container.ImageManager;
import org.ssoup.denv.server.domain.application.Application;
import org.ssoup.denv.server.domain.application.ApplicationFactory;
import org.ssoup.denv.server.domain.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.service.naming.NamingStrategy;

import java.io.InputStream;

/**
 * User: ALB
 * Date: 05/09/13 17:39
 */
public abstract class AbstractApplicationManager implements ApplicationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplicationManager.class);

    private ImageManager imageManager;

    private ContainerManager containerManager;

    private ApplicationFactory applicationFactory;

    private NamingStrategy namingStrategy;

    public AbstractApplicationManager(ImageManager imageManager, ContainerManager containerManager, ApplicationFactory applicationFactory, NamingStrategy namingStrategy) {
        this.imageManager = imageManager;
        this.containerManager = containerManager;
        this.applicationFactory = applicationFactory;
        this.namingStrategy = namingStrategy;
    }

    public ContainerManager getContainerManager() {
        return containerManager;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    @Override
    public void createApplications(Environment env) throws DenvException {
        for (ApplicationConfiguration appConf : env.getApplicationConfigurations()) {
            this.createApplication(env, appConf);
        }
    }

    @Override
    public void startApplications(Environment env) throws DenvException {
        for (Application application : env.getApplications()) {
            this.startApplication(env, application);
        }
    }

    @Override
    public void stopApplications(Environment env) throws ContainerizationException {
        for (Application application : env.getApplications()) {
            this.stopApplication(env, application);
        }
    }

    @Override
    public void deleteApplications(Environment env) throws DenvException {
        for (Application application : env.getApplications()) {
            this.deleteApplication(env, application);
        }
    }

    @Override
    public Application createApplication(Environment env, ApplicationConfiguration appConf) throws DenvException {
        Application newApplication = applicationFactory.createApplication(appConf.getName(), appConf);
        for (ServiceConfiguration serviceConf : appConf.getServiceConfigurations()) {
            Image image = imageManager.findOrBuildImage(env, appConf, serviceConf.getImage());
            String command = getCmd(env, serviceConf);
            String containerName = namingStrategy.generateContainerName(env, appConf, serviceConf.getImage());
            Container container = containerManager.createContainer(
                    env, containerName, image, command,
                    serviceConf.getPorts() != null ? serviceConf.getPorts().values().toArray(new Integer[0]) : null,
                    serviceConf.getVolumeInfos()
            );
            newApplication.registerContainer(serviceConf.getImage(), container);
        }
        env.registerApp(newApplication);
        return newApplication;
    }

    private String getCmd(Environment env, ServiceConfiguration serviceConfiguration) {
        String command = serviceConfiguration.getCommand();
        if (command != null) {
            command = command.replace("$envId", env.getId());
        }
        return command;
    }

    @Override
    public void startApplication(Environment env, Application application) throws DenvException {
        for (Container container : application.getContainers()) {
            containerManager.startContainer(env, container);
        }

    }

    @Override
    public void stopApplication(Environment env, Application application) throws ContainerizationException {
        for (Container container : application.getContainers()) {
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

    public ApplicationFactory getApplicationFactory() {
        return applicationFactory;
    }
}
