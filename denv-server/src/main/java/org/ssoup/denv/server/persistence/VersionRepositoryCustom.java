package org.ssoup.denv.server.persistence;

import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigVersionDesiredState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigVersionState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
public interface VersionRepositoryCustom<T extends EnvironmentConfigurationVersion> {

    void updateActualState(String envId, EnvironmentConfigVersionState actualState);

    void updateDesiredState(String envId, EnvironmentConfigVersionDesiredState desiredState);
}
