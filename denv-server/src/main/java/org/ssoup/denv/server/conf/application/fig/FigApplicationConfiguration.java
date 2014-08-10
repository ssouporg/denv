package org.ssoup.denv.server.conf.application.fig;

import org.springframework.context.annotation.Configuration;
import org.ssoup.denv.server.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.conf.application.ServiceConfiguration;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * User: ALB
 * Date: 09/08/14 17:37
 */
@Configuration
public class FigApplicationConfiguration extends HashMap<String, FigServiceConfiguration> implements ApplicationConfiguration {

    @Override
    public ServiceConfiguration getServiceConfiguration(String serviceName) {
        return this.get(serviceName);
    }

    @Override
    public void setServiceConfiguration(String serviceName, ServiceConfiguration serviceConfiguration) {
        if (!(serviceConfiguration instanceof FigServiceConfiguration)) {
            throw new InvalidParameterException("Provided service configuration is not a valid fig service configuration");
        }
        this.put(serviceName, (FigServiceConfiguration)serviceConfiguration);
    }
}
