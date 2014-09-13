package org.ssoup.denv.server.containerization.service.naming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.containerization.domain.runtime.Container;
import org.ssoup.denv.server.containerization.domain.runtime.Image;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;

/**
 * {environmentId}-{appConfId}-{imageId}
 * i.e: acceptance-WPwithFig-DB
 *
 * User: ALB
 * Date: 26/02/14 16:21
 */
@Service
public class DefaultNamingStrategy implements NamingStrategy {

    public static final String SEPARATOR = "-";

    private ApplicationConfigurationManager applicationConfigurationManager;

    @Autowired
    public DefaultNamingStrategy(ApplicationConfigurationManager applicationConfigurationManager) {
        this.applicationConfigurationManager = applicationConfigurationManager;
    }

    @Override
    public String generateImageName(String envName, ApplicationConfiguration appConf, String imageType) {
        return appConf.getId() + SEPARATOR + imageType;
    }

    @Override
    public String generateContainerName(Environment env, String imageName) throws DenvException {
        // ex: 14-APP-DB:4.3-2
        // return env.getId() + SEPARATOR + env.getId() + SEPARATOR + appConf.getId() + SEPARATOR + imageType;
        return env.getId() + SEPARATOR + imageName;
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
        ApplicationConfiguration appConf = applicationConfigurationManager.getApplicationConfiguration(appConfName);
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
        //Environment env = environmentManager.findEnvironment(envId);
        //containerInfo.setEnv(env);

        String envConfName = tok[1];
        /*EnvironmentConfiguration envConf = environmentConfigurationManager.getEnvironmentConfiguration(envConfName);
        containerInfo.setEnvConf(envConf);*/

        String appName = tok[2];
        /*ApplicationConfiguration appConf = envConf.getApplicationConfiguration(appName);
        containerInfo.setAppConf(appConf);*/

        String imageType = tok[3];
        containerInfo.setImageType(imageType);

        /*if (env != null) {
            Application app = env.getApplication(appName);
            containerInfo.setApp(app);
        }*/

        return containerInfo;
    }
}
