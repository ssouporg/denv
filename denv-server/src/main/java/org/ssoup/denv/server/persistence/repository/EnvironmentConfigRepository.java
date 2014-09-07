package org.ssoup.denv.server.persistence.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
@NoRepositoryBean
public interface EnvironmentConfigRepository<T extends EnvironmentConfiguration> extends PagingAndSortingRepository<T, String> {
}
