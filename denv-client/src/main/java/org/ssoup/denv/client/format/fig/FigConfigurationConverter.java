package org.ssoup.denv.client.format.fig;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfiguration;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfigurationImpl;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Map;

/**
 * User: ALB
 * Date: 11/08/14 15:49
 */
@Service
@Scope("singleton")
public class FigConfigurationConverter {

    public ContainerizedApplicationConfiguration convertApplicationConfiguration(String appConfId, String appConfName, String figAppConfig) {
        FigApplicationConfiguration figAppConfiguration = readFigAppConfiguration(figAppConfig);
        ContainerizedApplicationConfigurationImpl appConf = new ContainerizedApplicationConfigurationImpl();
        appConf.setId(appConfId);
        appConf.setName(appConfName);
        for (String serviceName : figAppConfiguration.keySet()) {
            FigServiceConfiguration serviceConfiguration = figAppConfiguration.get(serviceName);
            ContainerizedApplicationConfigurationImpl.ImageConfigurationImpl appImage = new ContainerizedApplicationConfigurationImpl.ImageConfigurationImpl();
            appImage.setId(serviceName.replace(" ", "_").replace(".", "_"));
            appImage.setName(serviceName);
            appImage.setSource(serviceConfiguration.getImage());
            // environment variables
            appImage.setEnvironment(new ArrayList<ContainerizedApplicationConfigurationImpl.EnvironmentVariableConfigurationImpl>());
            if (serviceConfiguration.getEnvironment() != null) {
                for (String environmentVariableName : serviceConfiguration.getEnvironment().keySet()) {
                    ContainerizedApplicationConfigurationImpl.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new ContainerizedApplicationConfigurationImpl.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariableName);
                    appEnvironmentVariable.setValue(serviceConfiguration.getEnvironment().get(environmentVariableName));
                    appImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            appImage.setLinks(new ArrayList<ContainerizedApplicationConfigurationImpl.LinkConfigurationImpl>());
            if (serviceConfiguration.getLinks() != null) {
                for (String link : serviceConfiguration.getLinks()) {
                    ContainerizedApplicationConfigurationImpl.LinkConfigurationImpl appLink = new ContainerizedApplicationConfigurationImpl.LinkConfigurationImpl();
                    appLink.setService(link);
                    appImage.getLinks().add(appLink);
                }
            }
            // ports
            appImage.setPorts(new ArrayList<ContainerizedApplicationConfigurationImpl.PortConfigurationImpl>());
            if (serviceConfiguration.getPorts() != null) {
                for (String port : serviceConfiguration.getPorts()) {
                    ContainerizedApplicationConfigurationImpl.PortConfigurationImpl appPort = new ContainerizedApplicationConfigurationImpl.PortConfigurationImpl();
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
            appImage.setVolumes(new ArrayList<ContainerizedApplicationConfigurationImpl.VolumeConfigurationImpl>());
            if (serviceConfiguration.getVolumes() != null) {
                for (String vol : serviceConfiguration.getVolumes()) {
                    ContainerizedApplicationConfigurationImpl.VolumeConfigurationImpl appVolume = new ContainerizedApplicationConfigurationImpl.VolumeConfigurationImpl();
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
            appConf.addImage(appImage);
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
