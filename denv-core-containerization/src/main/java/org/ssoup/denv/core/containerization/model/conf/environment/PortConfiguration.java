package org.ssoup.denv.core.containerization.model.conf.environment;

/**
* User: ALB
* Date: 23/08/14 21:17
*/
public interface PortConfiguration {
    int getHostPort();
    int getContainerPort();
}
