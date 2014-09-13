package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.persistence.ApplicationConfigRepository;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoDenvEnvironment;
import org.ssoup.denv.server.persistence.EnvironmentRepositoryImpl;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentManager;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public class MongoDBEnvironmentRepositoryImpl extends EnvironmentRepositoryImpl<MongoDenvEnvironment>
        implements MongoDBEnvironmentRepositoryCustom {

    private MongoOperations mongoOperations;

    @Autowired
    public MongoDBEnvironmentRepositoryImpl(ApplicationContext applicationContext, ApplicationConfigRepository applicationConfigRepository, EnvironmentManager environmentManager, MongoOperations mongoOperations) {
        super(applicationContext, applicationConfigRepository, environmentManager);
        this.mongoOperations = mongoOperations;
    }

    @Override
    public MongoDenvEnvironment save(MongoDenvEnvironment env) {
        Environment createdEnv = super.saveEnvironment(env);
        MongoDenvEnvironment mongoCreatedEnv = new MongoDenvEnvironment(createdEnv);
        mongoOperations.save(mongoCreatedEnv);
        return mongoOperations.findById(mongoCreatedEnv.getId(), MongoDenvEnvironment.class);
    }

    @Override
    public void delete(MongoDenvEnvironment env) {
        super.delete(env);
        mongoOperations.remove(env);
    }

    @Override
    public void delete(String envId) {
        MongoDenvEnvironment env = mongoOperations.findById(envId, MongoDenvEnvironment.class);
        if (env != null) {
            delete(env);
        }
    }
}
