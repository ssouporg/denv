package org.ssoup.denv.server.naming;

import eu.eee.envs.config.Application;
import eu.eee.envs.config.EnvironmentConfiguration;
import eu.eee.envs.model.application.ApplicationInstance;
import eu.eee.envs.model.environment.Environment;

/**
 * User: ALB
 * Date: 28/02/14 10:39
 */
public class ContainerEnvsInfo {
    private String envId; // may be needed if env is not registered yet in env manager
    private EnvironmentConfiguration envConf; // may be needed if env is not registered yet in env manager
    private Environment env;
    private Application app; // may be needed if app instance is not yet registered in env
    private ApplicationInstance appInstance;
    private String imageType;

    public ApplicationInstance getAppInstance() {
        return appInstance;
    }

    public void setAppInstance(ApplicationInstance appInstance) {
        this.appInstance = appInstance;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public EnvironmentConfiguration getEnvConf() {
        return envConf;
    }

    public void setEnvConf(EnvironmentConfiguration envConf) {
        this.envConf = envConf;
    }

    public Application getApp() {
        return app;
    }

    public void setApp(Application app) {
        this.app = app;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getEnvId() {
        return envId;
    }

    public void setEnvId(String envId) {
        this.envId = envId;
    }
}
