package org.ssoup.denv.server.persistence.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
@NoRepositoryBean
public interface ApplicationConfigRepository<T extends ApplicationConfiguration> extends PagingAndSortingRepository<T, String> {
    // List<ApplicationConfiguration> findByName(@Param("name") String name);
}
