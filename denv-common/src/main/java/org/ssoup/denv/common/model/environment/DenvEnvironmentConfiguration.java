package org.ssoup.denv.common.model.environment;

import org.ssoup.denv.common.model.application.ApplicationConfiguration;

import java.util.Arrays;
import java.util.Collection;

/**
 * User: ALB
 * Date: 25/08/14 08:57
 */
public class DenvEnvironmentConfiguration implements EnvironmentConfiguration {

    private String id;
    private Collection<String> labels;
    private String applicationConfigurationName;
    private ApplicationConfiguration applicationConfiguration;

    public DenvEnvironmentConfiguration() {
    }

    public DenvEnvironmentConfiguration(String id, String[] labels, String applicationConfigurationName) {
        this.id = id;
        this.labels = Arrays.asList(labels);
        this.applicationConfigurationName = applicationConfigurationName;
    }

    @Override
    public Collection<String> getLabels() {
        return labels;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public String getApplicationConfigurationName() {
        return this.applicationConfigurationName;
    }

    @Override
    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public void setLabels(Collection<String> labels) {
        this.labels = labels;
    }

    public void setApplicationConfigurationName(String applicationConfigurationName) {
        this.applicationConfigurationName = applicationConfigurationName;
    }

    public void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
