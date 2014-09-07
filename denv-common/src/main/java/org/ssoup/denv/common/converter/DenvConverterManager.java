package org.ssoup.denv.common.converter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Register message converters for Denv domain classes.
 *
 * User: ALB
 * Date: 27/05/14 11:26
 */
@Service
@Scope("singleton")
public class DenvConverterManager {

    private DenvEnvironmentConfigurationConverter denvEnvironmentConfigurationConverter;

    private DenvApplicationConfigurationConverter denvApplicationConfigurationConverter;

    private CollectionMessageConverter collectionMessageConverter;

    private StringHttpMessageConverter stringHttpMessageConverter;

    @Autowired
    public DenvConverterManager(DenvEnvironmentConfigurationConverter denvEnvironmentConfigurationConverter, DenvApplicationConfigurationConverter denvApplicationConfigurationConverter, CollectionMessageConverter collectionMessageConverter) {
        this.denvEnvironmentConfigurationConverter = denvEnvironmentConfigurationConverter;
        this.denvApplicationConfigurationConverter = denvApplicationConfigurationConverter;
        this.collectionMessageConverter = collectionMessageConverter;
        this.stringHttpMessageConverter = new StringHttpMessageConverter();
    }

    public void addConverters(List<HttpMessageConverter<?>> converters) {
        // in order to support hateoas and avoid : Unrecognized field "_links" (class org.springframework.hateoas.PagedResources)
        // http://stackoverflow.com/questions/23239052/why-does-resttemplate-not-bind-response-representation-to-pagedresources
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        converter.setObjectMapper(mapper);

        converters.add(0, converter);
        /*converters.add(denvEnvironmentConfigurationConverter);
        converters.add(denvApplicationConfigurationConverter);
        converters.add(collectionMessageConverter);
        converters.add(stringHttpMessageConverter);*/
    }

    public DenvEnvironmentConfigurationConverter getDenvEnvironmentConfigurationConverter() {
        return denvEnvironmentConfigurationConverter;
    }

    public void setDenvEnvironmentConfigurationConverter(DenvEnvironmentConfigurationConverter denvEnvironmentConfigurationConverter) {
        this.denvEnvironmentConfigurationConverter = denvEnvironmentConfigurationConverter;
    }

    public DenvApplicationConfigurationConverter getDenvApplicationConfigurationConverter() {
        return denvApplicationConfigurationConverter;
    }

    public void setDenvApplicationConfigurationConverter(DenvApplicationConfigurationConverter denvApplicationConfigurationConverter) {
        this.denvApplicationConfigurationConverter = denvApplicationConfigurationConverter;
    }

    public CollectionMessageConverter getCollectionMessageConverter() {
        return collectionMessageConverter;
    }

    public void setCollectionMessageConverter(CollectionMessageConverter collectionMessageConverter) {
        this.collectionMessageConverter = collectionMessageConverter;
    }
}
