package org.ssoup.denv.server.persistence;

import org.springframework.data.repository.CrudRepository;
import org.ssoup.denv.core.model.runtime.Environment;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
public interface EnvironmentRepository<T extends Environment> extends CrudRepository<T, String>,
            EnvironmentRepositoryCustom<T> {
}
