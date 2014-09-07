package org.ssoup.denv.server.api.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssoup.denv.common.api.DenvApiEndpoints;
import org.ssoup.denv.server.api.AbstractController;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;

/**
 * User: ALB
 */
//@RestController
//@RequestMapping(DenvApiEndpoints.APPS_CONFIGS)
public class AppsConfigsController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AppsConfigsController.class);

    private final ApplicationConfigurationManager applicationConfigurationManager;

    @Autowired
    public AppsConfigsController(ApplicationConfigurationManager applicationConfigurationManager) {
        this.applicationConfigurationManager = applicationConfigurationManager;
    }
/*
    // For the use of verbs in this controller see:
    // - http://stackoverflow.com/questions/630453/put-vs-post-in-rest
    // - http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-common
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> createApplicationConfiguration(
            @RequestBody DenvApplicationConfiguration appConfig
    ) throws DenvException {
        applicationConfigurationManager.registerApplicationConfiguration(appConfig);

        HttpHeaders responseHeaders = defaultResponseHeaders();
        responseHeaders.set("Location", DenvApiEndpoints.APPS_CONFIGS + "/" + appConfig.getName());
        return new ResponseEntity<String>(appConfig.getName(), responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Collection<String>> listApplicationConfigNames() throws DenvException {
        Collection<String> appConfigNames = applicationConfigurationManager.listApplicationConfigurationNames();
        return new ResponseEntity<Collection<String>>(appConfigNames, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{appConfigName}")
    public @ResponseBody
    ResponseEntity<ApplicationConfiguration> getApplicationConfig(@PathVariable String appConfigName) throws DenvException {
        ApplicationConfiguration appConfig = applicationConfigurationManager.getApplicationConfiguration(appConfigName);
        return new ResponseEntity<ApplicationConfiguration>(appConfig, HttpStatus.OK);
    }*/
}
