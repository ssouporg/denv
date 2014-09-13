package org.ssoup.denv.server.containerization.domain.runtime;

import org.springframework.data.annotation.Id;
import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;

/**
 * User: ALB
 * Date: 09/01/14 13:24
 */
public interface Image {
    @Id
    String getId();
    String getName();
    String getTag();
    ImageConfiguration getConf();
}
