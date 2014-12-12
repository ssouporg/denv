package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.ContainerizedEnvironmentRuntimeInfo;
import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

/**
 * User: ALB
 * Date: 25/08/14 08:57
 */
@Document(collection="environment")
public class MongoContainerizedEnvironment extends DenvContainerizedEnvironment {

    public MongoContainerizedEnvironment() {
    }

    public MongoContainerizedEnvironment(DenvContainerizedEnvironment env, ContainerizedEnvironmentConfiguration envConf) {
        super(env);
        this.setRuntimeInfo(new MongoContainerizedEnvironmentRuntimeInfo(env, envConf));
    }
}
