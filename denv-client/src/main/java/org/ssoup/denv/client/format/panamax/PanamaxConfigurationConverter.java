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
        ContainerizedEnvironmentConfigurationImpl appConf = new ContainerizedEnvironmentConfigurationImpl();
        appConf.setId(panamaxAppConfiguration.getName().replace(" ", "_").replace(".", "_"));
        appConf.setName(panamaxAppConfiguration.getName());
        appConf.setDescription(panamaxAppConfiguration.getDescription());
        for (PanamaxApplicationConfiguration.Image image : panamaxAppConfiguration.getImages()) {
            ContainerizedEnvironmentConfigurationImpl.ImageConfigurationImpl appImage = new ContainerizedEnvironmentConfigurationImpl.ImageConfigurationImpl();
            appImage.setId(image.getName().replace(" ", "_").replace(".", "_"));
            appImage.setName(image.getName());
            appImage.setDescription(image.getDescription());
            appImage.setSource(image.getSource());
            // environment variables
            appImage.setEnvironment(new ArrayList<ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl>());
            if (image.getEnvironment() != null) {
                for (PanamaxApplicationConfiguration.EnvironmentVariable environmentVariable : image.getEnvironment()) {
                    ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new ContainerizedEnvironmentConfigurationImpl.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariable.getVariable());
                    appEnvironmentVariable.setValue(environmentVariable.getValue());
                    appImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            appImage.setLinks(new ArrayList<ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl>());
            if (image.getLinks() != null) {
                for (PanamaxApplicationConfiguration.Link link : image.getLinks()) {
                    ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl appLink = new ContainerizedEnvironmentConfigurationImpl.LinkConfigurationImpl();
                    appLink.setService(link.getService());
                    appLink.setAlias(link.getAlias());
                    appImage.getLinks().add(appLink);
                }
            }
            // ports
            appImage.setPorts(new ArrayList<ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl>());
            if (image.getPorts() != null) {
                for (PanamaxApplicationConfiguration.Port port : image.getPorts()) {
                    ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl appPort = new ContainerizedEnvironmentConfigurationImpl.PortConfigurationImpl();
                    appPort.setHostPort(port.getHost_port());
                    appPort.setContainerPort(port.getContainer_port());
                    appImage.getPorts().add(appPort);
                }
            }
            // volumes
            appImage.setVolumes(new ArrayList<ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl>());
            if (image.getVolumes() != null) {
                for (PanamaxApplicationConfiguration.Volume vol : image.getVolumes()) {
                    ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl appVolume = new ContainerizedEnvironmentConfigurationImpl.VolumeConfigurationImpl();
                    appVolume.setHostPath(vol.getHost_path());
                    appVolume.setContainerPath(vol.getContainer_path());
                    appImage.getVolumes().add(appVolume);
                }
            }
            // denv variables
            appImage.setVariables(new ArrayList<ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl>());
            if (image.getVariables() != null) {
                for (PanamaxApplicationConfiguration.DenvVariable var : image.getVariables()) {
                    ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl denvVariable = new ContainerizedEnvironmentConfigurationImpl.DenvVariableConfigurationImpl();
                    denvVariable.setVariable(var.getVariable());
                    denvVariable.setValue(var.getValue());
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
}
