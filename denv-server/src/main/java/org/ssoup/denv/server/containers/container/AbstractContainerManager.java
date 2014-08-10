package org.ssoup.denv.server.containers.container;

import eu.eee.envs.containers.ContainerizationException;
import eu.eee.envs.containers.image.ImageManager;
import eu.eee.envs.containers.image.ImageManagerFactory;

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

    public AbstractContainerManager() {
    }

    protected ImageManager getImageManager() {
        if (this.imageManager == null) {
            this.imageManager = ImageManagerFactory.getDefaultImageManager();
        }
        return this.imageManager;
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
        //if (this.containers == null) {
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
}
