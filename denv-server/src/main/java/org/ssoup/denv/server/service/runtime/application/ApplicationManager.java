package org.ssoup.denv.server.service.runtime.application;

import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.server.exception.ContainerizationException;
import org.ssoup.denv.server.domain.runtime.container.Container;
import org.ssoup.denv.server.service.runtime.container.ContainerManager;
import org.ssoup.denv.server.service.runtime.container.ImageManager;
import org.ssoup.denv.server.domain.runtime.application.Application;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;

import java.io.InputStream;

/**
 * User: ALB
 * Date: 11/12/13 11:51
 */
public interface ApplicationManager {
    ImageManager getImageManager();
    ContainerManager getContainerManager();

    Application createApplication(Environment env) throws DenvException;

    void startApplication(Environment env, Application application) throws DenvException;

    void stopApplication(Environment env, Application application) throws ContainerizationException;

    void deleteApplication(Environment env, Application application) throws ContainerizationException;

    InputStream getResource(Container container, String resourcePath) throws ContainerizationException;

    InputStream getFolder(Container container, String folderPath) throws ContainerizationException;
}