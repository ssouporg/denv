package org.ssoup.denv.server.service.runtime.environment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.core.exception.DenvException;

import java.util.Collection;
import java.util.Map;

/**
 * User: ALB
 * Date: 26/02/14 15:11
 */
public interface EnvironmentService {

    /*
     * CRUD Operations
     */
    Environment createEnvironment(Environment env) throws DenvException;
    Environment findById(String envId) throws DenvException;
    Page<? extends Environment> listEnvs(Pageable pageable);
    void deleteEnvironment(String envId) throws DenvException;

    /*
     * Other operations
     */
    Environment updateEnvironment(Environment env) throws DenvException;
    void registerExistingEnvironments() throws DenvException;
    void addEventHandler(EnvsEventHandler eventHandler);
    Environment createBuildEnvironment(EnvironmentConfiguration builderEnvConf, String targetEnvConfId, String targetVersion) throws DenvException;
}
