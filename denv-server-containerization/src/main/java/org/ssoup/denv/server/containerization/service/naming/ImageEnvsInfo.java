package org.ssoup.denv.server.containerization.service.naming;

import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;

/**
 * User: ALB
 * Date: 28/02/14 10:39
 */
public class ImageEnvsInfo {
    private ApplicationConfiguration appConf;
    private String appVersion;
    private String imageType;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public ApplicationConfiguration getAppConf() {
        return appConf;
    }

    public void setAppConf(ApplicationConfiguration appConf) {
        this.appConf = appConf;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
