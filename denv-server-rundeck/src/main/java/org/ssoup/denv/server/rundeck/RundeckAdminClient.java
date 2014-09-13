package org.ssoup.denv.server.rundeck;

import org.springframework.stereotype.Service;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.service.admin.AbstractAdminClient;

/**
 * User: ALB
 * Date: 20/08/14 11:25
 */
@Service
public class RundeckAdminClient extends AbstractAdminClient {

    @Override
    public void deployApplication(String hostname, ApplicationConfiguration appConf) {

    }
}
