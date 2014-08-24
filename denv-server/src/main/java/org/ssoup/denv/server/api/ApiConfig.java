package org.ssoup.denv.server.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.ssoup.denv.server.api.converter.CollectionMessageConverter;
import org.ssoup.denv.server.api.converter.DenvMessageConverter;

import java.util.List;

/**
 * Register message converters for Denv domain classes.
 *
 * User: ALB
 * Date: 27/05/14 11:26
 */
@Configuration
public class ApiConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new DenvMessageConverter());
        converters.add(new CollectionMessageConverter());
        super.configureMessageConverters(converters);
    }
}
