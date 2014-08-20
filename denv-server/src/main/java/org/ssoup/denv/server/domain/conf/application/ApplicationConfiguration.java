package org.ssoup.denv.server.domain.conf.application;

import java.util.Collection;

/**
 * User: ALB
 * Date: 09/08/14 19:05
 */
public interface ApplicationConfiguration {
    String getName();
    Collection<ServiceConfiguration> getServiceConfigurations();
    ServiceConfiguration getServiceConfiguration(String serviceName);
    void addServiceConfiguration(ServiceConfiguration serviceConfiguration);
}
