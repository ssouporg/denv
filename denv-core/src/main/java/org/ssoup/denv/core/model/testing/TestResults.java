package org.ssoup.denv.core.model.testing;

/**
 * User: ALB
 * Date: 28/06/2015 13:46
 */
public class TestResults {

    private String testBuildNumber;
    private TestOutcome testOutcome;

    public TestOutcome getTestOutcome() {
        return testOutcome;
    }

    public void setTestOutcome(TestOutcome testOutcome) {
        this.testOutcome = testOutcome;
    }

    public String getTestBuildNumber() {
        return testBuildNumber;
    }

    public void setTestBuildNumber(String testBuildNumber) {
        this.testBuildNumber = testBuildNumber;
    }
}
