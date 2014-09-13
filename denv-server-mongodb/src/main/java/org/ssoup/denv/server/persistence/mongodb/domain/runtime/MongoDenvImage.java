package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;
import org.ssoup.denv.server.containerization.domain.runtime.AbstractImage;

/**
 * User: ALB
 * Date: 09/01/14 15:52
 */
@Document(collection="image")
public class MongoDenvImage extends AbstractImage {

    @DBRef
    @Field("imageConf")
    private ImageConfiguration imageConfForMongo;

    public MongoDenvImage() {
    }

    public MongoDenvImage(String id, String name, String tag, ImageConfiguration imageConf) {
        super(id, name, tag, imageConf);
    }

    @Override
    public void setConf(ImageConfiguration imageConf) {
        super.setConf(imageConf);
        this.imageConfForMongo = imageConf;
    }
}
