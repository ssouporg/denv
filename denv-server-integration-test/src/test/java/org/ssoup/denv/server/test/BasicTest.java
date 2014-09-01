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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.environment.DenvEnvironmentConfiguration;
import org.ssoup.denv.Denv;

import javax.inject.Inject;
import java.io.IOException;
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
public class BasicTest {

    private Logger logger = LoggerFactory.getLogger(BasicTest.class);

    public static final String FIG_APP_CONF_NAME = "testApp";
    public static final String PANAMAX_APP_CONF_NAME = "Wordpress with MySQL";

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
        String figApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("fig_app.yml"));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ResponseEntity<String> createResponseEntity = denvClient.sendCreateAppsConfigsFigRequest(appName, figApplicationConfiguration);
        ResponseEntity<Collection> listResponseEntity = denvClient.sendGetAppsConfigsListRequest();
        assertNotNull(listResponseEntity.getBody());
        assertTrue(listResponseEntity.getBody().contains(FIG_APP_CONF_NAME));
        ResponseEntity<ApplicationConfiguration> appConfResponseEntity = denvClient.sendGetAppsConfigsRequest(FIG_APP_CONF_NAME);
        ApplicationConfiguration appConf = appConfResponseEntity.getBody();
        assertNotNull(appConf);
        assertNotNull(appConf.getImages());
        assertEquals(4, appConf.getImages().size());
    }

    @Test
    public void registerPanamaxApp() throws IOException {
        String panamaxApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("panamax_app.pmx"));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ResponseEntity<String> createResponseEntity = denvClient.sendCreateAppsConfigsPanamaxRequest(panamaxApplicationConfiguration);
        ResponseEntity<Collection> listResponseEntity = denvClient.sendGetAppsConfigsListRequest();
        assertNotNull(listResponseEntity.getBody());
        assertTrue(listResponseEntity.getBody().contains(PANAMAX_APP_CONF_NAME));
        ResponseEntity<ApplicationConfiguration> appConfResponseEntity = denvClient.sendGetAppsConfigsRequest(PANAMAX_APP_CONF_NAME);
        ApplicationConfiguration appConf = appConfResponseEntity.getBody();
        assertNotNull(appConf);
        assertNotNull(appConf.getImages());
        assertEquals(2, appConf.getImages().size());
    }

    @Test
    public void runFigApp() throws IOException {
        registerFigApp();

        String envId = null;
        try {
            DenvEnvironmentConfiguration envConf = new DenvEnvironmentConfiguration(new String[]{}, FIG_APP_CONF_NAME);
            ResponseEntity<String> createResponseEntity = denvClient.sendCreateEnvRequest(envConf);
            envId = createResponseEntity.getBody();
            assertNotNull(envId);
        } finally {
            if (envId != null) {
                denvClient.sendDeleteEnvRequest(envId);
            }
        }
    }

    @Test
    public void runPanamaxApp() throws IOException {
        registerPanamaxApp();

        String envId = null;
        try {
            DenvEnvironmentConfiguration envConf = new DenvEnvironmentConfiguration(new String[]{}, PANAMAX_APP_CONF_NAME);
            ResponseEntity<String> createResponseEntity = denvClient.sendCreateEnvRequest(envConf);
            envId = createResponseEntity.getBody();
            assertNotNull(envId);
        } finally {
            if (envId != null) {
                denvClient.sendDeleteEnvRequest(envId);
            }
        }
    }

    @Test
    public void runFigAndPanamaxApps() throws IOException {
        registerFigApp();
        registerPanamaxApp();

        String figEnvId = null, panamaxEnvId = null;
        try {
            DenvEnvironmentConfiguration figEnvConf = new DenvEnvironmentConfiguration(new String[]{}, FIG_APP_CONF_NAME);
            ResponseEntity<String> createFigResponseEntity = denvClient.sendCreateEnvRequest(figEnvConf);
            figEnvId = createFigResponseEntity.getBody();
            assertNotNull(figEnvId);

            DenvEnvironmentConfiguration panamaxEnvConf = new DenvEnvironmentConfiguration(new String[]{}, PANAMAX_APP_CONF_NAME);
            ResponseEntity<String> createPanamaxResponseEntity = denvClient.sendCreateEnvRequest(panamaxEnvConf);
            panamaxEnvId = createPanamaxResponseEntity.getBody();
            assertNotNull(panamaxEnvId);
        } finally {
            if (figEnvId != null) {
                denvClient.sendDeleteEnvRequest(figEnvId);
            }
            if (panamaxEnvId != null) {
                denvClient.sendDeleteEnvRequest(panamaxEnvId);
            }
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
