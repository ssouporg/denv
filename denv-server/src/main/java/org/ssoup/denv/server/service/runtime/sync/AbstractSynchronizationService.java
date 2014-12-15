package org.ssoup.denv.server.service.runtime.sync;

import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigVersionState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentState;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.persistence.EnvironmentRepository;
import org.ssoup.denv.server.persistence.VersionRepository;

/**
 * User: ALB
 * Date: 15/11/2014 20:10
 */
public abstract class AbstractSynchronizationService<T extends Environment, V extends EnvironmentConfigurationVersion>
        implements SynchronizationService {

    private EnvironmentRepository<T> environmentRepository;
    private EnvironmentConfigRepository<? extends EnvironmentConfiguration> environmentConfigRepository;
    private VersionRepository<V> versionRepository;

    protected AbstractSynchronizationService(EnvironmentRepository environmentRepository, EnvironmentConfigRepository<? extends EnvironmentConfiguration> environmentConfigRepository, VersionRepository<V> versionRepository) {
        this.environmentRepository = environmentRepository;
        this.environmentConfigRepository = environmentConfigRepository;
        this.versionRepository = versionRepository;
    }

    @Override
    public void updateActualState() throws DenvException {
        for (T env : environmentRepository.findAll()) {
            if (env.getActualState() != EnvironmentState.DELETED) {
                updateActualState(env);
                environmentRepository.updateActualState(env.getId(), env.getActualState(), env.getRuntimeInfo());
            }
        }
    }

    protected void updateActualState(Environment env) throws DenvException {
        EnvironmentConfiguration envConf = environmentConfigRepository.findOne(env.getEnvironmentConfigurationId());
        updateActualState(env, envConf);
    }

    protected abstract void updateActualState(Environment env, EnvironmentConfiguration envConf) throws DenvException;

    @Override
    public void moveTowardsDesiredState() throws DenvException {
        for (T env : environmentRepository.findAll()) {
            if (env.getActualState() == EnvironmentState.DELETED) {
                environmentRepository.deletePermanently(env);
            } else {
                moveTowardsDesiredState(env);
                environmentRepository.updateActualState(env.getId(), env.getActualState(), env.getRuntimeInfo());
            }
        }

        for (V envConfVersion : versionRepository.findAll()) {
            if (envConfVersion.getActualState() == EnvironmentConfigVersionState.DELETED) {
                versionRepository.delete(envConfVersion);
            } else {
                moveTowardsDesiredState(envConfVersion);
                versionRepository.save(envConfVersion);
            }
        }
    }

    protected void moveTowardsDesiredState(Environment env) throws DenvException {
        if (env.getActualState() != EnvironmentState.INCONSISTENT) {
            EnvironmentConfiguration envConf = environmentConfigRepository.findOne(env.getEnvironmentConfigurationId());
            if (envConf != null) {
                moveTowardsDesiredState(env, envConf);
            }
        }
    }

    protected void moveTowardsDesiredState(EnvironmentConfigurationVersion envConfVersion) throws DenvException {
        EnvironmentConfiguration envConf = environmentConfigRepository.findOne(envConfVersion.getEnvConfId());
        if (envConf != null) {
            moveTowardsDesiredState(envConf, envConfVersion);
        }
    }

    protected abstract void moveTowardsDesiredState(Environment env,
                                                    EnvironmentConfiguration envConf) throws DenvException;

    protected abstract void moveTowardsDesiredState(EnvironmentConfiguration envConf,
                                                    EnvironmentConfigurationVersion envConfVersion) throws DenvException;
}
