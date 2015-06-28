package org.ssoup.denv.testing.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssoup.denv.core.api.DenvApiEndpoints;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.testing.TestResults;
import org.ssoup.denv.server.api.AbstractController;
import org.ssoup.denv.testing.service.TestingService;

@RestController
public class TestingController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(TestingController.class);

    private TestingService testingService;

    @Autowired
    public TestingController(TestingService testingService) {
        this.testingService = testingService;
    }

    @RequestMapping(value = DenvApiEndpoints.TEST_RESULTS, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Void> saveTestResults(@PathVariable String envConfId, @PathVariable String version, @RequestBody TestResults testResults) throws DenvException {
        testingService.saveTestResults(envConfId, version, testResults);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = DenvApiEndpoints.TEST_RESULTS, method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<TestResults> getTestResults(@PathVariable String envConfId, @PathVariable String version) throws DenvException {
        TestResults testResults = testingService.getTestResults(envConfId, version);
        return new ResponseEntity<TestResults>(testResults, HttpStatus.OK);
    }

    @RequestMapping(value = DenvApiEndpoints.TEST_RESULTS, method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<EnvironmentConfigurationVersion> getLatestStableVersion(@PathVariable String envConfId) throws DenvException {
        EnvironmentConfigurationVersion envConfVersion = testingService.getLatestStableVersion(envConfId);
        return new ResponseEntity<EnvironmentConfigurationVersion>(envConfVersion, HttpStatus.OK);
    }
}
