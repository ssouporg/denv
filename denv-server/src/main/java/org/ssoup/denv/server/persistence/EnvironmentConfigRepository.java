package org.ssoup.denv.server.persistence;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
@NoRepositoryBean
public interface EnvironmentConfigRepository<T extends EnvironmentConfiguration> extends PagingAndSortingRepository<T, String> {

}
