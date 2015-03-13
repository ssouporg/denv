package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.ssoup.denv.server.persistence.VersionRepository;
import org.ssoup.denv.server.persistence.mongodb.domain.config.MongoEnvironmentConfigurationVersionImpl;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public interface MongoDBVersionRepository extends MongoRepository<MongoEnvironmentConfigurationVersionImpl, String>,
        VersionRepository<MongoEnvironmentConfigurationVersionImpl> {
}
