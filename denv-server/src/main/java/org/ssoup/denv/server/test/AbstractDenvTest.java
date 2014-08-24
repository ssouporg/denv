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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.ssoup.denv.server.Denv;
import org.ssoup.denv.server.api.converter.CollectionMessageConverter;
import org.ssoup.denv.server.api.converter.DenvMessageConverter;
import org.ssoup.denv.server.api.v1.AppsConfigsController;
import org.ssoup.denv.server.api.v1.EnvsController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    protected static final String BASE_URL = "http://localhost:8080";

    private RestTemplate restTemplate;

    public AbstractDenvTest() {
        restTemplate = new TestRestTemplate();
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new DenvMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new CollectionMessageConverter());
        restTemplate.setMessageConverters(converters);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
    }

    protected HttpHeaders defaultRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-yaml");
        headers.add("Accept", "application/x-yaml");
        return headers;
    }

    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    protected ResponseEntity<String> sendCreateAppsConfigsDenvRequest(String appConf) {
        return restTemplate.exchange(BASE_URL + AppsConfigsController.PATH, HttpMethod.POST, new HttpEntity<String>(appConf, defaultRequestHeaders()), String.class);
    }

    protected ResponseEntity<Collection> sendGetAppsConfigsListRequest() {
        try {
        return restTemplate.exchange(BASE_URL + AppsConfigsController.PATH, HttpMethod.GET, new HttpEntity<Void>(defaultRequestHeaders()), Collection.class);
        }catch(HttpClientErrorException ex) {
            logger.error(ex.getResponseBodyAsString());
            ex.printStackTrace();
            throw ex;
        }
    }

    protected ResponseEntity<String> sendGetAppsConfigsRequest(String appConfName) {
        return restTemplate.exchange(BASE_URL + AppsConfigsController.PATH + "/" + appConfName, HttpMethod.GET, new HttpEntity<Void>(defaultRequestHeaders()), String.class);
    }

    protected ResponseEntity<String> sendCreateEnvRequest(String envConf) {
        return restTemplate.exchange(BASE_URL + EnvsController.PATH, HttpMethod.POST, new HttpEntity<String>(envConf, defaultRequestHeaders()), String.class);
    }

    protected ResponseEntity<String> sendListEnvsRequest() {
        return restTemplate.exchange(BASE_URL + EnvsController.PATH, HttpMethod.GET, new HttpEntity<Void>(defaultRequestHeaders()), String.class);
    }
}
