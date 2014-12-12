package org.ssoup.denv.server.service.runtime.sync;

import org.ssoup.denv.core.exception.DenvException;

/**
 * User: ALB
 * Date: 15/11/2014 19:00
 */
public interface SynchronizationService {

    /**
     * Checks the actual state of the environment and updates it in persistence store
     *
     * @throws DenvException
     */
    void updateActualState() throws DenvException;

    /**
     * Tries to update the actual state of the environment to the desired state
     * by taking appropriate actions to achieve the changes needed.
     *
     * @throws DenvException
     */
    void moveTowardsDesiredState() throws DenvException;
}
