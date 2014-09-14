package org.ssoup.denv.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * User: ALB
 * Date: 14/09/14 16:36
 */
@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = "org.ssoup.denv")
public class Main {

    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args ) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setWebEnvironment(false);
        app.setShowBanner(false);
        app.run(args);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        // this is needed to enable parsing of ${} inside @Value annotations.
        // @EnableAutoConfiguration would also enable it (but it is less controllable)
        return new PropertySourcesPlaceholderConfigurer();
    }
}
