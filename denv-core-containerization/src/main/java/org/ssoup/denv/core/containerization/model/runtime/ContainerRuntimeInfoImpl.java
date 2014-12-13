package org.ssoup.denv.core.containerization.model.runtime;

import org.springframework.data.annotation.Id;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.PortConfiguration;

import java.util.Map;

/**
 * User: ALB
 * Date: 12/09/14 14:19
 */
public class ContainerRuntimeInfoImpl implements ContainerRuntimeInfo {

    @Id
    private String id;

    private String[] names;
    private String imageConfigurationId;

    private String hostname;
    private Map<Integer, Integer> portMapping;

    private ContainerDesiredState desiredState;
    private ContainerState actualState;

    private Map<String, String> variables;

    public ContainerRuntimeInfoImpl() {
    }

    public ContainerRuntimeInfoImpl(ImageConfiguration imageConf) {
        this.imageConfigurationId = imageConf.getId();
        this.desiredState = imageConf.getInitialDesiredState();
        this.actualState = ContainerState.UNDEPLOYED;
    }

    public ContainerRuntimeInfoImpl(ContainerRuntimeInfo containerInfo) {
        this.id = containerInfo.getId();
        this.names = containerInfo.getAllNames();
        this.hostname = containerInfo.getHostname();
        this.portMapping = containerInfo.getPortMapping();
        this.imageConfigurationId = containerInfo.getImageConfigurationId();
        this.desiredState = containerInfo.getDesiredState();
        this.actualState = containerInfo.getActualState();
        this.variables = containerInfo.getVariables();
    }

    public ContainerRuntimeInfoImpl(String id, Image image, ContainerDesiredState desiredState, ContainerState actualState) {
        this.id = id;
        this.imageConfigurationId = image.getConf().getId();
        this.desiredState = desiredState;
        this.actualState = actualState;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return names != null && names.length > 0 ? names[0] : null;
    }

    @Override
    public String[] getAllNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    @Override
    public ContainerDesiredState getDesiredState() {
        return desiredState;
    }

    @Override
    public ContainerState getActualState() {
        return this.actualState;
    }

    @Override
    public boolean canBeStarted() {
        return this.actualState == ContainerState.STOPPED ||
                this.actualState == ContainerState.KILLED_BY_USER ||
                this.actualState == ContainerState.KILLED_BY_DENV;
    }

    @Override
    public boolean canBeStopped() {
        return this.actualState == ContainerState.STARTING ||
                this.actualState == ContainerState.STOPPED ||
                this.actualState == ContainerState.RESTARTING ||
                this.actualState == ContainerState.RESPONDING ||
                this.actualState == ContainerState.NOT_RESPONDING;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public Map<Integer, Integer> getPortMapping() {
        return portMapping;
    }

    public void setPortMapping(Map<Integer, Integer> portMapping) {
        this.portMapping = portMapping;
    }

    public void setDesiredState(ContainerDesiredState desiredState) {
        this.desiredState = desiredState;
    }

    public void setActualState(ContainerState actualState) {
        this.actualState = actualState;
    }

    @Override
    public String getImageConfigurationId() {
        return imageConfigurationId;
    }

    public void setImageConfigurationId(String imageConfigurationId) {
        this.imageConfigurationId = imageConfigurationId;
    }

    @Override
    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public String getVariableValue(String variable) {
        return this.variables.get(variable);
    }
}
