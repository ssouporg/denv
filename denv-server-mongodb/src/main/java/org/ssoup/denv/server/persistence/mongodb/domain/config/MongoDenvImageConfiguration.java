package org.ssoup.denv.server.persistence.mongodb.domain.config;

import org.springframework.data.mongodb.core.mapping.Document;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfigurationImpl;
import org.ssoup.denv.core.containerization.domain.conf.application.ImageConfiguration;

/**
* User: ALB
* Date: 07/09/14 15:58
*/
@Document(collection="imageConfiguration")
public class MongoDenvImageConfiguration extends ContainerizedApplicationConfigurationImpl.ImageConfigurationImpl {

    public MongoDenvImageConfiguration() {
    }

    public MongoDenvImageConfiguration(ImageConfiguration imageConf) {
        super(imageConf);
    }
}
