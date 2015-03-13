package org.ssoup.denv.server.service.conf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;

/**
 * User: ALB
 * Date: 11/03/2015 10:29
 */
public interface EnvironmentConfigurationService {
    /*
     * CRUD Operations
     */
    EnvironmentConfiguration saveEnvironmentConfiguration(EnvironmentConfiguration envConf) throws DenvException;
    EnvironmentConfiguration findById(String envConfId) throws DenvException;
    Page<? extends EnvironmentConfiguration> listEnvConfs(Pageable pageable);
    void deleteEnvironmentConfiguration(String envConfId) throws DenvException;
}
