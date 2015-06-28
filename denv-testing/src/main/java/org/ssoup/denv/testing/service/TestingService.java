package org.ssoup.denv.testing.service;

import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.testing.TestResults;

/**
 * User: ALB
 * Date: 26/02/14 15:11
 */
public interface TestingService {

    void saveTestResults(String envConfId, String version, TestResults testResults) throws DenvException;
    TestResults getTestResults(String envConfId, String version) throws DenvException;
    EnvironmentConfigurationVersion getLatestStableVersion(String envConfId) throws DenvException;
}
