package org.ssoup.denv.server.service.versioning;

import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;

import java.util.Comparator;

public interface VersioningPolicy {

    Comparator<EnvironmentConfigurationVersion> getVersionComparator();
}
