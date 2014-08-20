package org.ssoup.denv.server.test;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfigurationImpl;

import java.io.IOException;

import static org.junit.Assert.*;

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
    public void runEnvironment() throws IOException {
        String appName = "appName";
        String figEnvironmentConfiguration = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("app1.yml"));
        ApplicationConfiguration appConf = new ApplicationConfigurationImpl(appName);
        // when(adminClient.deployApplication(appName, appConf)).thenReturn();
        ResponseEntity<String> createResponseEntity = sendCreateEnvRequest(figEnvironmentConfiguration);
        ResponseEntity<String> listResponseEntity = sendListEnvsRequest();
        assertNotNull(listResponseEntity);
    }
}
