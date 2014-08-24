package org.ssoup.denv.server.domain.conf.application;

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
    String getPath();
}
