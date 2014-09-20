package org.ssoup.denv.core.model.runtime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.ssoup.denv.core.model.conf.node.NodeConfiguration;

import java.util.Collection;

/**
 * User: ALB
 * Date: 27/02/14 09:08
 */
public interface Environment {
    @Id
    String getId();

    String getName();

    Collection<String> getLabels();
    boolean hasLabel(String label);

    @JsonIgnore
    NodeConfiguration getNode();

    Collection<Application> getApplications();

    Application getApplication(String appId);

    void addApplication(Application app);

    void removeApplication(String appId);

    void setApplications(Collection<Application> apps);
}
