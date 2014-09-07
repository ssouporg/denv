package org.ssoup.denv.server.domain.runtime.container;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    String getImageId();

    @JsonIgnore
    Image getImage();

    boolean isRunning();
}
