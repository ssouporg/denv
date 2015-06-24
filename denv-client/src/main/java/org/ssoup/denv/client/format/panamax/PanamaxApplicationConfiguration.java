package org.ssoup.denv.client.format.panamax;

import java.util.Collection;

/**
 * User: ALB
 * Date: 09/08/14 17:37
 */
public class PanamaxApplicationConfiguration {

    private String name;
    private String description;
    private String keywords;
    private String recommended; // should be boolean but in some templates is empty string causing yaml parser to fail
    private String documentation;
    private Collection<String> authors;
    private String type;
    private Collection<Image> images;
    private String builderEnvConfId;

    public String getBuilderEnvConfId() {
        return builderEnvConfId;
    }

    public void setBuilderEnvConfId(String builderEnvConfId) {
        this.builderEnvConfId = builderEnvConfId;
    }

    public static class Image {
        private String name;
        private String source;
        private String description;
        private String commmand;
        private String readyWhenRespondingOnUrl;
        private String buildCommand;
        private Collection<String> servicesToVersionWhenBuildSucceeds;
        private String targetImage;
        private Collection<EnvironmentVariable> environment;
        private Collection<Link> links;
        private Collection<String> expose;
        private Collection<Port> ports;
        private Collection<Volume> volumes;
        private String category;
        private String type;
        private Collection<DenvVariable> variables;

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

        public Collection<EnvironmentVariable> getEnvironment() {
            return environment;
        }

        public void setEnvironment(Collection<EnvironmentVariable> environment) {
            this.environment = environment;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Collection<Port> getPorts() {
            return ports;
        }

        public void setPorts(Collection<Port> ports) {
            this.ports = ports;
        }

        public Collection<Link> getLinks() {
            return links;
        }

        public void setLinks(Collection<Link> links) {
            this.links = links;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Collection<Volume> getVolumes() {
            return volumes;
        }

        public void setVolumes(Collection<Volume> volumes) {
            this.volumes = volumes;
        }

        public Collection<String> getExpose() {
            return expose;
        }

        public void setExpose(Collection<String> expose) {
            this.expose = expose;
        }

        public Collection<DenvVariable> getVariables() {
            return variables;
        }

        public void setVariables(Collection<DenvVariable> variables) {
            this.variables = variables;
        }

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

        public Collection<String> getServicesToVersionWhenBuildSucceeds() {
            return servicesToVersionWhenBuildSucceeds;
        }

        public void setServicesToVersionWhenBuildSucceeds(Collection<String> servicesToVersionWhenBuildSucceeds) {
            this.servicesToVersionWhenBuildSucceeds = servicesToVersionWhenBuildSucceeds;
        }

        public String getCommmand() {
            return commmand;
        }

        public void setCommmand(String commmand) {
            this.commmand = commmand;
        }

        public String getReadyWhenRespondingOnUrl() {
            return readyWhenRespondingOnUrl;
        }

        public void setReadyWhenRespondingOnUrl(String readyWhenRespondingOnUrl) {
            this.readyWhenRespondingOnUrl = readyWhenRespondingOnUrl;
        }
    }

    public static class EnvironmentVariable {
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

    public static class Link {
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

    public static class Port {
        private int host_port;
        private int container_port;

        public int getHost_port() {
            return host_port;
        }

        public void setHost_port(int host_port) {
            this.host_port = host_port;
        }

        public int getContainer_port() {
            return container_port;
        }

        public void setContainer_port(int container_port) {
            this.container_port = container_port;
        }
    }

    public static class Volume {
        private String host_path;
        private String container_path;

        public String getContainer_path() {
            return container_path;
        }

        public void setContainer_path(String container_path) {
            this.container_path = container_path;
        }

        public String getHost_path() {
            return host_path;
        }

        public void setHost_path(String host_path) {
            this.host_path = host_path;
        }
    }

    public static class DenvVariable {
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

    public Collection<Image> getImages() {
        return this.images;
    }

    public void setImages(Collection<Image> images) {
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public Collection<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<String> authors) {
        this.authors = authors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
