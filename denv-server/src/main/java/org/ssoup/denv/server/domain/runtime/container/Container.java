package org.ssoup.denv.server.domain.runtime.container;

import org.ssoup.denv.server.domain.conf.application.VolumeConfiguration;

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

    VolumeConfiguration[] getVolumes();

    Image getImage();

    boolean isRunning();
}
