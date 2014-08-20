package org.ssoup.denv.server.api.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssoup.denv.server.api.AbstractController;
import org.ssoup.denv.server.domain.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.service.runtime.EnvironmentManager;

import java.util.Collection;

/**
 * User: ALB
 */
@RestController
@RequestMapping("/api/v1/envs")
public class EnvsController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(EnvsController.class);

    private final EnvironmentManager environmentManager;

    @Autowired
    public EnvsController(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    // For the use of verbs in this controller see:
    // - http://stackoverflow.com/questions/630453/put-vs-post-in-rest
    // - http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> createEnvironemntConfiguration(@RequestBody String environmentConfiguration) throws DenvException {
            String resourceId = null;
            /*SSOUPUtil.generateObjectId();
            String resourceUri = SSOUPUtil.buildObjectUri(resourceType, resourceId);
            OntResource metaInfo = SSOUPUtil.getResourceByType(resourceModel, Ssoup.MetaInfo);
            if (metaInfo == null) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
            Resource resource = metaInfo.getPropertyResourceValue(Ssoup.main_object);
            if (resource == null) {
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
            ResourceUtils.renameResource(resource, resourceUri);
            crudService.createResource(resourceType, resourceId, resourceModel);
            HttpHeaders responseHeaders = defaultResponseHeaders();
            responseHeaders.set("Location", resourceUri);
            return new ResponseEntity<String>(resourceId, responseHeaders, HttpStatus.CREATED);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }*/
        return null;
    }

    /*@RequestMapping(method = RequestMethod.PUT, value = "/{resourceId}")
    public ResponseEntity<Void> createOrUpdateNamedResource(@PathVariable String resourceRestName, @PathVariable String resourceId, @RequestBody Model resourceModel) throws SSOUPException {
        OntClass resourceType = ssoupService.findClassWithRestName(resourceRestName);
        try {
            if (ssoupService.resourceExists(resourceType, resourceId)) {
                crudService.replaceResource(resourceType, resourceId, resourceModel);
                return new ResponseEntity<Void>(HttpStatus.OK);
            } else {
                String resourceUri = crudService.createResource(resourceType, resourceId, resourceModel);
                HttpHeaders responseHeaders = defaultResponseHeaders();
                responseHeaders.set("Location", resourceUri);
                return new ResponseEntity<Void>(responseHeaders, HttpStatus.CREATED);
            }
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }*/

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Collection<Environment>> listEnvironments() throws DenvException {
        Collection<Environment> envs = environmentManager.listEnvironments();
        return new ResponseEntity<Collection<Environment>>(envs, HttpStatus.OK);
    }
}
