package org.ssoup.denv.server.domain.conf.application;

import org.ssoup.denv.server.domain.DenvDomainObject;

import java.util.Collection;

/**
 * User: ALB
 * Date: 09/08/14 19:05
 */
public interface ApplicationConfiguration extends DenvDomainObject {
    String getName();
    String getDescription();
    Collection<? extends ImageConfiguration> getImages();

}
