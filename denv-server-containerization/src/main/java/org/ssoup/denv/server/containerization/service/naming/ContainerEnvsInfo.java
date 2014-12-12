package org.ssoup.denv.server.containerization.service.naming;

import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.Environment;

/**
 * User: ALB
 * Date: 28/02/14 10:39
 */
public class ContainerEnvsInfo {
    private String envId; // may be needed if env is not registered yet in env manager
    private Environment env;
    private EnvironmentRuntimeInfo app;
    private EnvironmentConfiguration appConf; // may be needed if no runtime is running
    private String imageType;

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public EnvironmentRuntimeInfo getApp() {
        return app;
    }

    public void setApp(EnvironmentRuntimeInfo app) {
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

    public EnvironmentConfiguration getAppConf() {
        return appConf;
    }

    public void setAppConf(EnvironmentConfiguration appConf) {
        this.appConf = appConf;
    }
}
