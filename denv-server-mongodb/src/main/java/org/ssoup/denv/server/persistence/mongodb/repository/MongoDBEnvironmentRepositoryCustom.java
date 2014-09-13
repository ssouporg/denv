package org.ssoup.denv.server.persistence.mongodb.repository;

import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoDenvEnvironment;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public interface MongoDBEnvironmentRepositoryCustom {
    public MongoDenvEnvironment save(MongoDenvEnvironment env);
    void delete(MongoDenvEnvironment env);
    void delete(String envId);
}
