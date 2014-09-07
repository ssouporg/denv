package org.ssoup.denv.common.model.config.environment;

import org.ssoup.denv.common.model.config.application.ApplicationConfiguration;

import java.util.Arrays;
import java.util.Collection;

/**
 * User: ALB
 * Date: 25/08/14 08:57
 */
public class InMemoryDenvEnvironmentConfiguration implements EnvironmentConfiguration {

    private String id;
    private Collection<String> labels;
    private String applicationConfigurationName;

    public InMemoryDenvEnvironmentConfiguration() {
    }

    public InMemoryDenvEnvironmentConfiguration(String id, String[] labels, String applicationConfigurationName) {
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

    public void setLabels(Collection<String> labels) {
        this.labels = labels;
    }

    public void setApplicationConfigurationName(String applicationConfigurationName) {
        this.applicationConfigurationName = applicationConfigurationName;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
