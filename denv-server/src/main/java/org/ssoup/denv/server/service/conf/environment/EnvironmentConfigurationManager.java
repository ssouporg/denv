package org.ssoup.denv.server.service.conf.environment;

import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;

/**
 * User: ALB
 * Date: 11/08/14 14:00
 */
public interface EnvironmentConfigurationManager<T extends EnvironmentConfiguration> {
    T getEnvironmentConfiguration(String id);
    void registerEnvironmentConfiguration(T environmentConfiguration);
}
