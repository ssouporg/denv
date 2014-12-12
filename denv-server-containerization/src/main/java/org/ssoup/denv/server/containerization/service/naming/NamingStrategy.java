package org.ssoup.denv.server.containerization.service.naming;

import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.containerization.model.runtime.Container;
import org.ssoup.denv.core.containerization.model.runtime.Image;
import org.ssoup.denv.core.exception.DenvException;

/**
 * User: ALB
 * Date: 26/02/14 16:19
 */
public interface NamingStrategy {
    String generateImageName(EnvironmentConfiguration envConf, ImageConfiguration imageConf) throws DenvException;
    String generateImageName(Environment env, ImageConfiguration imageConf) throws DenvException;

    String generateImageName(Environment env, ImageConfiguration imageConf, String snapshotName);

    String generateContainerName(Environment env, String image) throws DenvException;
    String generateContainerName(Environment env, ImageConfiguration imageConf) throws DenvException;

    boolean isEnvContainer(String containerName) throws DenvException;

    ImageEnvsInfo extractEnvsInfoFromImage(Image image) throws DenvException;
    ContainerEnvsInfo extractEnvsInfoFromContainer(Container container) throws DenvException;
}
