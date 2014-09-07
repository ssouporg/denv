package org.ssoup.denv.server.persistence.mongodb.domain.config;

import org.springframework.data.mongodb.core.mapping.Document;
import org.ssoup.denv.common.model.config.application.*;

import java.util.Collection;

/**
 * User: ALB
 * Date: 11/08/14 15:35
 */
@Document(collection="applicationConfiguration")
public class MongoDenvApplicationConfiguration implements ApplicationConfiguration {

    private String name;
    private String description;
    private String documentation;
    private Collection<ImageConfigurationImpl> images;

    public static class ImageConfigurationImpl implements ImageConfiguration {
        private String name;
        private String source;
        private String description;
        private Collection<EnvironmentVariableConfigurationImpl> environment;
        private Collection<LinkConfigurationImpl> links;
        private Collection<PortConfigurationImpl> ports;
        private Collection<VolumeConfigurationImpl> volumes;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

    public Collection<ImageConfigurationImpl> getImages() {
        return this.images;
    }

    public ImageConfiguration getImageConfigurationByName(String imageName) {
        for (ImageConfiguration imageConf : images) {
            if (imageConf.getName().equals(imageName)) {
                return imageConf;
            }
        }
        return null;
    }

    public void setImages(Collection<ImageConfigurationImpl> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
