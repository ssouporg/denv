package org.ssoup.denv.server.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.ssoup.denv.core.api.DenvApiEndpoints;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersionImpl;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.service.runtime.runtime.EnvironmentRuntimeManager;
import org.ssoup.denv.server.service.versioning.VersionManager;

import java.util.Map;

/**
 * User: ALB
 * Date: 15/12/14 09:21
 */
@RestController
@RequestMapping(DenvApiEndpoints.ENV_CONFIG_VERSIONS)
public class VersionController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(VersionController.class);

    private EnvironmentConfigRepository environmentConfigRepository;

    private VersionManager versionManager;

    @Autowired
    public VersionController(EnvironmentConfigRepository environmentConfigRepository,
                             VersionManager versionManager) {
        this.environmentConfigRepository = environmentConfigRepository;
        this.versionManager = versionManager;
    }

    @RequestMapping(value = DenvApiEndpoints.ENV_CONFIG_VERSION, method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Void> addVersion(@PathVariable String envConfId, @PathVariable String version,
                                    @RequestBody Map<String, String> variables) throws DenvException {
        EnvironmentConfiguration envConf = (EnvironmentConfiguration) environmentConfigRepository.findOne(envConfId);
        // version = version.replace("\"", ""); // removes the quotes in the request body
        versionManager.addVersion(envConf, version, variables);
        return new ResponseEntity<Void>(super.defaultResponseHeaders(), HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = HATEOAS_MEDIA_TYPE_VALUE)
    public @ResponseBody
    ResponseEntity<PagedResources<EnvironmentConfigurationVersionImpl>> listVersions(@PathVariable String envConfId,
                                                                                     final Pageable pageable,
                                                                                     final PagedResourcesAssembler assembler)
            throws DenvException {
        Page versPage = (Page) versionManager.listVers(envConfId, pageable);
        return new ResponseEntity<PagedResources<EnvironmentConfigurationVersionImpl>>(
                assembler.toResource(versPage), super.defaultResponseHeaders(), HttpStatus.OK);
    }

    // /{version:.+} to avoid truncated dots
    @RequestMapping(value = "/{version:.+}", method = RequestMethod.GET, produces = HATEOAS_MEDIA_TYPE_VALUE)
    public @ResponseBody
    ResponseEntity<Resource<EnvironmentConfigurationVersionImpl>> getVersion(@PathVariable String envConfId,
                                                                             @PathVariable String version)
            throws DenvException {
        EnvironmentConfigurationVersionImpl ver = (EnvironmentConfigurationVersionImpl) versionManager.getVersion(envConfId, version);
        if (ver == null) {
            return new ResponseEntity<Resource<EnvironmentConfigurationVersionImpl>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Resource<EnvironmentConfigurationVersionImpl>>(
                new Resource<EnvironmentConfigurationVersionImpl>(ver),
                super.defaultResponseHeaders(), HttpStatus.OK);
    }

    // /{version:.+} to avoid truncated dots
    @RequestMapping(value = "/{version:.+}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<Resource<EnvironmentConfigurationVersionImpl>> deleteVersion(@PathVariable String envConfId,
                                                                                @PathVariable String version)
            throws DenvException {
        versionManager.deleteVersion(envConfId, version);
        return new ResponseEntity<Resource<EnvironmentConfigurationVersionImpl>>(
                super.defaultResponseHeaders(), HttpStatus.OK);
    }
}
