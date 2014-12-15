package org.ssoup.denv.server.persistence.mongodb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.EnvironmentState;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.persistence.EnvironmentRepositoryCustom;
import org.ssoup.denv.server.persistence.mongodb.domain.runtime.MongoContainerizedEnvironment;
import org.ssoup.denv.server.persistence.EnvironmentRepositoryImpl;
import org.ssoup.denv.server.service.runtime.runtime.EnvironmentRuntimeManager;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentManager;

/**
 * User: ALB
 * Date: 06/09/14 19:42
 */
public class MongoDBEnvironmentRepositoryImpl extends EnvironmentRepositoryImpl<MongoContainerizedEnvironment>
        implements MongoDBEnvironmentRepositoryCustom, EnvironmentRepositoryCustom<MongoContainerizedEnvironment> {

    private MongoOperations mongoOperations;

    @Autowired
    public MongoDBEnvironmentRepositoryImpl(ApplicationContext applicationContext, EnvironmentConfigRepository environmentConfigRepository, EnvironmentManager environmentManager,
                                            EnvironmentRuntimeManager environmentRuntimeManager, MongoOperations mongoOperations) {
        super(applicationContext, environmentConfigRepository, environmentManager, environmentRuntimeManager);
        this.mongoOperations = mongoOperations;
    }

    @Override
    public MongoContainerizedEnvironment save(MongoContainerizedEnvironment env) {
        DenvContainerizedEnvironment createdEnv = super.saveEnvironment(env);
        ContainerizedEnvironmentConfiguration envConf = (ContainerizedEnvironmentConfiguration)getEnvironmentConfigRepository().findOne(env.getEnvironmentConfigurationId());
        MongoContainerizedEnvironment mongoCreatedEnv = new MongoContainerizedEnvironment(createdEnv, envConf);
        mongoOperations.save(mongoCreatedEnv);
        return mongoOperations.findById(mongoCreatedEnv.getId(), MongoContainerizedEnvironment.class);
    }

    @Override
    public MongoContainerizedEnvironment saveOnly(Environment env) {
        ContainerizedEnvironmentConfiguration envConf = (ContainerizedEnvironmentConfiguration)getEnvironmentConfigRepository().findOne(env.getEnvironmentConfigurationId());
        MongoContainerizedEnvironment mongoCreatedEnv = new MongoContainerizedEnvironment((DenvContainerizedEnvironment)env, envConf);
        mongoOperations.save(mongoCreatedEnv);
        return mongoOperations.findById(mongoCreatedEnv.getId(), MongoContainerizedEnvironment.class);
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

    /**
     * Logical deletion
     * @param env
     */
    @Override
    public void delete(MongoContainerizedEnvironment env) {
        super.deleteEnvironment(env);
        updateDesiredState(env.getId(), env.getDesiredState());
    }

    public void deletePermanently(MongoContainerizedEnvironment env) {
        mongoOperations.remove(env);
    }

    @Override
    public void delete(String envId) {
        MongoContainerizedEnvironment env = mongoOperations.findById(envId, MongoContainerizedEnvironment.class);
        if (env != null) {
            delete(env);
        }
    }
}
