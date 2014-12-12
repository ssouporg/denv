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
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
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
    public void testAddEnv() throws Exception {
        registerPanamaxAppConfig();

        try {
            runCLICommand("add", INTEGRATION_ENV_ID, "-c", PANAMAX_ENV_CONF_ID, "-w", "-m", "" + (2 * 3600 * 1000));
            String out = getConsoleOutput();
            assertEquals("", out);

            listEnvironmentsAndCheckFor(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            deleteEnvConfig(PANAMAX_ENV_CONF_ID);
        }
    }

    @Test
    public void testListEnvs() throws Exception {
        registerPanamaxAppConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, PANAMAX_ENV_CONF_ID);

            runCLICommand("envs");
            String out = getConsoleOutput();
            assertTrue(out.contains(INTEGRATION_ENV_ID));
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            deleteEnvConfig(PANAMAX_ENV_CONF_ID);
        }
    }

    @Test
    public void testRemoveEnv() throws Exception {
        registerPanamaxAppConfig();

        try {
            createEnvironment(INTEGRATION_ENV_ID, PANAMAX_ENV_CONF_ID);

            runCLICommand("rm", INTEGRATION_ENV_ID);
            String out = getConsoleOutput();
            assertEquals("", out);

            listEnvironmentsShouldNotContain(INTEGRATION_ENV_ID);
        } finally {
            deleteEnvConfig(PANAMAX_ENV_CONF_ID);
        }
    }

    @Test
    public void testConfs() throws IOException {
        try {
            registerPanamaxAppConfig();

            runCLICommand("confs");
            String out = getConsoleOutput();
            assertTrue(out.contains(PANAMAX_ENV_CONF_ID));
        } finally {
            deleteEnvConfig(PANAMAX_ENV_CONF_ID);
        }
    }

    @Test
    public void testAddRemoteConf() throws IOException {
        String appConfId = null;
        try {
            runCLICommand("addconf", new String[]{PANAMAX_ENV_CONF_URL, "-f", "panamax"});
            assertEquals("", getConsoleErrorOutput());
            appConfId = getConsoleOutput().trim();
        } finally {
            if (appConfId != null) {
                deleteEnvConfig(appConfId);
            }
        }
    }

    @Test
    public void testAddAndStartRemoteConf() throws IOException, ResourceNotFoundException {
        String envConfId = null;
        try {
            runCLICommand("addconf", new String[]{PANAMAX_ENV_CONF_URL, "-f", "panamax", "--envs", INTEGRATION_ENV_ID});
            assertEquals("", getConsoleErrorOutput());
            envConfId = getConsoleOutput().trim();

            Environment env = getEnv(INTEGRATION_ENV_ID);
            assertNotNull(env);
            EnvironmentRuntimeInfo envInfo = env.getRuntimeInfo();
            assertNotNull(envInfo);
        } finally {
            deleteEnvironment(INTEGRATION_ENV_ID);
            if (envConfId != null) {
                deleteEnvConfig(envConfId);
            }
        }
    }
}
