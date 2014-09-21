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

}
