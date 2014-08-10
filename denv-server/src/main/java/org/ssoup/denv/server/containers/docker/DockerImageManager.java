package org.ssoup.denv.server.containers.docker;

import com.github.dockerjava.client.DockerClient;
import com.github.dockerjava.client.DockerException;
import org.ssoup.denv.server.containers.ContainerizationException;
import org.ssoup.denv.server.containers.image.AbstractImageManager;
import org.ssoup.denv.server.containers.image.Image;
import org.ssoup.denv.server.containers.image.ImageManager;
import org.ssoup.denv.server.domain.Application;
import org.ssoup.denv.server.domain.Environment;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.naming.NamingStrategy;

import java.util.List;

/**
 * User: ALB
 * Date: 09/01/14 15:51
 */
public class DockerImageManager extends AbstractImageManager implements ImageManager {
    private DockerClient dockerClient;

    public DockerImageManager() {
        Configuration config = ConfigManager.getConfiguration();
        String dockerHost = config.getString("dockerHost");
        Integer dockerPort = config.getInteger("dockerPort", 4243);
        String dockerAddress = "http://" + dockerHost + ":" + dockerPort;
        this.dockerClient = new DockerClient(dockerAddress);
    }

    @Override
    public Image findImage(Environment env, Application app, String imageType) throws EnvAutomationException {
        NamingStrategy namingStrategy = Envs.getContainerNamingStrategy();
        String appVersion = Envs.getVersioningPolicy().getAppVersion(env, app);
        String imageName = namingStrategy.generateImageName(env.getConf(), app.getConf(), imageType);
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
            List<com.github.dockerjava.client.model.Image> dockerImages = dockerClient.getImages(imageName);
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
