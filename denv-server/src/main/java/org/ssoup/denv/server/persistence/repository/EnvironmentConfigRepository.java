package org.ssoup.denv.server.persistence.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.environment.EnvironmentConfiguration;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
public interface EnvironmentConfigRepository extends PagingAndSortingRepository<EnvironmentConfiguration, String> {
}
