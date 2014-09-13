package org.ssoup.denv.server.service.conf.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.persistence.ApplicationConfigRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public T getApplicationConfiguration(String applicationConfigurationId) {
        return applicationConfigRepository.findOne(applicationConfigurationId);
    }

    @Override
    public Collection<String> listApplicationConfigurationIds() {
        List<String> appConfIds = new ArrayList<String>();
        for (ApplicationConfiguration appConf : applicationConfigRepository.findAll()) {
            appConfIds.add(appConf.getId());
        }
        return appConfIds;
    }

    @Override
    public void registerApplicationConfiguration(T applicationConfiguration) {
        applicationConfigRepository.save(applicationConfiguration);
    }
}
