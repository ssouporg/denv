package org.ssoup.denv.server.service.runtime.environment;

import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.core.exception.DenvException;

import java.util.Collection;

/**
 * User: ALB
 * Date: 26/02/14 15:11
 */
public interface EnvironmentManager {

    void registerExistingEnvironments() throws DenvException;

    Environment createEnvironment(Environment env) throws DenvException;

    Environment updateEnvironment(Environment actualEnv, Environment env) throws DenvException;

    void addEventHandler(EnvsEventHandler eventHandler);

    Environment createBuildEnvironment(EnvironmentConfiguration builderEnvConf, String targetEnvConfId, String targetVersion);
}
