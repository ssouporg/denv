package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ssoup.denv.core.api.DenvApiEndpoints;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.persistence.mongodb.domain.config.MongoEnvironmentConfigurationImpl;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
@RepositoryRestResource(collectionResourceRel = "environmentConfigs", path = DenvApiEndpoints.ENV_CONFIGS)
public interface MongoDBEnvironmentConfigRepository extends MongoRepository<MongoEnvironmentConfigurationImpl, String>,
        EnvironmentConfigRepository<MongoEnvironmentConfigurationImpl> {
}
