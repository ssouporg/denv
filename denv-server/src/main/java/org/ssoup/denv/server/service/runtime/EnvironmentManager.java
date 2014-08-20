package org.ssoup.denv.server.service.runtime;

import org.ssoup.denv.server.domain.conf.node.NodeConfiguration;
import org.ssoup.denv.server.domain.environment.Environment;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.server.exception.DenvException;

import java.util.Collection;

/**
 * User: ALB
 * Date: 26/02/14 15:11
 */
public interface EnvironmentManager {

    void registerExistingEnvironments() throws DenvException;

    Environment create(String version, NodeConfiguration node) throws DenvException;

    void start(Environment env) throws DenvException;

    void stop(Environment env) throws DenvException;

    void delete(Environment env) throws DenvException;

    Environment register(String envId, String version, NodeConfiguration node) throws DenvException;

    Collection<Environment> listEnvironments() throws DenvException;

    Environment findEnvironment(String envId) throws DenvException;

    void addEventHandler(EnvsEventHandler eventHandler);
}
