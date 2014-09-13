package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;
import org.ssoup.denv.server.containerization.domain.runtime.AbstractContainer;
import org.ssoup.denv.server.containerization.domain.runtime.Container;
import org.ssoup.denv.server.containerization.domain.runtime.Image;
import org.ssoup.denv.server.persistence.mongodb.annotation.CascadeSave;

/**
 * User: ALB
 * Date: 09/01/14 11:21
 */
public class MongoDenvContainer extends AbstractContainer {

    @DBRef
    @CascadeSave
    @Field("image")
    private Image imageForMongo;

    public MongoDenvContainer() {
    }

    public MongoDenvContainer(Container container) {
        super(container);
    }

    public MongoDenvContainer(String id, Image image, boolean running) {
        super(id, image, running);
    }

    @Override
    @Transient
    public Image getImage() {
        return this.getImage();
    }

    @Override
    public void setImage(Image image) {
        super.setImage(image);
        this.imageForMongo = image;
    }

    public Image getImageForMongo() {
        return imageForMongo;
    }

    public void setImageForMongo(Image imageForMongo) {
        this.imageForMongo = imageForMongo;
    }
}
