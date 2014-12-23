package org.ssoup.denv.core.containerization.model.conf.environment;

import org.springframework.data.annotation.Id;
import org.ssoup.denv.core.containerization.model.runtime.ContainerDesiredState;

import java.util.Collection;

/**
* User: ALB
* Date: 23/08/14 21:14
*/
public interface ImageConfiguration {
    @Id
    String getId();
    String getName();
    String getDescription();
    String getCommand();
    String getBuildCommand();
    Collection<String> getServicesToVersionWhenBuildSucceeds();
    String getTargetImage();
    String getSource();
    boolean isPrivileged();
    Collection<? extends EnvironmentVariableConfiguration> getEnvironment();
    Collection<? extends PortConfiguration> getPorts();
    Collection<? extends LinkConfiguration> getLinks();
    Collection<? extends VolumeConfiguration> getVolumes();
    Collection<? extends DenvVariableConfiguration> getVariables();
    ContainerDesiredState getInitialDesiredState();
}
