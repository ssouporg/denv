package org.ssoup.denv.server.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.exception.DenvRuntimeException;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentState;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentManager;
import org.ssoup.denv.server.service.runtime.runtime.EnvironmentRuntimeManager;

/**
 * User: ALB
 * Date: 07/09/14 09:23
 */
public abstract class EnvironmentRepositoryImpl<T extends Environment> implements EnvironmentRepositoryCustom<T> {

    private ApplicationContext applicationContext;

    private EnvironmentConfigRepository environmentConfigRepository;

    private EnvironmentManager environmentManager;

    private EnvironmentRuntimeManager environmentRuntimeManager;

    @Autowired
    public EnvironmentRepositoryImpl(ApplicationContext applicationContext, EnvironmentConfigRepository environmentConfigRepository, EnvironmentManager environmentManager, EnvironmentRuntimeManager environmentRuntimeManager) {
        this.applicationContext = applicationContext;
        this.environmentConfigRepository = environmentConfigRepository;
        this.environmentManager = environmentManager;
        this.environmentRuntimeManager = environmentRuntimeManager;
    }

    @Override
    public T saveEnvironment(T env) {
        try {
            T actualEnv = null;
            if (env.getId() != null) {
                // check if the environment already exists
                EnvironmentRepository environmentRepository = applicationContext.getBean(EnvironmentRepository.class);
                actualEnv = (T)environmentRepository.findOne(env.getId());
            }

            // Create/Update environment and add applications
            if (actualEnv == null) {
                // if not existing create a new environment
                actualEnv = (T)environmentManager.createEnvironment(env);
                if (env.getActualState() == null) {
                    ((DenvEnvironment) env).setActualState(EnvironmentState.STARTING);
                }
            } else {
                actualEnv = (T) environmentManager.updateEnvironment(actualEnv, env);
            }

            return actualEnv;
        } catch (DenvException e) {
            throw new DenvRuntimeException(e);
        }
    }

    @Override
    public void deleteEnvironment(T env) {
        ((DenvEnvironment)env).setDesiredState(EnvironmentDesiredState.DELETED);
    }

    public EnvironmentConfigRepository getEnvironmentConfigRepository() {
        return environmentConfigRepository;
    }

    public EnvironmentRuntimeManager getEnvironmentRuntimeManager() {
        return environmentRuntimeManager;
    }
}
