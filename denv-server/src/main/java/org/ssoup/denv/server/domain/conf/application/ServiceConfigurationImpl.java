package org.ssoup.denv.server.domain.conf.application;

import java.util.Map;

/**
 * User: ALB
 * Date: 11/08/14 15:45
 */
public class ServiceConfigurationImpl implements ServiceConfiguration {

    private String name;

    private String image;

    private VolumeInfo[] volumeInfos;

    private String command;

    private Map<Integer, Integer> ports;

    public ServiceConfigurationImpl() {
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public VolumeInfo[] getVolumeInfos() {
        return volumeInfos;
    }

    @Override
    public String getCommand() {
        return command;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setVolumeInfos(VolumeInfo[] volumeInfos) {
        this.volumeInfos = volumeInfos;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Map<Integer, Integer> getPorts() {
        return ports;
    }

    public void setPorts(Map<Integer, Integer> ports) {
        this.ports = ports;
    }
}
