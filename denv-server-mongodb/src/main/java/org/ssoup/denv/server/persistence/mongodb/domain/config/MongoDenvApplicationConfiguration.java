package org.ssoup.denv.server.persistence.mongodb.domain.config;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfigurationImpl;
import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;
import org.ssoup.denv.core.model.conf.application.ApplicationConfigurationImpl;
import org.ssoup.denv.server.persistence.mongodb.annotation.CascadeSave;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: ALB
 * Date: 11/08/14 15:35
 */
@Document(collection="applicationConfiguration")
public class MongoDenvApplicationConfiguration extends ContainerizedApplicationConfigurationImpl {

    @DBRef(lazy = false)
    @CascadeSave
    @JsonProperty("images")
    private Collection<MongoDenvImageConfiguration> imageConfs;

    @Override
    @Transient
    @JsonIgnore
    public Collection<ImageConfigurationImpl> getImages() {
        Collection<ImageConfigurationImpl> imgs = new ArrayList<ImageConfigurationImpl>();
        imgs.addAll(imageConfs);
        return imgs;
    }

    @Override
    public void setImages(Collection<ImageConfigurationImpl> images) {
        super.setImages(images);
        this.imageConfs = new ArrayList();
        for (ImageConfiguration image : images) {
            imageConfs.add(new MongoDenvImageConfiguration(image));
        }
    }

    public Collection<MongoDenvImageConfiguration> getImageConfs() {
        return this.imageConfs;
    }

    public void setImageConfs(Collection<MongoDenvImageConfiguration> imageConfs) {
        this.imageConfs = imageConfs;
        Collection<ImageConfigurationImpl> imgs = new ArrayList<ImageConfigurationImpl>();
        imgs.addAll(imageConfs);
        super.setImages(imgs);
    }
}
