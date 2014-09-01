package org.ssoup.denv.server.domain.runtime.container;

import org.ssoup.denv.common.model.application.LinkConfiguration;
import org.ssoup.denv.common.model.application.VolumeConfiguration;

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
}
