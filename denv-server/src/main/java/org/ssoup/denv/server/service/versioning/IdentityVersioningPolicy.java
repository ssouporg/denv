package org.ssoup.denv.server.service.versioning;

import org.springframework.stereotype.Service;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.environment.Environment;

/**
 * User: ALB
 * Date: 28/02/14 10:00
 */
@Service
public class IdentityVersioningPolicy implements VersioningPolicy {

    @Override
    public String getAppVersion(Environment env, ApplicationConfiguration appConf) {
        return env.getVersion();
    }
}
