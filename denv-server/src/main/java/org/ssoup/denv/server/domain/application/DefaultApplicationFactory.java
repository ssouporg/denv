package org.ssoup.denv.server.domain.application;

import org.springframework.stereotype.Service;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.exception.DenvException;

/**
 * User: ALB
 * Date: 26/02/14 16:57
 */
@Service
public class DefaultApplicationFactory implements ApplicationFactory {

    @Override
    public Application createApplication(String name, ApplicationConfiguration conf) throws DenvException {
        return new ApplicationImpl(name, conf);
    }
}
