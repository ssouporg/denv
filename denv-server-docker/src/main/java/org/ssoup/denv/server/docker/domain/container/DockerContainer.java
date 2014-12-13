package org.ssoup.denv.server.docker.domain.container;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import org.springframework.data.annotation.Transient;
import org.ssoup.denv.core.containerization.model.runtime.AbstractContainer;
import org.ssoup.denv.core.containerization.model.runtime.Container;
import org.ssoup.denv.core.containerization.model.runtime.ContainerState;
import org.ssoup.denv.core.containerization.model.runtime.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 09/01/14 11:21
 */
public class DockerContainer extends AbstractContainer {

    private CreateContainerResponse containerCreateResponse;
    private InspectContainerResponse containerInspectResponse;

    public DockerContainer() {
    }

    public DockerContainer(Container container) {
        super(container);
    }

    public DockerContainer(String id, Image image, boolean running) {
        super(id, image, running);
    }

    public DockerContainer(CreateContainerResponse containerCreateResponse,
                           InspectContainerResponse containerInspectResponse,
                           Image image) {
        super(containerCreateResponse.getId(), image, containerInspectResponse.getState().isRunning());
        this.containerCreateResponse = containerCreateResponse;
        this.containerInspectResponse = containerInspectResponse;
        fillPortMapping(containerInspectResponse);
    }

    public DockerContainer(com.github.dockerjava.api.model.Container dockerContainer,
                           InspectContainerResponse containerInspectResponse, Image image) {
        this.containerInspectResponse = containerInspectResponse;
        this.setId(dockerContainer.getId());
        if (dockerContainer.getNames() != null) {
            String[] names = new String[dockerContainer.getNames().length];
            for (int i = 0 ; i < dockerContainer.getNames().length ; i ++) {
                names[i] = dockerContainer.getNames()[i].substring(1); // discard first character /
            }
            this.setNames(names);
        }
        this.setImage(image);
        this.setRunning(this.containerInspectResponse.getState().isRunning());
        fillPortMapping(containerInspectResponse);
    }

    public void fillPortMapping(InspectContainerResponse containerInspectResponse) {
        Map<Integer, Integer> portMapping = new HashMap<Integer, Integer>();
        if (containerInspectResponse.getNetworkSettings() != null && containerInspectResponse.getNetworkSettings().getPorts() != null) {
            Ports ports = containerInspectResponse.getNetworkSettings().getPorts();
            for (ExposedPort port : ports.getBindings().keySet()) {
                Ports.Binding binding = ports.getBindings().get(port);
                portMapping.put(port.getPort(), binding.getHostPort());
            }
        }
        this.setPortMapping(portMapping);
    }
}
