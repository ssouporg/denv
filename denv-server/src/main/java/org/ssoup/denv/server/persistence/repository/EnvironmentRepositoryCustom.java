package org.ssoup.denv.server.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.ssoup.denv.server.domain.runtime.environment.DenvEnvironment;
import org.ssoup.denv.server.domain.runtime.environment.Environment;

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
}
