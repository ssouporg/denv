package org.ssoup.denv.server.containerization.service.versioning;

import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;

/**
 * User: ALB
 * Date: 28/02/14 10:00
 */
public interface ContainerVersioningPolicy {
    String getImageVersion(String envConfId, String envVersion, ImageConfiguration imageConf);
}
