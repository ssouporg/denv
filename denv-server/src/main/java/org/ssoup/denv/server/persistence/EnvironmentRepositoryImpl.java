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

    private EnvironmentManager environmentManager;

    private ApplicationManager applicationManager;

    @Autowired
    public EnvironmentRepositoryImpl(ApplicationContext applicationContext, EnvironmentManager environmentManager, ApplicationManager applicationManager) {
        this.applicationContext = applicationContext;
        this.environmentManager = environmentManager;
        this.applicationManager = applicationManager;
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
                actualEnv = (T)environmentManager.createEnvironment(env.getId(), env.getName(), env.getApplications(), env.getLabels(), null);
            } else {
                actualEnv = (T)environmentManager.addApplications(actualEnv, env.getApplications());
            }

            // Deploy/Undeploy and Start/Stop applications if needed
            for (Application app : env.getApplications()) {
                Application actualApp = actualEnv.getApplication(app.getId());

                // Deploy/Undeploy
                if (app.isDeployed() && !actualApp.isDeployed()) {
                    applicationManager.deployApplication(actualEnv, actualApp);
                } else if (!app.isDeployed() && actualApp.isDeployed()) {
                    // TODO: applicationManager.undeployApplication(actualEnv, actualApp);
                }

                // Start/Stop
                if (app.isStarted() && !actualApp.isStarted()) {
                    if (!actualApp.isDeployed()) {
                        throw new DenvException("Cannot start an application which is not deployed [" + actualApp.getId() + "]");
                    }
                    applicationManager.startApplication(actualEnv, actualApp);
                } else if (!app.isStarted() && actualApp.isStarted()) {
                    if (actualApp.isDeployed()) {
                        applicationManager.stopApplication(actualEnv, actualApp);
                    }
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
