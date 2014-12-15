package org.ssoup.denv.core.model.runtime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.ssoup.denv.core.model.conf.node.NodeConfiguration;

import java.util.Collection;

/**
 * User: ALB
 * Date: 27/02/14 09:08
 */
public interface Environment {
    @Id
    String getId();

    String getName();

    String getEnvironmentConfigurationId();
    String getVersion();
    String getSnapshotName();

    EnvironmentState getActualState();
    EnvironmentDesiredState getDesiredState();

    Collection<String> getLabels();
    boolean hasLabel(String label);

    @JsonIgnore
    NodeConfiguration getNode();

    EnvironmentRuntimeInfo getRuntimeInfo();

    boolean isBuilder();
}
