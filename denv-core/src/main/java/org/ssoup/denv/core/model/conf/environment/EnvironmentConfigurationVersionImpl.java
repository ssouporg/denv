package org.ssoup.denv.core.model.conf.environment;

import java.util.HashMap;
import java.util.Map;

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
    private String buildEnvId;
    private String buildError;
    private Map<String, String> variables = new HashMap<String, String>();

    public EnvironmentConfigurationVersionImpl() {
    }

    public EnvironmentConfigurationVersionImpl(String id, String envConfId, String version, Map<String, String> variables) {
        this.id = id;
        this.envConfId = envConfId;
        this.version = version;
        this.variables = variables;
        this.state = EnvironmentConfigVersionState.CREATED;
        this.desiredState = EnvironmentConfigVersionDesiredState.AVAILABLE;
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

    public String getBuildEnvId() {
        return buildEnvId;
    }

    public void setBuildEnvId(String buildEnvId) {
        this.buildEnvId = buildEnvId;
    }

    public String getBuildError() {
        return buildError;
    }

    public void setBuildError(String buildError) {
        this.buildError = buildError;
    }

    @Override
    public Map<String, String> getVariables() {
        return this.variables;
    }

    @Override
    public String getVariable(String variableName) {
        return this.variables.get(variableName);
    }

    @Override
    public void setVariable(String variableName, String value) {
        this.variables.put(variableName, value);
    }

    @Override
    public String substituteVariables(String v) {
        for (String var : getVariables().keySet()) {
            String value = getVariable(var);
            if (value != null) {
                v = v.replace("${" + var + "}", value);
            }
        }
        return v;
    }
}
