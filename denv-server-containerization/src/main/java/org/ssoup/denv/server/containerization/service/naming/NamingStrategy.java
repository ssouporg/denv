package org.ssoup.denv.server.containerization.service.naming;

import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.containerization.domain.runtime.Container;
import org.ssoup.denv.server.containerization.domain.runtime.Image;
import org.ssoup.denv.core.exception.DenvException;

/**
 * User: ALB
 * Date: 26/02/14 16:19
 */
public interface NamingStrategy {
    String generateImageName(String envName, ApplicationConfiguration appConf, String imageType);
    String generateContainerName(Environment env, String imageName) throws DenvException;

    boolean isEnvContainer(String containerName);

    ImageEnvsInfo extractEnvsInfoFromImage(Image image) throws DenvException;
    ContainerEnvsInfo extractEnvsInfoFromContainer(Container container) throws DenvException;
}
