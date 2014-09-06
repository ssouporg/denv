package org.ssoup.denv.server.service.conf.environment;

import org.ssoup.denv.common.model.environment.EnvironmentConfiguration;

/**
 * User: ALB
 * Date: 11/08/14 14:00
 */
public interface EnvironmentConfigurationManager {
    EnvironmentConfiguration getEnvironmentConfiguration(String id);
    void registerEnvironmentConfiguration(EnvironmentConfiguration environmentConfiguration);
}
