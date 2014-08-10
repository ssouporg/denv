package org.ssoup.denv.server.exception;

/**
 * User: ALB
 */
public class DenvException extends Exception {

    public DenvException() {
    }

    public DenvException(String message) {
        super(message);
    }

    public DenvException(String message, Throwable cause) {
        super(message, cause);
    }

    public DenvException(Throwable cause) {
        super(cause);
    }
}
