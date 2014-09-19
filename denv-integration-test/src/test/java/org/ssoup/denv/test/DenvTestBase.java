package org.ssoup.denv.test;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.ssoup.denv.cli.DenvCLI;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.client.format.fig.FigConfigurationConverter;
import org.ssoup.denv.client.format.panamax.PanamaxConfigurationConverter;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfiguration;
import org.ssoup.denv.core.exception.DenvHttpException;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplicationImpl;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ALB
 * Date: 15/09/14 12:01
 */
public abstract class DenvTestBase {

    public static final String INTEGRATION_ENV_ID = "integration";

    public static final String[] FIG_ENV_LABELS = {"Fig", "Wordpress", "Integration"};
    public static final String FIG_APP_CONF_ID = "testApp";
    public static final String FIG_ENV_FILE_NAME = "fig/fig_app.yml";

    public static final String[] PANAMAX_ENV_LABELS = {"Panamax", "Wordpress", "Integration"};
    public static final String PANAMAX_APP_CONF_ID = "Wordpress with MySQL";
    public static final String PANAMAX_ENV_FILE_NAME = "panamax/panamax_app.pmx";

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

    protected void registerFigAppConfig() throws IOException, DenvHttpException {
        String appName = FIG_APP_CONF_ID;
        String figApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(FIG_ENV_FILE_NAME));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ContainerizedApplicationConfiguration applicationConfiguration = figDenvApplicationConfigurationConverter.convertApplicationConfiguration(appName, figApplicationConfiguration);
        denvClient.createContainerizedAppConfig(applicationConfiguration);
        PagedResources page = denvClient.listAppConfigs();
        assertNotNull(page);
        assertTrue(page.getMetadata().getTotalElements() > 0);
        //assertTrue(page.getContent().contains(FIG_APP_CONF_ID));
        ContainerizedApplicationConfiguration appConf = denvClient.getContainerizedAppConfig(FIG_APP_CONF_ID);
        assertNotNull(appConf);
        assertNotNull(appConf.getImages());
        assertEquals(4, appConf.getImages().size());
    }

    protected void registerPanamaxAppConfig() throws IOException, DenvHttpException {
        String panamaxApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(PANAMAX_ENV_FILE_NAME));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ContainerizedApplicationConfiguration applicationConfiguration = panamaxDenvApplicationConfigurationConverter.convertApplicationConfiguration(panamaxApplicationConfiguration);
        denvClient.createContainerizedAppConfig(applicationConfiguration);
        PagedResources page = denvClient.listAppConfigs();
        assertNotNull(page);
        assertTrue(page.getMetadata().getTotalElements() > 0);
        //assertTrue(page.getContent().contains(PANAMAX_APP_CONF_ID));
        ContainerizedApplicationConfiguration appConf = denvClient.getContainerizedAppConfig(PANAMAX_APP_CONF_ID);
        assertNotNull(appConf);
        assertNotNull(appConf.getImages());
        assertEquals(2, appConf.getImages().size());
    }

    protected void createEnvironment(String envId) {
        DenvEnvironment env = new DenvEnvironment(envId, null, null);
        ResponseEntity<String> createResponseEntity = denvClient.sendCreateEnvRequest(env);
        assertNotNull(createResponseEntity.getHeaders().getLocation());
    }

    protected void createEnvironment(String envId, String appConfId) {
        DenvEnvironment env = new DenvEnvironment(envId, createApp(appConfId), null);
        ResponseEntity<String> createResponseEntity = denvClient.sendCreateEnvRequest(env);
        assertNotNull(createResponseEntity.getHeaders().getLocation());
    }

    protected void updateEnvironment(String envId, String appConfId) {
        DenvEnvironment env = new DenvEnvironment(envId, createApp(appConfId), null);
        ResponseEntity<String> createPanamaxResponseEntity = denvClient.sendUpdateEnvRequest(env);
        assertNotNull(createPanamaxResponseEntity.getHeaders().getLocation());
    }

    protected PagedResources<DenvEnvironment> listEnvironments() throws DenvHttpException {
        return denvClient.listEnvs();
    }

    protected PagedResources<DenvEnvironment> listEnvironmentsAndCheckFor(String envId) throws DenvHttpException {
        PagedResources<DenvEnvironment> envs = listEnvironments();
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

    protected PagedResources<DenvEnvironment> listEnvironmentsShouldNotContain(String envId) throws DenvHttpException {
        PagedResources<DenvEnvironment> envs = listEnvironments();
        assertNotNull(envs);
        assertNotNull(envs.getContent());

        for (DenvEnvironment env : envs.getContent()) {
            if (envId.equals(env.getId())) {
                assertTrue("Environment " + envId + " was found in list", false);
            }
        }
        return envs;
    }

    protected void deleteEnvironment(String envId) throws DenvHttpException {
        denvClient.deleteEnv(envId);
    }

    protected void runCLICommand(String command) {
        denvCLI.run(new String[]{command});
    }

    protected void runCLICommand(String command, String... args) {
        List<String> cargs = new ArrayList<String>();
        cargs.add(command);
        cargs.addAll(Arrays.asList(args));
        denvCLI.run(cargs.toArray(new String[0]));
    }

    protected String getConsoleOutput() {
        return denvConsole.getLocalOutputStream().toString();
    }

    protected String getConsoleErrorOutput() {
        return denvConsole.getLocalErrorStream().toString();
    }

    private Collection<Application> createApp(String appConfId) {
        Application app = new ContainerizedApplicationImpl(appConfId, appConfId);
        app.setDeployed(true);
        app.setStarted(true);
        return Arrays.asList(app);
    }
}
