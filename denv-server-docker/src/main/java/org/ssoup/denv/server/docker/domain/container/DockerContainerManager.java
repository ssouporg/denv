package org.ssoup.denv.server.docker.domain.container;

import com.github.dockerjava.client.DockerClient;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.command.CreateContainerCmd;
import com.github.dockerjava.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.conf.application.VolumeConfiguration;
import org.ssoup.denv.server.exception.ContainerizationException;
import org.ssoup.denv.server.service.runtime.container.AbstractContainerManager;
import org.ssoup.denv.server.domain.runtime.container.Container;
import org.ssoup.denv.server.domain.runtime.container.Image;
import org.ssoup.denv.server.service.runtime.container.ImageManager;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.versioning.VersioningPolicy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ALB
 * Date: 09/01/14 15:51
 */
@Service
public class DockerContainerManager extends AbstractContainerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainerManager.class);

    private int currentPortForwarding = 50000;

    private DockerClient dockerClient;

    @Autowired
    public DockerContainerManager(ImageManager imageManager, NamingStrategy namingStrategy, VersioningPolicy versioningPolicy) {
        super(imageManager, namingStrategy, versioningPolicy);
        // dockerClient = new DockerClient(dockerEnvironmentConfiguration.getDockerAddress());
    }

    @Override
    protected void registerExistingContainers() {
        for (com.github.dockerjava.client.model.Container dockerContainer : dockerClient.listContainersCmd().withShowAll(true).exec()) {
            try {
                Image image = getImageManager().findImage(dockerContainer.getImage());
                ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainerCmd(dockerContainer.getId()).exec();
                this.registerContainer(dockerContainer.getId(), new DockerContainer(dockerContainer, containerInspectResponse, image));
            } catch (Exception e) {
                LOGGER.error("An error occurred loading details of container", e);
            }
        }
    }

    @Override
    public Container findContainer(Environment env, String containerName) throws DenvException {
        com.github.dockerjava.client.model.Container dockerContainer = findContainer(containerName);
        return convertContainer(dockerContainer);
    }

    private com.github.dockerjava.client.model.Container findContainer(String containerName) throws ContainerizationException {
        List<com.github.dockerjava.client.model.Container> dockerContainers = dockerClient.listContainersCmd().withShowAll(true).exec();
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
                ContainerInspectResponse containerDetails = dockerClient.inspectContainerCmd(dockerContainer.getId()).exec();
                Image image = getImageManager().findImage(dockerContainer.getImage());
                return new DockerContainer(dockerContainer, containerDetails, image);
            } catch (DockerException e) {
                throw new ContainerizationException(e);
            }
        }
        return null;
    }

    @Override
    public Container createContainer(Environment env, String containerName, Image image, String command, Integer[] ports, VolumeConfiguration[] volumes) throws ContainerizationException {
        ContainerCreateResponse containerCreateResponse = null;
        ContainerInspectResponse containerInspectResponse = null;
        try {
            CreateContainerConfig createContainerConfig = new CreateContainerConfig();
            createContainerConfig.withImage(image.getId());
            // keep the container alive
            //containerConfig.setCmd(new String[]{"/bin/bash", "/home/synaptiq/run_tomcat.sh", dockerHost, "" + mysqlPort});
            if (command != null) {
                createContainerConfig.withCmd(command.split(" "));
            }
            /*if (image.getApplication() != null && "Web".equals(image.getImageType())) {
                containerConfig.setPortSpecs(new String[]{"8080:8080"});
            }*/
            List<ExposedPort> exposedPorts = new ArrayList<ExposedPort>();
            // TODO: fill in exposed ports
            createContainerConfig.withExposedPorts(exposedPorts.toArray(new ExposedPort[0]));
            //containerConfig.setPortSpecs(new String[]{"50000:21"});
            createContainerConfig.withTty(false);
            List<Volume> volumesList = new ArrayList<Volume>();
            if (volumes != null) {
                for (VolumeConfiguration volumeConfiguration : volumes) {
                    volumesList.add(new Volume(volumeConfiguration.getPath()));
                    
                }
            }
            createContainerConfig.withVolumes(volumesList.toArray(new Volume[0]));

            containerCreateResponse = new CreateContainerCmd(createContainerConfig).exec();

            containerInspectResponse = dockerClient.inspectContainerCmd(containerCreateResponse.getId()).exec();

            DockerContainer dockerContainer = new DockerContainer(containerCreateResponse, containerInspectResponse, image, volumes);

            return dockerContainer;
        } catch(Exception ex) {
            if (containerCreateResponse != null) {
                try {
                    dockerClient.killContainerCmd(containerCreateResponse.getId()).exec();
                    dockerClient.removeContainerCmd(containerCreateResponse.getId()).exec();
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
            //Binds binds = new Binds();
            Ports portBindings = new Ports();
            /*if (dockerContainer.getPortMapping() != null) {
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
            if (dockerContainer.getVolumes() != null) {
                for (ServiceConfiguration.VolumeInfo volumeInfo : dockerContainer.getVolumeInfos()) {
                    if (volumeInfo.getHostDir() != null) {
                        binds.add(volumeInfo.getHostDir() + ":" + volumeInfo.getName() + ":rw");
                    } else {
                        binds.add(volumeInfo.getName());
                    }
                }
            }*/
            dockerClient.startContainerCmd(dockerContainer.getId())
                    // .withBinds(bind)
                    .withPortBindings(portBindings)
                    .exec();
            return dockerContainer;
        } catch(Exception ex) {
            throw new ContainerizationException(ex);
        }
    }

    @Override
    public void stopContainer(Environment env, Container container) throws ContainerizationException {
        try {
            dockerClient.killContainerCmd(container.getId()).exec();
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    public void deleteContainer(Environment env, Container container) throws ContainerizationException {
        try {
            dockerClient.removeContainerCmd(container.getId()).exec();
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    public void saveContainerAsApplicationImage(Environment env, Container container, ApplicationConfiguration appConf, String imageType) throws DenvException {
        String imageName = this.getNamingStrategy().generateImageName(env.getId(), appConf, imageType);
        String appVersion = getVersioningPolicy().getAppVersion(env, appConf);
        saveContainer(env, container, imageName, appVersion);
    }

    @Override
    public void saveContainer(Environment env, Container container, String imageName) throws ContainerizationException {
        saveContainer(env, container, imageName, null);
    }

    public void saveContainer(Environment env, Container container, String imageName, String tag) throws ContainerizationException {
        try {
            String response = dockerClient.commitCmd(container.getId())
                    .withMessage("Created.")
                    .withRepository(imageName) // "synaptiq/sqc-db-" + dbImageName;
                    .withTag(tag)
                    .exec();
            LOGGER.info("Saving image " + imageName + ": " + response);
        } catch (DockerException ex) {
            throw new ContainerizationException(ex);
        }
    }

    @Override
    public InputStream getResource(Container container, String resourcePath) throws ContainerizationException {
        try {
            return  dockerClient.copyFileFromContainerCmd(container.getId(), resourcePath).exec().getEntityInputStream();
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    public InputStream getFolder(Container container, String folderPath) throws ContainerizationException {
        try {
            return dockerClient.copyFileFromContainerCmd(container.getId(), folderPath).exec().getEntityInputStream();
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }
}
