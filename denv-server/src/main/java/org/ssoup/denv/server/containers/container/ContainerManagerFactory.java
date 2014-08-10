package org.ssoup.denv.server.containers.container;

import org.ssoup.denv.server.containers.docker.DockerContainerManager;

/**
 * User: ALB
 * Date: 13/01/14 18:08
 */
public class ContainerManagerFactory {

    private static ContainerManager defaultContainerManager;

    public static ContainerManager getDefaultContainerManager() {
        if (defaultContainerManager == null) {
            defaultContainerManager = new DockerContainerManager();
        }
        return defaultContainerManager;
    }
}
