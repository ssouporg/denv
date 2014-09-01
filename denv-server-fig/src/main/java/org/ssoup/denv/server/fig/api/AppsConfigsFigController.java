package org.ssoup.denv.server.fig.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssoup.denv.common.api.DenvApiEndpoints;
import org.ssoup.denv.common.model.application.DenvApplicationConfiguration;
import org.ssoup.denv.server.api.AbstractController;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.fig.domain.conf.FigApplicationConfiguration;
import org.ssoup.denv.server.fig.service.FigDenvApplicationConfigurationConverter;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;

/**
 * User: ALB
 */
@RestController
@RequestMapping(DenvApiEndpoints.FIG_APPS_CONFIGS)
public class AppsConfigsFigController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AppsConfigsFigController.class);

    private final FigDenvApplicationConfigurationConverter figApplicationConfigurationConverter;
    private final ApplicationConfigurationManager applicationConfigurationManager;

    @Autowired
    public AppsConfigsFigController(FigDenvApplicationConfigurationConverter figApplicationConfigurationConverter, ApplicationConfigurationManager applicationConfigurationManager) {
        this.figApplicationConfigurationConverter = figApplicationConfigurationConverter;
        this.applicationConfigurationManager = applicationConfigurationManager;
    }

    // For the use of verbs in this controller see:
    // - http://stackoverflow.com/questions/630453/put-vs-post-in-rest
    // - http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-common
    @RequestMapping(method = RequestMethod.POST, value = "/{applicationConfigName}")
    public @ResponseBody ResponseEntity<String> createApplicationFromFigConfiguration(
            @PathVariable String applicationConfigName,
            @RequestBody FigApplicationConfiguration figApplicationConfiguration
    ) throws DenvException {
        DenvApplicationConfiguration appConfig = (DenvApplicationConfiguration)
                figApplicationConfigurationConverter.convertApplicationConfiguration(figApplicationConfiguration);
        appConfig.setName(applicationConfigName);
        applicationConfigurationManager.registerApplicationConfiguration(appConfig);

        HttpHeaders responseHeaders = defaultResponseHeaders();
        responseHeaders.set("Location", DenvApiEndpoints.FIG_APPS_CONFIGS + "/" + appConfig.getName());
        return new ResponseEntity<String>(appConfig.getName(), responseHeaders, HttpStatus.CREATED);
    }
}
