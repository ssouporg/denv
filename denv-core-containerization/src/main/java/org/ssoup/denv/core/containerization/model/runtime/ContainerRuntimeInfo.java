package org.ssoup.denv.core.containerization.model.runtime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;

import java.util.Map;

/**
 * User: ALB
 * Date: 12/12/13 16:44
 */
@JsonDeserialize(as=ContainerRuntimeInfoImpl.class)
public interface ContainerRuntimeInfo {
    String getId();

    void setId(String id);

    String getName();

    String[] getAllNames();

    String getHostname();

    Map<Integer, Integer> getPortMapping();

    String getImageConfigurationId();

    ContainerDesiredState getDesiredState();

    void setDesiredState(ContainerDesiredState containerState);

    ContainerState getActualState();

    void setActualState(ContainerState containerState);

    Map<String, String> getVariables();

    String getVariableValue(String variable);

    boolean canBeStarted();

    boolean canBeStopped();
}
