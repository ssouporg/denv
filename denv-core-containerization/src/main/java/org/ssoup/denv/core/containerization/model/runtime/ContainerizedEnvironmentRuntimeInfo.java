package org.ssoup.denv.core.containerization.model.runtime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;

import java.util.Collection;
import java.util.Map;

/**
 * User: ALB
 * Date: 12/09/14 11:23
 */
@JsonDeserialize(as = ContainerizedEnvironmentRuntimeInfoImpl.class)
public interface ContainerizedEnvironmentRuntimeInfo extends EnvironmentRuntimeInfo {
    void setContainerRuntimeInfo(String imageId, ContainerRuntimeInfo containerInfo);
    ContainerRuntimeInfo getContainerRuntimeInfo(String imageId);
    Map<String, ContainerRuntimeInfo> getContainersRuntimeInfo();
}
