package org.ssoup.denv.server.fig.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.config.application.InMemoryDenvApplicationConfiguration;
import org.ssoup.denv.server.fig.domain.conf.FigApplicationConfiguration;
import org.ssoup.denv.server.fig.domain.conf.FigServiceConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Map;

/**
 * User: ALB
 * Date: 11/08/14 15:49
 */
@Service
@Scope("singleton")
public class FigDenvApplicationConfigurationConverter {

    public ApplicationConfiguration convertApplicationConfiguration(String appConfName, String figAppConfig) {
        FigApplicationConfiguration figAppConfiguration = readFigAppConfiguration(figAppConfig);
        InMemoryDenvApplicationConfiguration appConf = new InMemoryDenvApplicationConfiguration();
        appConf.setName(appConfName);
        appConf.setImages(new ArrayList<InMemoryDenvApplicationConfiguration.ImageConfigurationImpl>());
        for (String serviceName : figAppConfiguration.keySet()) {
            FigServiceConfiguration serviceConfiguration = figAppConfiguration.get(serviceName);
            InMemoryDenvApplicationConfiguration.ImageConfigurationImpl appImage = new InMemoryDenvApplicationConfiguration.ImageConfigurationImpl();
            appImage.setName(serviceName);
            appImage.setSource(serviceConfiguration.getImage());
            // environment variables
            appImage.setEnvironment(new ArrayList<InMemoryDenvApplicationConfiguration.EnvironmentVariableConfigurationImpl>());
            if (serviceConfiguration.getEnvironment() != null) {
                for (String environmentVariableName : serviceConfiguration.getEnvironment().keySet()) {
                    InMemoryDenvApplicationConfiguration.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new InMemoryDenvApplicationConfiguration.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariableName);
                    appEnvironmentVariable.setValue(serviceConfiguration.getEnvironment().get(environmentVariableName));
                    appImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            appImage.setLinks(new ArrayList<InMemoryDenvApplicationConfiguration.LinkConfigurationImpl>());
            if (serviceConfiguration.getLinks() != null) {
                for (String link : serviceConfiguration.getLinks()) {
                    InMemoryDenvApplicationConfiguration.LinkConfigurationImpl appLink = new InMemoryDenvApplicationConfiguration.LinkConfigurationImpl();
                    appLink.setService(link);
                    appImage.getLinks().add(appLink);
                }
            }
            // ports
            appImage.setPorts(new ArrayList<InMemoryDenvApplicationConfiguration.PortConfigurationImpl>());
            if (serviceConfiguration.getPorts() != null) {
                for (String port : serviceConfiguration.getPorts()) {
                    InMemoryDenvApplicationConfiguration.PortConfigurationImpl appPort = new InMemoryDenvApplicationConfiguration.PortConfigurationImpl();
                    if (port.contains(":")) {
                        int index = port.indexOf(':');
                        appPort.setHostPort(Integer.parseInt(port.substring(0, index)));
                        appPort.setContainerPort(Integer.parseInt(port.substring(index + 1)));
                    } else {
                        appPort.setContainerPort(Integer.parseInt(port));
                    }
                    appImage.getPorts().add(appPort);
                }
            }
            // volumes
            appImage.setVolumes(new ArrayList<InMemoryDenvApplicationConfiguration.VolumeConfigurationImpl>());
            if (serviceConfiguration.getVolumes() != null) {
                for (String vol : serviceConfiguration.getVolumes()) {
                    InMemoryDenvApplicationConfiguration.VolumeConfigurationImpl appVolume = new InMemoryDenvApplicationConfiguration.VolumeConfigurationImpl();
                    if (vol.contains(":")) {
                        int index = vol.indexOf(':');
                        appVolume.setHostPath(vol.substring(0, index));
                        appVolume.setContainerPath(vol.substring(index + 1));
                    } else {
                        appVolume.setContainerPath(vol);
                    }
                    appImage.getVolumes().add(appVolume);
                }
            }
            appConf.getImages().add(appImage);
        }
        return appConf;
    }

    private FigApplicationConfiguration readFigAppConfiguration(String figAppConfig) {
        Yaml yaml = new Yaml();
        FigApplicationConfiguration figAppConfiguration = new FigApplicationConfiguration();
        Map<String, Object> figConfigMap = (Map<String, Object>)yaml.load(figAppConfig);
        for (String serviceName : figConfigMap.keySet()) {
            String serviceYaml = yaml.dump(figConfigMap.get(serviceName));
            FigServiceConfiguration serviceConfiguration = yaml.loadAs(serviceYaml, FigServiceConfiguration.class);
            figAppConfiguration.put(serviceName, serviceConfiguration);
        }
        return figAppConfiguration;
    }
}
