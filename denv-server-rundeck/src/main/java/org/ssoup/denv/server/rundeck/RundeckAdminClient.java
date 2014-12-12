package org.ssoup.denv.server.rundeck;

import org.springframework.stereotype.Service;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.server.service.admin.AbstractAdminClient;

/**
 * User: ALB
 * Date: 20/08/14 11:25
 */
@Service
public class RundeckAdminClient extends AbstractAdminClient {

    @Override
    public void deployApplication(String hostname, EnvironmentConfiguration appConf) {

    }
}
