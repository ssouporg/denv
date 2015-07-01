package org.ssoup.denv.server.service.versioning;

import org.springframework.stereotype.Service;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;

import java.util.Comparator;

/**
 * User: ALB
 * Date: 28/06/2015 14:43
 */
@Service
public class ExtendedSemanticVersioningPolicy implements VersioningPolicy {

    @Override
    public Comparator<EnvironmentConfigurationVersion> getVersionComparator() {
        return new Comparator<EnvironmentConfigurationVersion>() {
            @Override
            public int compare(EnvironmentConfigurationVersion envConfVer1, EnvironmentConfigurationVersion envConfVer2) {
                ExtendedSemanticVersion semVer1 = ExtendedSemanticVersion.valueOf(envConfVer1.getVersion());
                ExtendedSemanticVersion semVer2 = ExtendedSemanticVersion.valueOf(envConfVer2.getVersion());
                return semVer1.compareTo(semVer2);
            }
        };
    }
}
