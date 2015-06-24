package org.ssoup.denv.client.format.panamax;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfigurationImpl;
import org.ssoup.denv.core.containerization.model.runtime.ContainerDesiredState;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;

/**
 * User: ALB
 * Date: 11/08/14 15:49
 */
@Service
@Scope("singleton")
public class PanamaxConfigurationConverter {

    public ContainerizedEnvironmentConfiguration convertEnvironmentConfiguration(String panamaxAppConfig) {
        Yaml yaml = new Yaml();
        PanamaxApplicationConfiguration panamaxAppConfiguration = yaml.loadAs(panamaxAppConfig, PanamaxApplicationConfiguration.class);
        ContainerizedEnvironmentConfigurationImpl envConf = new ContainerizedEnvironmentConfigurationImpl();
        envConf.setId(panamaxAppConfiguration.getName().replace(" ", "_").replace(".", "_"));
        envConf.setName(panamaxAppConfiguration.getName());
        envConf.setDescription(panamaxAppConfiguration.getDescription());
        envConf.setBuilderEnvConfId(panamaxAppConfiguration.getBuilderEnvConfId());
        for (PanamaxApplicationConfiguration.Image image : panamaxAppConfiguration.getImages()) {
            ContainerizedEnvironmentConfigurationImpl.ImageConfigurationImpl envImage = new ContainerizedEnvironmentConfigurationImpl.ImageConfigurationImpl();
            envImage.setId(image.getName().replace(" ", "_").replace(".", "_"));
            envImage.setName(image.getName());
            envImage.setDescription(image.getDescription());
            envImage.setSource(image.getSource());
            envImage.setCommand(image.getCommmand());
            envImage.setReadyWhenRespondingOnUrl(image.getReadyWhenRespondingOnUrl());
            envImage.setBuildCommand(image.getBuildCommand());
            envImage.setServicesToVersionWhenBuildSucceeds(image.getServicesToVersionWhenBuildSucceeds());
            envImage.setTargetImage(image.getTargetImage());
            // environment variables
            envImage.setEnvironment(new ArrayList<ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl>());
            if (image.getEnvironment() != null) {
                for (PanamaxApplicationConfiguration.EnvironmentVariable environmentVariable : image.getEnvironment()) {
                    ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariable.getVariable());
                    appEnvironmentVariable.setValue(environmentVariable.getValue());
                    envImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            envImage.setLinks(new ArrayList<ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl>());
            if (image.getLinks() != null) {
                for (PanamaxApplicationConfiguration.Link link : image.getLinks()) {
                    ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl appLink = new ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl();
                    appLink.setService(link.getService());
                    appLink.setAlias(link.getAlias());
                    envImage.getLinks().add(appLink);
                }
            }
            // ports
            envImage.setPorts(new ArrayList<ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl>());
            if (image.getPorts() != null) {
                for (PanamaxApplicationConfiguration.Port port : image.getPorts()) {
                    ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl appPort = new ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl();
                    appPort.setHostPort(port.getHost_port());
                    appPort.setContainerPort(port.getContainer_port());
                    envImage.getPorts().add(appPort);
                }
            }
            // volumes
            envImage.setVolumes(new ArrayList<ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl>());
            if (image.getVolumes() != null) {
                for (PanamaxApplicationConfiguration.Volume vol : image.getVolumes()) {
                    ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl appVolume = new ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl();
                    appVolume.setHostPath(vol.getHost_path());
                    appVolume.setContainerPath(vol.getContainer_path());
                    envImage.getVolumes().add(appVolume);
                }
            }
            // denv variables
            envImage.setVariables(new ArrayList<ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl>());
            if (image.getVariables() != null) {
                for (PanamaxApplicationConfiguration.DenvVariable var : image.getVariables()) {
                    ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl denvVariable = new ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl();
                    denvVariable.setVariable(var.getVariable());
                    denvVariable.setValue(var.getValue());
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
}
