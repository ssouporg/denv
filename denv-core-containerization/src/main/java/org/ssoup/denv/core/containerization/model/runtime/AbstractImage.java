package org.ssoup.denv.core.containerization.model.runtime;

import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;

/**
 * User: ALB
 * Date: 12/09/14 14:32
 */
public abstract class AbstractImage implements Image {

    private String id;
    private String name;
    private String tag;
    private ImageConfiguration conf;

    public AbstractImage() {
    }

    public AbstractImage(String id, String name, String tag, ImageConfiguration imageConf) {
        this.id = id;
        this.conf = imageConf;
        this.name = name;
        this.tag = tag;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ImageConfiguration getConf() {
        return conf;
    }

    public void setConf(ImageConfiguration conf) {
        this.conf = conf;
    }
}
