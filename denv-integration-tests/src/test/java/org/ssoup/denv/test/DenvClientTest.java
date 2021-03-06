package org.ssoup.denv.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.ssoup.denv.Denv;

/**
 * User: ALB
 * Date: 20/08/14 09:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Denv.class)
@WebAppConfiguration
@IntegrationTest
public class DenvClientTest extends DenvTestBase {

    private Logger logger = LoggerFactory.getLogger(DenvClientTest.class);

    @Test
    public void runFigApp() throws Exception {
        registerFigEnvConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, FIG_ENV_CONF_ID);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            deleteEnvConfig(FIG_ENV_CONF_ID);
        }
    }

    @Test
    public void runPanamaxApp() throws Exception {
        registerPanamaxEnvConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, PANAMAX_ENV_CONF_ID);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            deleteEnvConfig(PANAMAX_ENV_CONF_ID);
        }
    }

    @Test
    public void runFigAndPanamaxEnvs() throws Exception {
        registerFigEnvConfig();
        registerPanamaxEnvConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, FIG_ENV_CONF_ID);
            createEnvironment(INTEGRATION_ENV2_ID, PANAMAX_ENV_CONF_ID);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
            listEnvironmentsAndCheckFor(INTEGRATION_ENV2_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            deleteEnvironment(INTEGRATION_ENV2_ID);
            deleteEnvConfig(PANAMAX_ENV_CONF_ID);
            deleteEnvConfig(FIG_ENV_CONF_ID);
        }
    }
}
