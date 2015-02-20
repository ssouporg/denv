package org.ssoup.denv.server.docker.domain.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.*;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.containerization.model.runtime.Container;
import org.ssoup.denv.core.containerization.model.runtime.Image;
import org.ssoup.denv.server.containerization.exception.ContainerNotFoundException;
import org.ssoup.denv.server.containerization.exception.ContainerizationException;
import org.ssoup.denv.server.containerization.service.container.AbstractContainerManager;
import org.ssoup.denv.server.containerization.service.container.ImageManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.containerization.service.versioning.VersioningPolicy;
import org.ssoup.denv.server.docker.domain.conf.DockerNodeConfiguration;
import org.ssoup.denv.server.service.conf.node.NodeManager;

import java.io.InputStream;
import java.net.Socket;
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
    private NodeManager nodeManager;

    @Autowired
    public DockerContainerManager(ImageManager imageManager, NamingStrategy namingStrategy,
                                  VersioningPolicy versioningPolicy, NodeManager nodeManager) {
        super(imageManager, namingStrategy, versioningPolicy);
        // dockerClient = new DockerClient(dockerEnvironmentConfiguration.getDockerAddress());
        this.nodeManager = nodeManager;
    }

    private DockerClient getDockerClient() {
        if (this.dockerClient == null) {
            this.dockerClient = new DockerClientImpl(((DockerNodeConfiguration)this.nodeManager.getDefaultNode()).getDockerAddress());
        }
        return this.dockerClient;
    }

    @Override
    protected void registerExistingContainers() {
        /*for (com.github.dockerjava.api.model.Container dockerContainer : getDockerClient().listContainersCmd().withShowAll(true).exec()) {
            try {
                Image image = getImageManager().findImage(null, null); // dockerContainer.getImageForMongo()); // TODO: extract the environment and the image configuration somehow
                InspectContainerResponse containerInspectResponse = getDockerClient().inspectContainerCmd(dockerContainer.getId()).exec();
                this.registerContainer(dockerContainer.getId(), new DockerContainer(dockerContainer, containerInspectResponse, image));
            } catch (Exception e) {
                LOGGER.error("An error occurred loading details of container", e);
            }
        }*/
    }

    @Override
    public Container findContainerById(Environment env, EnvironmentConfiguration envConf,
                                       ImageConfiguration imageConf, String containerId) throws DenvException {
        com.github.dockerjava.api.model.Container dockerContainer = findContainerById(containerId);
        return convertContainer(env, envConf, imageConf, dockerContainer);
    }

    @Override
    public Container findContainerByName(Environment env, EnvironmentConfiguration envConf,
                                         ImageConfiguration imageConf, String containerName) throws DenvException {
        com.github.dockerjava.api.model.Container dockerContainer = findContainerByName(containerName);
        return convertContainer(env, envConf, imageConf, dockerContainer);
    }

    private com.github.dockerjava.api.model.Container findContainerById(String containerId) throws ContainerizationException {
        List<com.github.dockerjava.api.model.Container> dockerContainers = getDockerClient().listContainersCmd().withShowAll(true).exec();
        if (dockerContainers != null) {
            for (com.github.dockerjava.api.model.Container dockerContainer : dockerContainers) {
                if (dockerContainer.getNames() != null) {
                    if (containerId.equals(dockerContainer.getId())) {
                        return dockerContainer;
                    }
                }
            }
        }
        return null;
    }

    private com.github.dockerjava.api.model.Container findContainerByName(String containerName) throws ContainerizationException {
        List<com.github.dockerjava.api.model.Container> dockerContainers = getDockerClient().listContainersCmd().withShowAll(true).exec();
        if (dockerContainers != null) {
            for (com.github.dockerjava.api.model.Container dockerContainer : dockerContainers) {
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

    private Container convertContainer(Environment env, EnvironmentConfiguration envConf,
                                       ImageConfiguration imageConf, com.github.dockerjava.api.model.Container dockerContainer) throws DenvException {
        if (dockerContainer != null) {
            try {
                InspectContainerResponse containerDetails = getDockerClient().inspectContainerCmd(dockerContainer.getId()).exec();
                Image image = getImageManager().findImage(envConf, env.getVersion(), imageConf);
                DockerContainer container = new DockerContainer(dockerContainer, containerDetails, image);
                fillVariables(env, imageConf, container);
                return container;
            } catch (DockerException e) {
                throw new ContainerizationException(e);
            }
        }
        return null;
    }

    @Override
    public Container createContainer(Environment env, String containerName, ImageConfiguration imageConf, Image image, String command) throws ContainerizationException {
        CreateContainerResponse containerCreateResponse = null;
        InspectContainerResponse containerInspectResponse = null;
        try {
            CreateContainerCmd createContainerCommand = getDockerClient().createContainerCmd(image.getId());
            createContainerCommand.withName(containerName);
            // keep the container alive
            //containerConfig.setCmd(new String[]{"/bin/bash", "/home/synaptiq/run_tomcat.sh", dockerHost, "" + mysqlPort});
            if (command != null) {
                createContainerCommand.withCmd(command.split(" "));
            }
            List<ExposedPort> exposedPorts = new ArrayList<ExposedPort>();
            if (imageConf.getPorts() != null) {
                for (PortConfiguration port : imageConf.getPorts()) {
                    exposedPorts.add(new ExposedPort("tcp", port.getContainerPort()));
                }
            }
            createContainerCommand.withExposedPorts(exposedPorts.toArray(new ExposedPort[0]));
            createContainerCommand.withTty(false);
            List<Volume> volumesList = new ArrayList<Volume>();
            if (imageConf.getVolumes() != null) {
                for (VolumeConfiguration volumeConfiguration : imageConf.getVolumes()) {
                    volumesList.add(new Volume(volumeConfiguration.getPath()));
                }
            }
            createContainerCommand.withVolumes(volumesList.toArray(new Volume[0]));

            if (imageConf.getEnvironment() != null) {
                List<String> environmentVariables = new ArrayList<String>();
                for (EnvironmentVariableConfiguration evc : imageConf.getEnvironment()) {
                    environmentVariables.add(evc.getVariable() + "=" + evc.getValue());
                }
                createContainerCommand.withEnv(environmentVariables.toArray(new String[0]));
            }

            containerCreateResponse = createContainerCommand.exec();

            containerInspectResponse = getDockerClient().inspectContainerCmd(containerCreateResponse.getId()).exec();

            DockerContainer dockerContainer = new DockerContainer(containerCreateResponse, containerInspectResponse, image);

            return dockerContainer;
        } catch(Exception ex) {
            if (containerCreateResponse != null) {
                try {
                    getDockerClient().killContainerCmd(containerCreateResponse.getId()).exec();
                    getDockerClient().removeContainerCmd(containerCreateResponse.getId()).exec();
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
            ImageConfiguration imageConf = container.getImage().getConf();
            Ports portBindings = new Ports();
            if (imageConf.getPorts() != null) {
                for (PortConfiguration port : imageConf.getPorts()) {
                    ExposedPort exposedPort = new ExposedPort("tcp", port.getContainerPort());
                    Ports.Binding binding = new Ports.Binding(port.getHostPort());
                    portBindings.bind(exposedPort, binding);
                }
            }
            /*if (dockerContainer.getVolumes() != null) {
                for (ServiceConfiguration.VolumeInfo volumeInfo : dockerContainer.getVolumeInfos()) {
                    if (volumeInfo.getHostDir() != null) {
                        binds.add(volumeInfo.getHostDir() + ":" + volumeInfo.getId() + ":rw");
                    } else {
                        binds.add(volumeInfo.getId());
                    }
                }
            }*/
            List<Link> links = new ArrayList<Link>();
            for (LinkConfiguration link : container.getImage().getConf().getLinks()) {
                String serviceNameInCurrentEnv = getNamingStrategy().generateContainerName(env, link.getService());
                String alias = link.getAlias();
                if (alias == null) {
                    alias = link.getService();
                }
                links.add(new Link(serviceNameInCurrentEnv, alias));
            }
            getDockerClient().startContainerCmd(dockerContainer.getId())
                    .withLinks(links.toArray(new Link[0]))
                    //.withBinds()
                    .withPortBindings(portBindings)
                    .withPrivileged(imageConf.isPrivileged())
                    .exec();
            InspectContainerResponse containerInspectResponse = getDockerClient().inspectContainerCmd(dockerContainer.getId()).exec();
            dockerContainer.setRunning(containerInspectResponse.getState().isRunning());
            dockerContainer.fillPortMapping(containerInspectResponse);
            fillVariables(env, imageConf, dockerContainer);
            return dockerContainer;
        } catch(Exception ex) {
            throw new ContainerizationException(ex);
        }
    }

    @Override
    protected String resolveDenvVariable(String value, Container container) {
        if (value.equals("DOCKER_HOST")) {
            DockerNodeConfiguration dockerNodeConfiguration = (DockerNodeConfiguration) this.nodeManager.getDefaultNode();
            return dockerNodeConfiguration.getDockerHost();
        }
        return super.resolveDenvVariable(value, container);
    }

    @Override
    public void stopContainer(Environment env, Container container) throws ContainerizationException {
        DockerContainer dockerContainer = (DockerContainer) container;
        try {
            getDockerClient().killContainerCmd(container.getId()).exec();
            InspectContainerResponse containerInspectResponse = getDockerClient().inspectContainerCmd(container.getId()).exec();
            if (!containerInspectResponse.getState().isRunning()) {
                dockerContainer.setRunning(false);
            }
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    public void deleteContainer(Environment env, String containerId) throws ContainerizationException {
        try {
            getDockerClient().removeContainerCmd(containerId).withForce().exec();
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                throw new ContainerNotFoundException(e);
            } else {
                throw new ContainerizationException(e);
            }
        }
    }

    @Override
    public void saveContainerAsEnvironmentImage(Environment env, EnvironmentConfiguration envConf,
                                                ImageConfiguration imageConf, Container container) throws DenvException {
        String imageName = this.getNamingStrategy().generateImageName(envConf, imageConf);
        String imageVersion = getVersioningPolicy().getImageVersion(envConf.getId(), env.getVersion(), imageConf);
        saveContainer(env, container, imageName, imageVersion);
    }

    @Override
    public void saveContainer(Environment env, Container container, String imageName) throws ContainerizationException {
        if (imageName.contains(":")) {
            String[] toks = imageName.split(":");
            if (toks.length == 2) {
                saveContainer(env, container, toks[0], toks[1]);
            } else {
                throw new ContainerizationException("Invalid name for image : " + imageName);
            }
        } else {
            saveContainer(env, container, imageName, env.getVersion());
        }
    }

    public void saveContainer(Environment env, Container container, String imageName, String tag) throws ContainerizationException {
        try {
            String response = getDockerClient().commitCmd(container.getId())
                    .withMessage("Created.")
                    .withRepository(imageName)
                    .withTag(tag)
                    .exec();
            LOGGER.info("Saving image " + imageName + ":" + tag + " : " + response);
        } catch (DockerException ex) {
            throw new ContainerizationException(ex);
        }
    }

    @Override
    public InputStream getResource(Container container, String resourcePath) throws ContainerizationException {
        try {
            return  getDockerClient().copyFileFromContainerCmd(container.getId(), resourcePath).exec();
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    public InputStream getFolder(Container container, String folderPath) throws ContainerizationException {
        try {
            return getDockerClient().copyFileFromContainerCmd(container.getId(), folderPath).exec();
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    public boolean isContainerListeningOnPort(Container container, PortConfiguration port) {
        DockerNodeConfiguration dockerNodeConfiguration = (DockerNodeConfiguration)nodeManager.getDefaultNode();
        Integer hostPort = container.getPortMapping().get(port.getContainerPort());
        return serverListening(dockerNodeConfiguration.getDockerHost(), hostPort);
    }

    private boolean serverListening(String host, int port)
    {
        Socket s = null;
        try
        {
            s = new Socket(host, port);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            if(s != null)
                try {s.close();}
                catch(Exception e){}
        }
    }
}
