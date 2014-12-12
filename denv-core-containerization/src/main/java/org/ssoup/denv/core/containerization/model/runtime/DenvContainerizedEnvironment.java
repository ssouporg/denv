package org.ssoup.denv.core.containerization.model.runtime;

import org.ssoup.denv.core.model.conf.node.NodeConfiguration;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;

/**
 * User: ALB
 * Date: 16/11/2014 22:12
 */
public class DenvContainerizedEnvironment extends DenvEnvironment {

    private ContainerizedEnvironmentRuntimeInfo runtimeInfo;

    public DenvContainerizedEnvironment() {
    }

    public DenvContainerizedEnvironment(Environment env) {
        super(env);
    }

    public DenvContainerizedEnvironment(String id, String name, String environmentConfigurationId, ContainerizedEnvironmentRuntimeInfo envInfo, NodeConfiguration node) {
        super(id, name, environmentConfigurationId, node);
        this.runtimeInfo = envInfo;
    }

    public DenvContainerizedEnvironment(String id, String name, String environmentConfigurationId, String version, ContainerizedEnvironmentRuntimeInfo envInfo, NodeConfiguration node) {
        super(id, name, environmentConfigurationId, version, node);
        this.runtimeInfo = envInfo;
    }

    public DenvContainerizedEnvironment(String id, String name, String environmentConfigurationId, String version, String snapshotName, ContainerizedEnvironmentRuntimeInfo envInfo, NodeConfiguration node) {
        super(id, name, environmentConfigurationId, version, snapshotName, node);
        this.runtimeInfo = envInfo;
    }

    @Override
    public ContainerizedEnvironmentRuntimeInfo getRuntimeInfo() {
        return runtimeInfo;
    }

    public void setRuntimeInfo(ContainerizedEnvironmentRuntimeInfo runtimeInfo) {
        this.runtimeInfo = runtimeInfo;
    }
}
