package org.ssoup.denv.server.fig.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.conf.application.DenvApplicationConfiguration;
import org.ssoup.denv.server.fig.domain.conf.FigApplicationConfiguration;
import org.ssoup.denv.server.fig.domain.conf.FigServiceConfiguration;

import java.util.ArrayList;

/**
 * User: ALB
 * Date: 11/08/14 15:49
 */
@Service
@Scope("singleton")
public class FigDenvApplicationConfigurationConverter {

    public ApplicationConfiguration convertApplicationConfiguration(FigApplicationConfiguration figAppConfig) {
        DenvApplicationConfiguration appConf = new DenvApplicationConfiguration();
        appConf.setImages(new ArrayList<DenvApplicationConfiguration.ImageConfigurationImpl>());
        for (String serviceName : figAppConfig.keySet()) {
            FigServiceConfiguration serviceConfiguration = figAppConfig.get(serviceName);
            DenvApplicationConfiguration.ImageConfigurationImpl appImage = new DenvApplicationConfiguration.ImageConfigurationImpl();
            appImage.setName(serviceName);
            appImage.setSource(serviceConfiguration.getImage());
            // environment variables
            appImage.setEnvironment(new ArrayList<DenvApplicationConfiguration.EnvironmentVariableConfigurationImpl>());
            if (serviceConfiguration.getEnvironment() != null) {
                for (String environmentVariableName : serviceConfiguration.getEnvironment().keySet()) {
                    DenvApplicationConfiguration.EnvironmentVariableConfigurationImpl appEnvironmentVariable = new DenvApplicationConfiguration.EnvironmentVariableConfigurationImpl();
                    appEnvironmentVariable.setVariable(environmentVariableName);
                    appEnvironmentVariable.setValue(serviceConfiguration.getEnvironment().get(environmentVariableName));
                    appImage.getEnvironment().add(appEnvironmentVariable);
                }
            }
            // links
            appImage.setLinks(new ArrayList<DenvApplicationConfiguration.LinkConfigurationImpl>());
            if (serviceConfiguration.getLinks() != null) {
                for (String link : serviceConfiguration.getLinks()) {
                    DenvApplicationConfiguration.LinkConfigurationImpl appLink = new DenvApplicationConfiguration.LinkConfigurationImpl();
                    appLink.setService(link);
                    appImage.getLinks().add(appLink);
                }
            }
            // ports
            appImage.setPorts(new ArrayList<DenvApplicationConfiguration.PortConfigurationImpl>());
            if (serviceConfiguration.getPorts() != null) {
                for (String port : serviceConfiguration.getPorts()) {
                    DenvApplicationConfiguration.PortConfigurationImpl appPort = new DenvApplicationConfiguration.PortConfigurationImpl();
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
            appImage.setVolumes(new ArrayList<DenvApplicationConfiguration.VolumeConfigurationImpl>());
            if (serviceConfiguration.getVolumes() != null) {
                for (String vol : serviceConfiguration.getVolumes()) {
                    DenvApplicationConfiguration.VolumeConfigurationImpl appVolume = new DenvApplicationConfiguration.VolumeConfigurationImpl();
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
}
