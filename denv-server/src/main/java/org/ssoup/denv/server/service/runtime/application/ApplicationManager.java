package org.ssoup.denv.server.service.runtime.application;

import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.exception.DenvException;

/**
 * User: ALB
 * Date: 11/12/13 11:51
 */
public interface ApplicationManager {

    void deployApplication(Environment env, Application app) throws DenvException;

    void startApplication(Environment env, Application app) throws DenvException;

    void stopApplication(Environment env, Application app) throws DenvException;

    void deleteApplication(Environment env, Application app) throws DenvException;
}
