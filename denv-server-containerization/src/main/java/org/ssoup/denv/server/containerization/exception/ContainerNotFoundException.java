package org.ssoup.denv.server.containerization.exception;

/**
 * User: ALB
 * Date: 12/12/13 16:21
 */
public class ContainerNotFoundException extends ContainerizationException {
    public ContainerNotFoundException() {
        super();
    }

    public ContainerNotFoundException(String message) {
        super(message);
    }

    public ContainerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerNotFoundException(Throwable cause) {
        super(cause);
    }
}
