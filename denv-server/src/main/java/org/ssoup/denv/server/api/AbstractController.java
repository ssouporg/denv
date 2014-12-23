package org.ssoup.denv.server.api;

import org.springframework.http.HttpHeaders;

/**
 * User: ALB
 * Date: 13/06/14 10:29
 */
public abstract class AbstractController {

    public static final String HATEOAS_MEDIA_TYPE_VALUE = "application/hal+json";

    protected HttpHeaders defaultResponseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", HATEOAS_MEDIA_TYPE_VALUE);
        return headers;
    }
}
