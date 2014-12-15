package org.ssoup.denv.server.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssoup.denv.core.api.DenvApiEndpoints;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.service.runtime.runtime.EnvironmentRuntimeManager;
import org.ssoup.denv.server.service.versioning.VersionManager;

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
    public VersionController(EnvironmentConfigRepository environmentConfigRepository, EnvironmentRuntimeManager environmentRuntimeManager) {
        this.environmentConfigRepository = environmentConfigRepository;
        this.versionManager = versionManager;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Void> build(@PathVariable String envConfId, @RequestBody String version) throws DenvException {
        EnvironmentConfiguration envConf = (EnvironmentConfiguration)environmentConfigRepository.findOne(envConfId);
        versionManager.scheduleBuild(envConf, version);
        return new ResponseEntity<Void>(super.defaultResponseHeaders(), HttpStatus.ACCEPTED);
    }
}
