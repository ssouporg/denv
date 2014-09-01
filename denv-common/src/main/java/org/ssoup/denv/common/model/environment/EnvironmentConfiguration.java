package org.ssoup.denv.common.model.environment;

import org.ssoup.denv.common.model.application.ApplicationConfiguration;

import java.util.Collection;

/**
 * User: ALB
 * Date: 25/08/14 08:55
 */
public interface EnvironmentConfiguration {
    Collection<String> getLabels();
    boolean hasLabel(String label);
    String getApplicationConfigurationName();
    ApplicationConfiguration getApplicationConfiguration();
}
