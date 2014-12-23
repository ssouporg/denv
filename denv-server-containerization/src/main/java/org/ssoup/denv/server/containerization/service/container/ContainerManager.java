package org.ssoup.denv.server.containerization.service.container;

import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.PortConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.Image;
import org.ssoup.denv.core.containerization.model.runtime.Container;
import org.ssoup.denv.server.containerization.exception.ContainerizationException;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.exception.DenvException;

import java.io.InputStream;
import java.util.List;

/**
 * User: ALB
 * Date: 09/01/14 13:31
 */
public interface ContainerManager {

    List<Container> getAllContainers() throws ContainerizationException;

    Container findContainerById(Environment env, EnvironmentConfiguration envConf, ImageConfiguration imageConf, String containerName) throws DenvException;

    Container findContainerByName(Environment env, EnvironmentConfiguration envConf, ImageConfiguration imageConf, String containerName) throws DenvException;

    Container createContainer(Environment env, String containerName, ImageConfiguration imageConf, Image image, String command) throws ContainerizationException;

    Container startContainer(Environment env, Container container) throws ContainerizationException;

    void stopContainer(Environment env, Container container) throws ContainerizationException;

    void deleteContainer(Environment env, String containerId) throws ContainerizationException;

    void saveContainerAsEnvironmentImage(Environment env, EnvironmentConfiguration envConf, ImageConfiguration imageConf, Container container) throws DenvException;

    void saveContainer(Environment env, Container container, String imageName) throws ContainerizationException;

    InputStream getResource(Container container, String resourcePath) throws ContainerizationException;

    InputStream getFolder(Container container, String folderPath) throws ContainerizationException;

    boolean isContainerListeningOnPort(Container container, PortConfiguration port);
}
