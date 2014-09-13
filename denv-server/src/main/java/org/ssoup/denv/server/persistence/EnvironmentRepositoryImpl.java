package org.ssoup.denv.server.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.exception.DenvRuntimeException;
import org.ssoup.denv.server.service.runtime.application.ApplicationManager;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentManager;

/**
 * User: ALB
 * Date: 07/09/14 09:23
 */
public class EnvironmentRepositoryImpl<T extends Environment> implements EnvironmentRepositoryCustom<T> {

    private ApplicationContext applicationContext;

    private ApplicationConfigRepository applicationConfigRepository;

    private EnvironmentManager environmentManager;

    private ApplicationManager applicationManager;

    @Autowired
    public EnvironmentRepositoryImpl(ApplicationContext applicationContext, ApplicationConfigRepository applicationConfigRepository, EnvironmentManager environmentManager) {
        this.applicationContext = applicationContext;
        this.applicationConfigRepository = applicationConfigRepository;
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
                actualEnv = (T)environmentManager.createEnvironment(env.getId(), env.getApplications(), env.getLabels(), null);
            }
            for (Application app : env.getApplications()) {
                Application actualApp = actualEnv.getApplication(app.getId());
                if (app.isStarted() && !actualApp.isStarted()) {
                    applicationManager.startApplication(actualEnv, actualApp);
                } else if (!app.isStarted() && actualApp.isStarted()) {
                    applicationManager.stopApplication(actualEnv, actualApp);
                }
            }
            return actualEnv;
        } catch (DenvException e) {
            throw new DenvRuntimeException(e);
        }
    }

    @Override
    public void delete(Environment env) {
        try {
            environmentManager.deleteEnvironment(env);
        } catch (DenvException e) {
            throw new DenvRuntimeException(e);
        }
    }
}
