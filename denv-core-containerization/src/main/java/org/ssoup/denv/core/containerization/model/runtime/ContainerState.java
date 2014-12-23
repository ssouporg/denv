package org.ssoup.denv.core.containerization.model.runtime;

/**
 * User: ALB
 * Date: 15/11/2014 07:30
 */
public enum ContainerState {
    UNDEPLOYED,
    STOPPED,
    STARTING,
    STOPPING,
    SUCCEEDED,
    FAILED,
    RESPONDING,
    NOT_RESPONDING,
    KILLED_BY_USER,
    KILLED_BY_DENV,
    RESTARTING;

    public boolean isDeployed() {
        return this != UNDEPLOYED;
    }

    public boolean isStarted() {
        return this == RESPONDING || this == NOT_RESPONDING;
    }

    public boolean isStopped() {
        return this == STOPPED || this == SUCCEEDED || this == FAILED || this == KILLED_BY_USER || this == KILLED_BY_DENV;
    }
}
