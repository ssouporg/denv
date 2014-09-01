package org.ssoup.denv.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.ssoup.denv.common.api.DenvApiEndpoints;
import org.ssoup.denv.common.converter.DenvConverterManager;
import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.environment.EnvironmentConfiguration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        denvConverterManager.addConverters(converters);
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

    public ResponseEntity<String> sendCreateAppsConfigsDenvRequest(String appConf) {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS,
                HttpMethod.POST,
                new HttpEntity<String>(appConf, defaultRequestHeaders()),
                String.class
        );
    }

    public ResponseEntity<Collection> sendGetAppsConfigsListRequest() {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS,
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                Collection.class
        );
    }

    public ResponseEntity<ApplicationConfiguration> sendGetAppsConfigsRequest(String appConfName) {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS + "/{appConfName}",
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                ApplicationConfiguration.class,
                appConfName
        );
    }

    public ResponseEntity<String> sendCreateEnvRequest(EnvironmentConfiguration environmentConfiguration) {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS,
                HttpMethod.POST,
                new HttpEntity<EnvironmentConfiguration>(environmentConfiguration, defaultRequestHeaders()),
                String.class
        );
    }

    public ResponseEntity<String> sendListEnvsRequest() {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS,
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                String.class
        );
    }

    public ResponseEntity<Void> sendDeleteEnvRequest(String envId) {
        return restTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS + "/{envId}",
                HttpMethod.DELETE,
                new HttpEntity<Void>(defaultRequestHeaders()),
                Void.class,
                envId
        );
    }

    // FIG and PANAMAX api

    public ResponseEntity<String> sendCreateAppsConfigsFigRequest(String appConfigName, String appConf) {
        return getRestTemplate().exchange(
                getBaseUrl() + DenvApiEndpoints.FIG_APPS_CONFIGS + "/{appConfigName}",
                HttpMethod.POST,
                new HttpEntity<String>(appConf, defaultRequestHeaders()),
                String.class,
                appConfigName
        );
    }

    public ResponseEntity<String> sendCreateAppsConfigsPanamaxRequest(String appConf) {
        return getRestTemplate().exchange(
                getBaseUrl() + DenvApiEndpoints.PANAMAX_APPS_CONFIGS,
                HttpMethod.POST,
                new HttpEntity<String>(appConf, defaultRequestHeaders()),
                String.class
        );
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
