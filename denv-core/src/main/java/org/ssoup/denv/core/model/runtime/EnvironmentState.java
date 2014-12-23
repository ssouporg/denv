package org.ssoup.denv.core.model.runtime;

/**
 * User: ALB
 * Date: 17/11/2014 13:43
 */
public enum EnvironmentState {
    CREATED,
    STARTING,
    STARTED,
    SUCCEEDED,
    FAILED,
    STOPPING,
    STOPPED,
    DELETING,
    DELETED,
    CONF_UNKNOWN;

    public boolean canCreateSnapshot() {
        return this == STARTED || this == SUCCEEDED || this == FAILED || this == STOPPED;
    }
}
