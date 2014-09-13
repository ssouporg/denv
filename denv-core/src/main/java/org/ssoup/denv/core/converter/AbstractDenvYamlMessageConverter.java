package org.ssoup.denv.core.converter;

import org.springframework.http.MediaType;
import org.yaml.snakeyaml.Yaml;

/**
 * User: ALB
 * Date: 24/08/14 14:55
 */
public abstract class AbstractDenvYamlMessageConverter<T> extends AbstractDenvMessageConverter<T> {

    private Yaml yaml;

    public AbstractDenvYamlMessageConverter() {
        super(buildMediaTypeArray());
        this.yaml = new Yaml();
    }

    protected Yaml getYaml() {
        return yaml;
    }

    protected static MediaType[] buildMediaTypeArray(){
        // now register the media types this converter can handle
        MediaType[] mediatypes = new MediaType[1];
        MediaType mt = new MediaType("*", "x-yaml");
        mediatypes[0] = mt;
        return mediatypes;
    }
}
