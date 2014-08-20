package org.ssoup.denv.server.docker.domain.container;

import com.github.dockerjava.client.DockerClient;
import com.github.dockerjava.client.DockerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.docker.domain.conf.DockerNodeConfiguration;
import org.ssoup.denv.server.domain.conf.node.NodeConfiguration;
import org.ssoup.denv.server.service.admin.AdminClient;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfiguration;
import org.ssoup.denv.server.domain.conf.application.ApplicationConfigurationManager;
import org.ssoup.denv.server.domain.container.ContainerizationException;
import org.ssoup.denv.server.domain.container.AbstractImageManager;
import org.ssoup.denv.server.domain.container.Image;
import org.ssoup.denv.server.domain.container.ImageManager;
import org.ssoup.denv.server.domain.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.versioning.VersioningPolicy;

import java.util.List;

/**
 * User: ALB
 * Date: 09/01/14 15:51
 */
@Service
@Scope("singleton")
public class DockerImageManager extends AbstractImageManager implements ImageManager {

    private DockerClient dockerClient;

    @Autowired
    public DockerImageManager(AdminClient adminClient, ApplicationConfigurationManager applicationConfigurationManager, NamingStrategy namingStrategy, VersioningPolicy versioningPolicy) {
        super(adminClient, applicationConfigurationManager, namingStrategy, versioningPolicy);
    }

    @Override
    public Image findImage(Environment env, ApplicationConfiguration appConf, String imageType) throws DenvException {
        if (this.dockerClient == null) {
            this.dockerClient = new DockerClient(((DockerNodeConfiguration)env.getNode()).getDockerAddress());
        }
        String appVersion = getVersioningPolicy().getAppVersion(env, appConf);
        String imageName = this.getNamingStrategy().generateImageName(env.getId(), appConf, imageType);
        com.github.dockerjava.client.model.Image dockerImage = this.findDockerImage(imageName, appVersion);
        if (dockerImage != null) {
            return new DockerImage(dockerImage);
        }
        return null;
    }

    @Override
    public Image findImage(String imageName) throws DenvException {
        com.github.dockerjava.client.model.Image dockerImage;
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
    }

    private com.github.dockerjava.client.model.Image findDockerImage(String imageName, String tag) throws ContainerizationException {
        try {
            List<com.github.dockerjava.client.model.Image> dockerImages = dockerClient.listImagesCmd().withFilter(imageName).exec();
            if (dockerImages != null) {
                String fullImageName = imageName + ":" + (tag != null ? tag : "latest");
                for (com.github.dockerjava.client.model.Image dockerImage : dockerImages) {
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
}
