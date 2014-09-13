package org.ssoup.denv.server.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Register message converters for Denv domain classes.
 *
 * User: ALB
 * Date: 27/05/14 11:26
 */
@Configuration
public class ApiConfig extends WebMvcConfigurerAdapter {
/*
    @Autowired
    private DenvConverterManager denvConverterManager;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        denvConverterManager.addConverters(converters);
        super.configureMessageConverters(converters);
    }

    public void setDenvConverterManager(DenvConverterManager denvConverterManager) {
        this.denvConverterManager = denvConverterManager;
    }*/
}
