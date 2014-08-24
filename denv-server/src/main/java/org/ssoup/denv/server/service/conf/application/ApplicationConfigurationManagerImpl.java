package org.ssoup.denv.server.service.conf.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 11/08/14 14:01
 */
@Service
@Scope("singleton")
public class ApplicationConfigurationManagerImpl implements ApplicationConfigurationManager {

    private Map<String, ApplicationConfiguration> applicationConfigurationMap = new HashMap<String, ApplicationConfiguration>();

    @Override
    public ApplicationConfiguration getApplicationConfiguration(String applicationConfigurationName) {
        return applicationConfigurationMap.get(applicationConfigurationName);
    }

    @Override
    public Collection<String> listApplicationConfigurationNames() {
        return applicationConfigurationMap.keySet();
    }

    @Override
    public void registerApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        applicationConfigurationMap.put(applicationConfiguration.getName(), applicationConfiguration);
    }
}
