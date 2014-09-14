package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;
import org.ssoup.denv.server.containerization.domain.runtime.AbstractImage;
import org.ssoup.denv.server.docker.domain.container.DockerImage;
import org.ssoup.denv.server.persistence.mongodb.domain.config.MongoDenvImageConfiguration;

/**
 * User: ALB
 * Date: 09/01/14 15:52
 */
public class MongoDockerImage extends DockerImage {

    @DBRef(lazy = false)
    @JsonProperty("imageConf")
    private MongoDenvImageConfiguration imageConf;

    public MongoDockerImage() {
    }

    public MongoDockerImage(DockerImage image) {
        super(image.getId(), image.getName(), image.getTag(), null);
        this.setImageConf(new MongoDenvImageConfiguration(image.getConf()));
    }

    public MongoDockerImage(String id, String name, String tag, ImageConfiguration imageConf) {
        super(id, name, tag, imageConf);
    }

    @Override
    @Transient
    @JsonIgnore
    public ImageConfiguration getConf() {
        return imageConf;
    }

    @Override
    public void setConf(ImageConfiguration imageConf) {
        super.setConf(imageConf);
        this.imageConf = new MongoDenvImageConfiguration(imageConf);
    }

    public MongoDenvImageConfiguration getImageConf() {
        return imageConf;
    }

    public void setImageConf(MongoDenvImageConfiguration imageConf) {
        this.imageConf = imageConf;
        super.setConf(imageConf);
    }
}
