package org.ssoup.denv.core.model.conf.application;

import org.springframework.data.annotation.Id;

/**
 * User: ALB
 * Date: 12/09/14 10:11
 */
public interface ApplicationConfiguration {
    @Id
    String getId();
    String getName();
    String getDescription();
}
