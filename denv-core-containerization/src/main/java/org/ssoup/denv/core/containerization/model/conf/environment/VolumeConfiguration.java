package org.ssoup.denv.core.containerization.model.conf.environment;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
* User: ALB
* Date: 23/08/14 21:16
*/
public interface VolumeConfiguration {
    String getHostPath();
    String getContainerPath();

    /**
     * Returns the full path of the volume, ie. hostPath:containerPath if both are present.
     *
     * @return the full path of the volume.
     */
    @JsonIgnore
    String getPath();
}
