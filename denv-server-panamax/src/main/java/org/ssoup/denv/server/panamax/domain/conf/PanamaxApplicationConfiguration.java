package org.ssoup.denv.server.panamax.domain.conf;

import java.util.Collection;

/**
 * User: ALB
 * Date: 09/08/14 17:37
 */
public class PanamaxApplicationConfiguration {

    private String name;
    private String description;
    private String keywords;
    private boolean recommended;
    private String documentation;
    private Collection<String> authors;
    private String type;
    private Collection<Image> images;

    public static class Image {
        private String name;
        private String source;
        private String description;
        private Collection<EnvironmentVariable> environment;
        private Collection<Link> links;
        private Collection<Port> ports;
        private Collection<Volume> volumes;
        private String category;
        private String type;

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

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
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
