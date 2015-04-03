package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.EnvironmentState;
import org.ssoup.denv.server.persistence.EnvironmentRepositoryCustom;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoContainerizedEnvironment;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public class MongoDBEnvironmentRepositoryImpl implements EnvironmentRepositoryCustom<MongoContainerizedEnvironment> {

    private MongoOperations mongoOperations;

    @Autowired
    public MongoDBEnvironmentRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public MongoContainerizedEnvironment newEnvironmentInstance(Environment env, EnvironmentConfiguration envConf) {
        return new MongoContainerizedEnvironment((DenvContainerizedEnvironment)env, (ContainerizedEnvironmentConfiguration)envConf);
    }

    @Override
    public void updateActualState(String envId, EnvironmentState actualState, EnvironmentRuntimeInfo runtimeInfo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(envId));
        query.fields().include("id");

        Update update = new Update();
        update.set("state", actualState);
        if (runtimeInfo != null) {
            update.set("runtimeInfo", runtimeInfo);
        }

        mongoOperations.updateFirst(query, update, MongoContainerizedEnvironment.class);
    }

    @Override
    public void updateDesiredState(String envId, EnvironmentDesiredState desiredState) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(envId));
        query.fields().include("id");

        Update update = new Update();
        update.set("desiredState", desiredState);

        mongoOperations.updateFirst(query, update, MongoContainerizedEnvironment.class);
    }
}
