package org.ssoup.denv.server.service.admin;

import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;

/**
 * User: ALB
 * Date: 11/12/13 17:14
 */
public interface AdminClient {
    void deployApplication(String hostname, ApplicationConfiguration appConf);
}
