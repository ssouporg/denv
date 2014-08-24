package org.ssoup.denv.server.api.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

/**
 * User: ALB
 * Date: 24/08/14 14:55
 */
public abstract class AbstractDenvMessageConverter<T> extends AbstractHttpMessageConverter<T> {

    public AbstractDenvMessageConverter(MediaType ... mediaTypes) {
        super(mediaTypes);
    }
}
