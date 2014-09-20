package org.ssoup.denv.core.exception;

import org.springframework.http.ResponseEntity;

/**
 * User: ALB
 * Date: 20/09/14 20:25
 */
public class ResourceNotFoundException extends DenvException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
