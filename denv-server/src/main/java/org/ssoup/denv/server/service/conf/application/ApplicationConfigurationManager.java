package org.ssoup.denv.server.service.conf.application;

import org.ssoup.denv.common.model.application.ApplicationConfiguration;

import java.util.Collection;

/**
 * User: ALB
 * Date: 11/08/14 14:00
 */
public interface ApplicationConfigurationManager {
    ApplicationConfiguration getApplicationConfiguration(String applicationConfigurationName);
    Collection<String> listApplicationConfigurationNames();
    void registerApplicationConfiguration(ApplicationConfiguration applicationConfiguration);
}
