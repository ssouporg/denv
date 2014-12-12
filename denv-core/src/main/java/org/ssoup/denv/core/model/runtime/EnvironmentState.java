package org.ssoup.denv.core.model.runtime;

/**
 * User: ALB
 * Date: 17/11/2014 13:43
 */
public enum EnvironmentState {

    STARTING,
    STARTED,
    STOPPING,
    STOPPED,
    DELETING,
    DELETED;

    public boolean canCreateSnapshot() {
        return this == STARTED || this == STOPPED;
    }
}