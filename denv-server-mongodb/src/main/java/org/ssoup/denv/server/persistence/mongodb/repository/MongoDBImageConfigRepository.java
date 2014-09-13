package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ssoup.denv.server.containerization.persistence.ImageConfigRepository;
import org.ssoup.denv.server.persistence.mongodb.domain.config.MongoDenvImageConfiguration;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
@RepositoryRestResource(collectionResourceRel = "imageConfigs", path = "imageConfigs")
public interface MongoDBImageConfigRepository extends MongoRepository<MongoDenvImageConfiguration, String>,
        ImageConfigRepository<MongoDenvImageConfiguration> {
}
