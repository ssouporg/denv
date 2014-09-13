package org.ssoup.denv.server.containerization.service.container;

import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;
import org.ssoup.denv.core.containerization.domain.conf.application.PortConfiguration;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.containerization.domain.runtime.Image;
import org.ssoup.denv.server.containerization.domain.runtime.Container;
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

    List<Container> getAllRunningContainers() throws ContainerizationException;

    Container findContainer(Environment env, ImageConfiguration imageConf, String containerName) throws DenvException;

    Container createContainer(Environment env, String containerName, ImageConfiguration imageConf, Image image, String command) throws ContainerizationException;

    Container startContainer(Environment env, Container container) throws ContainerizationException;

    void stopContainer(Environment env, Container container) throws ContainerizationException;

    void deleteContainer(Environment env, Container container) throws ContainerizationException;

    void saveContainerAsApplicationImage(Environment env, Container container, ApplicationConfiguration applicationConfiguration, String imageType) throws DenvException;

    void saveContainer(Environment env, Container container, String imageName) throws ContainerizationException;

    InputStream getResource(Container container, String resourcePath) throws ContainerizationException;

    InputStream getFolder(Container container, String folderPath) throws ContainerizationException;

    boolean isContainerListeningOnPort(Container linkedContainer, PortConfiguration port);
}
