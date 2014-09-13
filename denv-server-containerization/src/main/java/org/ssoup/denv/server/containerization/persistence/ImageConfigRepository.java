package org.ssoup.denv.server.containerization.persistence;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;

/**
 * User: ALB
 * Date: 06/09/14 14:14
 */
@NoRepositoryBean
public interface ImageConfigRepository<T extends ImageConfiguration> extends PagingAndSortingRepository<T, String> {
}
