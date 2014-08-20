package org.ssoup.denv.server.domain.application;

import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.container.Container;

import java.util.Collection;

/**
 * User: ALB
 * Date: 09/01/14 17:27
 */
public interface Application {
    String getName();
    ApplicationConfiguration getConf();
    void registerContainer(String imageType, Container container);
    Container getContainer(String imageType);
    Collection<Container> getContainers();
}
