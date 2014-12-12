package org.ssoup.denv.core.containerization.model.conf.environment;

import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;

import java.util.Map;

/**
 * User: ALB
 * Date: 09/08/14 19:05
 */
public interface ContainerizedEnvironmentConfiguration extends EnvironmentConfiguration {
    Map<String, ? extends ImageConfiguration> getImages();
    ImageConfiguration getImageConfiguration(String imageId);
    void addImage(ImageConfiguration image);
}
