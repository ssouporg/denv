package org.ssoup.denv.core.exception;

/**
 * User: ALB
 */
public class DenvRuntimeException extends RuntimeException {

    public DenvRuntimeException() {
    }

    public DenvRuntimeException(String message) {
        super(message);
    }

    public DenvRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DenvRuntimeException(Throwable cause) {
        super(cause);
    }
}
