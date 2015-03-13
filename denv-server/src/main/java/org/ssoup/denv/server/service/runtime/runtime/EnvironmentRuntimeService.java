package org.ssoup.denv.server.service.runtime.runtime;

import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.runtime.Environment;

/**
 * User: ALB
 * Date: 11/12/13 11:51
 */
public interface EnvironmentRuntimeService {
    void saveSnapshot(Environment env, String snapshotName) throws DenvException;
}
