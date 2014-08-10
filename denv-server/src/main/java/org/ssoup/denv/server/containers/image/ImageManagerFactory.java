package org.ssoup.denv.server.containers.image;

import org.ssoup.denv.server.containers.docker.DockerImageManager;

/**
 * User: ALB
 * Date: 13/01/14 18:08
 */
public class ImageManagerFactory {

    private static ImageManager defaultImageManager;

    public static ImageManager getDefaultImageManager() {
        if (defaultImageManager == null) {
            defaultImageManager = new DockerImageManager();
        }
        return defaultImageManager;
    }
}
