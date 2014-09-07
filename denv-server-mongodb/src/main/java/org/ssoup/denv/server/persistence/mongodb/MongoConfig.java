package org.ssoup.denv.server.persistence.mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.ssoup.denv.server.persistence.mongodb.repository.MongoRepositoryPackageMarker;
import org.ssoup.denv.server.persistence.repository.EnvironmentRepositoryCustom;

/**
 * User: ALB
 * Date: 07/09/14 10:50
 */
@Configuration
@EnableMongoRepositories(basePackageClasses = MongoRepositoryPackageMarker.class)
public class MongoConfig {
}
