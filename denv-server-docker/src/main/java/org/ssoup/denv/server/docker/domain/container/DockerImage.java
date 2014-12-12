package org.ssoup.denv.server.docker.domain.container;

import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.AbstractImage;

/**
 * User: ALB
 * Date: 09/01/14 15:52
 */
public class DockerImage extends AbstractImage {

    private com.github.dockerjava.api.model.Image dockerImage;

    public DockerImage() {
    }

    public DockerImage(String id, String name, String tag, ImageConfiguration imageConf){
        super(id, name, tag, imageConf);
    }

    public DockerImage(ImageConfiguration imageConf, com.github.dockerjava.api.model.Image dockerImage) {
        this.setConf(imageConf);
        this.dockerImage = dockerImage;
        this.setId(this.dockerImage.getId());
        this.setName(imageConf.getName());
        String[] toks = this.dockerImage.getRepoTags()[0].split(":");
        this.setName(toks[0]);
        this.setTag(toks[1]);
    }

    public com.github.dockerjava.api.model.Image getDockerImage() {
        return dockerImage;
    }
}
