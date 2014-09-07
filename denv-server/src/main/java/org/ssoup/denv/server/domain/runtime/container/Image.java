package org.ssoup.denv.server.domain.runtime.container;

import org.ssoup.denv.common.model.config.application.ImageConfiguration;

/**
 * User: ALB
 * Date: 09/01/14 13:24
 */
public interface Image {
    String getId();
    String getName();
    String getTag();
    ImageConfiguration getConf();
}
