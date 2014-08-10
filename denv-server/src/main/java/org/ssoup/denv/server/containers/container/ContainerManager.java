package org.ssoup.denv.server.containers.container;

import org.ssoup.denv.server.conf.application.ServiceConfiguration;
import org.ssoup.denv.server.containers.ContainerizationException;
import org.ssoup.denv.server.containers.image.Image;
import org.ssoup.denv.server.domain.Application;
import org.ssoup.denv.server.domain.Environment;
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

    Container createContainer(Environment env, String containerName, Image image, String[] command, Integer[] ports, ServiceConfiguration.VolumeInfo[] volumes) throws ContainerizationException;

    Container startContainer(Environment env, Container container) throws ContainerizationException;

    void stopContainer(Environment env, Container container) throws ContainerizationException;

    void deleteContainer(Environment env, Container container) throws ContainerizationException;

    void saveContainerAsApplicationImage(Environment env, Container container, Application application, String imageType) throws DenvException;

    void saveContainer(Environment env, Container container, String imageName) throws ContainerizationException;

    InputStream getResource(Container container, String resourcePath) throws ContainerizationException;

    InputStream getFolder(Container container, String folderPath) throws ContainerizationException;
}
