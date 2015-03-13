package org.ssoup.denv.server.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ssoup.denv.core.model.runtime.Environment;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
@NoRepositoryBean
public interface EnvironmentRepository<T extends Environment> extends PagingAndSortingRepository<T, String>,
            EnvironmentRepositoryCustom<T> {
}
