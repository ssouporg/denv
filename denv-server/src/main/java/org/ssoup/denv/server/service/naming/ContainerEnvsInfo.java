package org.ssoup.denv.server.service.naming;

import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.application.Application;
import org.ssoup.denv.server.domain.environment.Environment;

/**
 * User: ALB
 * Date: 28/02/14 10:39
 */
public class ContainerEnvsInfo {
    private String envId; // may be needed if env is not registered yet in env manager
    private Environment env;
    private Application app;
    private ApplicationConfiguration appConf; // may be needed if no application is running
    private String imageType;

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
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

    public ApplicationConfiguration getAppConf() {
        return appConf;
    }

    public void setAppConf(ApplicationConfiguration appConf) {
        this.appConf = appConf;
    }
}
