package org.ssoup.denv.server.persistence;

import org.ssoup.denv.core.model.runtime.Environment;

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

    void delete(Environment env);
}
