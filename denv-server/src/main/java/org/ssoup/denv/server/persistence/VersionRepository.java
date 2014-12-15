package org.ssoup.denv.server.persistence;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
@NoRepositoryBean
public interface VersionRepository<T extends EnvironmentConfigurationVersion> extends PagingAndSortingRepository<T, String> {

}
