package org.ssoup.denv.server.service.runtime.environment;

import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;
import org.ssoup.denv.common.model.config.node.NodeConfiguration;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.server.exception.DenvException;

/**
 * User: ALB
 * Date: 26/02/14 15:11
 */
public interface EnvironmentManager {

    void registerExistingEnvironments() throws DenvException;

    Environment createEnvironment(EnvironmentConfiguration envConf) throws DenvException;

    Environment createEnvironment(EnvironmentConfiguration envConf, NodeConfiguration node) throws DenvException;

    void startEnvironment(Environment env) throws DenvException;

    void stopEnvironment(Environment env) throws DenvException;

    Environment registerEnvironment(String envId, EnvironmentConfiguration environmentConfiguration, NodeConfiguration node) throws DenvException;

    public void deleteEnvironment(Environment env) throws DenvException;

    void addEventHandler(EnvsEventHandler eventHandler);
}
