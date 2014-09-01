package org.ssoup.denv.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.ssoup.denv.common.converter.DenvConverterManager;

import java.util.List;

/**
 * Register message converters for Denv domain classes.
 *
 * User: ALB
 * Date: 27/05/14 11:26
 */
@Configuration
public class ApiConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private DenvConverterManager denvConverterManager;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        denvConverterManager.addConverters(converters);
        super.configureMessageConverters(converters);
    }

    public void setDenvConverterManager(DenvConverterManager denvConverterManager) {
        this.denvConverterManager = denvConverterManager;
    }
}
