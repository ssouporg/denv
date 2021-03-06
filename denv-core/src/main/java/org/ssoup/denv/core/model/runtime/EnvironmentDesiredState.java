package org.ssoup.denv.core.model.runtime;

/**
 * User: ALB
 * Date: 17/11/2014 13:43
 */
public enum EnvironmentDesiredState {

    STARTED,
    SUCCEEDED,
    STOPPED,
    DELETED;

    public boolean toBeExecutedOnce() {
        return this == SUCCEEDED;
    }
}
