package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.springframework.data.annotation.Transient;
import org.ssoup.denv.server.domain.runtime.container.Container;
import org.ssoup.denv.server.domain.runtime.container.Image;

import java.util.Map;

/**
 * User: ALB
 * Date: 09/01/14 11:21
 */
public class MongoDenvContainer implements Container {
    private String id;
    private String[] names;
    private String imageId;

    private String hostname;
    private Map<Integer, Integer> portMapping;

    private boolean running;

    public MongoDenvContainer() {
    }

    public MongoDenvContainer(Container container) {
        this.id = container.getId();
        this.names = container.getAllNames();
        this.imageId = container.getImage() != null ? container.getImage().getId() : null;
        this.hostname = container.getHostname();
        this.portMapping = container.getPortMapping();
        this.running = container.isRunning();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return names != null && names.length > 0 ? names[0] : null;
    }

    @Override
    public String[] getAllNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public String getImageId() {
        return this.imageId;
    }

    @Override
    @Transient
    public Image getImage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public Map<Integer, Integer> getPortMapping() {
        return portMapping;
    }

    public void setPortMapping(Map<Integer, Integer> portMapping) {
        this.portMapping = portMapping;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
