package org.ssoup.denv.server.service.versioning;

import org.springframework.stereotype.Service;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;

import java.util.Comparator;

/**
 * User: ALB
 * Date: 28/06/2015 14:43
 */
@Service
public class SemanticVersioningPolicy implements VersioningPolicy {

    @Override
    public Comparator<EnvironmentConfigurationVersion> getVersionComparator() {
        return new Comparator<EnvironmentConfigurationVersion>() {
            @Override
            public int compare(EnvironmentConfigurationVersion envConfVer1, EnvironmentConfigurationVersion envConfVer2) {
                SemanticVersion semVer1 = SemanticVersion.valueOf(envConfVer1.getVersion());
                SemanticVersion semVer2 = SemanticVersion.valueOf(envConfVer2.getVersion());
                return semVer1.compareTo(semVer2);
            }
        };
    }
}
