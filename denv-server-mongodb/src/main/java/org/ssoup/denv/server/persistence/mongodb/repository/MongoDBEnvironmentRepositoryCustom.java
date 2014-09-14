package org.ssoup.denv.server.persistence.mongodb.repository;

import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoDockerEnvironment;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public interface MongoDBEnvironmentRepositoryCustom {
    public MongoDockerEnvironment save(MongoDockerEnvironment env);
    void delete(MongoDockerEnvironment env);
    void delete(String envId);
}
