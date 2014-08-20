package org.ssoup.denv.server.domain.conf.application;

import java.util.Map;

/**
 * User: ALB
 * Date: 09/08/14 19:03
 */
public interface ServiceConfiguration {

    String getName();
    String getImage();
    VolumeInfo[] getVolumeInfos();
    String getCommand();
    Map<Integer, Integer> getPorts();

    public static class VolumeInfo {
        private String name;
        private String hostDir;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHostDir() {
            return hostDir;
        }

        public void setHostDir(String hostDir) {
            this.hostDir = hostDir;
        }
    }
}
