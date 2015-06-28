package org.ssoup.denv.server.service.versioning;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersionImpl;

import java.util.List;
import java.util.Map;

/**
 * User: ALB
 * Date: 15/12/2014 09:22
 */
public interface VersionService {

    void addVersion(EnvironmentConfiguration envConf, String version, Map<String, String> variables);
    EnvironmentConfigurationVersion getVersion(String envConfId, String version);
    Page<? extends EnvironmentConfigurationVersion> listVersions(String envConfId, Pageable pageable);
    List<? extends EnvironmentConfigurationVersion> listAllVersions(String envConfId);
    void deleteVersion(String envConfId, String version);
    // void build(EnvironmentConfiguration envConf, String version) throws DenvException;
}
