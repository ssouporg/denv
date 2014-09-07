package org.ssoup.denv.server.domain.runtime.environment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;
import org.ssoup.denv.common.model.config.node.NodeConfiguration;
import org.ssoup.denv.server.domain.runtime.application.Application;

/**
 * User: ALB
 * Date: 27/02/14 09:08
 */
public interface Environment {
    @Id
    String getId();

    String getEnvironmentConfigurationId();

    @JsonIgnore
    EnvironmentConfiguration getEnvironmentConfiguration();

    @JsonIgnore
    NodeConfiguration getNode();

    Application getApplication();

    void setApplication(Application app);

    boolean isStarted();
}
