package org.ssoup.denv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * User: ALB
 */
@Configuration
@PropertySource("classpath:/application.properties")
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan
public class Denv {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Denv.class);
        app.run(args);
    }
}
