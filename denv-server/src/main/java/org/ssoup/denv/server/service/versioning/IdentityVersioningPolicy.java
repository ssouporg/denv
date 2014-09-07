package org.ssoup.denv.server.service.versioning;

import org.springframework.stereotype.Service;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.runtime.environment.Environment;

/**
 * User: ALB
 * Date: 28/02/14 10:00
 */
@Service
public class IdentityVersioningPolicy implements VersioningPolicy {

    @Override
    public String getAppVersion(Environment env, ApplicationConfiguration appConf) {
        return "1.0"; // env.getVersion();
    }
}
