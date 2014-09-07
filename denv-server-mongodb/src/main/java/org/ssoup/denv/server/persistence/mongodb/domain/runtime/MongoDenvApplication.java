package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.ssoup.denv.server.domain.runtime.application.Application;
import org.ssoup.denv.server.domain.runtime.container.Container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 26/02/14 14:13
 */
public class MongoDenvApplication implements Application {

    private String name;
    private String applicationConfigurationName;
    private Map<String, Container> containers = new HashMap<String, Container>();

    public MongoDenvApplication() {
    }

    public MongoDenvApplication(Application application) {
        this.name = application.getName();
        this.applicationConfigurationName = application.getApplicationConfigurationName();
        for (Container container : application.getContainers()) {
            this.registerContainer(container.getImageId(), new MongoDenvContainer(container));
        }
    }

    @Override
    public void registerContainer(String imageId, Container container) {
        this.containers.put(imageId, container);
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

    public void setName(String name) {
        this.name = name;
    }
}
