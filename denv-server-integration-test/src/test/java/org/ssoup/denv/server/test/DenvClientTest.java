package org.ssoup.denv.server.test;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.ssoup.denv.Denv;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.config.application.InMemoryDenvApplicationConfiguration;
import org.ssoup.denv.common.model.config.environment.InMemoryDenvEnvironmentConfiguration;
import org.ssoup.denv.server.fig.service.FigDenvApplicationConfigurationConverter;
import org.ssoup.denv.server.panamax.service.PanamaxDenvApplicationConfigurationConverter;

import javax.inject.Inject;
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
public class DenvClientTest {

    private Logger logger = LoggerFactory.getLogger(DenvClientTest.class);

    public static final String FIG_ENV_CONF_ID = "Wordpress Fig - integration";
    public static final String[] FIG_ENV_CONF_LABELS = {"Fig", "Wordpress", "Integration"};
    public static final String FIG_APP_CONF_NAME = "testApp";
    public static final String FIG_ENV_FILE_NAME = "fig/fig_app.yml";

    public static final String PANAMAX_ENV_CONF_ID = "Wordpress Panamax - integration";
    public static final String[] PANAMAX_ENV_CONF_LABELS = {"Panamax", "Wordpress", "Integration"};
    public static final String PANAMAX_APP_CONF_NAME = "Wordpress with MySQL";
    public static final String PANAMAX_ENV_FILE_NAME = "panamax/panamax_app.pmx";

    @Inject
    private PanamaxDenvApplicationConfigurationConverter panamaxDenvApplicationConfigurationConverter;

    @Inject
    private FigDenvApplicationConfigurationConverter figDenvApplicationConfigurationConverter;

    @Inject
    private DenvClient denvClient;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private int serverPort;

/*    @Mock
    private AdminClient adminClient;

    @InjectMocks
    private DockerImageManager dockerImageManager;*/

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        denvClient.setBaseUrl("http://" + serverAddress + ":" + serverPort);
    }

    @Test
    public void registerFigApp() throws IOException {
        String appName = FIG_APP_CONF_NAME;
        String figApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(FIG_ENV_FILE_NAME));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ApplicationConfiguration applicationConfiguration = figDenvApplicationConfigurationConverter.convertApplicationConfiguration(appName, figApplicationConfiguration);
        ResponseEntity<String> createResponseEntity = denvClient.sendCreateAppConfigRequest(applicationConfiguration);
        ResponseEntity<PagedResources> listResponseEntity = denvClient.sendGetAppConfigsListRequest();
        PagedResources page = listResponseEntity.getBody();
        assertNotNull(page);
        assertTrue(page.getMetadata().getTotalElements() > 0);
        //assertTrue(page.getContent().contains(FIG_APP_CONF_NAME));
        ResponseEntity<Resource<InMemoryDenvApplicationConfiguration>> appConfResponseEntity =
                denvClient.sendGetAppConfigRequest(FIG_APP_CONF_NAME);
        ApplicationConfiguration appConf = (ApplicationConfiguration)appConfResponseEntity.getBody().getContent();
        assertNotNull(appConf);
        assertNotNull(appConf.getImages());
        assertEquals(4, appConf.getImages().size());
    }

    @Test
    public void registerPanamaxApp() throws IOException {
        String panamaxApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(PANAMAX_ENV_FILE_NAME));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ApplicationConfiguration applicationConfiguration = panamaxDenvApplicationConfigurationConverter.convertApplicationConfiguration(panamaxApplicationConfiguration);
        ResponseEntity<String> createResponseEntity = denvClient.sendCreateAppConfigRequest(applicationConfiguration);
        ResponseEntity<PagedResources> listResponseEntity = denvClient.sendGetAppConfigsListRequest();
        PagedResources page = listResponseEntity.getBody();
        assertNotNull(page);
        assertTrue(page.getMetadata().getTotalElements() > 0);
        //assertTrue(page.getContent().contains(PANAMAX_APP_CONF_NAME));
        ResponseEntity<Resource<InMemoryDenvApplicationConfiguration>> appConfResponseEntity =
                denvClient.sendGetAppConfigRequest(PANAMAX_APP_CONF_NAME);
        ApplicationConfiguration appConf = (ApplicationConfiguration)appConfResponseEntity.getBody().getContent();
        assertNotNull(appConf);
        assertNotNull(appConf.getImages());
        assertEquals(2, appConf.getImages().size());
    }

    @Test
    public void runFigApp() throws IOException {
        registerFigApp();

        try {
            InMemoryDenvEnvironmentConfiguration envConf = new InMemoryDenvEnvironmentConfiguration(
                    FIG_ENV_CONF_ID, FIG_ENV_CONF_LABELS, FIG_APP_CONF_NAME);
            ResponseEntity<String> createResponseEntity = denvClient.sendCreateEnvConfigRequest(envConf);
            createResponseEntity.getBody();
            assertNotNull(createResponseEntity.getHeaders().getLocation());
        } finally {
            denvClient.sendDeleteEnvConfigRequest(FIG_ENV_CONF_ID);
        }
    }

    @Test
    public void runPanamaxApp() throws IOException {
        registerPanamaxApp();

        try {
            InMemoryDenvEnvironmentConfiguration envConf =
                    new InMemoryDenvEnvironmentConfiguration(
                            PANAMAX_ENV_CONF_ID, PANAMAX_ENV_CONF_LABELS, PANAMAX_APP_CONF_NAME);
            ResponseEntity<String> createResponseEntity = denvClient.sendCreateEnvConfigRequest(envConf);
            assertNotNull(createResponseEntity.getHeaders().getLocation());
        } finally {
            denvClient.sendDeleteEnvConfigRequest(PANAMAX_ENV_CONF_ID);
        }
    }

    @Test
    public void runFigAndPanamaxApps() throws IOException {
        registerFigApp();
        registerPanamaxApp();

        try {
            InMemoryDenvEnvironmentConfiguration figEnvConf = new InMemoryDenvEnvironmentConfiguration(
                    FIG_ENV_CONF_ID, FIG_ENV_CONF_LABELS, FIG_APP_CONF_NAME);
            ResponseEntity<String> createFigResponseEntity = denvClient.sendCreateEnvConfigRequest(figEnvConf);
            assertNotNull(createFigResponseEntity.getHeaders().getLocation());

            InMemoryDenvEnvironmentConfiguration panamaxEnvConf = new InMemoryDenvEnvironmentConfiguration(
                    PANAMAX_ENV_CONF_ID, PANAMAX_ENV_CONF_LABELS, PANAMAX_APP_CONF_NAME);
            ResponseEntity<String> createPanamaxResponseEntity = denvClient.sendCreateEnvConfigRequest(panamaxEnvConf);
            assertNotNull(createPanamaxResponseEntity.getHeaders().getLocation());
        } finally {
            //denvClient.sendDeleteEnvConfigRequest(FIG_ENV_CONF_ID);
            //denvClient.sendDeleteEnvConfigRequest(PANAMAX_ENV_CONF_ID);
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
