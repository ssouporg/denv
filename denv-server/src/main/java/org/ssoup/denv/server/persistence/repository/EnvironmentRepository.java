package org.ssoup.denv.server.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.ssoup.denv.server.domain.runtime.environment.DenvEnvironment;
import org.ssoup.denv.server.domain.runtime.environment.Environment;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
public interface EnvironmentRepository<T extends Environment> extends CrudRepository<T, String>,
            EnvironmentRepositoryCustom<T> {
}
