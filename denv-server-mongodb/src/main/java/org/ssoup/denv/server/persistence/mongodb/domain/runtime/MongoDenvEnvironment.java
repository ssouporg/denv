package org.ssoup.denv.server.persistence.mongodb.domain.runtime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

/**
 * User: ALB
 * Date: 25/08/14 08:57
 */
@Document(collection="environment")
public class MongoDenvEnvironment extends DenvEnvironment {

    public MongoDenvEnvironment() {
    }

    public MongoDenvEnvironment(Environment env) {
        super(env);
    }
}
