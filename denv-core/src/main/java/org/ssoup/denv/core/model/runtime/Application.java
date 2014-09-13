package org.ssoup.denv.core.model.runtime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * User: ALB
 * Date: 09/01/14 17:27
 */
@JsonDeserialize(as = ApplicationImpl.class)
public interface Application {
    String getId();
    String getApplicationConfigurationId();

    // runtime info
    boolean isDeployed();
    void setDeployed(boolean deployed);

    // runtime info
    boolean isStarted();
    void setStarted(boolean started);
}
