package org.ssoup.denv.server.service.runtime.environment;

import org.ssoup.denv.core.model.conf.node.NodeConfiguration;
import org.ssoup.denv.core.model.runtime.Application;
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

    Environment createEnvironment(Collection<Application> apps, Collection<String> labels) throws DenvException;

    Environment createEnvironment(Collection<Application> apps, Collection<String> labels, String nodeConfId) throws DenvException;

    Environment createEnvironment(String envId, Collection<Application> apps, Collection<String> labels, String nodeConfId) throws DenvException;

    Environment addApplications(Environment env, Collection<Application> apps) throws DenvException;

    public void deleteEnvironment(Environment env) throws DenvException;

    void addEventHandler(EnvsEventHandler eventHandler);

    void applyChanges(Environment asIsEnv, Environment toBeEnv);
}
