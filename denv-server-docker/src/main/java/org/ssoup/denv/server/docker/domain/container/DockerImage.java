package org.ssoup.denv.server.docker.domain.container;

import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;
import org.ssoup.denv.server.containerization.domain.runtime.AbstractImage;

/**
 * User: ALB
 * Date: 09/01/14 15:52
 */
public class DockerImage extends AbstractImage {

    private com.github.dockerjava.api.model.Image dockerImage;

    public DockerImage() {
    }

    public DockerImage(ImageConfiguration imageConf, com.github.dockerjava.api.model.Image dockerImage) {
        this.dockerImage = dockerImage;
        this.setId(this.dockerImage.getId());
        String[] toks = this.dockerImage.getRepoTags()[0].split(":");
        this.setName(toks[0]);
        this.setTag(toks[1]);
    }

    public com.github.dockerjava.api.model.Image getDockerImage() {
        return dockerImage;
    }
}
