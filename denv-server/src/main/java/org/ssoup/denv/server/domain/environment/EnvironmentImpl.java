package org.ssoup.denv.server.domain.environment;

import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.application.Application;
import org.ssoup.denv.server.domain.conf.node.NodeConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 08/01/14 17:30
 */
public class EnvironmentImpl implements Environment {

    private NodeConfiguration node;

    private String id;
    private String version;

    private Map<String, ApplicationConfiguration> applicationConfigurationMap = new HashMap<String, ApplicationConfiguration>();
    private Map<String, Application> apps = new HashMap<String, Application>();

    public EnvironmentImpl(String id, String version, NodeConfiguration node) {
        this.node = node;
        this.id = id;
        this.version = version;
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
    public String getVersion() {
        return version;
    }

    @Override
    public ApplicationConfiguration getApplicationConfiguration(String applicationName) {
        return applicationConfigurationMap.get(applicationName);
    }

    @Override
    public Collection<ApplicationConfiguration> getApplicationConfigurations() {
        return applicationConfigurationMap.values();
    }

    @Override
    public Application getApplication(String appName) {
        return apps.get(appName);
    }

    @Override
    public void registerApp(Application app) {
        apps.put(app.getName(), app);
    }

    @Override
    public Collection<Application> getApplications() {
        return apps.values();
    }

    public void setId(String id) {
        this.id = id;
    }
}
