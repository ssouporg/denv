package org.ssoup.denv.server.domain.environment;

import org.ssoup.denv.server.domain.application.Application;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.conf.node.NodeConfiguration;

import java.util.Collection;

/**
 * User: ALB
 * Date: 27/02/14 09:08
 */
public interface Environment {
    String getVersion();

    String getId();

    NodeConfiguration getNode();

    ApplicationConfiguration getApplicationConfiguration(String applicationName);

    Collection<ApplicationConfiguration> getApplicationConfigurations();

    Application getApplication(String appName);

    void registerApp(Application app);

    Collection<Application> getApplications();
}
