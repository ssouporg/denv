package org.ssoup.denv.server.domain.runtime.environment;

import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.environment.EnvironmentConfiguration;
import org.ssoup.denv.common.model.node.NodeConfiguration;
import org.ssoup.denv.server.domain.runtime.application.Application;

/**
 * User: ALB
 * Date: 08/01/14 17:30
 */
public class DenvEnvironment implements Environment {

    private String id;
    private NodeConfiguration node;
    private EnvironmentConfiguration environmentConfiguration;
    private Application app;

    public DenvEnvironment(String id, EnvironmentConfiguration environmentConfiguration, NodeConfiguration node) {
        this.node = node;
        this.environmentConfiguration = environmentConfiguration;
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public NodeConfiguration getNode() {
        return node;
    }

    @Override
    public EnvironmentConfiguration getEnvironmentConfiguration() {
        return environmentConfiguration;
    }

    @Override
    public ApplicationConfiguration getApplicationConfiguration() {
        return environmentConfiguration.getApplicationConfiguration();
    }

    @Override
    public Application getApplication() {
        return app;
    }

    @Override
    public void registerApp(Application app) {
        this.app = app;
    }

    public void setId(String id) {
        this.id = id;
    }
}
