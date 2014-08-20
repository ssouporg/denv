package org.ssoup.denv.server.domain.conf.application;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 11/08/14 14:01
 */
public abstract class AbstractApplicationConfigurationManager implements ApplicationConfigurationManager {

    private Map<String, ApplicationConfiguration> applicationConfigurationMap = new HashMap<String, ApplicationConfiguration>();

    @Override
    public ApplicationConfiguration getApplicationConfiguration(String applicationName) {
        return applicationConfigurationMap.get(applicationName);
    }

    protected void addApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfigurationMap.put(applicationConfiguration.getName(), applicationConfiguration);
    }
}
