package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoDenvApplication;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoDenvEnvironment;
import org.ssoup.denv.server.persistence.repository.EnvironmentConfigRepository;
import org.ssoup.denv.server.persistence.repository.EnvironmentRepositoryImpl;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentManager;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public class MongoDBEnvironmentRepositoryImpl extends EnvironmentRepositoryImpl<MongoDenvEnvironment>
        implements MongoDBEnvironmentRepositoryCustom {

    private MongoOperations mongoOperations;

    @Autowired
    public MongoDBEnvironmentRepositoryImpl(ApplicationContext applicationContext, EnvironmentConfigRepository environmentConfigRepository, EnvironmentManager environmentManager, MongoOperations mongoOperations) {
        super(applicationContext, environmentConfigRepository, environmentManager);
        this.mongoOperations = mongoOperations;
    }

    @Override
    public MongoDenvEnvironment save(MongoDenvEnvironment env) {
        Environment createdEnv = super.saveEnvironment(env);
        env.setId(createdEnv.getId());
        env.setApplication(new MongoDenvApplication(createdEnv.getApplication()));
        mongoOperations.save(env);
        return mongoOperations.findById(env.getId(), MongoDenvEnvironment.class);
    }
}
