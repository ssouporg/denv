package org.ssoup.denv.server.domain.runtime.application;

import org.ssoup.denv.server.domain.runtime.container.Container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 26/02/14 14:13
 */
public class DenvApplication implements Application {

    private String name;
    private String applicationConfigurationName;
    private Map<String, Container> containers = new HashMap<String, Container>();

    public DenvApplication(String name, String applicationConfigurationName) {
        this.name = name;
        this.applicationConfigurationName = applicationConfigurationName;
    }

    @Override
    public void registerContainer(String imageName, Container container) {
        this.containers.put(imageName, container);
    }

    @Override
    public Container getContainer(String imageName) {
        return this.containers.get(imageName);
    }

    @Override
    public Collection<Container> getContainers() {
        return containers.values();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getApplicationConfigurationName() {
        return applicationConfigurationName;
    }

    public void setApplicationConfigurationName(String applicationConfigurationName) {
        this.applicationConfigurationName = applicationConfigurationName;
    }
}
