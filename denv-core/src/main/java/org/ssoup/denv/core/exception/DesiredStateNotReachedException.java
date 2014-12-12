package org.ssoup.denv.core.exception;

/**
 * User: ALB
 * Date: 20/09/14 20:25
 */
public class DesiredStateNotReachedException extends DenvException {

    public DesiredStateNotReachedException() {
    }

    public DesiredStateNotReachedException(String message) {
        super(message);
    }

    public DesiredStateNotReachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DesiredStateNotReachedException(Throwable cause) {
        super(cause);
    }
}
