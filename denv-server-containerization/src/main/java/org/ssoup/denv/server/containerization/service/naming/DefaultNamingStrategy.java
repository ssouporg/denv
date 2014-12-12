package org.ssoup.denv.server.containerization.service.naming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.containerization.model.runtime.Container;
import org.ssoup.denv.core.containerization.model.runtime.Image;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;

/**
 * {environmentId}-{imageConfId}[-{snapshotName}]
 * i.e: acceptance-sqo-db-testscenario1
 *
 * User: ALB
 * Date: 26/02/14 16:21
 */
@Service
public class DefaultNamingStrategy implements NamingStrategy {

    public static final String SEPARATOR = "-";

    private EnvironmentConfigRepository environmentConfigRepository;

    @Autowired
    public DefaultNamingStrategy(EnvironmentConfigRepository environmentConfigRepository) {
        this.environmentConfigRepository = environmentConfigRepository;
    }

    @Override
    public String generateImageName(EnvironmentConfiguration envConf, ImageConfiguration imageConf) {
        if (imageConf.getSource() != null) {
            // if the image configuration specifies a source, use it
            String[] toks = imageConf.getSource().split(":");
            if (toks.length == 2) {
                // if the image source contains a version, ignore it
                return toks[0];
            } else {
                // otherwise use the environment version
                return imageConf.getSource();
            }
        } else {
            return envConf.getId() + SEPARATOR + imageConf.getId();
        }
    }

    @Override
    public String generateImageName(Environment env, ImageConfiguration imageConf) {
        return generateImageName(env, imageConf, env.getSnapshotName());
    }

    @Override
    public String generateImageName(Environment env, ImageConfiguration imageConf, String snapshotName) {
        EnvironmentConfiguration envConf = (EnvironmentConfiguration) this.environmentConfigRepository.findOne(env.getEnvironmentConfigurationId());
        String imageName = generateImageName(envConf, imageConf);
        if (snapshotName != null) {
            imageName += SEPARATOR + snapshotName;
        }
        return imageName;
    }

    @Override
    public String generateContainerName(Environment env, String imageId) {
        ContainerizedEnvironmentConfiguration envConf = (ContainerizedEnvironmentConfiguration) this.environmentConfigRepository.findOne(env.getEnvironmentConfigurationId());
        ImageConfiguration imageConf = envConf.getImageConfiguration(imageId);
        return generateContainerName(env, imageConf);
    }

    @Override
    public String generateContainerName(Environment env, ImageConfiguration imageConf) {
        // ex: 14-sqo-db
        return env.getId() + SEPARATOR + imageConf.getId();
    }

    @Override
    public boolean isEnvContainer(String containerName) {
        String[] tok = containerName.split(SEPARATOR);
        return tok.length == 4;
    }

    @Override
    public ImageEnvsInfo extractEnvsInfoFromImage(Image image) throws DenvException {
        ImageEnvsInfo imageInfo = new ImageEnvsInfo();
        /*String[] tok = image.getName().split(SEPARATOR);
        if (tok == null || tok.length < 2) {
            return null;
        }
        String appConfName = tok[0];
        String imageType = tok[1];
        ApplicationConfiguration appConf = applicationConfigurationManager.getApplicationConfiguration(appConfName);
        imageInfo.setAppConf(appConf);
        imageInfo.setAppVersion(image.getTag());
        imageInfo.setImageType(imageType);*/
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
