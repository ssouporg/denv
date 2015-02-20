package org.ssoup.denv.core.model.conf.environment;

import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * User: ALB
 * Date: 12/09/14 10:11
 */
public interface EnvironmentConfigurationVersion {
    @Id
    String getId();

    String getEnvConfId();
    String getVersion();
    EnvironmentConfigVersionState getActualState();
    EnvironmentConfigVersionDesiredState getDesiredState();
    Map<String, String> getVariables();
    String getVariable(String variableName);
    String substituteVariables(String v);
    String getBuildEnvId();
    String getBuildError();
}
