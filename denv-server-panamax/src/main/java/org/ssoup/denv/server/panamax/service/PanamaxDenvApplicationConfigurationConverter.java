package org.ssoup.denv.server.panamax.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.common.model.application.ApplicationConfiguration;
import org.ssoup.denv.common.model.application.DenvApplicationConfiguration;
import org.ssoup.denv.server.panamax.domain.conf.PanamaxApplicationConfiguration;

import java.util.ArrayList;

/**
 * User: ALB
 * Date: 11/08/14 15:49
 */
@Service
@Scope("singleton")
public class PanamaxDenvApplicationConfigurationConverter {

    public ApplicationConfiguration convertApplicationConfiguration(PanamaxApplicationConfiguration panamaxAppConfig) {
        DenvApplicationConfiguration appConf = new DenvApplicationConfiguration();
        appConf.setName(panamaxAppConfig.getName());
        appConf.setDescription(panamaxAppConfig.getDescription());
        appConf.setImages(new ArrayList<DenvApplicationConfiguration.ImageConfigurationImpl>());
        for (PanamaxApplicationConfiguration.Image image : panamaxAppConfig.getImages()) {
            DenvApplicationConfiguration.ImageConfigurationImpl appImage = new DenvApplicationConfiguration.ImageConfigurationImpl();
            appImage.setName(image.getName());
            appImage.setDescription(image.getDescription());
            appImage.setSource(image.getSource());
            // environment variables
            appImage.setEnvironment(new ArrayList<DenvApplicationConfiguration.EnvironmentVariableConfigurationImpl>());
            if (image.getEnvironment() != null) {
                for (PanamaxApplicationConfiguration.EnvironmentVariable environmentVariable : image.getEnvironment()) {
                    DenvApplicationConfiguration.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new DenvApplicationConfiguration.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariable.getVariable());
                    appEnvironmentVariable.setValue(environmentVariable.getValue());
                    appImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            appImage.setLinks(new ArrayList<DenvApplicationConfiguration.LinkConfigurationImpl>());
            if (image.getLinks() != null) {
                for (PanamaxApplicationConfiguration.Link link : image.getLinks()) {
                    DenvApplicationConfiguration.LinkConfigurationImpl appLink = new DenvApplicationConfiguration.LinkConfigurationImpl();
                    appLink.setService(link.getService());
                    appLink.setAlias(link.getAlias());
                    appImage.getLinks().add(appLink);
                }
            }
            // ports
            appImage.setPorts(new ArrayList<DenvApplicationConfiguration.PortConfigurationImpl>());
            if (image.getPorts() != null) {
                for (PanamaxApplicationConfiguration.Port port : image.getPorts()) {
                    DenvApplicationConfiguration.PortConfigurationImpl appPort = new DenvApplicationConfiguration.PortConfigurationImpl();
                    appPort.setHostPort(port.getHost_port());
                    appPort.setContainerPort(port.getContainer_port());
                    appImage.getPorts().add(appPort);
                }
            }
            // volumes
            appImage.setVolumes(new ArrayList<DenvApplicationConfiguration.VolumeConfigurationImpl>());
            if (image.getVolumes() != null) {
                for (PanamaxApplicationConfiguration.Volume vol : image.getVolumes()) {
                    DenvApplicationConfiguration.VolumeConfigurationImpl appVolume = new DenvApplicationConfiguration.VolumeConfigurationImpl();
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
