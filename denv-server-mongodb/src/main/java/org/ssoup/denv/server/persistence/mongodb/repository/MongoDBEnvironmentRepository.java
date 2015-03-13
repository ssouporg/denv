package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.ssoup.denv.server.persistence.EnvironmentRepositoryCustom;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoContainerizedEnvironment;
import org.ssoup.denv.server.persistence.EnvironmentRepository;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public interface MongoDBEnvironmentRepository extends MongoRepository<MongoContainerizedEnvironment, String>,
        EnvironmentRepository<MongoContainerizedEnvironment>, EnvironmentRepositoryCustom<MongoContainerizedEnvironment> {
}
