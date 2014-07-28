package org.ssoup.denv.server.web;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssoup.backend.exception.ResourceNotFoundException;
import org.ssoup.backend.exception.SSOUPException;
import org.ssoup.backend.service.CRUDService;
import org.ssoup.backend.service.SSOUPService;
import org.ssoup.backend.util.SSOUPUtil;
import org.ssoup.backend.vocabulary.Ssoup;

/**
 * User: ALB
 * Date: 27/05/14 10:05
 */
@RestController
@RequestMapping("/{resourceRestName}")
public class CRUDController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(CRUDController.class);

    private final SSOUPService ssoupService;

    private final CRUDService crudService;

    @Autowired
    public CRUDController(SSOUPService ssoupService, CRUDService crudService) {
        this.ssoupService = ssoupService;
        this.crudService = crudService;
    }

    // For the use of verbs in this controller see:
    // - http://stackoverflow.com/questions/630453/put-vs-post-in-rest
    // - http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> createResourceGenerateId(@PathVariable String resourceRestName, @RequestBody Model resourceModel) throws SSOUPException {
        OntClass resourceType = ssoupService.findClassWithRestName(resourceRestName);
        try {
            String resourceId = SSOUPUtil.generateObjectId();
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
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{resourceId}")
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
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Model> listResources(@PathVariable String resourceRestName,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                        @RequestParam(value = "perPage", required = false, defaultValue = "10") Integer perPage) throws SSOUPException {
        OntClass resourceType = ssoupService.findClassWithRestName(resourceRestName);
        Model modelToReturn = crudService.listResources(resourceType, page, perPage);

        OntResource returnList = SSOUPUtil.getMainObject(modelToReturn);
        Integer totalCount = returnList.getProperty(Ssoup.size).getInt();
        HttpHeaders responseHeaders = defaultResponseHeaders();
        responseHeaders.set("X-Total-Count", totalCount.toString());
        // TODO: set link header to first, last, prev, next pages,see http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
        // responseHeaders.set("Link");

        return new ResponseEntity<Model>(modelToReturn, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{resourceId}")
    public @ResponseBody ResponseEntity<Model> retrieveResource(@PathVariable String resourceRestName, @PathVariable String resourceId) throws SSOUPException {
        OntClass resourceType = ssoupService.findClassWithRestName(resourceRestName);
        try {
            return new ResponseEntity<Model>(crudService.retrieveResource(resourceType, resourceId), defaultResponseHeaders(), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<Model>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{resourceId}")
    public @ResponseBody ResponseEntity<Model> updateResource(@PathVariable String resourceRestName, @PathVariable String resourceId, @RequestBody Model resourceModel) throws SSOUPException {
        OntClass resourceType = ssoupService.findClassWithRestName(resourceRestName);
        try {
            return new ResponseEntity<Model>(crudService.updateResource(resourceType, resourceId, resourceModel), defaultResponseHeaders(), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<Model>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{resourceId}")
    public @ResponseBody ResponseEntity<Void> deleteResource(@PathVariable String resourceRestName, @PathVariable String resourceId) throws SSOUPException {
        OntClass resourceType = ssoupService.findClassWithRestName(resourceRestName);
        try {
            crudService.deleteResource(resourceType, resourceId);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Void>(defaultResponseHeaders(), HttpStatus.NO_CONTENT);
    }
}
