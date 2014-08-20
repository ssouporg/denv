package org.ssoup.denv.server.domain.application;

import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.container.Container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 26/02/14 14:13
 */
public class ApplicationImpl implements Application {

    private String name;
    private ApplicationConfiguration conf;
    private Map<String, Container> containers = new HashMap<String, Container>();

    public ApplicationImpl(String name, ApplicationConfiguration conf) {
        this.name = name;
        this.conf = conf;
    }

    @Override
    public void registerContainer(String imageType, Container container) {
        this.containers.put(imageType, container);
    }

    @Override
    public Container getContainer(String imageType) {
        return this.containers.get(imageType);
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
    public ApplicationConfiguration getConf() {
        return conf;
    }
}
