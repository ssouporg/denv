package org.ssoup.denv.core.model.runtime;

/**
 * User: ALB
 * Date: 13/09/14 19:59
 */
public class ApplicationImpl implements Application {

    private String id;
    private String applicationConfigurationId;

    // Deploy info
    private boolean deployed;

    // Runtime info
    private boolean started;

    public ApplicationImpl() {
    }

    public ApplicationImpl(String id, String applicationConfigurationId) {
        this.id = id;
        this.applicationConfigurationId = applicationConfigurationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationConfigurationId() {
        return applicationConfigurationId;
    }

    public void setApplicationConfigurationId(String applicationConfigurationId) {
        this.applicationConfigurationId = applicationConfigurationId;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
