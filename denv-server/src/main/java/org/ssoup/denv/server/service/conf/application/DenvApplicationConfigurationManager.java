package org.ssoup.denv.server.service.conf.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.server.persistence.repository.ApplicationConfigRepository;

import java.util.*;

/**
 * User: ALB
 * Date: 11/08/14 14:01
 */
@Service
@Scope("singleton")
public class DenvApplicationConfigurationManager<T extends ApplicationConfiguration> implements ApplicationConfigurationManager<T> {

    private ApplicationConfigRepository<T> applicationConfigRepository;

    @Autowired
    public DenvApplicationConfigurationManager(ApplicationConfigRepository applicationConfigRepository) {
        this.applicationConfigRepository = applicationConfigRepository;
    }

    @Override
    public T getApplicationConfiguration(String applicationConfigurationName) {
        return applicationConfigRepository.findOne(applicationConfigurationName);
    }

    @Override
    public Collection<String> listApplicationConfigurationNames() {
        List<String> appConfNames = new ArrayList<String>();
        for (ApplicationConfiguration appConf : applicationConfigRepository.findAll()) {
            appConfNames.add(appConf.getName());
        }
        return appConfNames;
    }

    @Override
    public void registerApplicationConfiguration(T applicationConfiguration) {
        applicationConfigRepository.save(applicationConfiguration);
    }
}
