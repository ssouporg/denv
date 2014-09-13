package org.ssoup.denv.server.service.versioning;

import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;

/**
 * User: ALB
 * Date: 28/02/14 10:00
 */
public interface VersioningPolicy {
    String getAppVersion(Environment env, ApplicationConfiguration appConf);
}
