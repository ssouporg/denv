package org.ssoup.denv.server.docker.domain.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.DockerException;
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
import org.ssoup.denv.core.containerization.domain.conf.application.*;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.containerization.domain.runtime.Container;
import org.ssoup.denv.server.containerization.domain.runtime.Image;
import org.ssoup.denv.server.containerization.exception.ContainerizationException;
import org.ssoup.denv.server.containerization.service.container.AbstractContainerManager;
import org.ssoup.denv.server.containerization.service.container.ImageManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.docker.domain.conf.DockerNodeConfiguration;
import org.ssoup.denv.server.service.conf.node.NodeManager;
import org.ssoup.denv.server.service.versioning.VersioningPolicy;

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
    public DockerContainerManager(ImageManager imageManager, NamingStrategy namingStrategy, VersioningPolicy versioningPolicy, NodeManager nodeManager) {
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
        for (com.github.dockerjava.api.model.Container dockerContainer : getDockerClient().listContainersCmd().withShowAll(true).exec()) {
            try {
                Image image = getImageManager().findImage(null, null); // dockerContainer.getImageForMongo()); // TODO: extract the environment and the image configuration somehow
                InspectContainerResponse containerInspectResponse = getDockerClient().inspectContainerCmd(dockerContainer.getId()).exec();
                this.registerContainer(dockerContainer.getId(), new DockerContainer(dockerContainer, containerInspectResponse, image));
            } catch (Exception e) {
                LOGGER.error("An error occurred loading details of container", e);
            }
        }
    }

    @Override
    public Container findContainer(Environment env, ImageConfiguration imageConf, String containerName) throws DenvException {
        com.github.dockerjava.api.model.Container dockerContainer = findContainer(containerName);
        return convertContainer(env, imageConf, dockerContainer);
    }

    private com.github.dockerjava.api.model.Container findContainer(String containerName) throws ContainerizationException {
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

    private Container convertContainer(Environment env, ImageConfiguration imageConf, com.github.dockerjava.api.model.Container dockerContainer) throws DenvException {
        if (dockerContainer != null) {
            try {
                InspectContainerResponse containerDetails = getDockerClient().inspectContainerCmd(dockerContainer.getId()).exec();
                Image image = getImageManager().findImage(env, imageConf);
                return new DockerContainer(dockerContainer, containerDetails, image);
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
                    .exec();
            InspectContainerResponse containerInspectResponse = getDockerClient().inspectContainerCmd(dockerContainer.getId()).exec();
            dockerContainer.setRunning(containerInspectResponse.getState().isRunning());
            return dockerContainer;
        } catch(Exception ex) {
            throw new ContainerizationException(ex);
        }
    }

    @Override
    public void stopContainer(Environment env, Container container) throws ContainerizationException {
        try {
            getDockerClient().killContainerCmd(container.getId()).exec();
            InspectContainerResponse containerInspectResponse = getDockerClient().inspectContainerCmd(container.getId()).exec();
            container.setRunning(containerInspectResponse.getState().isRunning());
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    public void deleteContainer(Environment env, Container container) throws ContainerizationException {
        try {
            getDockerClient().removeContainerCmd(container.getId()).withForce().exec();
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
            String response = getDockerClient().commitCmd(container.getId())
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
    public boolean isContainerListeningOnPort(Container linkedContainer, PortConfiguration port) {
        DockerNodeConfiguration dockerNodeConfiguration = (DockerNodeConfiguration)nodeManager.getDefaultNode();
        return serverListening(dockerNodeConfiguration.getDockerHost(), port.getHostPort());
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
