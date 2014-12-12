package org.ssoup.denv.core.model.conf.environment;

/**
 * User: ALB
 * Date: 12/09/14 14:42
 */
public class EnvironmentConfigurationImpl implements EnvironmentConfiguration {

    private String id;
    private String name;
    private String description;

    public EnvironmentConfigurationImpl() {
    }

    public EnvironmentConfigurationImpl(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public EnvironmentConfigurationImpl(EnvironmentConfiguration appConf) {
        this.id = appConf.getId();
        this.name = appConf.getName();
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

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
