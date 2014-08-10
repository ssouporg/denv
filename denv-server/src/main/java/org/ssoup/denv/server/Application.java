package org.ssoup.denv.server;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.ssoup.denv.server.conf.application.fig.FigApplicationConfiguration;
import org.ssoup.denv.server.conf.application.fig.FigServiceConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * User: ALB
 */
@Configuration
@PropertySource("classpath:/application.properties")
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args) throws Exception {
        /*SpringApplication app = new SpringApplication(Application.class);
        app.run(args);*/

        FigApplicationConfiguration figConfiguration = new FigApplicationConfiguration();
        Yaml yaml = new Yaml();
        Map<String, Object> figConfig = (Map<String, Object>)yaml.load(Application.class.getClassLoader().getResourceAsStream("app1.yml"));
        for (String service : figConfig.keySet()) {
            String serviceYaml = yaml.dump(figConfig.get(service));
            FigServiceConfiguration figServiceConfig = yaml.loadAs(serviceYaml, FigServiceConfiguration.class);
            figConfiguration.put(service, figServiceConfig);
        }
        System.out.println(yaml.dump(figConfig));
    }
}
