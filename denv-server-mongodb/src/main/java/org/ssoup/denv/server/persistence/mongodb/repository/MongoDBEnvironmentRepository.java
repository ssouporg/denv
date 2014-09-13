package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoDenvEnvironment;
import org.ssoup.denv.server.persistence.EnvironmentRepository;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
@RepositoryRestResource(collectionResourceRel = "environments", path = "environments")
public interface MongoDBEnvironmentRepository extends MongoRepository<MongoDenvEnvironment, String>,
        EnvironmentRepository<MongoDenvEnvironment>, MongoDBEnvironmentRepositoryCustom {
}
