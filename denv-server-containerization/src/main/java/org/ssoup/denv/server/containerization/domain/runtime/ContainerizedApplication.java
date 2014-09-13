package org.ssoup.denv.server.containerization.domain.runtime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.ssoup.denv.core.model.runtime.Application;

import java.util.Collection;

/**
 * User: ALB
 * Date: 12/09/14 11:23
 */
@JsonDeserialize(as = ContainerizedApplicationImpl.class)
public interface ContainerizedApplication extends Application {
    void registerContainer(String imageId, Container container);
    Container getContainer(String imageId);
    Collection<Container> getContainers();
}
