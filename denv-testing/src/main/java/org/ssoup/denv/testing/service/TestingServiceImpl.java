package org.ssoup.denv.testing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.testing.TestOutcome;
import org.ssoup.denv.core.model.testing.TestResults;
import org.ssoup.denv.server.service.versioning.VersionService;
import org.ssoup.denv.server.service.versioning.VersioningPolicy;

import java.util.Collections;
import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {

    public static final String TEST_BUILD_NUMBER = "TEST_BUILD_NUMBER";
    public static final String TEST_OUTCOME = "TEST_OUTCOME";

    private VersionService versionService;
    private VersioningPolicy versioningPolicy;

    @Autowired
    public TestingServiceImpl(VersionService versionService, VersioningPolicy versioningPolicy) {
        this.versionService = versionService;
        this.versioningPolicy = versioningPolicy;
    }

    @Override
    public void saveTestResults(String envConfId, String version, TestResults testResults) throws DenvException {
        EnvironmentConfigurationVersion envConfVersion = versionService.getVersion(envConfId, version);
        envConfVersion.setVariable(TEST_BUILD_NUMBER, testResults.getTestBuildNumber());
        envConfVersion.setVariable(TEST_OUTCOME, testResults.getTestOutcome().name());
        versionService.updateVersion(envConfVersion);
    }

    @Override
    public TestResults getTestResults(String envConfId, String version) throws DenvException {
        EnvironmentConfigurationVersion envConfVersion = versionService.getVersion(envConfId, version);
        return getTestResults(envConfVersion);
    }

    private TestResults getTestResults(EnvironmentConfigurationVersion envConfVersion) {
        TestResults testResults = new TestResults();
        testResults.setTestBuildNumber(envConfVersion.getVariable(TEST_BUILD_NUMBER));
        TestOutcome testOutcome = TestOutcome.valueOf(envConfVersion.getVariable(TEST_OUTCOME));
        testResults.setTestOutcome(testOutcome);
        return testResults;
    }

    @Override
    public EnvironmentConfigurationVersion getLatestStableVersion(String envConfId) throws DenvException {
        List<? extends EnvironmentConfigurationVersion> vers = versionService.listAllVersions(envConfId);
        if (vers.size() > 0) {
            Collections.sort(vers, Collections.reverseOrder(versioningPolicy.getVersionComparator()));
            for (EnvironmentConfigurationVersion ver : vers) {
                TestResults testResults = getTestResults(ver);
                if (testResults.getTestOutcome() == TestOutcome.SUCCESS) {
                    return ver;
                }
            }
        }
        return null;
    }
}
