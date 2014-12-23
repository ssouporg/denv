package org.ssoup.denv.client.format.fig;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfigurationImpl;
import org.ssoup.denv.core.containerization.model.runtime.ContainerDesiredState;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * User: ALB
 * Date: 11/08/14 15:49
 */
@Service
@Scope("singleton")
public class FigConfigurationConverter {

    public ContainerizedEnvironmentConfiguration convertEnvironmentConfiguration
            (String appConfId, String appConfName, String builderEnvConfId, String figAppConfig) {
        FigApplicationConfiguration figAppConfiguration = readFigAppConfiguration(figAppConfig);
        ContainerizedEnvironmentConfigurationImpl envConf = new ContainerizedEnvironmentConfigurationImpl();
        envConf.setId(appConfId);
        envConf.setName(appConfName);
        envConf.setBuilderEnvConfId(builderEnvConfId);
        for (String serviceName : figAppConfiguration.keySet()) {
            FigServiceConfiguration serviceConfiguration = figAppConfiguration.get(serviceName);
            ContainerizedEnvironmentConfigurationImpl.ImageConfigurationImpl envImage = new ContainerizedEnvironmentConfigurationImpl.ImageConfigurationImpl();
            envImage.setId(serviceName.replace(" ", "_").replace(".", "_"));
            envImage.setName(serviceName);
            envImage.setSource(serviceConfiguration.getImage());
            envImage.setCommand(serviceConfiguration.getCommand());
            envImage.setBuildCommand(serviceConfiguration.getBuildCommand());
            envImage.setServicesToVersionWhenBuildSucceeds(serviceConfiguration.getServicesToVersionWhenBuildSucceeds());
            envImage.setTargetImage(serviceConfiguration.getTargetImage());
            envImage.setPrivileged(serviceConfiguration.isPrivileged());
            // environment variables
            envImage.setEnvironment(new ArrayList<ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl>());
            if (serviceConfiguration.getEnvironment() != null) {
                for (String environmentVariableName : serviceConfiguration.getEnvironment().keySet()) {
                    ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariableName);
                    appEnvironmentVariable.setValue(serviceConfiguration.getEnvironment().get(environmentVariableName));
                    envImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            envImage.setLinks(new ArrayList<ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl>());
            if (serviceConfiguration.getLinks() != null) {
                for (String link : serviceConfiguration.getLinks()) {
                    ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl appLink = new ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl();
                    appLink.setService(link);
                    envImage.getLinks().add(appLink);
                }
            }
            // ports
            envImage.setPorts(new ArrayList<ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl>());
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
                    envImage.getPorts().add(appPort);
                }
            }
            // volumes
            envImage.setVolumes(new ArrayList<ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl>());
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
                    envImage.getVolumes().add(appVolume);
                }
            }
            // denv variables
            envImage.setVariables(new ArrayList<ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl>());
            if (serviceConfiguration.getVariables() != null) {
                for (String denvVariableName : serviceConfiguration.getVariables().keySet()) {
                    ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl denvVariable = new ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl();
                    denvVariable.setVariable(denvVariableName);
                    denvVariable.setValue(serviceConfiguration.getVariables().get(denvVariableName));
                    envImage.getVariables().add(denvVariable);
                }
            }

            if (envImage.getBuildCommand() != null && envImage.getServicesToVersionWhenBuildSucceeds() != null) {
                envImage.setInitialDesiredState(ContainerDesiredState.SUCCEEDED);
            } else {
                if (envImage.getPorts().size() > 0) {
                    envImage.setInitialDesiredState(ContainerDesiredState.RESPONDING);
                } else {
                    envImage.setInitialDesiredState(ContainerDesiredState.STARTED);
                }
            }
            envConf.addImage(envImage);
        }
        return envConf;
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
