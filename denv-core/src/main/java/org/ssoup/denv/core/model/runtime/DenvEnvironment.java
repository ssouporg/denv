package org.ssoup.denv.core.model.runtime;

import org.ssoup.denv.core.model.conf.node.NodeConfiguration;

import java.util.*;

/**
 * User: ALB
 * Date: 08/01/14 17:30
 */
public abstract class DenvEnvironment implements Environment {

    private String id;

    private String name;

    private String environmentConfigurationId;
    private String version;
    private String snapshotName;

    private EnvironmentState state;
    private EnvironmentDesiredState desiredState;

    // Config info
    private NodeConfiguration node;

    /**
     * true for build environments
     */
    private boolean builder;
    private String builderTargetEnvConfId;
    private String builderTargetVersion;

    // Runtime info
    // map appId => Application
    private List<String> labels = new ArrayList<String>();

    public DenvEnvironment() {
    }

    public DenvEnvironment(Environment env) {
        this.id = env.getId();
        this.name = env.getName();
        this.environmentConfigurationId = env.getEnvironmentConfigurationId();
        this.version = env.getVersion();
        this.snapshotName = env.getSnapshotName();
        this.state = env.getActualState();
        this.desiredState = env.getDesiredState();
        this.node = env.getNode();
        this.builder = env.isBuilder();
        this.builderTargetEnvConfId = env.getBuilderTargetEnvConfId();
        this.builderTargetVersion = env.getBuilderTargetVersion();
    }

    public DenvEnvironment(String id, String name, String environmentConfigurationId, NodeConfiguration node) {
        this.id = id;
        this.name = name;
        this.state = EnvironmentState.CREATED;
        this.desiredState = EnvironmentDesiredState.STARTED;
        this.environmentConfigurationId = environmentConfigurationId;
        this.node = node;
    }

    public DenvEnvironment(String id, String name, String environmentConfigurationId, String version, NodeConfiguration node) {
        this.id = id;
        this.name = name;
        this.state = EnvironmentState.CREATED;
        this.desiredState = EnvironmentDesiredState.STARTED;
        this.environmentConfigurationId = environmentConfigurationId;
        this.version = version;
        this.node = node;
    }

    public DenvEnvironment(String id, String name, String environmentConfigurationId, String version, String snapshotName, NodeConfiguration node) {
        this.id = id;
        this.name = name;
        this.state = EnvironmentState.CREATED;
        this.desiredState = EnvironmentDesiredState.STARTED;
        this.environmentConfigurationId = environmentConfigurationId;
        this.node = node;
        this.version = version;
        this.snapshotName = snapshotName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public NodeConfiguration getNode() {
        return node;
    }

    @Override
    public Collection<String> getLabels() {
        return labels;
    }

    @Override
    public boolean hasLabel(String label) {
        return labels.contains(label);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnvironmentConfigurationId() {
        return environmentConfigurationId;
    }

    public void setEnvironmentConfigurationId(String environmentConfigurationId) {
        this.environmentConfigurationId = environmentConfigurationId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static String buildVersionId(String envConfId, String version) {
        return envConfId + ":" + version;
    }

    @Override
    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    @Override
    public EnvironmentState getActualState() {
        return state;
    }

    public void setActualState(EnvironmentState state) {
        this.state = state;
    }

    @Override
    public EnvironmentDesiredState getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(EnvironmentDesiredState desiredState) {
        this.desiredState = desiredState;
    }

    public boolean isBuilder() {
        return builder;
    }

    public void setBuilder(boolean builder) {
        this.builder = builder;
    }

    @Override
    public String getBuilderTargetEnvConfId() {
        return builderTargetEnvConfId;
    }

    public void setBuilderTargetEnvConfId(String builderTargetEnvConfId) {
        this.builderTargetEnvConfId = builderTargetEnvConfId;
    }

    @Override
    public String getBuilderTargetVersion() {
        return builderTargetVersion;
    }

    public void setBuilderTargetVersion(String builderTargetVersion) {
        this.builderTargetVersion = builderTargetVersion;
    }

    @Override
    public String getBuilderTarget() {
        if (builderTargetEnvConfId != null && builderTargetVersion != null) {
            return buildVersionId(builderTargetEnvConfId, builderTargetVersion);
        }
        return null;
    }
}
