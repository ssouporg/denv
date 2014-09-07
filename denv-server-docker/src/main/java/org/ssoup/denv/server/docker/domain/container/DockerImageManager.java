package org.ssoup.denv.server.docker.domain.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.core.DockerClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.docker.domain.conf.DockerNodeConfiguration;
import org.ssoup.denv.common.model.config.application.ImageConfiguration;
import org.ssoup.denv.server.domain.runtime.container.Image;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.ContainerizationException;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.service.admin.AdminClient;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;
import org.ssoup.denv.server.service.conf.node.NodeManager;
import org.ssoup.denv.server.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.runtime.container.AbstractImageManager;
import org.ssoup.denv.server.service.runtime.container.ImageManager;
import org.ssoup.denv.server.service.versioning.VersioningPolicy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * User: ALB
 * Date: 09/01/14 15:51
 */
@Service
@Scope("singleton")
public class DockerImageManager extends AbstractImageManager implements ImageManager {

    private DockerClient dockerClient;
    private NodeManager nodeManager;

    @Autowired
    public DockerImageManager(AdminClient adminClient, ApplicationConfigurationManager applicationConfigurationManager, NamingStrategy namingStrategy, VersioningPolicy versioningPolicy, NodeManager nodeManager) {
        super(adminClient, applicationConfigurationManager, namingStrategy, versioningPolicy);
        this.nodeManager = nodeManager;
    }

    private DockerClient getDockerClient() {
        if (this.dockerClient == null) {
            this.dockerClient = new DockerClientImpl(((DockerNodeConfiguration)this.nodeManager.getDefaultNode()).getDockerAddress());
        }
        return this.dockerClient;
    }

    @Override
    public Image findImage(Environment env, ImageConfiguration imageConf) throws DenvException {
        // String appVersion = getVersioningPolicy().getAppVersion(env, appConf);
        // String imageName = this.getNamingStrategy().generateImageName(env.getId(), appConf, imageType);
        com.github.dockerjava.api.model.Image dockerImage = null;
        String[] toks = imageConf.getSource().split(":");
        if (toks.length == 2) {
            dockerImage = this.findDockerImage(toks[0], toks[1]);
        } else {
            dockerImage = this.findDockerImage(imageConf.getSource(), null);
        }

        if (dockerImage != null) {
            return new DockerImage(imageConf, dockerImage);
        }
        return null;
    }

    /*@Override
    public Image findImage(String imageName) throws DenvException {
        com.github.dockerjava.common.model.Image dockerImage;
        String[] toks = imageName.split(":");
        if (toks.length == 2) {
            dockerImage = this.findDockerImage(toks[0], toks[1]);
        } else {
            dockerImage = this.findDockerImage(imageName, null);
        }

        if (dockerImage != null) {
            return new DockerImage(dockerImage);
        }
        return null;
    }*/

    private com.github.dockerjava.api.model.Image findDockerImage(String imageName, String tag) throws ContainerizationException {
        try {
            List<com.github.dockerjava.api.model.Image> dockerImages = getDockerClient().listImagesCmd().withFilter(imageName).exec();
            if (dockerImages != null) {
                String fullImageName = imageName + ":" + (tag != null ? tag : "latest");
                for (com.github.dockerjava.api.model.Image dockerImage : dockerImages) {
                    if (dockerImage.getRepoTags() != null) {
                        for (String repoTag : dockerImage.getRepoTags()) {
                            if (fullImageName.equals(repoTag)) {
                                return dockerImage;
                            }
                        }
                    }
                }
            }
            return null;
        } catch (DockerException e) {
            throw new ContainerizationException(e);
        }
    }

    @Override
    protected Image buildImage(Environment env, ImageConfiguration imageConf) throws DenvException {
        // try to pull the image from docker registry
        String imageSource = imageConf.getSource();
        String imageRepository = imageConf.getSource(), imageTag = null;
        if (imageSource.contains(":")) {
            imageRepository = imageSource.split(":")[0];
            imageTag = imageSource.split(":")[1];
        }
        PullImageCmd pullImageCommand = getDockerClient().pullImageCmd(imageRepository);
        if (imageTag != null) {
            pullImageCommand.withTag(imageTag);
        }
        InputStream pullStream = pullImageCommand.exec();
        try {
            // wait for the pull operation to finish
            byte[] buf = new byte[1024];
            while (pullStream.read(buf) != -1);
        } catch(IOException ex) {
        }

        Image image = this.findImage(env, imageConf);
        if (image == null) {
            // otherwise try to create the image with an image builder
            image = super.buildImage(env, imageConf);
        }
        return image;
    }
}
