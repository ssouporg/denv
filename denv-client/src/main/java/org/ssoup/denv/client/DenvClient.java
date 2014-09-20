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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.ssoup.denv.core.api.DenvApiEndpoints;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfiguration;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfigurationImpl;
import org.ssoup.denv.core.exception.ResourceNotFoundException;
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

    public String createOrUpdateContainerizedAppConfig(ContainerizedApplicationConfiguration appConf) {
        ResponseEntity<String> res = sendCreateOrUpdateContainerizedAppConfigRequest(appConf);
        String location = res.getHeaders().getLocation().getPath();
        // location will be something like: http://localhost:8080/applicationConfigs/{appConfId}
        String appConfId = location.substring(location.lastIndexOf('/') + 1);
        return appConfId;
    }

    public ResponseEntity<String> sendCreateOrUpdateContainerizedAppConfigRequest(ContainerizedApplicationConfiguration appConf) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS,
                HttpMethod.POST,
                new HttpEntity<ApplicationConfigurationImpl>(new ContainerizedApplicationConfigurationImpl(appConf), defaultRequestHeaders()),
                String.class
        );
    }

    public PagedResources<ContainerizedApplicationConfigurationImpl> listAppConfigs() {
        ResponseEntity<PagedResources<ContainerizedApplicationConfigurationImpl>> res = sendGetAppConfigsListRequest();
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

    public ContainerizedApplicationConfiguration getContainerizedAppConfig(String appConfId) {
        ResponseEntity<Resource<ContainerizedApplicationConfigurationImpl>> res = sendGetContainerizedAppConfigRequest(appConfId);
        return res.getBody().getContent();
    }

    public ResponseEntity<Resource<ContainerizedApplicationConfigurationImpl>> sendGetContainerizedAppConfigRequest(String appConfId) {
        ParameterizedTypeReference<Resource<ContainerizedApplicationConfigurationImpl>> parameterizedTypeReference =
                new ParameterizedTypeReference<Resource<ContainerizedApplicationConfigurationImpl>>() {};
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS + "/{appConfId}",
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference,
                appConfId
        );
    }

    public void deleteAppConfig(String appConfId) {
        ResponseEntity<Void> res = sendDeleteAppConfigRequest(appConfId);
    }

    public ResponseEntity<Void> sendDeleteAppConfigRequest(String appConfId) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.APPS_CONFIGS + "/{appConfId}",
                HttpMethod.DELETE,
                new HttpEntity<Void>(defaultRequestHeaders()),
                Void.class,
                appConfId
        );
    }

    public String createEnv(DenvEnvironment env) {
        ResponseEntity<String> res = sendCreateEnvRequest(env);
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

    public DenvEnvironment getEnv(String envId) throws ResourceNotFoundException {
        try {
            ResponseEntity<Resource<DenvEnvironment>> res = sendGetEnvRequest(envId);
            return res.getBody().getContent();
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Environment " + envId + " does not exist");
            }
            throw ex;
        }
    }

    public ResponseEntity<Resource<DenvEnvironment>> sendGetEnvRequest(String envId) {
        ParameterizedTypeReference<Resource<DenvEnvironment>> parameterizedTypeReference =
                new ParameterizedTypeReference<Resource<DenvEnvironment>>() {};
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS  + "/{envId}",
                HttpMethod.GET,
                new HttpEntity<String>(envId, defaultRequestHeaders()),
                parameterizedTypeReference,
                envId
        );
    }

    public String updateEnv(DenvEnvironment env) throws ResourceNotFoundException {
        // the PUT method will succeed even if the resource does not exist,so we check for its existence first
        // getEnv will throw ResourceNotFoundException if the environment does not exist
        getEnv(env.getId());

        ResponseEntity<String> res = sendUpdateEnvRequest(env);
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

    public PagedResources<DenvEnvironment> listEnvs() {
        ResponseEntity<PagedResources<DenvEnvironment>> res = sendListEnvsRequest();
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

    public void deleteEnv(String envId) {
        ResponseEntity<Void> res = sendDeleteEnvRequest(envId);
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
