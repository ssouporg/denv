package org.ssoup.denv.server.containers.docker;

import com.github.dockerjava.client.DockerClient;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.CommitConfig;
import com.github.dockerjava.client.model.ContainerConfig;
import com.github.dockerjava.client.model.ContainerCreateResponse;
import com.github.dockerjava.client.model.ContainerInspectResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssoup.denv.server.conf.application.ServiceConfiguration;
import org.ssoup.denv.server.containers.ContainerizationException;
import org.ssoup.denv.server.containers.container.AbstractContainerManager;
import org.ssoup.denv.server.containers.container.Container;
import org.ssoup.denv.server.containers.container.ContainerManager;
import org.ssoup.denv.server.containers.image.Image;
import org.ssoup.denv.server.domain.Environment;
import org.ssoup.denv.server.exception.DenvException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ALB
 * Date: 09/01/14 15:51
 */
public class DockerContainerManager extends AbstractContainerManager implements ContainerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainerManager.class);

    private DockerClient dockerClient;
    private int currentPortForwarding = 50000;

    public DockerContainerManager() {
        Configuration config = ConfigManager.getConfiguration();
        String dockerHost = config.getString("dockerHost");
        Integer dockerPort = config.getInteger("dockerPort", 4243);
        String dockerAddress = "http://" + dockerHost + ":" + dockerPort;
        this.dockerClient = new DockerClient(dockerAddress);
    }

    @Override
    protected void registerExistingContainers() {
        for (com.github.dockerjava.client.model.Container dockerContainer : dockerClient.listContainers(true)) {
            try {
                Image image = getImageManager().findImage(dockerContainer.getImage());
                ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainer(dockerContainer.id);
                this.registerContainer(dockerContainer.getId(), new DockerContainer(dockerContainer, containerInspectResponse, image));
            } catch (Exception e) {
                LOGGER.error("An error occurred loading details of containers", e);
            }
        }
    }

    @Override
    public Container findContainer(Environment env, String containerName) throws DenvException {
        com.github.dockerjava.client.model.Container dockerContainer = findContainer(containerName);
        return convertContainer(dockerContainer);
    }

    private com.github.dockerjava.client.model.Container findContainer(String containerName) throws ContainerizationException {
        List<com.github.dockerjava.client.model.Container> dockerContainers = dockerClient.listContainers(true);
        if (dockerContainers != null) {
            for (com.github.dockerjava.client.model.Container dockerContainer : dockerContainers) {
                if (dockerContainer.getNames() != null) {
                    for (String name : dockerContainer.getNames()) {
                        if (containerName.equals(name)) {
                            return dockerContainer;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Container convertContainer(com.github.dockerjava.client.model.Container dockerContainer) throws DenvException {
        if (dockerContainer != null) {
            try {
                ContainerInspectResponse containerDetails = dockerClient.inspectContainer(dockerContainer.id);
                Image image = getImageManager().findImage(dockerContainer.image);
                return new DockerContainer(dockerContainer, containerDetails, image);
            } catch (DockerException e) {
                throw new ContainerizationException(e);
            }
        }
        return null;
    }

    @Override
    public Container createContainer(Environment env, String containerName, Image image, String[] command, Integer[] ports, VolumeInfo[] volumes) throws ContainerizationException {
        ContainerCreateResponse containerCreateResponse = null;
        ContainerInspectResponse containerInspectResponse = null;
        try {
            ContainerConfig containerConfig = new ContainerConfig();
            containerConfig.setImage(image.getId());
            // keep the containers alive
            //containerConfig.setCmd(new String[]{"/bin/bash", "/home/synaptiq/run_tomcat.sh", dockerHost, "" + mysqlPort});
            if (command != null) {
                containerConfig.setCmd(command);
            }
            /*if (image.getApplication() != null && "Web".equals(image.getImageType())) {
                containerConfig.setPortSpecs(new String[]{"8080:8080"});
            }*/
            Map<String, Map<String, String>> exposedPorts = new HashMap<String, Map<String, String>>();

            if (ports != null) {
                for (Integer port : ports) {
                    Map<String, String> portConfig = new HashMap<String, String>();
                    String portDefinition = "" + port + "/tcp";
                    exposedPorts.put(portDefinition, portConfig); // empty port config for container creation

                    Map<String, String>[] p = new Map[1]; // port config is added at time of container start : see http://stackoverflow.com/questions/20428302/binding-a-port-to-a-host-interface-using-the-rest-api/20429133
                    p[0] = new HashMap<String, String>();
                    p[0].put("HostPort", "" + currentPortForwarding);
                    currentPortForwarding ++;

                }
            }
            containerConfig.setExposedPorts(exposedPorts);
            //containerConfig.setPortSpecs(new String[]{"50000:21"});
            containerConfig.setTty(false);
            Map<String, Map<String, String>> volumesMap = new HashMap<String, Map<String, String>>();

            if (volumes != null) {
                for (ServiceConfiguration.VolumeInfo volumeInfo : volumes) {
                    volumesMap.put(volumeInfo.getName(), new HashMap<String, String>());
                    
                }
            }
            containerConfig.setVolumes(volumesMap);

            containerCreateResponse = dockerClient.createContainer(containerName, containerConfig);

            containerInspectResponse = dockerClient.inspectContainer(containerCreateResponse.id);

            DockerContainer dockerContainer = new DockerContainer(containerCreateResponse, containerInspectResponse, image,volumes );

            return dockerContainer;
        } catch(Exception ex) {
            if (containerCreateResponse != null) {
                try {
                    dockerClient.kill(containerCreateResponse.id);
                    dockerClient.removeContainer(containerCreateResponse.id);
                }
                catch(DockerException e) {
                    throw new ContainerizationException(e);
                }
            }
            throw new ContainerizationException(ex);
        }
    }

    @Override
    public Container startContainer(Environment env, Container container) throws ContainerizationException {
        DockerContainer dockerContainer = (DockerContainer) container;
        try {
            Map<String, Map<String, String>[]> portBindings = new HashMap<String, Map<String, String>[]>();
            if (dockerContainer.getPortMapping() != null) {
                for (Integer port : dockerContainer.getPortMapping().keySet()) {
                    Map<String, String> portConfig = new HashMap<String, String>();
                    String portDefinition = "" + port + "/tcp";
                    Map<String, String>[] p = new Map[1]; // port config is added at time of container start : see http://stackoverflow.com/questions/20428302/binding-a-port-to-a-host-interface-using-the-rest-api/20429133
                    p[0] = new HashMap<String, String>();
                    p[0].put("HostPort", "" + currentPortForwarding);
                    currentPortForwarding ++;
                    portBindings.put(portDefinition, p);
                }
            }

            List<String> binds = new ArrayList<String>();
            if (dockerContainer.getVolumes() != null) {
                for (ServiceConfiguration.VolumeInfo volumeInfo : dockerContainer.getVolumeInfos()) {
                    if (volumeInfo.getHostDir() != null) {
                        binds.add(volumeInfo.getHostDir() + ":" + volumeInfo.getName() + ":rw");
                    } else {
                        binds.add(volumeInfo.getName());
                    }
                }
            }
            HostConfig hostConfig = new HostConfig();
            hostConfig.setBinds(binds.toArray(new String[0]));
            hostConfig.setPortBindings(portBindings);
            dockerClient.startContainer(dockerContainer.getId(), hostConfig);
            return dockerContainer;
        } catch(Exception ex) {
            throw new ContainerizationException(ex);
        }
    }

    @Override
    public void stopContainer(Environment env, Container container) throws ContainerizationException {
        try {
            dockerClient.kill(container.getId());
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    public void deleteContainer(Environment env, Container container) throws ContainerizationException {
        try {
            dockerClient.removeContainer(container.getId());
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    public void saveContainerAsApplicationImage(Environment env, Container container, Application app, String imageType) throws EnvAutomationException {
        String imageName = Envs.getContainerNamingStrategy().generateImageName(env.getConf(), app.getConf(), imageType);
        String appVersion = Envs.getVersioningPolicy().getAppVersion(env, app);
        saveContainer(env, container, imageName, appVersion);
    }

    @Override
    public void saveContainer(Environment env, Container container, String imageName) throws ContainerizationException {
        saveContainer(env, container, imageName, null);
    }

    public void saveContainer(Environment env, Container container, String imageName, String tag) throws ContainerizationException {
        CommitConfig commitConfig = new CommitConfig();
        commitConfig.container = container.getId();
        commitConfig.message = "Created.";
        commitConfig.repo = imageName; // "synaptiq/sqc-db-" + dbImageName;
        commitConfig.tag = tag;
        try {
            String response = dockerClient.commit(commitConfig);
            LOGGER.info("Saving image " + imageName + ": " + response);
        } catch (DockerException ex) {
            throw new ContainerizationException(ex);
        }
    }

    @Override
    public InputStream getResource(Container container, String resourcePath) throws ContainerizationException {
        try {
            return  dockerClient.copyFile(container.getId(), resourcePath);
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }

    }


    @Override
    public InputStream getFolder(Container container, String folderPath) throws ContainerizationException {
        try {
            return  dockerClient.copyFile(container.getId(), folderPath);
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }

    }
}
