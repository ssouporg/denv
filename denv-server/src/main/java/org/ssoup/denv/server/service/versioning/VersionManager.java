package org.ssoup.denv.server.service.versioning;

import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;

/**
 * User: ALB
 * Date: 15/12/2014 09:22
 */
public interface VersionManager {

    void scheduleBuild(EnvironmentConfiguration envConf, String version);
    void build(EnvironmentConfiguration envConf, String version) throws DenvException;
}
