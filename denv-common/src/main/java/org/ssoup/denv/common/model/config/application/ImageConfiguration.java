package org.ssoup.denv.common.model.config.application;

import java.util.Collection;

/**
* User: ALB
* Date: 23/08/14 21:14
*/
public interface ImageConfiguration {
    String getName();
    String getDescription();
    String getSource();
    Collection<? extends EnvironmentVariableConfiguration> getEnvironment();
    Collection<? extends PortConfiguration> getPorts();
    Collection<? extends LinkConfiguration> getLinks();
    Collection<? extends VolumeConfiguration> getVolumes();
}
