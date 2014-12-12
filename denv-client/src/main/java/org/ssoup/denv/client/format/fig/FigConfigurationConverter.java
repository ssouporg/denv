package org.ssoup.denv.client.format.fig;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfigurationImpl;
import org.ssoup.denv.core.containerization.model.runtime.ContainerDesiredState;
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

    public ContainerizedEnvironmentConfiguration convertEnvironmentConfiguration(String appConfId, String appConfName, String figAppConfig) {
        FigApplicationConfiguration figAppConfiguration = readFigAppConfiguration(figAppConfig);
        ContainerizedEnvironmentConfigurationImpl appConf = new ContainerizedEnvironmentConfigurationImpl();
        appConf.setId(appConfId);
        appConf.setName(appConfName);
        for (String serviceName : figAppConfiguration.keySet()) {
            FigServiceConfiguration serviceConfiguration = figAppConfiguration.get(serviceName);
            ContainerizedEnvironmentConfigurationImpl.ImageConfigurationImpl appImage = new ContainerizedEnvironmentConfigurationImpl.ImageConfigurationImpl();
            appImage.setId(serviceName.replace(" ", "_").replace(".", "_"));
            appImage.setName(serviceName);
            appImage.setSource(serviceConfiguration.getImage());
            appImage.setPrivileged(serviceConfiguration.isPrivileged());
            // environment variables
            appImage.setEnvironment(new ArrayList<ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl>());
            if (serviceConfiguration.getEnvironment() != null) {
                for (String environmentVariableName : serviceConfiguration.getEnvironment().keySet()) {
                    ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariableName);
                    appEnvironmentVariable.setValue(serviceConfiguration.getEnvironment().get(environmentVariableName));
                    appImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            appImage.setLinks(new ArrayList<ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl>());
            if (serviceConfiguration.getLinks() != null) {
                for (String link : serviceConfiguration.getLinks()) {
                    ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl appLink = new ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl();
                    appLink.setService(link);
                    appImage.getLinks().add(appLink);
                }
            }
            // ports
            appImage.setPorts(new ArrayList<ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl>());
            if (serviceConfiguration.getPorts() != null) {
                for (String port : serviceConfiguration.getPorts()) {
                    ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl appPort = new ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl();
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
            appImage.setVolumes(new ArrayList<ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl>());
            if (serviceConfiguration.getVolumes() != null) {
                for (String vol : serviceConfiguration.getVolumes()) {
                    ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl appVolume = new ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl();
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
            // denv variables
            appImage.setVariables(new ArrayList<ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl>());
            if (serviceConfiguration.getVariables() != null) {
                for (String denvVariableName : serviceConfiguration.getVariables().keySet()) {
                    ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl denvVariable = new ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl();
                    denvVariable.setVariable(denvVariableName);
                    denvVariable.setValue(serviceConfiguration.getVariables().get(denvVariableName));
                    appImage.getVariables().add(denvVariable);
                }
            }

            if (appImage.getPorts().size() > 0) {
                appImage.setInitialDesiredState(ContainerDesiredState.RESPONDING);
            } else {
                appImage.setInitialDesiredState(ContainerDesiredState.STARTED);
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
