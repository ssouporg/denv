package org.ssoup.denv.server.domain.conf.application;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 11/08/14 15:35
 */
public class ApplicationConfigurationImpl implements ApplicationConfiguration {

    private String name;
    private Map<String, ServiceConfiguration> serviceConfigurationMap = new HashMap<String, ServiceConfiguration>();

    public ApplicationConfigurationImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<ServiceConfiguration> getServiceConfigurations() {
        return serviceConfigurationMap.values();
    }

    @Override
    public ServiceConfiguration getServiceConfiguration(String serviceName) {
        return this.serviceConfigurationMap.get(serviceName);
    }

    @Override
    public void addServiceConfiguration(ServiceConfiguration serviceConfiguration) {
        this.serviceConfigurationMap.put(serviceConfiguration.getName(), serviceConfiguration);
    }
}
