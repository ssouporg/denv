package org.ssoup.denv.server.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssoup.denv.core.api.DenvApiEndpoints;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.persistence.EnvironmentRepository;
import org.ssoup.denv.server.service.runtime.runtime.EnvironmentRuntimeManager;

/**
 * User: ALB
 * Date: 27/05/14 10:05
 */
@RestController
@RequestMapping(DenvApiEndpoints.ENVS)
public class EnvironmentController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentController.class);

    private EnvironmentRepository environmentRepository;

    private EnvironmentRuntimeManager environmentRuntimeManager;

    @Autowired
    public EnvironmentController(EnvironmentRuntimeManager environmentRuntimeManager) {
        this.environmentRuntimeManager = environmentRuntimeManager;
    }

    @RequestMapping(method = RequestMethod.POST, value = DenvApiEndpoints.ENV_SAVE_SNAPSHOT)
    public @ResponseBody
    ResponseEntity<Void> saveSnapshot(@PathVariable String envId, @RequestBody String snapshotName) throws DenvException {
        Environment env = (Environment)environmentRepository.findOne(envId);
        environmentRuntimeManager.saveSnapshot(env, snapshotName);
        return new ResponseEntity<Void>(super.defaultResponseHeaders(), HttpStatus.CREATED);
    }
}
