package org.ssoup.denv.server.api.v1;

import org.ssoup.denv.server.api.AbstractController;

/**
 * User: ALB
 */
//@RestController
//@RequestMapping(DenvApiEndpoints.ENVS)
public class EnvsController extends AbstractController {

    /*private static final Logger logger = LoggerFactory.getLogger(EnvsController.class);

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
    @RequestMapping(method = RequestMethod.PUT, value = "/{envId}/start")
    public @ResponseBody ResponseEntity<Void> startEnvironment(@PathVariable String envId) throws DenvException {
        Environment env = environmentManager.findEnvironment(envId);
        environmentManager.startEnvironment(env);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }*/
}
