package org.ssoup.denv.server.persistence;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
@NoRepositoryBean
public interface ApplicationConfigRepository<T extends ApplicationConfiguration> extends PagingAndSortingRepository<T, String> {
    // List<ApplicationConfiguration> findByName(@Param("name") String name);
}
