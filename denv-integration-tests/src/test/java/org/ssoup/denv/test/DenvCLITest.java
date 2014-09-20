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
import org.ssoup.denv.core.exception.ResourceNotFoundException;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.Environment;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testCreateEnv() throws IOException {
        try {
            runCLICommand("create", INTEGRATION_ENV_ID);
            String out = getConsoleOutput();
            assertEquals("", out);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
        }
    }

    @Test
    public void testListEnvs() throws IOException {
        registerPanamaxAppConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, PANAMAX_APP_CONF_ID);

            runCLICommand("list");
            String out = getConsoleOutput();
            assertTrue(out.contains(INTEGRATION_ENV_ID));
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            deleteAppConfig(PANAMAX_APP_CONF_ID);
        }
    }

    @Test
    public void testRemoveEnv() throws IOException {
        registerPanamaxAppConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, PANAMAX_APP_CONF_ID);

            runCLICommand("rm", INTEGRATION_ENV_ID);
            String out = getConsoleOutput();
            assertEquals("", out);

            listEnvironmentsShouldNotContain(INTEGRATION_ENV_ID);
        } finally {
            deleteAppConfig(PANAMAX_APP_CONF_ID);
        }
    }

    @Test
    public void testApps() throws IOException {
        try {
            registerPanamaxAppConfig();

            runCLICommand("apps");
            String out = getConsoleOutput();
            assertTrue(out.contains(PANAMAX_APP_CONF_ID));
        } finally {
            deleteAppConfig(PANAMAX_APP_CONF_ID);
        }
    }

    @Test
    public void testAddRemoteApp() throws IOException {
        String appConfId = null;
        try {
            runCLICommand("add", new String[]{"-s", PANAMAX_APP_CONF_URL, "-f", "panamax"});
            assertEquals("", getConsoleErrorOutput());
            appConfId = getConsoleOutput().trim();
        } finally {
            if (appConfId != null) {
                deleteAppConfig(appConfId);
            }
        }
    }

    @Test
    public void testAddAndStartRemoteApp() throws IOException, ResourceNotFoundException {
        String appConfId = null;
        try {
            createEnvironment(INTEGRATION_ENV_ID);

            runCLICommand("add", new String[]{"-s", PANAMAX_APP_CONF_URL, "-f", "panamax", "--run", "--envs", INTEGRATION_ENV_ID});
            assertEquals("", getConsoleErrorOutput());
            appConfId = getConsoleOutput().trim();

            Environment env = getEnv(INTEGRATION_ENV_ID);
            assertNotNull(env);
            Application app = env.getApplication(appConfId);
            assertNotNull(app);
            assertTrue(app.isDeployed());
            assertTrue(app.isStarted());
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            if (appConfId != null) {
                deleteAppConfig(appConfId);
            }
        }
    }
}
