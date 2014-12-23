package org.ssoup.denv.server.service.versioning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersionImpl;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.server.persistence.VersionRepository;

/**
 * User: ALB
 * Date: 15/12/2014 10:27
 */
@Service
public class VersionManagerImpl implements VersionManager {

    private VersionRepository versionRepository;

    @Autowired
    public VersionManagerImpl(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Override
    public void addVersion(EnvironmentConfiguration envConf, String version) {
        EnvironmentConfigurationVersionImpl envConfVersion = new EnvironmentConfigurationVersionImpl(
                DenvEnvironment.buildVersionId(envConf.getId(), version),
                envConf.getId(), version);
        versionRepository.save(envConfVersion);
    }

    @Override
    public EnvironmentConfigurationVersion getVersion(String envConfId, String version) {
        return (EnvironmentConfigurationVersion)versionRepository.findOne(DenvEnvironment.buildVersionId(envConfId, version));
    }

    @Override
    public Page<? extends EnvironmentConfigurationVersion> listVers(String envConfId, Pageable pageable) {
        return versionRepository.findAll(pageable);
    }

    @Override
    public void deleteVersion(String envConfId, String version) {
        versionRepository.delete(DenvEnvironment.buildVersionId(envConfId, version));
    }
}
