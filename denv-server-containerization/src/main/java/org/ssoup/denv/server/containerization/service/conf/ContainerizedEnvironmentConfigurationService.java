package org.ssoup.denv.server.containerization.service.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.service.conf.AbstractEnvironmentConfigurationService;

/**
 * User: ALB
 * Date: 12/09/14 10:54
 */
@Service
@Scope("singleton")
public class ContainerizedEnvironmentConfigurationService extends AbstractEnvironmentConfigurationService<EnvironmentConfiguration> {

    @Autowired
    public ContainerizedEnvironmentConfigurationService(EnvironmentConfigRepository environmentConfigRepository) {
        super(environmentConfigRepository);
    }
}
