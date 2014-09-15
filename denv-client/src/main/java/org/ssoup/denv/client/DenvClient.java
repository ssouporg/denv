package org.ssoup.denv.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.ssoup.denv.core.api.DenvApiEndpoints;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfiguration;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfigurationImpl;
import org.ssoup.denv.core.exception.DenvHttpException;
import org.ssoup.denv.core.model.conf.application.ApplicationConfigurationImpl;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * User: ALB
 * Date: 28/08/14 14:38
 */
@Service
public class DenvClient {

    private Logger logger = LoggerFactory.getLogger(DenvClient.class);

    @Value("${DENV_SERVER_URL}")
    private String baseUrl;

    //private RestTemplate restTemplate;

    private RestTemplate hateoasRestTemplate;

    private MediaType HATEOAS_MEDIA_TYPE;

    public DenvClient() {
    }

    @PostConstruct
    public void init() {
        HATEOAS_MEDIA_TYPE = MediaType.parseMediaType("application/hal+json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(HATEOAS_MEDIA_TYPE));
        converter.setObjectMapper(mapper);

        hateoasRestTemplate = new RestTemplate(Arrays.<HttpMessageConverter<?>> asList(converter));
        hateoasRestTemplate.setErrorHandler(new DefaultResponseErrorHandler());

        //restTemplate = new RestTemplate(Arrays.<HttpMessageConverter<?>> asList(new MappingJackson2HttpMessageConverter()));
        //restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
    }

    protected HttpHeaders defaultRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", HATEOAS_MEDIA_TYPE.toString());
        headers.add("Accept", HATEOAS_MEDIA_TYPE.toString());
        //headers.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
        // headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    public String createContainerizedAppConfig(ContainerizedApplicationConfiguration appConf) throws DenvHttpException {
        ResponseEntity<String> res = sendCreateContainerizedAppConfigRequest(appConf);
        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new DenvHttpException(res);
        }
        return res.getBody();
    }

    public ResponseEntity<String> sendCreateContainerizedAppConfigRequest(ContainerizedApplicationConfiguration appConf) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS,
                HttpMethod.POST,
                new HttpEntity<ApplicationConfigurationImpl>(new ContainerizedApplicationConfigurationImpl(appConf), defaultRequestHeaders()),
                String.class
        );
    }

    public PagedResources<ContainerizedApplicationConfigurationImpl> listAppConfigs() throws DenvHttpException {
        ResponseEntity<PagedResources<ContainerizedApplicationConfigurationImpl>> res = sendGetAppConfigsListRequest();
        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new DenvHttpException(res);
        }
        return res.getBody();
    }

    public ResponseEntity<PagedResources<ContainerizedApplicationConfigurationImpl>> sendGetAppConfigsListRequest() {
        ParameterizedTypeReference<PagedResources<ContainerizedApplicationConfigurationImpl>> parameterizedTypeReference =
                new ParameterizedTypeReference<PagedResources<ContainerizedApplicationConfigurationImpl>>() {};
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS,
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference
        );
    }

    public ContainerizedApplicationConfiguration getContainerizedAppConfig(String appConfId) throws DenvHttpException {
        ResponseEntity<Resource<ContainerizedApplicationConfigurationImpl>> res = sendGetContainerizedAppConfigRequest(appConfId);
        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new DenvHttpException(res);
        }
        return res.getBody().getContent();
    }

    public ResponseEntity<Resource<ContainerizedApplicationConfigurationImpl>> sendGetContainerizedAppConfigRequest(String appConfId) {
        ParameterizedTypeReference<Resource<ContainerizedApplicationConfigurationImpl>> parameterizedTypeReference =
                new ParameterizedTypeReference<Resource<ContainerizedApplicationConfigurationImpl>>() {};
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS + "/{appConfName}",
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference,
                appConfId
        );
    }

    public String createEnv(DenvEnvironment env) throws DenvHttpException {
        ResponseEntity<String> res = sendCreateEnvRequest(env);
        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new DenvHttpException(res);
        }
        return res.getBody();
    }

    public ResponseEntity<String> sendCreateEnvRequest(DenvEnvironment env) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS,
                HttpMethod.POST,
                new HttpEntity<DenvEnvironment>(env, defaultRequestHeaders()),
                String.class
        );
    }

    public String updateEnv(DenvEnvironment env) throws DenvHttpException {
        ResponseEntity<String> res = sendUpdateEnvRequest(env);
        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new DenvHttpException(res);
        }
        return res.getBody();
    }

    public ResponseEntity<String> sendUpdateEnvRequest(DenvEnvironment env) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS + "/{envId}",
                HttpMethod.PUT,
                new HttpEntity<DenvEnvironment>(env, defaultRequestHeaders()),
                String.class,
                env.getId()
        );
    }

    public PagedResources<DenvEnvironment> listEnvs() throws DenvHttpException {
        ResponseEntity<PagedResources<DenvEnvironment>> res = sendListEnvsRequest();
        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new DenvHttpException(res);
        }
        return res.getBody();
    }

    public ResponseEntity<PagedResources<DenvEnvironment>> sendListEnvsRequest() {
        ParameterizedTypeReference<PagedResources<DenvEnvironment>> parameterizedTypeReference =
                new ParameterizedTypeReference<PagedResources<DenvEnvironment>>() {};
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS,
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference
        );
    }

    public void deleteEnv(String envId) throws DenvHttpException {
        ResponseEntity<Void> res = sendDeleteEnvRequest(envId);
        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new DenvHttpException(res);
        }
    }

    public ResponseEntity<Void> sendDeleteEnvRequest(String envId) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS + "/{envId}",
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
