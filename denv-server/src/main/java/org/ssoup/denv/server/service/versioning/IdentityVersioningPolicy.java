package org.ssoup.denv.server.service.versioning;

import org.springframework.stereotype.Service;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;

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
