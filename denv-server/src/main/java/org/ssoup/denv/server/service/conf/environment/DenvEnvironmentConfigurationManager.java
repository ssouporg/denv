package org.ssoup.denv.server.service.conf.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
//import org.ssoup.denv.common.model.config.environment.DenvEnvironmentConfiguration;
import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;
import org.ssoup.denv.server.persistence.repository.EnvironmentConfigRepository;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;

/**
 * User: ALB
 * Date: 11/08/14 14:01
 */
@Service
@Scope("singleton")
public class DenvEnvironmentConfigurationManager<T extends EnvironmentConfiguration> implements EnvironmentConfigurationManager<T> {

    private EnvironmentConfigRepository<T> environmentConfigRepository;

    private ApplicationConfigurationManager applicationConfigurationManager;

    @Autowired
    public DenvEnvironmentConfigurationManager(EnvironmentConfigRepository environmentConfigRepository, ApplicationConfigurationManager applicationConfigurationManager) {
        this.environmentConfigRepository = environmentConfigRepository;
        this.applicationConfigurationManager = applicationConfigurationManager;
    }

    @Override
    public T getEnvironmentConfiguration(String id) {
        return environmentConfigRepository.findOne(id);
    }

    @Override
    public void registerEnvironmentConfiguration(T environmentConfiguration) {
        if (environmentConfiguration.getApplicationConfigurationName() != null) {
            ApplicationConfiguration applicationConfiguration =
                    applicationConfigurationManager.getApplicationConfiguration(environmentConfiguration.getApplicationConfigurationName());
            //((DenvEnvironmentConfiguration)environmentConfiguration).setApplicationConfiguration(applicationConfiguration);
        }
        environmentConfigRepository.save(environmentConfiguration);
    }
}
