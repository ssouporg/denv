package org.ssoup.denv.server.service.versioning;

import org.springframework.beans.factory.annotation.Autowired;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersionImpl;
import org.ssoup.denv.server.persistence.VersionRepository;

/**
 * User: ALB
 * Date: 15/12/2014 10:27
 */
public abstract class AbstractVersionManager implements VersionManager {

    private VersionRepository versionRepository;

    @Autowired
    public AbstractVersionManager(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Override
    public void scheduleBuild(EnvironmentConfiguration envConf, String version) {
        EnvironmentConfigurationVersionImpl envConfVersion = new EnvironmentConfigurationVersionImpl(envConf.getId(), version);
        versionRepository.save(envConfVersion);
    }
}
