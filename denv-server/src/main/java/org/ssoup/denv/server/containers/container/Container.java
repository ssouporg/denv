package org.ssoup.denv.server.containers.container;

import org.ssoup.denv.server.conf.application.ServiceConfiguration;
import org.ssoup.denv.server.containers.image.Image;

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

    ServiceConfiguration.VolumeInfo[] getVolumeInfos();

    Image getImage();

    boolean isRunning();
}
