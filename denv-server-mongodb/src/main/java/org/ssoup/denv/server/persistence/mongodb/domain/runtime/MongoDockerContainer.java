package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.ssoup.denv.server.containerization.domain.runtime.Image;
import org.ssoup.denv.server.docker.domain.container.DockerContainer;
import org.ssoup.denv.server.docker.domain.container.DockerImage;

/**
 * User: ALB
 * Date: 09/01/14 11:21
 */
public class MongoDockerContainer extends DockerContainer {

    public MongoDockerContainer() {
    }

    public MongoDockerContainer(DockerContainer container) {
        super(container);
        if (container.getImage() != null) {
            this.setImage(new MongoDockerImage((DockerImage) container.getImage()));
        }
    }

    public MongoDockerContainer(String id, Image image, boolean running) {
        super(id, image, running);
    }
}
