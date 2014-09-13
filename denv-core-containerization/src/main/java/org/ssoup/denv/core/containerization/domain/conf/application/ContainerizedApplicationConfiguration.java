package org.ssoup.denv.core.containerization.domain.conf.application;

import org.springframework.data.annotation.Id;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;

import java.util.Collection;

/**
 * User: ALB
 * Date: 09/08/14 19:05
 */
public interface ContainerizedApplicationConfiguration extends ApplicationConfiguration {
    Collection<? extends ImageConfiguration> getImages();
    ImageConfiguration getImageConfigurationByName(String imageName);
}
