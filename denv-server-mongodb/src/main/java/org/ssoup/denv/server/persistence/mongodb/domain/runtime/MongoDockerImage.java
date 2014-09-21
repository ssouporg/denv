package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;
import org.ssoup.denv.server.docker.domain.container.DockerImage;

/**
 * User: ALB
 * Date: 09/01/14 15:52
 */
public class MongoDockerImage extends DockerImage {

    public MongoDockerImage() {
    }

    public MongoDockerImage(DockerImage image) {
        super(image.getId(), image.getName(), image.getTag(), image.getConf());
    }

    public MongoDockerImage(String id, String name, String tag, ImageConfiguration imageConf) {
        super(id, name, tag, imageConf);
    }
}
