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
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfigurationImpl;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.*;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.exception.DesiredStateNotReachedException;
import org.ssoup.denv.core.exception.ResourceNotFoundException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigVersionState;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationImpl;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersionImpl;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentState;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * User: ALB
 * Date: 28/08/14 14:38
 */
@Service
public class DenvClient {

    public static final long CHECK_STATE_INTERVAL_IN_MILLIS = 3000;
    public static final long CHECK_VERSION_STATE_INTERVAL_IN_MILLIS = 3000;

    private Logger logger = LoggerFactory.getLogger(DenvClient.class);

    @Value("${DENV_SERVER_URL}")
    private String baseUrl;

    //private RestTemplate restTemplate;

    private RestTemplate hateoasRestTemplate;

    private MediaType HATEOAS_MEDIA_TYPE;

    public DenvClient() {
        init();
    }

    public DenvClient(String baseUrl) {
        this.baseUrl = baseUrl;
        init();
    }

    /// ***********************
    /// * Public methods
    /// ***********************

    public DenvEnvironment getEnv(String envId) throws ResourceNotFoundException {
        try {
            ResponseEntity<Resource<DenvContainerizedEnvironment>> res = sendGetEnvRequest(envId);
            return res.getBody().getContent();
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Environment " + envId + " does not exist");
            }
            throw ex;
        }
    }

    ///
    /// Environment related methods
    ///

    public PagedResources<DenvContainerizedEnvironment> listEnvironments() {
        ResponseEntity<PagedResources<DenvContainerizedEnvironment>> res = sendListEnvsRequest();
        return res.getBody();
    }

    public DenvEnvironment waitForDesiredState(String envId, long maxWaitInMillis) throws DenvException {
        DenvEnvironment env = null;
        long startTime = System.currentTimeMillis();
        while (true) {
            boolean desiredStateReached = true;
            String errorMessage = null;
            env = getEnv(envId);
            if (env.getDesiredState() == EnvironmentDesiredState.STOPPED) {
                desiredStateReached = (env.getActualState() == EnvironmentState.STOPPED);
            }
            if (env.getDesiredState() == EnvironmentDesiredState.STARTED) {
                if (env instanceof DenvContainerizedEnvironment) {
                    DenvContainerizedEnvironment cenv = (DenvContainerizedEnvironment) env;
                    if (cenv.getRuntimeInfo() == null || cenv.getRuntimeInfo().getContainersRuntimeInfo() == null) {
                        desiredStateReached = false;
                    } else {
                        for (ContainerRuntimeInfo containerRuntimeInfo : cenv.getRuntimeInfo().getContainersRuntimeInfo().values()) {
                            if (!containerRuntimeInfo.getDesiredState().isSatisfiedBy(containerRuntimeInfo.getActualState())) {
                                desiredStateReached = false;
                                errorMessage = "Container " + containerRuntimeInfo.getId() + " should be in state " + containerRuntimeInfo.getDesiredState() +
                                        " but is currently in state " + containerRuntimeInfo.getActualState();
                                break;
                            }
                        }
                    }
                } else {
                    if (env == null) {
                        desiredStateReached = false;
                    }
                }
            }

            if (desiredStateReached) {
                break;
            } else {
                if (System.currentTimeMillis() - startTime + CHECK_STATE_INTERVAL_IN_MILLIS > maxWaitInMillis) {
                    throw new DesiredStateNotReachedException(errorMessage);
                }

                try {
                    Thread.sleep(CHECK_STATE_INTERVAL_IN_MILLIS);
                } catch (InterruptedException e) {
                }
                continue;
            }
        }
        return env;
    }

    public DenvEnvironment waitForEnvironmentDeleted(String envId, long maxWaitInMillis) throws DenvException {
        DenvEnvironment env = null;
        long startTime = System.currentTimeMillis();
        while (true) {
            try {
                getEnv(envId);

                if (System.currentTimeMillis() - startTime + CHECK_STATE_INTERVAL_IN_MILLIS > maxWaitInMillis) {
                    throw new DesiredStateNotReachedException();
                }

                try {
                    Thread.sleep(CHECK_STATE_INTERVAL_IN_MILLIS);
                } catch (InterruptedException e) {
                }
            } catch (ResourceNotFoundException ex) {
                break;
            }
        }
        return env;
    }

    public String createEnvironment(String envId, String envConfId) {
        return createEnvironment(envId, envConfId, null);
    }

    public String createEnvironment(String envId, String envConfId, String version) {
        return createEnvironment(envId, envConfId, version, null);
    }

    public String createEnvironment(String envId, String envConfId, String version, String snapshotName) {
        DenvEnvironment env = new DenvContainerizedEnvironment(envId, envId, envConfId, version, snapshotName, null, null);
        ResponseEntity<String> createResponseEntity = sendCreateEnvRequest(env);
        assertNotNull(createResponseEntity.getHeaders().getLocation());
        return createResponseEntity.getBody();
    }

    public String updateEnvironment(DenvEnvironment env) throws ResourceNotFoundException {
        // the PUT method will succeed even if the resource does not exist,so we check for its existence first
        // getEnv will throw ResourceNotFoundException if the environment does not exist
        getEnv(env.getId());

        ResponseEntity<String> res = sendUpdateEnvRequest((DenvContainerizedEnvironment)env);
        return res.getBody();
    }

    public void deleteEnvironment(String envId) {
        ResponseEntity<Void> res = sendDeleteEnvRequest(envId);
    }

    ///
    /// Application Config related internal methods
    ///

    public ContainerizedEnvironmentConfiguration getContainerizedEnvConfig(String envConfId) {
        ResponseEntity<Resource<ContainerizedEnvironmentConfigurationImpl>> res = sendGetContainerizedEnvConfigRequest(envConfId);
        return res.getBody().getContent();
    }

    public PagedResources<ContainerizedEnvironmentConfigurationImpl> listEnvConfigs() {
        ResponseEntity<PagedResources<ContainerizedEnvironmentConfigurationImpl>> res = sendGetEnvConfigsListRequest();
        return res.getBody();
    }

    public String createOrUpdateContainerizedEnvConfig(ContainerizedEnvironmentConfiguration envConf) {
        ResponseEntity<Void> res = sendCreateOrUpdateContainerizedEnvConfigRequest(envConf);
        assertNotNull(res.getHeaders().getLocation());
        String envConfId = res.getHeaders().getLocation().getPath();
        return envConfId;
    }

    public void deleteEnvConfig(String envConfId) {
        ResponseEntity<Void> res = sendDeleteEnvConfigRequest(envConfId);
    }

    ///
    /// Versions related methods
    ///
    public void addVersion(String envConfId, String version, Map<String, String> variables) {
        ResponseEntity<Void> res = sendAddVersionRequest(envConfId, version, variables);
    }

    public PagedResources<EnvironmentConfigurationVersionImpl> listVers(String envConfId) {
        ResponseEntity<PagedResources<EnvironmentConfigurationVersionImpl>> res = sendGetVersionListRequest(envConfId);
        return res.getBody();
    }

    public EnvironmentConfigurationVersionImpl getVersion(String envConfId, String version) throws ResourceNotFoundException {
        try {
            ResponseEntity<Resource<EnvironmentConfigurationVersionImpl>> res = sendGetVersionRequest(envConfId, version);
            return res.getBody().getContent();
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Version " + envConfId + ":" + version + " does not exist");
            }
            throw ex;
        }
    }

    public void deleteVersion(String envConfId, String version) {
        ResponseEntity<Void> res = sendDeleteVersionRequest(envConfId, version);
    }

    public EnvironmentConfigurationVersion waitForVersionBuild(String envConfId, String version, long maxWaitInMillis) throws DenvException {
        EnvironmentConfigurationVersion envConfVersion = null;
        long startTime = System.currentTimeMillis();
        while (true) {
            String errorMessage = null;
            envConfVersion = getVersion(envConfId, version);
            if (envConfVersion.getActualState() == EnvironmentConfigVersionState.AVAILABLE) {
                break;
            } else {
                if (System.currentTimeMillis() - startTime + CHECK_VERSION_STATE_INTERVAL_IN_MILLIS > maxWaitInMillis) {
                    errorMessage = "Version " + envConfId + ":" + version + " is not AVAILABLE, " +
                            "its currently in state " + envConfVersion.getActualState();
                    throw new DesiredStateNotReachedException(errorMessage);
                }

                try {
                    Thread.sleep(CHECK_STATE_INTERVAL_IN_MILLIS);
                } catch (InterruptedException e) {
                }
                continue;
            }
        }
        return envConfVersion;
    }

    ///
    /// Snapshots related methods
    ///

    public void saveSnapshot(String envId, String snapshotName) {
        sendSaveSnapshotRequest(envId, snapshotName);
    }

    ///
    /// Resource related methods
    ///

    public InputStream getResource(String envId, String envConfId, String resourcePath) {
        // TODO
        return null;
    }

    public InputStream getFolder(String envId, String appConfId, String folderPath) {
        // TODO
        return null;
    }

    ///
    /// Setters and getters
    ///

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /// ***********************
    /// * Internal methods
    /// ***********************

    //@PostConstruct
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
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        //headers.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
        // headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    ///
    /// Environment related internal methods
    ///
    protected String createEnv(DenvEnvironment env) {
        ResponseEntity<String> res = sendCreateEnvRequest(env);
        return res.getBody();
    }

    protected ResponseEntity<String> sendCreateEnvRequest(DenvEnvironment env) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS,
                HttpMethod.POST,
                new HttpEntity<DenvEnvironment>(env, defaultRequestHeaders()),
                String.class
        );
    }

    protected ResponseEntity<Resource<DenvContainerizedEnvironment>> sendGetEnvRequest(String envId) {
        ParameterizedTypeReference<Resource<DenvContainerizedEnvironment>> parameterizedTypeReference =
                new ParameterizedTypeReference<Resource<DenvContainerizedEnvironment>>() {};
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS  + "/{envId}",
                HttpMethod.GET,
                new HttpEntity<String>(envId, defaultRequestHeaders()),
                parameterizedTypeReference,
                envId
        );
    }

    protected ResponseEntity<String> sendUpdateEnvRequest(DenvContainerizedEnvironment env) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS + "/{envId}",
                HttpMethod.PUT,
                new HttpEntity<DenvContainerizedEnvironment>(env, defaultRequestHeaders()),
                String.class,
                env.getId()
        );
    }

    protected ResponseEntity<PagedResources<DenvContainerizedEnvironment>> sendListEnvsRequest() {
        ParameterizedTypeReference<PagedResources<DenvContainerizedEnvironment>> parameterizedTypeReference =
                new ParameterizedTypeReference<PagedResources<DenvContainerizedEnvironment>>() {};
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS,
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference
        );
    }

    protected ResponseEntity<Void> sendDeleteEnvRequest(String envId) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS + "/{envId}",
                HttpMethod.DELETE,
                new HttpEntity<Void>(defaultRequestHeaders()),
                Void.class,
                envId
        );
    }

    ///
    /// Application Config related internal methods
    ///

    protected ResponseEntity<Void> sendCreateOrUpdateContainerizedEnvConfigRequest(ContainerizedEnvironmentConfiguration envConf) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENV_CONFIGS,
                HttpMethod.POST,
                new HttpEntity<EnvironmentConfigurationImpl>(new ContainerizedEnvironmentConfigurationImpl(envConf), defaultRequestHeaders()),
                Void.class
        );
    }

    protected ResponseEntity<PagedResources<ContainerizedEnvironmentConfigurationImpl>> sendGetEnvConfigsListRequest() {
        ParameterizedTypeReference<PagedResources<ContainerizedEnvironmentConfigurationImpl>> parameterizedTypeReference =
                new ParameterizedTypeReference<PagedResources<ContainerizedEnvironmentConfigurationImpl>>() {
                };
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENV_CONFIGS,
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference
        );
    }

    protected ResponseEntity<Resource<ContainerizedEnvironmentConfigurationImpl>> sendGetContainerizedEnvConfigRequest
            (String envConfId) {
        ParameterizedTypeReference<Resource<ContainerizedEnvironmentConfigurationImpl>> parameterizedTypeReference =
                new ParameterizedTypeReference<Resource<ContainerizedEnvironmentConfigurationImpl>>() {};
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENV_CONFIGS + "/{envConfId}",
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference,
                envConfId
        );
    }

    protected ResponseEntity<Void> sendDeleteEnvConfigRequest(String envConfId) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENV_CONFIGS + "/{envConfId}",
                HttpMethod.DELETE,
                new HttpEntity<Void>(defaultRequestHeaders()),
                Void.class,
                envConfId
        );
    }

    ///
    /// Snapshots related methods
    ///

    protected ResponseEntity<Void> sendSaveSnapshotRequest(String envId, String snapshotName) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENVS + DenvApiEndpoints.ENV_SAVE_SNAPSHOT,
                HttpMethod.POST,
                new HttpEntity<String>(snapshotName, defaultRequestHeaders()),
                Void.class,
                envId
        );
    }

    ///
    /// Versions related internal methods
    ///

    protected ResponseEntity<Void> sendAddVersionRequest(String envConfId, String version, Map<String, String> variables) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENV_CONFIG_VERSIONS + DenvApiEndpoints.ENV_CONFIG_VERSION,
                HttpMethod.POST,
                new HttpEntity<Map<String, String>>(variables, defaultRequestHeaders()),
                Void.class,
                envConfId,
                version
        );
    }

    protected ResponseEntity<PagedResources<EnvironmentConfigurationVersionImpl>> sendGetVersionListRequest(String envConfId) {
        ParameterizedTypeReference<PagedResources<EnvironmentConfigurationVersionImpl>> parameterizedTypeReference =
                new ParameterizedTypeReference<PagedResources<EnvironmentConfigurationVersionImpl>>() {
                };
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENV_CONFIG_VERSIONS,
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference,
                envConfId
        );
    }

    protected ResponseEntity<Resource<EnvironmentConfigurationVersionImpl>> sendGetVersionRequest
            (String envConfId, String version) {
        ParameterizedTypeReference<Resource<EnvironmentConfigurationVersionImpl>> parameterizedTypeReference =
                new ParameterizedTypeReference<Resource<EnvironmentConfigurationVersionImpl>>() {};
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENV_CONFIG_VERSIONS + "/{version}",
                HttpMethod.GET,
                new HttpEntity<Void>(defaultRequestHeaders()),
                parameterizedTypeReference,
                envConfId, version
        );
    }

    protected ResponseEntity<Void> sendDeleteVersionRequest(String envConfId, String version) {
        return hateoasRestTemplate.exchange(
                getBaseUrl() + DenvApiEndpoints.ENV_CONFIG_VERSIONS + "/{version}",
                HttpMethod.DELETE,
                new HttpEntity<Void>(defaultRequestHeaders()),
                Void.class,
                envConfId, version
        );
    }
}
