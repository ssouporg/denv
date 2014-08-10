package org.ssoup.denv.server.containers.image;

import eu.eee.envs.Envs;
import eu.eee.envs.admin.AdminClient;
import eu.eee.envs.config.Application;
import eu.eee.envs.config.ApplicationConfiguration;
import eu.eee.envs.config.ConfigManager;
import eu.eee.envs.containers.container.Container;
import eu.eee.envs.containers.container.ContainerManagerFactory;
import eu.eee.envs.containers.docker.DockerContainerManager;
import eu.eee.envs.exception.EnvAutomationException;
import eu.eee.envs.model.environment.Environment;
import org.apache.commons.configuration.Configuration;

/**
 * User: ALB
 * Date: 13/01/14 14:28
 */
public abstract class AbstractImageManager implements ImageManager {

    private AdminClient adminClient = Envs.getAdminClient();

    private DockerContainerManager containerManager;

    private Configuration config = ConfigManager.getConfiguration();

    protected DockerContainerManager getContainerManager() {
        if (containerManager == null) {
            containerManager = (DockerContainerManager) ContainerManagerFactory.getDefaultContainerManager();
        }
        return containerManager;
    }

    @Override
    public Image findOrBuildImage(Environment env, Application app, String imageType) throws EnvAutomationException {
        Image image = this.findImage(env, app, imageType);
        if (image == null) {
            Image baseImage = this.findImage(config.getString("baseImage"));
            // A) Go via Rundeck
                // run a command to keep containers alive
                String[] command = new String[]{"tail", "-F", "/var/log/dmesg"};
                String containerName = Envs.getContainerNamingStrategy().generateContainerName(env, app, imageType);
                Container container = getContainerManager().createContainer(env, containerName, baseImage, command, null, null);
                adminClient.deployApplication(container.getHostname(), app);
            // or B)
                // String[] command = new String[]{"apt-get", "install", "synaptiq-sqo=" + application.getVersion()};
                // Container containers = containerizationManager.startNewContainer(env, baseImage, command);
            getContainerManager().saveContainerAsApplicationImage(env, container, app, "Web");
            return this.findImage(env, app, imageType);
        }
        return image;
    }

    protected ImageInfo extractImageInfoFromName(String imageName) throws EnvAutomationException {
        String[] tok = imageName.split(":");
        if (tok == null || tok.length < 2) {
            return null;
        }
        String[] tok2 = tok[0].split("-");
        if (tok2 == null || tok2.length < 2) {
            return null;
        }
        ImageInfo imageInfo = new ImageInfo();

        imageInfo.imageNameWithoutVersion = tok[0];
        String version = tok[1];
        String appConfName = tok2[0];
        ApplicationConfiguration appConf = Envs.getApplicationConfiguration(appConfName);
        //imageInfo.application = Envs.getApplicationInstanceFactory().createApplicationInstance(appConf);
        imageInfo.imageType = tok2[1];

        return imageInfo;
    }

    protected class ImageInfo {
        public String imageNameWithoutVersion;
        public Application application;
        public String imageType;
    }
}
