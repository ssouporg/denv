package org.ssoup.denv.server.test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.ssoup.denv.server.Denv;

/**
 * User: ALB
 * Date: 20/08/14 09:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Denv.class)
@WebAppConfiguration
@IntegrationTest
public abstract class AbstractDenvTest {

    private Logger logger = LoggerFactory.getLogger(AbstractDenvTest.class);

    private RestTemplate restTemplate = new TestRestTemplate();

    private static final String BASE_URL = "http://localhost:8080/api/v1";

    private HttpHeaders defaultRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/json");
        headers.add("Accept", "application/json");
        return headers;
    }

    protected ResponseEntity<String> sendCreateEnvRequest(String envConf) {
        return restTemplate.exchange(BASE_URL + "/envs", HttpMethod.POST, new HttpEntity<String>(envConf, defaultRequestHeaders()), String.class);
    }

    protected ResponseEntity<String> sendListEnvsRequest() {
        return restTemplate.exchange(BASE_URL + "/envs", HttpMethod.GET, new HttpEntity<Void>(defaultRequestHeaders()), String.class);
    }
}
