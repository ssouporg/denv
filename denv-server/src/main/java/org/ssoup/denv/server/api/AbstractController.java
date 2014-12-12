package org.ssoup.denv.server.api;

import org.springframework.http.HttpHeaders;

/**
 * User: ALB
 * Date: 13/06/14 10:29
 */
public abstract class AbstractController {

    protected HttpHeaders defaultResponseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/ld+json");
        return headers;
    }
}
