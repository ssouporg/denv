package org.ssoup.denv.server.containerization.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssoup.denv.core.api.DenvApiEndpoints;
import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.api.AbstractController;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentService;
import org.ssoup.denv.server.service.runtime.runtime.EnvironmentRuntimeService;

/**
 * User: ALB
 * Date: 27/05/14 10:05
 */
@RestController
@RequestMapping(DenvApiEndpoints.ENVS)
public class EnvironmentController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentController.class);

    private EnvironmentService environmentService;

    private EnvironmentRuntimeService environmentRuntimeService;

    @Autowired
    public EnvironmentController(EnvironmentService environmentService, EnvironmentRuntimeService environmentRuntimeService) {
        this.environmentService = environmentService;
        this.environmentRuntimeService = environmentRuntimeService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Void> createEnvironment(@RequestBody DenvContainerizedEnvironment env) throws DenvException {
        Environment createdEnv = environmentService.createEnvironment(env);
        // if it is an update maybe call environmentService.updateEnvironment(actualEnv, env);
        HttpHeaders headers = super.defaultResponseHeaders();
        headers.add("Location", createdEnv.getId());
        return new ResponseEntity<Void>(headers, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{envId}", method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity<Void> updateEnvironment(@PathVariable String envId, @RequestBody DenvContainerizedEnvironment env) throws DenvException {
        env.setId(envId);
        Environment createdEnv = environmentService.updateEnvironment(env);
        // if it is an update maybe call environmentService.updateEnvironment(actualEnv, env);
        HttpHeaders headers = super.defaultResponseHeaders();
        headers.add("Location", createdEnv.getId());
        return new ResponseEntity<Void>(headers, HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = HATEOAS_MEDIA_TYPE_VALUE)
    public @ResponseBody
    ResponseEntity<PagedResources<DenvContainerizedEnvironment>> listEnvironments(final Pageable pageable, final PagedResourcesAssembler assembler)
            throws DenvException {
        Page envsPage = (Page) environmentService.listEnvs(pageable);
        return new ResponseEntity<PagedResources<DenvContainerizedEnvironment>>(
                assembler.toResource(envsPage), super.defaultResponseHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{envId}", method = RequestMethod.GET, produces = HATEOAS_MEDIA_TYPE_VALUE)
    public @ResponseBody
    ResponseEntity<Resource<DenvContainerizedEnvironment>> getEnvironment(@PathVariable String envId)
            throws DenvException {
        DenvContainerizedEnvironment env = (DenvContainerizedEnvironment) environmentService.findById(envId);
        if (env == null) {
            return new ResponseEntity<Resource<DenvContainerizedEnvironment>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Resource<DenvContainerizedEnvironment>>(
                new Resource<DenvContainerizedEnvironment>(env),
                super.defaultResponseHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{envId}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<Resource<DenvContainerizedEnvironment>> deleteEnvironment(@PathVariable String envId)
            throws DenvException {
        environmentService.deleteEnvironment(envId);
        return new ResponseEntity<Resource<DenvContainerizedEnvironment>>(
                super.defaultResponseHeaders(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = DenvApiEndpoints.ENV_SAVE_SNAPSHOT)
    public @ResponseBody
    ResponseEntity<Void> saveSnapshot(@PathVariable String envId, @RequestBody String snapshotName) throws DenvException {
        Environment env = (Environment)environmentService.findById(envId);
        environmentRuntimeService.saveSnapshot(env, snapshotName);
        return new ResponseEntity<Void>(super.defaultResponseHeaders(), HttpStatus.CREATED);
    }
}
