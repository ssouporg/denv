package org.ssoup.denv.core.exception;

import org.springframework.http.ResponseEntity;

/**
 * User: ALB
 * Date: 12/09/14 15:36
 */
public class DenvHttpException extends DenvException {

    private ResponseEntity responseEntity;

    public DenvHttpException(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }

    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }
}
