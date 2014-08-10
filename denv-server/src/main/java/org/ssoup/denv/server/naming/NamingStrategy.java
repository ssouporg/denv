package org.ssoup.denv.server.naming;

import org.ssoup.denv.server.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.conf.env.EnvironmentConfiguration;
import org.ssoup.denv.server.containers.container.Container;
import org.ssoup.denv.server.containers.image.Image;
import org.ssoup.denv.server.domain.Application;
import org.ssoup.denv.server.domain.Environment;
import org.ssoup.denv.server.exception.DenvException;

/**
 * User: ALB
 * Date: 26/02/14 16:19
 */
public interface NamingStrategy {
    String generateImageName(EnvironmentConfiguration envConf, ApplicationConfiguration appConf, String imageType);
    String generateContainerName(Environment env, Application app, String imageType) throws DenvException;

    boolean isEnvContainer(String containerName);

    ImageEnvsInfo extractEnvsInfoFromImage(Image image) throws DenvException;
    ContainerEnvsInfo extractEnvsInfoFromContainer(Container container) throws DenvException;
}
