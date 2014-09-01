package org.ssoup.denv.server.service.runtime.container;

import org.ssoup.denv.common.model.application.ImageConfiguration;
import org.ssoup.denv.server.domain.runtime.container.Image;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;

/**
 * User: ALB
 * Date: 09/01/14 13:31
 */
public interface ImageManager {
    Image findImage(Environment env, ImageConfiguration imageConf) throws DenvException;
    Image findOrBuildImage(Environment env, ImageConfiguration imageConf) throws DenvException;
}
