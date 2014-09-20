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
        registerFigAppConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, FIG_APP_CONF_ID);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            deleteAppConfig(FIG_APP_CONF_ID);
        }
    }

    @Test
    public void runPanamaxApp() throws Exception {
        registerPanamaxAppConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, PANAMAX_APP_CONF_ID);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            deleteAppConfig(PANAMAX_APP_CONF_ID);
        }
    }

    @Test
    public void runFigAndPanamaxApps() throws Exception {
        registerFigAppConfig();
        registerPanamaxAppConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, FIG_APP_CONF_ID);

            updateEnvironment(INTEGRATION_ENV_ID, PANAMAX_APP_CONF_ID);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            deleteAppConfig(PANAMAX_APP_CONF_ID);
            deleteAppConfig(FIG_APP_CONF_ID);
        }
    }
}
