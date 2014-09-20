package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplication;

/**
 * User: ALB
 * Date: 25/08/14 08:57
 */
@Document(collection="environment")
public class MongoDockerEnvironment extends DenvEnvironment {

    public MongoDockerEnvironment() {
    }

    public MongoDockerEnvironment(Environment env) {
        super(env.getId(), env.getName(), null, env.getNode());
        for (Application app : env.getApplications()) {
            this.addApplication(new MongoDockerApplication((ContainerizedApplication)app));
        }
    }
}
