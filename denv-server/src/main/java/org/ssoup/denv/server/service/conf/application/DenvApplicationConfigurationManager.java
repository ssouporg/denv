package org.ssoup.denv.server.service.conf.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.server.persistence.repository.ApplicationConfigRepository;

import java.util.*;

/**
 * User: ALB
 * Date: 11/08/14 14:01
 */
@Service
@Scope("singleton")
public class DenvApplicationConfigurationManager implements ApplicationConfigurationManager {

    private ApplicationConfigRepository applicationConfigRepository;

    @Autowired
    public DenvApplicationConfigurationManager(ApplicationConfigRepository applicationConfigRepository) {
        this.applicationConfigRepository = applicationConfigRepository;
    }

    @Override
    public ApplicationConfiguration getApplicationConfiguration(String applicationConfigurationName) {
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
    public void registerApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        applicationConfigRepository.save(applicationConfiguration);
    }
}
