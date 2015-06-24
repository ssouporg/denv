package org.ssoup.denv.core.containerization.model.conf.environment;

import org.springframework.data.annotation.Id;
import org.ssoup.denv.core.containerization.model.runtime.ContainerDesiredState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 11/08/14 15:35
 */
public class ContainerizedEnvironmentConfigurationImpl extends EnvironmentConfigurationImpl implements ContainerizedEnvironmentConfiguration {

    private Map<String, ImageConfigurationImpl> images;
    private String builderEnvConfId;

    public ContainerizedEnvironmentConfigurationImpl() {
    }

    public ContainerizedEnvironmentConfigurationImpl(ContainerizedEnvironmentConfiguration conf) {
        super(conf);
        this.images = (Map<String, ImageConfigurationImpl>)conf.getImages();
        this.builderEnvConfId = conf.getBuilderEnvConfId();
    }

    public static class ImageConfigurationImpl implements ImageConfiguration {
        @Id
        private String id;
        private String name;
        private String source;
        private String description;
        private String command;
        private String readyWhenRespondingOnUrl;
        private String buildCommand;
        private Collection<String> servicesToVersionWhenBuildSucceeds;
        private String targetImage;
        private boolean privileged;
        private Collection<EnvironmentVariableConfigurationImpl> environment;
        private Collection<LinkConfigurationImpl> links;
        private Collection<PortConfigurationImpl> ports;
        private Collection<VolumeConfigurationImpl> volumes;
        private Collection<DenvVariableConfigurationImpl> variables;
        private ContainerDesiredState initialDesiredState;

        public ImageConfigurationImpl() {
        }

        public ImageConfigurationImpl(ImageConfiguration imageConf) {
            this.id = imageConf.getId();
            this.name = imageConf.getName();
            this.source = imageConf.getSource();
            this.description = imageConf.getDescription();
            this.buildCommand = imageConf.getBuildCommand();
            this.command = imageConf.getCommand();
            this.servicesToVersionWhenBuildSucceeds = imageConf.getServicesToVersionWhenBuildSucceeds();
            this.targetImage = imageConf.getTargetImage();
            this.environment = (Collection<EnvironmentVariableConfigurationImpl>) imageConf.getEnvironment();
            this.privileged = imageConf.isPrivileged();
            this.links = (Collection<LinkConfigurationImpl>) imageConf.getLinks();
            this.ports = (Collection<PortConfigurationImpl>) imageConf.getPorts();
            this.volumes = (Collection<VolumeConfigurationImpl>) imageConf.getVolumes();
            this.initialDesiredState = imageConf.getInitialDesiredState();
            this.variables = (Collection<DenvVariableConfigurationImpl>)imageConf.getVariables();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Collection<EnvironmentVariableConfigurationImpl> getEnvironment() {
            return environment;
        }

        public void setEnvironment(Collection<EnvironmentVariableConfigurationImpl> environment) {
            this.environment = environment;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Collection<PortConfigurationImpl> getPorts() {
            return ports;
        }

        public void setPorts(Collection<PortConfigurationImpl> ports) {
            this.ports = ports;
        }

        public Collection<LinkConfigurationImpl> getLinks() {
            return links;
        }

        public void setLinks(Collection<LinkConfigurationImpl> links) {
            this.links = links;
        }

        @Override
        public Collection<VolumeConfigurationImpl> getVolumes() {
            return volumes;
        }

        public void setVolumes(Collection<VolumeConfigurationImpl> volumes) {
            this.volumes = volumes;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public ContainerDesiredState getInitialDesiredState() {
            return initialDesiredState;
        }

        public void setInitialDesiredState(ContainerDesiredState initialDesiredState) {
            this.initialDesiredState = initialDesiredState;
        }

        @Override
        public boolean isPrivileged() {
            return privileged;
        }

        public void setPrivileged(boolean privileged) {
            this.privileged = privileged;
        }

        public Collection<DenvVariableConfigurationImpl> getVariables() {
            return variables;
        }

        public void setVariables(Collection<DenvVariableConfigurationImpl> variables) {
            this.variables = variables;
        }

        @Override
        public String getBuildCommand() {
            return buildCommand;
        }

        public void setBuildCommand(String buildCommand) {
            this.buildCommand = buildCommand;
        }

        public String getTargetImage() {
            return targetImage;
        }

        public void setTargetImage(String targetImage) {
            this.targetImage = targetImage;
        }

        @Override
        public Collection<String> getServicesToVersionWhenBuildSucceeds() {
            return servicesToVersionWhenBuildSucceeds;
        }

        public void setServicesToVersionWhenBuildSucceeds(Collection<String> servicesToVersionWhenBuildSucceeds) {
            this.servicesToVersionWhenBuildSucceeds = servicesToVersionWhenBuildSucceeds;
        }

        @Override
        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        @Override
        public String getReadyWhenRespondingOnUrl() {
            return readyWhenRespondingOnUrl;
        }

        public void setReadyWhenRespondingOnUrl(String readyWhenRespondingOnUrl) {
            this.readyWhenRespondingOnUrl = readyWhenRespondingOnUrl;
        }
    }

    public static class EnvironmentVariableConfigurationImpl implements EnvironmentVariableConfiguration {
        private String variable;
        private String value;

        public String getVariable() {
            return variable;
        }

        public void setVariable(String variable) {
            this.variable = variable;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class LinkConfigurationImpl implements LinkConfiguration {
        private String service;
        private String alias;

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }
    }

    public static class PortConfigurationImpl implements PortConfiguration {
        private int hostPort;
        private int containerPort;

        public int getHostPort() {
            return hostPort;
        }

        public void setHostPort(int hostPort) {
            this.hostPort = hostPort;
        }

        public int getContainerPort() {
            return containerPort;
        }

        public void setContainerPort(int containerPort) {
            this.containerPort = containerPort;
        }
    }

    public static class VolumeConfigurationImpl implements VolumeConfiguration {
        private String hostPath;
        private String containerPath;

        public String getHostPath() {
            return hostPath;
        }

        public void setHostPath(String hostPath) {
            this.hostPath = hostPath;
        }

        public String getContainerPath() {
            return containerPath;
        }

        public void setContainerPath(String containerPath) {
            this.containerPath = containerPath;
        }

        public String getPath() {
            if (hostPath == null) return containerPath;
            if (containerPath == null) return  hostPath;
            return hostPath + ":" + containerPath;
        }
    }

    public static class DenvVariableConfigurationImpl implements DenvVariableConfiguration {
        private String variable;
        private String value;

        public String getVariable() {
            return variable;
        }

        public void setVariable(String variable) {
            this.variable = variable;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @Override
    public Map<String, ImageConfigurationImpl> getImages() {
        if (this.images == null) {
            this.images = new HashMap<String, ImageConfigurationImpl>();
        }
        return this.images;
    }

    @Override
    public ImageConfiguration getImageConfiguration(String imageId) {
        return getImages().get(imageId);
    }

    @Override
    public void addImage(ImageConfiguration image) {
        getImages().put(image.getId(), new ImageConfigurationImpl(image));
    }

    public void setImages(Map<String, ImageConfigurationImpl> images) {
        if (images == null) {
            this.images = null;
            return;
        }
        this.images = new HashMap<String, ImageConfigurationImpl>();
        for (ImageConfiguration image : images.values()) {
            this.addImage(image);
        }
    }

    public String getBuilderEnvConfId() {
        return builderEnvConfId;
    }

    public void setBuilderEnvConfId(String builderEnvConfId) {
        this.builderEnvConfId = builderEnvConfId;
    }
}
