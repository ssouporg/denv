package org.ssoup.denv.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.ssoup.denv.common.api.DenvApiEndpoints;
import org.ssoup.denv.common.converter.DenvConverterManager;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.config.application.InMemoryDenvApplicationConfiguration;
import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;

import javax.annotation.PostConstruct;

/**
 * User: ALB
 * Date: 28/08/14 14:38
 */
@Service
public class DenvClient {

    private Logger logger = LoggerFactory.getLogger(DenvClient.class);

    private String baseUrl;

    private RestTemplate restTemplate;

    private DenvConverterManager denvConverterManager;

    @Autowired
    public DenvClient(DenvConverterManager denvConverterManager) {
        this.denvConverterManager = denvConverterManager;
    }

    @PostConstruct
    public void init() {
        restTemplate = new TestRestTemplate();
        /*List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        denvConverterManager.addConverters(converters);
        restTemplate.setMessageConverters(converters);*/
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
    }

    protected HttpHeaders defaultRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
        // headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public ResponseEntity<String> sendCreateAppConfigRequest(ApplicationConfiguration appConf) {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS,
                HttpMethod.POST,
                new HttpEntity<ApplicationConfiguration>(appConf, defaultRequestHeaders()),
                String.class
        );
    }

    public ResponseEntity<PagedResources> sendGetAppConfigsListRequest() {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS,
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                PagedResources.class
        );
    }

    public ResponseEntity<Resource<InMemoryDenvApplicationConfiguration>> sendGetAppConfigRequest(String appConfName) {
        ParameterizedTypeReference<Resource<InMemoryDenvApplicationConfiguration>> parameterizedTypeReference =
                new ParameterizedTypeReference<Resource<InMemoryDenvApplicationConfiguration>>() {};
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS + "/{appConfName}",
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference,
                appConfName
        );
    }

    public ResponseEntity<String> sendCreateEnvConfigRequest(EnvironmentConfiguration environmentConfiguration) {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS_CONFIGS,
                HttpMethod.POST,
                new HttpEntity<EnvironmentConfiguration>(environmentConfiguration, defaultRequestHeaders()),
                String.class
        );
    }

    public ResponseEntity<String> sendListEnvConfigsRequest() {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS_CONFIGS,
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                String.class
        );
    }

    public ResponseEntity<Void> sendDeleteEnvConfigRequest(String envId) {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS_CONFIGS + "/{envId}",
                HttpMethod.DELETE,
                new HttpEntity<Void>(defaultRequestHeaders()),
                Void.class,
                envId
        );
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
