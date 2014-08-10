package org.ssoup.denv.server.containers.image;

import org.ssoup.denv.server.domain.Application;
import org.ssoup.denv.server.domain.Environment;
import org.ssoup.denv.server.exception.DenvException;

/**
 * User: ALB
 * Date: 09/01/14 13:31
 */
public interface ImageManager {
    Image findImage(String imageName) throws DenvException;
    Image findImage(Environment env, Application application, String imageType) throws DenvException;
    Image findOrBuildImage(Environment env, Application application, String imageType) throws DenvException;
}
