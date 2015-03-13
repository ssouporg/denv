package org.ssoup.denv.server.persistence;

import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.EnvironmentState;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
public interface EnvironmentRepositoryCustom<T extends Environment> {

    T newEnvironmentInstance(Environment env, EnvironmentConfiguration envConf);

    void updateActualState(String envId, EnvironmentState actualState, EnvironmentRuntimeInfo runtimeInfo);

    void updateDesiredState(String envId, EnvironmentDesiredState desiredState);
}
