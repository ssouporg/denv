package org.ssoup.denv.server.service.versioning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigVersionDesiredState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersionImpl;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.server.persistence.VersionRepository;

import java.util.List;
import java.util.Map;

/**
 * User: ALB
 * Date: 15/12/2014 10:27
 */
@Service
public class VersionServiceImpl implements VersionService {

    private VersionRepository versionRepository;

    @Autowired
    public VersionServiceImpl(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Override
    public void addVersion(EnvironmentConfiguration envConf, String version, Map<String, String> variables) {
        EnvironmentConfigurationVersionImpl envConfVersion = new EnvironmentConfigurationVersionImpl(
                DenvEnvironment.buildVersionId(envConf.getId(), version),
                envConf.getId(), version, variables);
        versionRepository.save(envConfVersion);
    }

    @Override
    public void updateVersion(EnvironmentConfigurationVersion envConfVersion) {
        versionRepository.save(envConfVersion);
    }

    @Override
    public EnvironmentConfigurationVersion getVersion(String envConfId, String version) {
        return (EnvironmentConfigurationVersion)versionRepository.findOne(DenvEnvironment.buildVersionId(envConfId, version));
    }

    @Override
    public Page<? extends EnvironmentConfigurationVersion> listVersions(String envConfId, Pageable pageable) {
        return versionRepository.listByEnvConf(envConfId, pageable);
    }

    @Override
    public List<? extends EnvironmentConfigurationVersion> listAllVersions(String envConfId) {
        return versionRepository.listAllByEnvConf(envConfId);
    }

    @Override
    public void deleteVersion(String envConfId, String version) {
        versionRepository.updateDesiredState(DenvEnvironment.buildVersionId(envConfId, version), EnvironmentConfigVersionDesiredState.DELETED);
    }
}
