package org.ssoup.denv.server.test;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.ssoup.denv.server.fig.api.AppsConfigsFigController;
import org.ssoup.denv.server.panamax.api.AppsConfigsPanamaxController;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

/**
 * User: ALB
 * Date: 20/08/14 09:33
 */

public class BasicTest extends AbstractDenvTest {

    private Logger logger = LoggerFactory.getLogger(BasicTest.class);

/*    @Mock
    private AdminClient adminClient;

    @InjectMocks
    private DockerImageManager dockerImageManager;*/

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void runFigApp() throws IOException {
        String appName = "appName";
        String figApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("app1.yml"));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ResponseEntity<String> createResponseEntity = sendCreateAppsConfigsFigRequest("testApp", figApplicationConfiguration);
        ResponseEntity<Collection> listResponseEntity = sendGetAppsConfigsListRequest();
        assertNotNull(listResponseEntity);
    }

    @Test
    public void runPanamaxApp() throws IOException {
        String appName = "appName";
        String panamaxApplicationConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("wordpress.pmx"));
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ResponseEntity<String> createResponseEntity = sendCreateAppsConfigsPanamaxRequest(panamaxApplicationConfiguration);
        ResponseEntity<Collection> listResponseEntity = sendGetAppsConfigsListRequest();
        assertNotNull(listResponseEntity);
    }

    protected ResponseEntity<String> sendCreateAppsConfigsFigRequest(String appConfigName, String appConf) {
        return getRestTemplate().exchange(AbstractDenvTest.BASE_URL + AppsConfigsFigController.PATH + "/" + appConfigName, HttpMethod.POST, new HttpEntity<String>(appConf, defaultRequestHeaders()), String.class);
    }

    protected ResponseEntity<String> sendCreateAppsConfigsPanamaxRequest(String appConf) {
        return getRestTemplate().exchange(AbstractDenvTest.BASE_URL + AppsConfigsPanamaxController.PATH, HttpMethod.POST, new HttpEntity<String>(appConf, defaultRequestHeaders()), String.class);
    }
}
