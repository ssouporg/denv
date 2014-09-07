package org.ssoup.denv.server.service.conf.application;

import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;

import java.util.Collection;

/**
 * User: ALB
 * Date: 11/08/14 14:00
 */
public interface ApplicationConfigurationManager<T extends ApplicationConfiguration> {
    T getApplicationConfiguration(String applicationConfigurationName);
    Collection<String> listApplicationConfigurationNames();
    void registerApplicationConfiguration(T applicationConfiguration);
}
