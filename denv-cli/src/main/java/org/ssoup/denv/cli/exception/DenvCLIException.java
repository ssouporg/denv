package org.ssoup.denv.cli.exception;

import org.ssoup.denv.core.exception.DenvException;

/**
 * User: ALB
 */
public class DenvCLIException extends DenvException {

    public DenvCLIException() {
    }

    public DenvCLIException(String message) {
        super(message);
    }

    public DenvCLIException(String message, Throwable cause) {
        super(message, cause);
    }

    public DenvCLIException(Throwable cause) {
        super(cause);
    }
}
