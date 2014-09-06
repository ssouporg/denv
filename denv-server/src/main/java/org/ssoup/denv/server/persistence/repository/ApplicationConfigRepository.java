package org.ssoup.denv.server.persistence.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.ssoup.denv.common.model.application.ApplicationConfiguration;

import java.util.List;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
public interface ApplicationConfigRepository extends PagingAndSortingRepository<ApplicationConfiguration, String> {
    // List<ApplicationConfiguration> findByName(@Param("name") String name);
}
