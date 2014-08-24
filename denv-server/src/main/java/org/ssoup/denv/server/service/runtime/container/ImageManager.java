package org.ssoup.denv.server.service.runtime.container;

import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.runtime.container.Image;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;

/**
 * User: ALB
 * Date: 09/01/14 13:31
 */
public interface ImageManager {
    Image findImage(String imageName) throws DenvException;
    Image findImage(Environment env, ApplicationConfiguration appConf, String imageType) throws DenvException;
    Image findOrBuildImage(Environment env, ApplicationConfiguration appConf, String imageType) throws DenvException;
}
