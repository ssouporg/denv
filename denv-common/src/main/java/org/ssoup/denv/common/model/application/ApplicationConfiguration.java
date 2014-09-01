package org.ssoup.denv.common.model.application;

import java.util.Collection;

/**
 * User: ALB
 * Date: 09/08/14 19:05
 */
public interface ApplicationConfiguration {
    String getName();
    String getDescription();
    Collection<? extends ImageConfiguration> getImages();
    ImageConfiguration getImageConfigurationByName(String imageName);
}
