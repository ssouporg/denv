package org.ssoup.denv.server.containerization.service.versioning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.server.service.versioning.VersionService;

/**
 * User: ALB
 * Date: 28/02/14 10:00
 */
@Service
public class ExplicitVersioningPolicy implements VersioningPolicy {

    public static final String VERSION_SUFFIX = "_VERSION";
    private VersionService versionManager;

    @Autowired
    public ExplicitVersioningPolicy(VersionService versionManager) {
        this.versionManager = versionManager;
    }

    @Override
    public String getImageVersion(String envConfId, String envVersion, ImageConfiguration imageConf) {
        EnvironmentConfigurationVersion environmentConfigurationVersion = null;
        if (envVersion != null) {
            environmentConfigurationVersion = (EnvironmentConfigurationVersion) versionManager.getVersion(envConfId, envVersion);
        }

        if (imageConf.getSource() != null) {
            String[] toks = imageConf.getSource().split(":");
            if (toks.length == 2) {
                // if the image source contains a version, use it after substituting variables
                if (envVersion != null && environmentConfigurationVersion != null) {
                    return environmentConfigurationVersion.substituteVariables(toks[1]);
                } else {
                    return toks[1];
                }
            } else {
                // otherwise use the environment version
                return envVersion;
            }
        } else {
            return envVersion;
        }
    }
}
