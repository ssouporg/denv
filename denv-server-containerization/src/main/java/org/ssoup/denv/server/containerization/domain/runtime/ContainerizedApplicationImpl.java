package org.ssoup.denv.server.containerization.domain.runtime;

import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.ApplicationImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 26/02/14 14:13
 */
public class ContainerizedApplicationImpl extends ApplicationImpl implements ContainerizedApplication {

    private Map<String, Container> containers = new HashMap<String, Container>();

    public ContainerizedApplicationImpl() {
    }

    public ContainerizedApplicationImpl(Application app) {
        super(app.getId(), app.getApplicationConfigurationId());
        this.setStarted(app.isStarted());
        this.setDeployed(app.isDeployed());
    }

    public ContainerizedApplicationImpl(String id, String applicationConfigurationId) {
        super(id, applicationConfigurationId);
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
    public void registerContainer(String imageId, Container container) {
        this.containers.put(imageId, container);
    }
}
