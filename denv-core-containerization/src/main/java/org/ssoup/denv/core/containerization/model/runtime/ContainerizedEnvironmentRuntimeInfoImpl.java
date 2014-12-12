package org.ssoup.denv.core.containerization.model.runtime;

import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfoImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ALB
 * Date: 26/02/14 14:13
 */
public class ContainerizedEnvironmentRuntimeInfoImpl extends EnvironmentRuntimeInfoImpl implements ContainerizedEnvironmentRuntimeInfo {

    private Map<String, ContainerRuntimeInfo> containersRuntimeInfo = new HashMap<String, ContainerRuntimeInfo>();

    public ContainerizedEnvironmentRuntimeInfoImpl() {
    }

    public ContainerizedEnvironmentRuntimeInfoImpl(DenvContainerizedEnvironment env, ContainerizedEnvironmentConfiguration envConf) {
        for (ImageConfiguration imageConf : envConf.getImages().values()) {
            if (env != null && env.getRuntimeInfo() != null && env.getRuntimeInfo().getContainerRuntimeInfo(imageConf.getId()) != null) {
                setContainerRuntimeInfo(imageConf.getId(), env.getRuntimeInfo().getContainerRuntimeInfo(imageConf.getId()));
            } else {
                ContainerRuntimeInfo containerInfo = new ContainerRuntimeInfoImpl(imageConf);
                setContainerRuntimeInfo(imageConf.getId(), containerInfo);
            }
        }
    }

    public ContainerizedEnvironmentRuntimeInfoImpl(ContainerizedEnvironmentRuntimeInfo envInfo) {
        if (envInfo != null) {
            for (ContainerRuntimeInfo containerInfo : envInfo.getContainersRuntimeInfo().values()) {
                this.setContainerRuntimeInfo(containerInfo.getImageConfigurationId(), containerInfo);
            }
        }
    }

    @Override
    public ContainerRuntimeInfo getContainerRuntimeInfo(String imageName) {
        return this.containersRuntimeInfo.get(imageName);
    }

    @Override
    public Map<String, ContainerRuntimeInfo> getContainersRuntimeInfo() {
        return containersRuntimeInfo;
    }

    @Override
    public void setContainerRuntimeInfo(String imageId, ContainerRuntimeInfo containerInfo) {
        this.containersRuntimeInfo.put(imageId, containerInfo);
    }
}
