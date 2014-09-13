package org.ssoup.denv.server.containerization.service.container;

import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.containerization.domain.runtime.Image;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.server.service.admin.AdminClient;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;
import org.ssoup.denv.server.service.versioning.VersioningPolicy;

/**
 * User: ALB
 * Date: 13/01/14 14:28
 */
public abstract class AbstractImageManager implements ImageManager {

    private AdminClient adminClient;

    private ApplicationConfigurationManager applicationConfigurationManager;

    private NamingStrategy namingStrategy;

    private VersioningPolicy versioningPolicy;

    protected AbstractImageManager(AdminClient adminClient, ApplicationConfigurationManager applicationConfigurationManager, NamingStrategy namingStrategy, VersioningPolicy versioningPolicy) {
        this.adminClient = adminClient;
        this.applicationConfigurationManager = applicationConfigurationManager;
        this.namingStrategy = namingStrategy;
        this.versioningPolicy = versioningPolicy;
    }

    @Override
    public Image findOrBuildImage(Environment env, ImageConfiguration imageConf) throws DenvException {
        Image image = this.findImage(env, imageConf);
        if (image == null) {
            image = buildImage(env, imageConf);
        }
        return image;
    }

    protected Image buildImage(Environment env, ImageConfiguration imageConf) throws DenvException {
        /*
        Image baseImage = this.findImage(conf.getString("baseImage"));
        // A) Go via Rundeck
            // run a command to keep container alive
            String[] command = new String[]{"tail", "-F", "/var/log/dmesg"};
            String containerName = getNamingStrategy().generateContainerName(env, appConf, imageType);
            Container container = getContainerManager().createContainer(env, containerName, baseImage, command, null, null);
            adminClient.deployApplication(container.getHostname(), appConf);
        // or B)
            // String[] command = new String[]{"apt-get", "install", "synaptiq-sqo=" + application.getVersion()};
            // Container container = containerizationManager.startNewContainer(env, baseImage, command);
        getContainerManager().saveContainerAsApplicationImage(env, container, appConf, "Web");
        */
        return this.findImage(env, imageConf);
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

        imageInfo.imageNameWithoutVersion = tok[0];
        String version = tok[1];
        String appConfName = tok2[0];
        ApplicationConfiguration appConf = applicationConfigurationManager.getApplicationConfiguration(appConfName);
        //imageInfo.application = getApplicationFactory().createApplication(appConf);
        imageInfo.imageType = tok2[1];

        return imageInfo;
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public VersioningPolicy getVersioningPolicy() {
        return versioningPolicy;
    }

    protected class ImageInfo {
        public String imageNameWithoutVersion;
        public Application deployedApplication;
        public String imageType;
    }
}
