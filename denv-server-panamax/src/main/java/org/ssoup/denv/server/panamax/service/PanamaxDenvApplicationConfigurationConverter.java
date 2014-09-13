package org.ssoup.denv.server.panamax.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfiguration;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfigurationImpl;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.panamax.domain.conf.PanamaxApplicationConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;

/**
 * User: ALB
 * Date: 11/08/14 15:49
 */
@Service
@Scope("singleton")
public class PanamaxDenvApplicationConfigurationConverter {

    public ContainerizedApplicationConfiguration convertApplicationConfiguration(String panamaxAppConfig) {
        Yaml yaml = new Yaml();
        PanamaxApplicationConfiguration panamaxAppConfiguration = yaml.loadAs(panamaxAppConfig, PanamaxApplicationConfiguration.class);
        ContainerizedApplicationConfigurationImpl appConf = new ContainerizedApplicationConfigurationImpl();
        appConf.setId(panamaxAppConfiguration.getName());
        appConf.setDescription(panamaxAppConfiguration.getDescription());
        appConf.setImages(new ArrayList<ContainerizedApplicationConfigurationImpl.ImageConfigurationImpl>());
        for (PanamaxApplicationConfiguration.Image image : panamaxAppConfiguration.getImages()) {
            ContainerizedApplicationConfigurationImpl.ImageConfigurationImpl appImage = new ContainerizedApplicationConfigurationImpl.ImageConfigurationImpl();
            appImage.setId(image.getName());
            appImage.setDescription(image.getDescription());
            appImage.setSource(image.getSource());
            // environment variables
            appImage.setEnvironment(new ArrayList<ContainerizedApplicationConfigurationImpl.EnvironmentVariableConfigurationImpl>());
            if (image.getEnvironment() != null) {
                for (PanamaxApplicationConfiguration.EnvironmentVariable environmentVariable : image.getEnvironment()) {
                    ContainerizedApplicationConfigurationImpl.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new ContainerizedApplicationConfigurationImpl.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariable.getVariable());
                    appEnvironmentVariable.setValue(environmentVariable.getValue());
                    appImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            appImage.setLinks(new ArrayList<ContainerizedApplicationConfigurationImpl.LinkConfigurationImpl>());
            if (image.getLinks() != null) {
                for (PanamaxApplicationConfiguration.Link link : image.getLinks()) {
                    ContainerizedApplicationConfigurationImpl.LinkConfigurationImpl appLink = new ContainerizedApplicationConfigurationImpl.LinkConfigurationImpl();
                    appLink.setService(link.getService());
                    appLink.setAlias(link.getAlias());
                    appImage.getLinks().add(appLink);
                }
            }
            // ports
            appImage.setPorts(new ArrayList<ContainerizedApplicationConfigurationImpl.PortConfigurationImpl>());
            if (image.getPorts() != null) {
                for (PanamaxApplicationConfiguration.Port port : image.getPorts()) {
                    ContainerizedApplicationConfigurationImpl.PortConfigurationImpl appPort = new ContainerizedApplicationConfigurationImpl.PortConfigurationImpl();
                    appPort.setHostPort(port.getHost_port());
                    appPort.setContainerPort(port.getContainer_port());
                    appImage.getPorts().add(appPort);
                }
            }
            // volumes
            appImage.setVolumes(new ArrayList<ContainerizedApplicationConfigurationImpl.VolumeConfigurationImpl>());
            if (image.getVolumes() != null) {
                for (PanamaxApplicationConfiguration.Volume vol : image.getVolumes()) {
                    ContainerizedApplicationConfigurationImpl.VolumeConfigurationImpl appVolume = new ContainerizedApplicationConfigurationImpl.VolumeConfigurationImpl();
                    appVolume.setHostPath(vol.getHost_path());
                    appVolume.setContainerPath(vol.getContainer_path());
                    appImage.getVolumes().add(appVolume);
                }
            }
            appConf.getImages().add(appImage);
        }
        return appConf;
    }
}
