package org.ssoup.denv.server.panamax.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.config.application.InMemoryDenvApplicationConfiguration;
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

    public ApplicationConfiguration convertApplicationConfiguration(String panamaxAppConfig) {
        Yaml yaml = new Yaml();
        PanamaxApplicationConfiguration panamaxAppConfiguration = yaml.loadAs(panamaxAppConfig, PanamaxApplicationConfiguration.class);
        InMemoryDenvApplicationConfiguration appConf = new InMemoryDenvApplicationConfiguration();
        appConf.setName(panamaxAppConfiguration.getName());
        appConf.setDescription(panamaxAppConfiguration.getDescription());
        appConf.setImages(new ArrayList<InMemoryDenvApplicationConfiguration.ImageConfigurationImpl>());
        for (PanamaxApplicationConfiguration.Image image : panamaxAppConfiguration.getImages()) {
            InMemoryDenvApplicationConfiguration.ImageConfigurationImpl appImage = new InMemoryDenvApplicationConfiguration.ImageConfigurationImpl();
            appImage.setName(image.getName());
            appImage.setDescription(image.getDescription());
            appImage.setSource(image.getSource());
            // environment variables
            appImage.setEnvironment(new ArrayList<InMemoryDenvApplicationConfiguration.EnvironmentVariableConfigurationImpl>());
            if (image.getEnvironment() != null) {
                for (PanamaxApplicationConfiguration.EnvironmentVariable environmentVariable : image.getEnvironment()) {
                    InMemoryDenvApplicationConfiguration.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new InMemoryDenvApplicationConfiguration.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariable.getVariable());
                    appEnvironmentVariable.setValue(environmentVariable.getValue());
                    appImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            appImage.setLinks(new ArrayList<InMemoryDenvApplicationConfiguration.LinkConfigurationImpl>());
            if (image.getLinks() != null) {
                for (PanamaxApplicationConfiguration.Link link : image.getLinks()) {
                    InMemoryDenvApplicationConfiguration.LinkConfigurationImpl appLink = new InMemoryDenvApplicationConfiguration.LinkConfigurationImpl();
                    appLink.setService(link.getService());
                    appLink.setAlias(link.getAlias());
                    appImage.getLinks().add(appLink);
                }
            }
            // ports
            appImage.setPorts(new ArrayList<InMemoryDenvApplicationConfiguration.PortConfigurationImpl>());
            if (image.getPorts() != null) {
                for (PanamaxApplicationConfiguration.Port port : image.getPorts()) {
                    InMemoryDenvApplicationConfiguration.PortConfigurationImpl appPort = new InMemoryDenvApplicationConfiguration.PortConfigurationImpl();
                    appPort.setHostPort(port.getHost_port());
                    appPort.setContainerPort(port.getContainer_port());
                    appImage.getPorts().add(appPort);
                }
            }
            // volumes
            appImage.setVolumes(new ArrayList<InMemoryDenvApplicationConfiguration.VolumeConfigurationImpl>());
            if (image.getVolumes() != null) {
                for (PanamaxApplicationConfiguration.Volume vol : image.getVolumes()) {
                    InMemoryDenvApplicationConfiguration.VolumeConfigurationImpl appVolume = new InMemoryDenvApplicationConfiguration.VolumeConfigurationImpl();
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
