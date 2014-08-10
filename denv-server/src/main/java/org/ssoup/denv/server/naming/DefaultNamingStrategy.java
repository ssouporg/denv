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
 * Date: 26/02/14 16:21
 */
public class DefaultNamingStrategy implements NamingStrategy {

    public static final String SEPARATOR = "-";

    @Override
    public String generateImageName(EnvironmentConfiguration envConf, ApplicationConfiguration appConf, String imageType) {
        return appConf.getName() + SEPARATOR + imageType;
    }

    @Override
    public String generateContainerName(Environment env, Application app, String imageType) throws DenvException {
        // ex: 14-SQO-DB:4.3-2
        return env.getId() + SEPARATOR + env.getConf().getName() + SEPARATOR + app.getName() + SEPARATOR + imageType;
    }

    @Override
    public boolean isEnvContainer(String containerName) {
        String[] tok = containerName.split(SEPARATOR);
        return tok.length == 4;
    }

    @Override
    public ImageEnvsInfo extractEnvsInfoFromImage(Image image) throws DenvException {
        ImageEnvsInfo imageInfo = new ImageEnvsInfo();
        String[] tok = image.getName().split(SEPARATOR);
        if (tok == null || tok.length < 2) {
            return null;
        }
        String appConfName = tok[0];
        String imageType = tok[1];
        ApplicationConfiguration appConf = Envs.getApplicationConfiguration(appConfName);
        imageInfo.setAppConf(appConf);
        imageInfo.setAppVersion(image.getTag());
        imageInfo.setImageType(imageType);
        return imageInfo;
    }

    @Override
    public ContainerEnvsInfo extractEnvsInfoFromContainer(Container container) throws DenvException {
        ContainerEnvsInfo containerInfo = new ContainerEnvsInfo();
        String[] tok = container.getName().split(SEPARATOR);
        if (tok == null || tok.length < 4) {
            return null;
        }
        String envId = tok[0];
        containerInfo.setEnvId(envId);
        Environment env = Envs.getEnvironmentManager().findEnvironment(envId);
        containerInfo.setEnv(env);

        String envConfName = tok[1];
        EnvironmentConfiguration envConf = Envs.getEnvironmentConfiguration(envConfName);
        containerInfo.setEnvConf(envConf);

        String appName = tok[2];
        Application app = envConf.findApplication(appName);
        containerInfo.setApp(app);

        String imageType = tok[3];
        containerInfo.setImageType(imageType);

        if (env != null) {
            ApplicationInstance appInstance = env.getApplicationInstance(appName);
            containerInfo.setAppInstance(appInstance);
        }

        return containerInfo;
    }
}
