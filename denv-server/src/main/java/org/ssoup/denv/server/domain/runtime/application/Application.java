package org.ssoup.denv.server.domain.runtime.application;

import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.runtime.container.Container;

import java.util.Collection;

/**
 * User: ALB
 * Date: 09/01/14 17:27
 */
public interface Application {
    String getName();
    String getApplicationConfigurationName();
    void registerContainer(String imageName, Container container);
    Container getContainer(String imageName);
    Collection<Container> getContainers();
}
