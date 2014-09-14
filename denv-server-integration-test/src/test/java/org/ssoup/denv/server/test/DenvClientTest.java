package org.ssoup.denv.server.test;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.ssoup.denv.Denv;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfiguration;
import org.ssoup.denv.core.exception.DenvHttpException;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplicationImpl;
import org.ssoup.denv.server.fig.service.FigDenvApplicationConfigurationConverter;
import org.ssoup.denv.server.panamax.service.PanamaxDenvApplicationConfigurationConverter;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * User: ALB
 * Date: 20/08/14 09:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Denv.class)
@WebAppConfiguration
@IntegrationTest
public class DenvClientTest {

    private Logger logger = LoggerFactory.getLogger(DenvClientTest.class);

    public static final String INTEGRATION_ENV_ID = "integration";

    public static final String[] FIG_ENV_LABELS = {"Fig", "Wordpress", "Integration"};
    public static final String FIG_APP_CONF_ID = "testApp";
    public static final String FIG_ENV_FILE_NAME = "fig/fig_app.yml";

    public static final String[] PANAMAX_ENV_LABELS = {"Panamax", "Wordpress", "Integration"};
    public static final String PANAMAX_APP_CONF_ID = "Wordpress with MySQL";
    public static final String PANAMAX_ENV_FILE_NAME = "panamax/panamax_app.pmx";

    @Inject
    private PanamaxDenvApplicationConfigurationConverter panamaxDenvApplicationConfigurationConverter;

    @Inject
    private FigDenvApplicationConfigurationConverter figDenvApplicationConfigurationConverter;

    @Inject
    private DenvClient denvClient;

/*    @Mock
    private AdminClient adminClient;

    @InjectMocks
    private DockerImageManager dockerImageManager;*/

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerFigApp() throws IOException, DenvHttpException {
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

    @Test
    public void registerPanamaxApp() throws IOException, DenvHttpException {
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

    @Test
    public void runFigApp() throws Exception {
        registerFigApp();

        try {
            DenvEnvironment env = new DenvEnvironment(INTEGRATION_ENV_ID, createApp(FIG_APP_CONF_ID), null);
            ResponseEntity<String> createResponseEntity = denvClient.sendCreateEnvRequest(env);
            createResponseEntity.getBody();
            assertNotNull(createResponseEntity.getHeaders().getLocation());
        } finally {
            denvClient.deleteEnv(INTEGRATION_ENV_ID);
        }
    }

    @Test
    public void runPanamaxApp() throws Exception {
        registerPanamaxApp();

        try {
            DenvEnvironment env =
                    new DenvEnvironment(INTEGRATION_ENV_ID, createApp(PANAMAX_APP_CONF_ID), null);
            ResponseEntity<String> createResponseEntity = denvClient.sendCreateEnvRequest(env);
            assertNotNull(createResponseEntity.getHeaders().getLocation());
            PagedResources envs = denvClient.listEnvs();
            assertNotNull(envs);
            assertTrue(envs.getMetadata().getTotalElements() > 0);
        } finally {
            denvClient.deleteEnv(INTEGRATION_ENV_ID);
        }
    }

    @Test
    public void runFigAndPanamaxApps() throws Exception {
        registerFigApp();
        registerPanamaxApp();

        try {
            DenvEnvironment figEnv = new DenvEnvironment(INTEGRATION_ENV_ID, createApp(FIG_APP_CONF_ID), null);
            ResponseEntity<String> createFigResponseEntity = denvClient.sendCreateEnvRequest(figEnv);
            assertNotNull(createFigResponseEntity.getHeaders().getLocation());

            DenvEnvironment panamaxEnv = new DenvEnvironment(
                    INTEGRATION_ENV_ID, createApp(PANAMAX_APP_CONF_ID), null);
            ResponseEntity<String> createPanamaxResponseEntity = denvClient.sendCreateEnvRequest(panamaxEnv);
            assertNotNull(createPanamaxResponseEntity.getHeaders().getLocation());
        } finally {
            //denvClient.sendDeleteEnvConfigRequest(FIG_ENV_CONF_ID);
            //denvClient.sendDeleteEnvConfigRequest(PANAMAX_ENV_CONF_ID);
        }
    }

    private Collection<Application> createApp(String appConfId) {
        Application app = new ContainerizedApplicationImpl(appConfId, appConfId);
        app.setDeployed(true);
        app.setStarted(true);
        return Arrays.asList(app);
    }
}
