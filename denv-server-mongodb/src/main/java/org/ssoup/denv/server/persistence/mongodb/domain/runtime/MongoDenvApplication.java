package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplicationImpl;

/**
 * User: ALB
 * Date: 26/02/14 14:13
 */
public class MongoDenvApplication extends ContainerizedApplicationImpl {

    public MongoDenvApplication(String id, String applicationConfigurationId) {
        super(id, applicationConfigurationId);
    }
}
