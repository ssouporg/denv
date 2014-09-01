package org.ssoup.denv.server.service.versioning;

import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.runtime.environment.Environment;

/**
 * User: ALB
 * Date: 28/02/14 10:00
 */
public interface VersioningPolicy {
    String getAppVersion(Environment env, ApplicationConfiguration appConf);
}
