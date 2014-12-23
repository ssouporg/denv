package org.ssoup.denv.server.containerization.service.versioning;

import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;

/**
 * User: ALB
 * Date: 28/02/14 10:00
 */
@Service
public class IdentityVersioningPolicy implements VersioningPolicy {

    @Override
    public String getImageVersion(String envVersion, ImageConfiguration imageConf) {
        if (imageConf.getSource() != null) {
            String[] toks = imageConf.getSource().split(":");
            if (toks.length == 2) {
                // if the image source contains a version, use it
                return toks[1];
            } else {
                // otherwise use the environment version
                return envVersion;
            }
        } else {
            return envVersion;
        }
    }
}
