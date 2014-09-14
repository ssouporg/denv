package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.ssoup.denv.server.containerization.domain.runtime.Container;
import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplication;
import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplicationImpl;
import org.ssoup.denv.server.containerization.domain.runtime.Image;
import org.ssoup.denv.server.docker.domain.container.DockerContainer;
import org.ssoup.denv.server.docker.domain.container.DockerImage;

/**
 * User: ALB
 * Date: 26/02/14 14:13
 */
public class MongoDockerApplication extends ContainerizedApplicationImpl {

    public MongoDockerApplication() {
    }

    public MongoDockerApplication(ContainerizedApplication app) {
        super(app.getId(), app.getApplicationConfigurationId());
        this.setStarted(app.isStarted());
        this.setDeployed(app.isDeployed());
        for (Container container : app.getContainers()) {
            if (container.getImage() != null) {
                Image image = new MongoDockerImage((DockerImage)container.getImage());
                this.registerContainer(image.getId(), new MongoDockerContainer((DockerContainer)container));
            }
        }
    }

    public MongoDockerApplication(String id, String applicationConfigurationId) {
        super(id, applicationConfigurationId);
    }
}
