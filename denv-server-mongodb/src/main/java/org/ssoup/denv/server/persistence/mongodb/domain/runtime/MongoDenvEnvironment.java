package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;
import org.ssoup.denv.common.model.config.node.NodeConfiguration;
import org.ssoup.denv.server.domain.runtime.application.Application;
import org.ssoup.denv.server.domain.runtime.environment.Environment;

import java.util.Arrays;
import java.util.Collection;

/**
 * User: ALB
 * Date: 25/08/14 08:57
 */
@Document(collection="environment")
public class MongoDenvEnvironment implements Environment {

    private String id;
    private NodeConfiguration node;
    private String environmentConfigurationId;
    private boolean started;

    @Transient
    private EnvironmentConfiguration environmentConfiguration;

    private Application app;

    public MongoDenvEnvironment() {
    }

    public MongoDenvEnvironment(String id, EnvironmentConfiguration environmentConfiguration, NodeConfiguration node) {
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
        return environmentConfigurationId;
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
