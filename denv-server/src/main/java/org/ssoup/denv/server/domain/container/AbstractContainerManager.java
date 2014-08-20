package org.ssoup.denv.server.domain.container;

import org.ssoup.denv.server.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.versioning.VersioningPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ALB
 * Date: 13/01/14 14:28
 */
public abstract class AbstractContainerManager implements ContainerManager {

    private Map<String, Container> containers;

    private ImageManager imageManager;

    private NamingStrategy namingStrategy;

    private VersioningPolicy versioningPolicy;

    public AbstractContainerManager(ImageManager imageManager, NamingStrategy namingStrategy, VersioningPolicy versioningPolicy) {
        this.imageManager = imageManager;
        this.namingStrategy = namingStrategy;
        this.versioningPolicy = versioningPolicy;
    }

    private void initContainers() throws ContainerizationException {
        containers = new HashMap<String, Container>();
        registerExistingContainers();
    }

    protected abstract void registerExistingContainers();

    protected void registerContainer(String containerId, Container container) throws ContainerizationException {
        this.containers.put(containerId, container);
    }

    protected Map<String, Container> getContainers() throws ContainerizationException {
        //if (this.container == null) {
            this.initContainers();
        //}
        return this.containers;
    }

    @Override
    public List<Container> getAllContainers() throws ContainerizationException {
        return new ArrayList<Container>(this.getContainers().values());
    }

    @Override
    public List<Container> getAllRunningContainers() throws ContainerizationException {
        List<Container> runningContainers = new ArrayList<Container>();
        for (Container container : getContainers().values()) {
            if (container.isRunning()) {
                runningContainers.add(container);
            }
        }
        return runningContainers;
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    public VersioningPolicy getVersioningPolicy() {
        return versioningPolicy;
    }
}
