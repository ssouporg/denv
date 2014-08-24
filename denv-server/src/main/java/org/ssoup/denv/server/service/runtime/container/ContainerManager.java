package org.ssoup.denv.server.service.runtime.container;

import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.conf.application.VolumeConfiguration;
import org.ssoup.denv.server.domain.runtime.container.Container;
import org.ssoup.denv.server.exception.ContainerizationException;
import org.ssoup.denv.server.domain.runtime.container.Image;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;

import java.io.InputStream;
import java.util.List;

/**
 * User: ALB
 * Date: 09/01/14 13:31
 */
public interface ContainerManager {

    List<Container> getAllContainers() throws ContainerizationException;

    List<Container> getAllRunningContainers() throws ContainerizationException;

    Container findContainer(Environment env, String containerName) throws DenvException;

    Container createContainer(Environment env, String containerName, Image image, String command, Integer[] ports, VolumeConfiguration[] volumes) throws ContainerizationException;

    Container startContainer(Environment env, Container container) throws ContainerizationException;

    void stopContainer(Environment env, Container container) throws ContainerizationException;

    void deleteContainer(Environment env, Container container) throws ContainerizationException;

    void saveContainerAsApplicationImage(Environment env, Container container, ApplicationConfiguration applicationConfiguration, String imageType) throws DenvException;

    void saveContainer(Environment env, Container container, String imageName) throws ContainerizationException;

    InputStream getResource(Container container, String resourcePath) throws ContainerizationException;

    InputStream getFolder(Container container, String folderPath) throws ContainerizationException;
}
