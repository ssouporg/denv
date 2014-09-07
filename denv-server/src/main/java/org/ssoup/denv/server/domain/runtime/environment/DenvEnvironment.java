package org.ssoup.denv.server.domain.runtime.environment;

import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;
import org.ssoup.denv.common.model.config.node.NodeConfiguration;
import org.ssoup.denv.server.domain.runtime.application.Application;

/**
 * User: ALB
 * Date: 08/01/14 17:30
 */
public class DenvEnvironment implements Environment {

    private String id;
    private NodeConfiguration node;
    private String environmentConfigurationId;
    private EnvironmentConfiguration environmentConfiguration;
    private Application app;
    private boolean started;

    public DenvEnvironment() {
    }

    public DenvEnvironment(String id, EnvironmentConfiguration environmentConfiguration, NodeConfiguration node) {
        this.node = node;
        this.environmentConfigurationId = environmentConfiguration.getId();
        this.environmentConfiguration = environmentConfiguration;
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEnvironmentConfigurationId() {
        return null;
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
    public Application getApplication() {
        return app;
    }

    @Override
    public void setApplication(Application app) {
        this.app = app;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEnvironmentConfigurationId(String environmentConfigurationId) {
        this.environmentConfigurationId = environmentConfigurationId;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
