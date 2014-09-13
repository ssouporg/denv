package org.ssoup.denv.server.containerization.domain.runtime;

import java.util.Map;

/**
 * User: ALB
 * Date: 12/12/13 16:44
 */
public interface Container {
    String getId();

    String getName();

    String[] getAllNames();

    String getHostname();

    Map<Integer, Integer> getPortMapping();

    Image getImage();

    boolean isRunning();

    void setRunning(boolean running);
}
