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
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.server.api.AbstractController;
import org.ssoup.denv.server.service.conf.EnvironmentConfigurationService;

/**
 * User: ALB
 * Date: 27/05/14 10:05
 */
@RestController
@RequestMapping(DenvApiEndpoints.ENV_CONFIGS)
public class EnvironmentConfigurationController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentConfigurationController.class);

    private EnvironmentConfigurationService environmentConfigurationService;

    @Autowired
    public EnvironmentConfigurationController(EnvironmentConfigurationService environmentConfigurationService) {
        this.environmentConfigurationService = environmentConfigurationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Void> createEnvironmentConfiguration(@RequestBody ContainerizedEnvironmentConfiguration envConf) throws DenvException {
        EnvironmentConfiguration createdEnvConf = environmentConfigurationService.saveEnvironmentConfiguration(envConf);
        HttpHeaders headers = super.defaultResponseHeaders();
        headers.add("Location", createdEnvConf.getId());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = HATEOAS_MEDIA_TYPE_VALUE)
    public @ResponseBody
    ResponseEntity<PagedResources<ContainerizedEnvironmentConfiguration>> listEnvironmentConfigurations(final Pageable pageable, final PagedResourcesAssembler assembler)
            throws DenvException {
        Page envConfsPage = (Page) environmentConfigurationService.listEnvConfs(pageable);
        return new ResponseEntity<PagedResources<ContainerizedEnvironmentConfiguration>>(
                assembler.toResource(envConfsPage), super.defaultResponseHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{envConfId}", method = RequestMethod.GET, produces = HATEOAS_MEDIA_TYPE_VALUE)
    public @ResponseBody
    ResponseEntity<Resource<ContainerizedEnvironmentConfiguration>> getEnvironmentConfiguration(@PathVariable String envConfId)
            throws DenvException {
        ContainerizedEnvironmentConfiguration envConf = (ContainerizedEnvironmentConfiguration) environmentConfigurationService.findById(envConfId);
        if (envConf == null) {
            return new ResponseEntity<Resource<ContainerizedEnvironmentConfiguration>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Resource<ContainerizedEnvironmentConfiguration>>(
                new Resource<ContainerizedEnvironmentConfiguration>(envConf),
                super.defaultResponseHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{envConfId}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<Resource<ContainerizedEnvironmentConfiguration>> deleteEnvironmentConfiguration(@PathVariable String envConfId)
            throws DenvException {
        environmentConfigurationService.deleteEnvironmentConfiguration(envConfId);
        return new ResponseEntity<Resource<ContainerizedEnvironmentConfiguration>>(
                super.defaultResponseHeaders(), HttpStatus.OK);
    }
}
