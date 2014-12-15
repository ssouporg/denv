package org.ssoup.denv.server.persistence.mongodb.repository;

import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.model.runtime.EnvironmentState;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoContainerizedEnvironment;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public interface MongoDBEnvironmentRepositoryCustom {
    public MongoContainerizedEnvironment save(MongoContainerizedEnvironment env);
    void delete(MongoContainerizedEnvironment env);
    void delete(String envId);
}
