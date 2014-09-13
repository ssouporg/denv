package org.ssoup.denv.core.containerization.domain.conf.application;

/**
* User: ALB
* Date: 23/08/14 21:17
*/
public interface PortConfiguration {
    int getHostPort();
    int getContainerPort();
}
