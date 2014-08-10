package org.ssoup.denv.server.containers.docker;

import com.github.dockerjava.client.model.ContainerCreateResponse;
import com.github.dockerjava.client.model.ContainerInspectResponse;
import com.github.dockerjava.client.model.ExposedPort;
import com.github.dockerjava.client.model.Ports;
import org.ssoup.denv.server.conf.application.ServiceConfiguration;
import org.ssoup.denv.server.containers.container.Container;
import org.ssoup.denv.server.containers.image.Image;

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
    private ServiceConfiguration.VolumeInfo[] volumes ;
    private ContainerCreateResponse containerCreateResponse;
    private ContainerInspectResponse containerInspectResponse;

    private boolean running;

    public DockerContainer(ContainerCreateResponse containerCreateResponse, ContainerInspectResponse containerInspectResponse, Image image , ServiceConfiguration.VolumeInfo[] volumes) {
        this.containerCreateResponse = containerCreateResponse;
        this.containerInspectResponse = containerInspectResponse;
        this.id = this.containerCreateResponse.getId();
        this.image = image;
        this.running = this.containerInspectResponse.getState().isRunning();
        fillPortMapping(containerInspectResponse);
        this.volumes = volumes;
    }

    public DockerContainer(String id, Image image, boolean running) {
        this.id = id;
        this.image = image;
        this.running = running;
    }

    public DockerContainer(com.github.dockerjava.client.model.Container dockerContainer, ContainerInspectResponse containerInspectResponse, Image image) {
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

    private void fillPortMapping(ContainerInspectResponse containerInspectResponse) {
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

    public ServiceConfiguration.VolumeInfo[] getVolumeInfos() {
        return volumes;
    }

    @Override
    public Map<Integer, Integer> getPortMapping() {
        return portMapping;
    }

    public void setPortMapping(Map<Integer, Integer> portMapping) {
        this.portMapping = portMapping;
    }
}
