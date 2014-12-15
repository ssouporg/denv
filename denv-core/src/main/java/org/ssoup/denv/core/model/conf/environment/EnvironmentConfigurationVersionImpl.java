package org.ssoup.denv.core.model.conf.environment;

/**
 * User: ALB
 * Date: 15/12/14 10:04
 */
public class EnvironmentConfigurationVersionImpl implements EnvironmentConfigurationVersion {

    private String id;
    private String envConfId;
    private String version;
    private EnvironmentConfigVersionState state;
    private EnvironmentConfigVersionDesiredState desiredState;

    public EnvironmentConfigurationVersionImpl() {
    }

    public EnvironmentConfigurationVersionImpl(String envConfId, String version) {
        this.id = buildVersionId(envConfId, version);
        this.envConfId = envConfId;
        this.version = version;
        this.state = EnvironmentConfigVersionState.CREATED;
        this.desiredState = EnvironmentConfigVersionDesiredState.AVAILABLE;
    }

    private String buildVersionId(String envConfId, String version) {
        return envConfId + "-" + version;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getEnvConfId() {
        return envConfId;
    }

    public void setEnvConfId(String envConfId) {
        this.envConfId = envConfId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public EnvironmentConfigVersionState getActualState() {
        return state;
    }

    public void setActualState(EnvironmentConfigVersionState state) {
        this.state = state;
    }

    @Override
    public EnvironmentConfigVersionDesiredState getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(EnvironmentConfigVersionDesiredState desiredState) {
        this.desiredState = desiredState;
    }
}
