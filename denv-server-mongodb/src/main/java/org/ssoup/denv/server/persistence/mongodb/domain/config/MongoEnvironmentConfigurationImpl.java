package org.ssoup.denv.server.persistence.mongodb.domain.config;

import org.springframework.data.mongodb.core.mapping.Document;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfigurationImpl;

/**
 * User: ALB
 * Date: 11/08/14 15:35
 */
@Document(collection="configuration")
public class MongoEnvironmentConfigurationImpl extends ContainerizedEnvironmentConfigurationImpl {

}
