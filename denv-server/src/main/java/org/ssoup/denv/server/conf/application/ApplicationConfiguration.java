package org.ssoup.denv.server.conf.application;

/**
 * User: ALB
 * Date: 09/08/14 19:05
 */
public interface ApplicationConfiguration {

    public ServiceConfiguration getServiceConfiguration(String serviceName);
    public void setServiceConfiguration(String serviceName, ServiceConfiguration serviceConfiguration);
}
