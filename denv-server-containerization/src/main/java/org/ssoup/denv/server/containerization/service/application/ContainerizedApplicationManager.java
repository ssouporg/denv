package org.ssoup.denv.server.containerization.service.application;

import org.ssoup.denv.server.containerization.domain.runtime.Container;
import org.ssoup.denv.server.containerization.exception.ContainerizationException;
import org.ssoup.denv.server.containerization.service.container.ContainerManager;
import org.ssoup.denv.server.containerization.service.container.ImageManager;
import org.ssoup.denv.server.service.runtime.application.ApplicationManager;

import java.io.InputStream;

/**
 * User: ALB
 * Date: 12/09/14 10:30
 */
public interface ContainerizedApplicationManager extends ApplicationManager {
    ImageManager getImageManager();
    ContainerManager getContainerManager();
    InputStream getResource(Container container, String resourcePath) throws ContainerizationException;
    InputStream getFolder(Container container, String folderPath) throws ContainerizationException;
}
