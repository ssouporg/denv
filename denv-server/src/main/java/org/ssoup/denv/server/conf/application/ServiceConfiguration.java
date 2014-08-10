package org.ssoup.denv.server.conf.application;

/**
 * User: ALB
 * Date: 09/08/14 19:03
 */
public interface ServiceConfiguration {

    VolumeInfo[] getVolumeInfos();

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
