package org.ssoup.denv.common.model.config.environment;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;

import java.util.Collection;

/**
 * User: ALB
 * Date: 25/08/14 08:55
 */
public interface EnvironmentConfiguration {
    @Id
    String getId();

    Collection<String> getLabels();
    boolean hasLabel(String label);
    String getApplicationConfigurationName();
}
