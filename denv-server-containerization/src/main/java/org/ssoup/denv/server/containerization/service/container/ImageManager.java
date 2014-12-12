package org.ssoup.denv.server.containerization.service.container;

import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.containerization.model.runtime.Image;
import org.ssoup.denv.core.exception.DenvException;

/**
 * User: ALB
 * Date: 09/01/14 13:31
 */
public interface ImageManager {
    Image findImage(Environment env, ImageConfiguration imageConf) throws DenvException;
    Image findOrBuildImage(Environment env, ImageConfiguration imageConf) throws DenvException;
}
