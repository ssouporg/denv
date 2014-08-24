package org.ssoup.denv.server.service.naming;

import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.runtime.container.Container;
import org.ssoup.denv.server.domain.runtime.container.Image;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;

/**
 * User: ALB
 * Date: 26/02/14 16:19
 */
public interface NamingStrategy {
    String generateImageName(String envName, ApplicationConfiguration appConf, String imageType);
    String generateContainerName(Environment env, ApplicationConfiguration appConf, String imageType) throws DenvException;

    boolean isEnvContainer(String containerName);

    ImageEnvsInfo extractEnvsInfoFromImage(Image image) throws DenvException;
    ContainerEnvsInfo extractEnvsInfoFromContainer(Container container) throws DenvException;
}
