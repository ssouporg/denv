package org.ssoup.denv.server.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.exception.DenvRuntimeException;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentManager;

/**
 * User: ALB
 * Date: 07/09/14 09:23
 */
public class EnvironmentRepositoryImpl<T extends Environment> implements EnvironmentRepositoryCustom<T> {

    private ApplicationContext applicationContext;

    private EnvironmentConfigRepository environmentConfigRepository;

    private EnvironmentManager environmentManager;

    @Autowired
    public EnvironmentRepositoryImpl(ApplicationContext applicationContext, EnvironmentConfigRepository environmentConfigRepository, EnvironmentManager environmentManager) {
        this.applicationContext = applicationContext;
        this.environmentConfigRepository = environmentConfigRepository;
        this.environmentManager = environmentManager;
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
            if (actualEnv == null) {
                // if not existing create a new environment
                EnvironmentConfiguration envConf = env.getEnvironmentConfiguration();
                if (envConf == null) {
                    envConf = (EnvironmentConfiguration)environmentConfigRepository.findOne(env.getEnvironmentConfigurationId());
                }
                actualEnv = (T)environmentManager.createEnvironment(envConf);
            }
            if (env.isStarted() && !actualEnv.isStarted()) {
                environmentManager.startEnvironment(actualEnv);
            } else if (!env.isStarted() && actualEnv.isStarted()) {
                environmentManager.stopEnvironment(actualEnv);
            }
            return actualEnv;
        } catch (DenvException e) {
            throw new DenvRuntimeException(e);
        }
    }
}
