package org.ssoup.denv.core.containerization.domain.conf.application;

import org.springframework.data.annotation.Id;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;

import java.util.Collection;
import java.util.Map;

/**
 * User: ALB
 * Date: 09/08/14 19:05
 */
public interface ContainerizedApplicationConfiguration extends ApplicationConfiguration {
    Map<String, ? extends ImageConfiguration> getImages();
    ImageConfiguration getImageConfiguration(String imageId);
    void addImage(ImageConfiguration image);
}
