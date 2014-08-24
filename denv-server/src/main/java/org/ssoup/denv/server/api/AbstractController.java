package org.ssoup.denv.server.api;

import org.springframework.http.HttpHeaders;

/**
 * User: ALB
 */
public abstract class AbstractController {

    protected HttpHeaders defaultResponseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-yaml");
        return headers;
    }
}
