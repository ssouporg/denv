package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ssoup.denv.server.persistence.mongodb.domain.config.MongoDenvApplicationConfiguration;
import org.ssoup.denv.server.persistence.repository.ApplicationConfigRepository;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
@RepositoryRestResource(collectionResourceRel = "applicationConfigs", path = "applicationConfigs")
public interface MongoDBApplicationConfigRepository extends MongoRepository<MongoDenvApplicationConfiguration, String>,
        ApplicationConfigRepository<MongoDenvApplicationConfiguration> {
}
