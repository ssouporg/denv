package org.ssoup.denv.server.panamax.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssoup.denv.server.api.AbstractController;
import org.ssoup.denv.server.domain.conf.application.DenvApplicationConfiguration;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.panamax.domain.conf.PanamaxApplicationConfiguration;
import org.ssoup.denv.server.panamax.service.PanamaxDenvApplicationConfigurationConverter;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;

/**
 * User: ALB
 */
@RestController
@RequestMapping(AppsConfigsPanamaxController.PATH)
public class AppsConfigsPanamaxController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(AppsConfigsPanamaxController.class);

    public static final String PATH = "/api/v1/apps/configs/panamax";
    private final PanamaxDenvApplicationConfigurationConverter panamaxDenvApplicationConfigurationConverter;
    private final ApplicationConfigurationManager applicationConfigurationManager;

    @Autowired
    public AppsConfigsPanamaxController(PanamaxDenvApplicationConfigurationConverter panamaxDenvApplicationConfigurationConverter, ApplicationConfigurationManager applicationConfigurationManager) {
        this.panamaxDenvApplicationConfigurationConverter = panamaxDenvApplicationConfigurationConverter;
        this.applicationConfigurationManager = applicationConfigurationManager;
    }

    // For the use of verbs in this controller see:
    // - http://stackoverflow.com/questions/630453/put-vs-post-in-rest
    // - http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> createApplicationFromPanamaxConfiguration(
            @RequestBody PanamaxApplicationConfiguration panamaxApplicationConfiguration
    ) throws DenvException {
        DenvApplicationConfiguration appConfig = (DenvApplicationConfiguration)
                panamaxDenvApplicationConfigurationConverter.convertApplicationConfiguration(panamaxApplicationConfiguration);
        applicationConfigurationManager.registerApplicationConfiguration(appConfig);

        HttpHeaders responseHeaders = defaultResponseHeaders();
        responseHeaders.set("Location", AppsConfigsPanamaxController.PATH + "/" + appConfig.getName());
        return new ResponseEntity<String>(appConfig.getName(), responseHeaders, HttpStatus.CREATED);
    }
}
