package org.ssoup.denv.server.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigVersionDesiredState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigVersionState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;

import java.util.List;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
public interface VersionRepositoryCustom<T extends EnvironmentConfigurationVersion> {

    Page<? extends EnvironmentConfigurationVersion> listByEnvConf(String envConfId, Pageable pageable);

    List<? extends EnvironmentConfigurationVersion> listAllByEnvConf(String envConfId);

    void updateActualState(String envId, EnvironmentConfigVersionState actualState);

    void updateDesiredState(String envId, EnvironmentConfigVersionDesiredState desiredState);
}
