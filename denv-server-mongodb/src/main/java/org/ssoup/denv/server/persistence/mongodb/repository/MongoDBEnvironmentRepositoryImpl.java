package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoDockerEnvironment;
import org.ssoup.denv.server.persistence.EnvironmentRepositoryImpl;
import org.ssoup.denv.server.service.runtime.application.ApplicationManager;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentManager;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public class MongoDBEnvironmentRepositoryImpl extends EnvironmentRepositoryImpl<MongoDockerEnvironment>
        implements MongoDBEnvironmentRepositoryCustom {

    private MongoOperations mongoOperations;

    @Autowired
    public MongoDBEnvironmentRepositoryImpl(ApplicationContext applicationContext, EnvironmentManager environmentManager, ApplicationManager applicationManager, MongoOperations mongoOperations) {
        super(applicationContext, environmentManager, applicationManager);
        this.mongoOperations = mongoOperations;
    }

    @Override
    public MongoDockerEnvironment save(MongoDockerEnvironment env) {
        Environment createdEnv = super.saveEnvironment(env);
        MongoDockerEnvironment mongoCreatedEnv = new MongoDockerEnvironment(createdEnv);
        mongoOperations.save(mongoCreatedEnv);
        return mongoOperations.findById(mongoCreatedEnv.getId(), MongoDockerEnvironment.class);
    }

    @Override
    public void delete(MongoDockerEnvironment env) {
        super.delete(env);
        mongoOperations.remove(env);
    }

    @Override
    public void delete(String envId) {
        MongoDockerEnvironment env = mongoOperations.findById(envId, MongoDockerEnvironment.class);
        if (env != null) {
            delete(env);
        }
    }
}
