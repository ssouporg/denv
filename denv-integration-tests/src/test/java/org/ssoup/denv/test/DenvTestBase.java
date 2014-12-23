package org.ssoup.denv.test;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.PagedResources;
import org.ssoup.denv.cli.DenvCLI;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.client.format.fig.FigConfigurationConverter;
import org.ssoup.denv.client.format.panamax.PanamaxConfigurationConverter;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.ContainerizedEnvironmentRuntimeInfoImpl;
import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.exception.ResourceNotFoundException;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.Environment;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ALB
 * Date: 15/09/14 12:01
 */
public abstract class DenvTestBase {

    public static final String INTEGRATION_ENV_ID = "integration";

    public static final String[] FIG_ENV_LABELS = {"Fig", "Wordpress", "Integration"};
    public static final String FIG_ENV_CONF_ID = "figTestApp";
    public static final String FIG_ENV_FILE_NAME = "fig/fig_app.yml";

    public static final String[] PANAMAX_ENV_LABELS = {"Panamax", "Wordpress", "Integration"};
    public static final String PANAMAX_ENV_CONF_ID = "Wordpress_with_MySQL";
    public static final String PANAMAX_ENV_CONF_URL = "https://raw.githubusercontent.com/CenturyLinkLabs/panamax-public-templates/master/drupal_7.28_with_mysql.pmx";
    public static final String PANAMAX_ENV_FILE_NAME = "panamax/panamax_app.pmx";

    public static final long MAX_WAIT_FOR_DESIRED_STATE_IN_MILLIS = 120000;

    @Inject
    private DenvClient denvClient;

    @Inject
    private DenvCLI denvCLI;

    @Inject
    private DenvConsole denvConsole;

    @Inject
    private PanamaxConfigurationConverter panamaxDenvApplicationConfigurationConverter;

    @Inject
    private FigConfigurationConverter figDenvApplicationConfigurationConverter;

    @Before
    public void init() {
        denvConsole.setUseLocalStreams(true);
        MockitoAnnotations.initMocks(this);
    }

    protected void registerFigAppConfig() throws IOException {
        String figApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(FIG_ENV_FILE_NAME));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ContainerizedEnvironmentConfiguration applicationConfiguration = figDenvApplicationConfigurationConverter.convertEnvironmentConfiguration(FIG_ENV_CONF_ID, FIG_ENV_CONF_ID, null, figApplicationConfiguration);
        denvClient.createOrUpdateContainerizedEnvConfig(applicationConfiguration);
        PagedResources page = denvClient.listEnvConfigs();
        assertNotNull(page);
        assertTrue(page.getMetadata().getTotalElements() > 0);
        //assertTrue(page.getContent().contains(FIG_ENV_CONF_ID));
        ContainerizedEnvironmentConfiguration appConf = denvClient.getContainerizedEnvConfig(FIG_ENV_CONF_ID);
        assertNotNull(appConf);
        assertNotNull(appConf.getImages());
        assertEquals(4, appConf.getImages().size());
    }

    protected void registerPanamaxAppConfig() throws IOException {
        String panamaxApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(PANAMAX_ENV_FILE_NAME));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ContainerizedEnvironmentConfiguration envConf = panamaxDenvApplicationConfigurationConverter.convertEnvironmentConfiguration(panamaxApplicationConfiguration);
        denvClient.createOrUpdateContainerizedEnvConfig(envConf);
        PagedResources page = denvClient.listEnvConfigs();
        assertNotNull(page);
        assertTrue(page.getMetadata().getTotalElements() > 0);
        //assertTrue(page.getContent().contains(PANAMAX_ENV_CONF_ID));
        ContainerizedEnvironmentConfiguration appConf = denvClient.getContainerizedEnvConfig(PANAMAX_ENV_CONF_ID);
        assertNotNull(appConf);
        assertNotNull(appConf.getImages());
        assertEquals(2, appConf.getImages().size());
    }

    protected void deleteEnvConfig(String envConfId) {
        denvClient.deleteEnvConfig(envConfId);
    }

    protected PagedResources<DenvContainerizedEnvironment> listEnvironments() {
        return denvClient.listEnvironments();
    }

    protected PagedResources<DenvContainerizedEnvironment> listEnvironmentsAndCheckFor(String envId) {
        PagedResources<DenvContainerizedEnvironment> envs = listEnvironments();
        assertNotNull(envs);
        assertNotNull(envs.getContent());
        assertTrue(envs.getContent().size() > 0);
        assertTrue(envs.getMetadata().getTotalElements() > 0);

        for (DenvEnvironment env : envs.getContent()) {
            if (envId.equals(env.getId())) {
                return envs;
            }
        }
        assertTrue("Environment " + envId + " could not be found in list", false);
        return null;
    }

    protected PagedResources<DenvContainerizedEnvironment> listEnvironmentsShouldNotContain(String envId) {
        PagedResources<DenvContainerizedEnvironment> envs = listEnvironments();
        assertNotNull(envs);
        assertNotNull(envs.getContent());

        for (DenvEnvironment env : envs.getContent()) {
            if (envId.equals(env.getId())) {
                assertTrue("Environment " + envId + " was found in list", false);
            }
        }
        return envs;
    }

    protected Environment getEnv(String envId) throws ResourceNotFoundException {
        return denvClient.getEnv(envId);
    }

    protected void createEnvironment(String envId, String envConfId) throws DenvException {
        denvClient.createEnvironment(envId, envConfId);
        denvClient.waitForDesiredState(envId, MAX_WAIT_FOR_DESIRED_STATE_IN_MILLIS);
    }

    protected void updateEnvironment(String envId, String envConfId) throws ResourceNotFoundException {
        DenvEnvironment env = new DenvContainerizedEnvironment(envId, envId, envConfId, createRuntimeInfo(envConfId), null);
        denvClient.updateEnvironment(env);
    }

    protected void deleteEnvironment(String envId) {
        denvClient.deleteEnvironment(envId);
    }

    protected void runCLICommand(String command) {
        runCLICommand(new String[]{command});
    }

    protected void runCLICommand(String command, String... args) {
        List<String> cargs = new ArrayList<String>();
        cargs.add(command);
        cargs.addAll(Arrays.asList(args));
        runCLICommand(cargs.toArray(new String[0]));
    }

    protected void runCLICommand(String[] commandAndArgs) {
        denvCLI.run(commandAndArgs);
    }

    protected String getConsoleOutput() {
        return denvConsole.getLocalOutputStream().toString().trim();
    }

    protected String getConsoleErrorOutput() {
        return denvConsole.getLocalErrorStream().toString();
    }

    private ContainerizedEnvironmentRuntimeInfoImpl createRuntimeInfo(String envConfId) {
        return new ContainerizedEnvironmentRuntimeInfoImpl();
    }
}
