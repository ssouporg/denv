package org.ssoup.denv.server.domain.runtime.environment;

import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.environment.EnvironmentConfiguration;
import org.ssoup.denv.common.model.node.NodeConfiguration;
import org.ssoup.denv.server.domain.runtime.application.Application;

/**
 * User: ALB
 * Date: 27/02/14 09:08
 */
public interface Environment {
    String getId();

    EnvironmentConfiguration getEnvironmentConfiguration();

    NodeConfiguration getNode();

    ApplicationConfiguration getApplicationConfiguration();

    Application getApplication();

    void registerApp(Application app);
}
