package org.ssoup.denv.server.fig.conf;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.domain.conf.application.*;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * User: ALB
 * Date: 11/08/14 15:49
 */
@Service
@Scope("singleton")
public class FigApplicationConfigurationManager extends AbstractApplicationConfigurationManager {

    public ApplicationConfiguration readApplicationConfiguration(String figConfiguration) {
        Yaml yaml = new Yaml();
        Map<String, Object> figConfig = (Map<String, Object>)yaml.load(figConfiguration);
        String applicationName = (String)figConfig.get("name");
        ApplicationConfiguration applicationConfiguration = new ApplicationConfigurationImpl(applicationName);
        for (String service : figConfig.keySet()) {
            String serviceYaml = yaml.dump(figConfig.get(service));
            FigServiceConfiguration serviceConfiguration = yaml.loadAs(serviceYaml, FigServiceConfiguration.class);
            applicationConfiguration.addServiceConfiguration(convertServiceConfiguration(serviceConfiguration));
        }
        return applicationConfiguration;
    }

    private ServiceConfiguration convertServiceConfiguration(FigServiceConfiguration figServiceConfiguration) {
        ServiceConfigurationImpl serviceConfiguration = new ServiceConfigurationImpl();
        serviceConfiguration.setName(figServiceConfiguration.getImage());
        serviceConfiguration.setImage(figServiceConfiguration.getImage());
        serviceConfiguration.setCommand(figServiceConfiguration.getCommand());
        /// TODO: fill in ports and volumes
        //serviceConfiguration.setPorts(figServiceConfiguration.getPorts());
        //serviceConfiguration.setVolumeInfos(figServiceConfiguration.getVolumes());
        return serviceConfiguration;
    }
}
