package org.ssoup.denv.server.domain.application;

import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.exception.DenvException;

/**
 * User: ALB
 * Date: 26/02/14 17:25
 */
public interface ApplicationFactory {

    public Application createApplication(String name, ApplicationConfiguration conf) throws DenvException;
}
