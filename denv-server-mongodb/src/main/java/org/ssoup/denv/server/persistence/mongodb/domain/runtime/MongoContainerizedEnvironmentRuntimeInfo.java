package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.*;

/**
 * User: ALB
 * Date: 26/02/14 14:13
 */
public class MongoContainerizedEnvironmentRuntimeInfo extends ContainerizedEnvironmentRuntimeInfoImpl {

    public MongoContainerizedEnvironmentRuntimeInfo() {
    }

    public MongoContainerizedEnvironmentRuntimeInfo(DenvContainerizedEnvironment env, ContainerizedEnvironmentConfiguration envConf) {
        super(env, envConf);
    }

    public MongoContainerizedEnvironmentRuntimeInfo(ContainerizedEnvironmentRuntimeInfo envInfo) {
        super(envInfo);
    }
}
