package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigVersionDesiredState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigVersionState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.EnvironmentState;
import org.ssoup.denv.server.persistence.EnvironmentRepositoryCustom;
import org.ssoup.denv.server.persistence.VersionRepositoryCustom;
import org.ssoup.denv.server.persistence.mongodb.domain.config.MongoEnvironmentConfigurationImpl;
import org.ssoup.denv.server.persistence.mongodb.domain.config.MongoEnvironmentConfigurationVersionImpl;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoContainerizedEnvironment;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public class MongoDBVersionRepositoryImpl implements VersionRepositoryCustom<MongoEnvironmentConfigurationVersionImpl> {

    private MongoOperations mongoOperations;

    @Autowired
    public MongoDBVersionRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void updateActualState(String envConfVersionId, EnvironmentConfigVersionState actualState) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(envConfVersionId));
        query.fields().include("id");

        Update update = new Update();
        update.set("state", actualState);

        mongoOperations.updateFirst(query, update, MongoEnvironmentConfigurationVersionImpl.class);
    }

    @Override
    public void updateDesiredState(String envConfVersionId, EnvironmentConfigVersionDesiredState desiredState) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(envConfVersionId));
        query.fields().include("id");

        Update update = new Update();
        update.set("desiredState", desiredState);

        mongoOperations.updateFirst(query, update, MongoEnvironmentConfigurationVersionImpl.class);
    }
}
