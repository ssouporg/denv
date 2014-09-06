package org.ssoup.denv.server.api.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssoup.denv.common.api.DenvApiEndpoints;
import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.environment.DenvEnvironmentConfiguration;
import org.ssoup.denv.server.api.AbstractController;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;
import org.ssoup.denv.server.service.conf.environment.EnvironmentConfigurationManager;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentManager;

import java.util.Collection;

/**
 * User: ALB
 */
@RestController
@RequestMapping(DenvApiEndpoints.ENVS)
public class EnvsController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(EnvsController.class);

    private final EnvironmentManager environmentManager;

    private final EnvironmentConfigurationManager environmentConfigurationManager;

    private final ApplicationConfigurationManager applicationConfigurationManager;

    @Autowired
    public EnvsController(EnvironmentManager environmentManager, EnvironmentConfigurationManager environmentConfigurationManager, ApplicationConfigurationManager applicationConfigurationManager) {
        this.environmentManager = environmentManager;
        this.environmentConfigurationManager = environmentConfigurationManager;
        this.applicationConfigurationManager = applicationConfigurationManager;
    }

    // For the use of verbs in this controller see:
    // - http://stackoverflow.com/questions/630453/put-vs-post-in-rest
    // - http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-common
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> createEnvironment
        (@RequestBody DenvEnvironmentConfiguration environmentConfiguration) throws DenvException {
        environmentConfigurationManager.registerEnvironmentConfiguration(environmentConfiguration);
        Environment env = environmentManager.createEnvironment(environmentConfiguration);
        environmentManager.startEnvironment(env);
        return new ResponseEntity<String>(env.getId(), HttpStatus.OK);
    }

    /*@RequestMapping(method = RequestMethod.PUT, value = "/{resourceId}")
    public ResponseEntity<Void> createOrUpdateNamedResource(@PathVariable String resourceRestName, @PathVariable String resourceId, @RequestBody Model resourceModel) throws SSOUPException {
    }*/

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Collection<Environment>> listEnvironments() throws DenvException {
        Collection<Environment> envs = environmentManager.listEnvironments();
        return new ResponseEntity<Collection<Environment>>(envs, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{envId}")
    public @ResponseBody ResponseEntity<Void> deleteEnvironment
            (@PathVariable String envId) throws DenvException {
        Environment env = environmentManager.findEnvironment(envId);
        environmentManager.deleteEnvironment(env);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
