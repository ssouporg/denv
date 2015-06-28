package org.ssoup.denv.server.containerization.service.container;

import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.containerization.model.runtime.Image;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.server.service.admin.AdminClient;
import org.ssoup.denv.server.containerization.service.versioning.ContainerVersioningPolicy;

/**
 * User: ALB
 * Date: 13/01/14 14:28
 */
public abstract class AbstractImageManager implements ImageManager {

    private AdminClient adminClient;

    private NamingStrategy namingStrategy;

    private ContainerVersioningPolicy versioningPolicy;

    protected AbstractImageManager(AdminClient adminClient, NamingStrategy namingStrategy, ContainerVersioningPolicy versioningPolicy) {
        this.adminClient = adminClient;
        this.namingStrategy = namingStrategy;
        this.versioningPolicy = versioningPolicy;
    }

    @Override
    public Image findOrBuildImage(EnvironmentConfiguration envConf, String envVersion, ImageConfiguration imageConf) throws DenvException {
        Image image = this.findImage(envConf, envVersion, imageConf);
        if (image == null) {
            image = buildImage(envConf, envVersion, imageConf);
        }
        if (image == null) {
            throw new DenvException("Could not find or build image " + imageConf.getId());
        }
        return image;
    }

    protected Image buildImage(EnvironmentConfiguration envConf, String envVersion, ImageConfiguration imageConf) throws DenvException {
        /*
        Image baseImage = this.findImage(conf.getString("baseImage"));
        // A) Go via Rundeck
            // run a command to keep container alive
            String[] command = new String[]{"tail", "-F", "/var/log/dmesg"};
            String containerName = getNamingStrategy().generateContainerName(env, appConf, imageType);
            Container container = getContainerManager().createContainer(env, containerName, baseImage, command, null, null);
            adminClient.deployApplication(container.getHostname(), appConf);
        // or B)
            // String[] command = new String[]{"apt-get", "install", "synaptiq-sqo=" + runtime.getVersion()};
            // Container container = containerizationManager.startNewContainer(env, baseImage, command);
        getContainerManager().saveContainerAsApplicationImage(env, container, appConf, "Web");
        */
        return this.findImage(envConf, envVersion, imageConf);
    }

    protected ImageInfo extractImageInfoFromName(String imageName) throws DenvException {
        String[] tok = imageName.split(":");
        if (tok == null || tok.length < 2) {
            return null;
        }
        String[] tok2 = tok[0].split("-");
        if (tok2 == null || tok2.length < 2) {
            return null;
        }
        ImageInfo imageInfo = new ImageInfo();

        /*imageInfo.imageNameWithoutVersion = tok[0];
        String version = tok[1];
        String appConfName = tok2[0];
        ApplicationConfiguration appConf = applicationConfigurationManager.getApplicationConfiguration(appConfName);
        //imageInfo.runtime = getApplicationFactory().createApplication(appConf);
        imageInfo.imageType = tok2[1];*/

        return imageInfo;
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public ContainerVersioningPolicy getVersioningPolicy() {
        return versioningPolicy;
    }

    protected class ImageInfo {
        public String imageNameWithoutVersion;
        public EnvironmentRuntimeInfo deployedApplication;
        public String imageType;
    }
}
