package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.ssoup.denv.core.containerization.model.runtime.*;

/**
 * User: ALB
 * Date: 09/01/14 11:21
 */
public class MongoContainerRuntimeInfo extends ContainerRuntimeInfoImpl {

    public MongoContainerRuntimeInfo() {
    }

    public MongoContainerRuntimeInfo(ContainerRuntimeInfo containerInfo) {
        super();
    }

    public MongoContainerRuntimeInfo(String id, Image image, ContainerDesiredState desiredState, ContainerState actualState) {
        super(id, image, desiredState, actualState);
    }
}
