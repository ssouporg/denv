package org.ssoup.denv.common.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
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
        converters.add(denvEnvironmentConfigurationConverter);
        converters.add(denvApplicationConfigurationConverter);
        converters.add(collectionMessageConverter);
        converters.add(stringHttpMessageConverter);
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
