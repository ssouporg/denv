package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoDenvEnvironment;
import org.ssoup.denv.server.persistence.repository.EnvironmentRepository;
import org.ssoup.denv.server.persistence.repository.EnvironmentRepositoryCustom;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public interface MongoDBEnvironmentRepositoryCustom {
    public MongoDenvEnvironment save(MongoDenvEnvironment env);
}
