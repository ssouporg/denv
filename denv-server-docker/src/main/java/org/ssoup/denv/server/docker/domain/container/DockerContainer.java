package org.ssoup.denv.server.docker.domain.container;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import org.ssoup.denv.server.domain.runtime.container.Container;
import org.ssoup.denv.server.domain.runtime.container.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 09/01/14 11:21
 */
public class DockerContainer implements Container {
    private String id;
    private String[] names;
    private Image image;

    private String hostname;
    private Map<Integer, Integer> portMapping;

    private CreateContainerResponse containerCreateResponse;
    private InspectContainerResponse containerInspectResponse;

    private boolean running;

    public DockerContainer() {
    }

    public DockerContainer(Container container) {
        this.id = container.getId();
        this.names = container.getAllNames();
        this.hostname = container.getHostname();
        this.portMapping = container.getPortMapping();
        this.running = container.isRunning();
    }

    public DockerContainer(CreateContainerResponse containerCreateResponse, InspectContainerResponse containerInspectResponse, Image image) {
        this.containerCreateResponse = containerCreateResponse;
        this.containerInspectResponse = containerInspectResponse;
        this.id = this.containerCreateResponse.getId();
        this.image = image;
        this.running = this.containerInspectResponse.getState().isRunning();
        fillPortMapping(containerInspectResponse);
    }

    public DockerContainer(String id, Image image, boolean running) {
        this.id = id;
        this.image = image;
        this.running = running;
    }

    public DockerContainer(com.github.dockerjava.api.model.Container dockerContainer, InspectContainerResponse containerInspectResponse, Image image) {
        this.containerInspectResponse = containerInspectResponse;
        this.id = dockerContainer.getId();
        if (dockerContainer.getNames() != null) {
            this.names = new String[dockerContainer.getNames().length];
            for (int i = 0 ; i < dockerContainer.getNames().length ; i ++) {
                this.names[i] = dockerContainer.getNames()[i].substring(1); // discard first character /
            }
        }
        this.image = image;
        this.running = this.containerInspectResponse.getState().isRunning();
        fillPortMapping(containerInspectResponse);
    }

    private void fillPortMapping(InspectContainerResponse containerInspectResponse) {
        this.portMapping = new HashMap<Integer, Integer>();
        if (containerInspectResponse.getNetworkSettings() != null && containerInspectResponse.getNetworkSettings().getPorts() != null) {
            Ports ports = containerInspectResponse.getNetworkSettings().getPorts();
            for (ExposedPort port : ports.getBindings().keySet()) {
                Ports.Binding binding = ports.getBindings().get(port);
                portMapping.put(port.getPort(), binding.getHostPort());
            }
        }
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
    public String getImageId() {
        return image != null ? image.getId() : null;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public boolean isRunning() {
        return this.running;
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

    public void setRunning(boolean running) {
        this.running = running;
    }
}
