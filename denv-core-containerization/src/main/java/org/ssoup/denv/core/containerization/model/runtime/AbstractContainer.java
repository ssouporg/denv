package org.ssoup.denv.core.containerization.model.runtime;

import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * User: ALB
 * Date: 12/09/14 14:19
 */
public abstract class AbstractContainer implements Container {

    private String id;

    private String[] names;
    private Image image;

    private String hostname;
    private Map<Integer, Integer> portMapping;

    private boolean running;

    private Map<String, String> variables;

    public AbstractContainer() {
    }

    public AbstractContainer(Container container) {
        this.id = container.getId();
        this.names = container.getAllNames();
        this.hostname = container.getHostname();
        this.portMapping = container.getPortMapping();
        this.running = container.isRunning();
    }

    public AbstractContainer(String id, Image image, boolean running) {
        this.id = id;
        this.image = image;
        this.running = running;
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

    @Override
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    @Override
    public String getVariableValue(String variable) {
        return getVariables().get(variable);
    }
}
