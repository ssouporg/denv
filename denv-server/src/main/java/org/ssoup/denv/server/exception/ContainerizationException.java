package org.ssoup.denv.server.exception;

import org.ssoup.denv.server.exception.DenvException;

/**
 * User: ALB
 * Date: 12/12/13 16:21
 */
public class ContainerizationException extends DenvException {
    public ContainerizationException() {
        super();
    }

    public ContainerizationException(String message) {
        super(message);
    }

    public ContainerizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerizationException(Throwable cause) {
        super(cause);
    }
}
