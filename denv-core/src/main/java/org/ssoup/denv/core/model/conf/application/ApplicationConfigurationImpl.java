package org.ssoup.denv.core.model.conf.application;

/**
 * User: ALB
 * Date: 12/09/14 14:42
 */
public class ApplicationConfigurationImpl implements ApplicationConfiguration {

    private String id;
    private String description;

    public ApplicationConfigurationImpl() {
    }

    public ApplicationConfigurationImpl(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public ApplicationConfigurationImpl(ApplicationConfiguration appConf) {
        this.id = appConf.getId();
        this.description = appConf.getDescription();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
