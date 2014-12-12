package org.ssoup.denv.core.containerization.model.runtime;

/**
 * User: ALB
 * Date: 15/11/2014 07:30
 */
public enum ContainerDesiredState {
    UNDEPLOYED,
    STOPPED,
    STARTED,
    RESPONDING;

    public boolean isSatisfiedBy(ContainerState containerActualState) {
        if (this == UNDEPLOYED) {
            return containerActualState == ContainerState.UNDEPLOYED;
        }
        if (this == STOPPED) {
            return containerActualState == ContainerState.STOPPED ||
                    containerActualState == ContainerState.KILLED_BY_DENV ||
                    containerActualState == ContainerState.KILLED_BY_USER;
        }
        if (this == STARTED) {
            return containerActualState == ContainerState.RESPONDING ||
                    containerActualState == ContainerState.NOT_RESPONDING;
        }
        if (this == RESPONDING) {
            return containerActualState == ContainerState.RESPONDING;
        }
        return true;
    }
}
