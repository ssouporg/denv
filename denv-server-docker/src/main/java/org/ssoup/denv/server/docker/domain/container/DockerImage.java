package org.ssoup.denv.server.docker.domain.container;

import org.ssoup.denv.server.domain.container.Image;

/**
 * User: ALB
 * Date: 09/01/14 15:52
 */
public class DockerImage implements Image {

    private String id;
    private String name;
    private String tag;
    private com.github.dockerjava.client.model.Image dockerImage;

    public DockerImage(com.github.dockerjava.client.model.Image dockerImage) {
        this.dockerImage = dockerImage;
        this.id = this.dockerImage.getId();
        String[] toks = this.dockerImage.getRepoTags()[0].split(":");
        this.name = toks[0];
        this.tag = toks[1];
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public com.github.dockerjava.client.model.Image getDockerImage() {
        return dockerImage;
    }
}
