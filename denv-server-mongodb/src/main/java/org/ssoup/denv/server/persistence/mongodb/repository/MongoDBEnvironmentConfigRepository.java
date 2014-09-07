package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ssoup.denv.server.persistence.mongodb.domain.config.MongoDenvEnvironmentConfiguration;
import org.ssoup.denv.server.persistence.repository.EnvironmentConfigRepository;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
@RepositoryRestResource(collectionResourceRel = "environmentConfigs", path = "environmentConfigs")
public interface MongoDBEnvironmentConfigRepository extends MongoRepository<MongoDenvEnvironmentConfiguration, String>,
        EnvironmentConfigRepository<MongoDenvEnvironmentConfiguration> {
}
