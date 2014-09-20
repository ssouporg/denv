package org.ssoup.denv.core.containerization.domain.conf.application;

import org.springframework.data.annotation.Id;

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
    String getSource();
    Collection<? extends EnvironmentVariableConfiguration> getEnvironment();
    Collection<? extends PortConfiguration> getPorts();
    Collection<? extends LinkConfiguration> getLinks();
    Collection<? extends VolumeConfiguration> getVolumes();
}
