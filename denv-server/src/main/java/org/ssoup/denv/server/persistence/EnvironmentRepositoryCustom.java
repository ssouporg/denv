package org.ssoup.denv.server.persistence;

import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.EnvironmentState;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
public interface EnvironmentRepositoryCustom<T extends Environment> {

    /**
     * Create/update an environment in both the container engine and the persisted environment repository.
     *
     * @param env The environment to be created/updated
     * @return the created/updated environment
     */
    public T saveEnvironment(T env);

    void updateActualState(String envId, EnvironmentState actualState, EnvironmentRuntimeInfo runtimeInfo);

    void updateDesiredState(String envId, EnvironmentDesiredState desiredState);

    void deleteEnvironment(T env);

    void deletePermanently(T env);
}
