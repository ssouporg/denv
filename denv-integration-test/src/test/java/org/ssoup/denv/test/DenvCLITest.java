package org.ssoup.denv.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.ssoup.denv.Denv;
import org.ssoup.denv.core.exception.DenvHttpException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * User: ALB
 * Date: 20/08/14 09:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Denv.class)
@WebAppConfiguration
@IntegrationTest
public class DenvCLITest extends DenvTestBase {

    private Logger logger = LoggerFactory.getLogger(DenvCLITest.class);

    @Test
    public void testCreateEnv() throws IOException, DenvHttpException {
        try {
            runCLICommand("create", INTEGRATION_ENV_ID);
            String res = getConsoleOutput();
            assertEquals("", res);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
        }
    }

    @Test
    public void testListEnvs() throws IOException, DenvHttpException {
        registerPanamaxAppConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, PANAMAX_APP_CONF_ID);

            runCLICommand("list");
            String res = getConsoleOutput();
            assertTrue(res.contains(INTEGRATION_ENV_ID));
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
        }
    }

    @Test
    public void testRemoveEnv() throws IOException, DenvHttpException {
        registerPanamaxAppConfig();

        createEnvironment(INTEGRATION_ENV_ID, PANAMAX_APP_CONF_ID);

        runCLICommand("rm", INTEGRATION_ENV_ID);
        String res = getConsoleOutput();
        assertEquals("", res);

        listEnvironmentsShouldNotContain(INTEGRATION_ENV_ID);
    }

    @Test
    public void testApps() throws IOException, DenvHttpException {
        try {
            registerPanamaxAppConfig();

            runCLICommand("apps");
            String res = getConsoleOutput();
            assertTrue(res.contains(PANAMAX_APP_CONF_ID));
        } finally {
        }
    }

    @Test
    public void testAddApp() throws IOException, DenvHttpException {
        try {
            registerPanamaxAppConfig();

            runCLICommand("add", new String[]{PANAMAX_APP_CONF_ID, "--run", "--env", INTEGRATION_ENV_ID});
            String res = getConsoleOutput();
            assertEquals("", res);
        } finally {
        }
    }

    @Test
    public void testAddAndStartApp() throws IOException, DenvHttpException {
        try {
            registerPanamaxAppConfig();

            createEnvironment(INTEGRATION_ENV_ID);

            runCLICommand("add", new String[]{PANAMAX_APP_CONF_ID, "--start", "--env", INTEGRATION_ENV_ID});
            String res = getConsoleOutput();
            assertEquals("", res);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
        }
    }
}
