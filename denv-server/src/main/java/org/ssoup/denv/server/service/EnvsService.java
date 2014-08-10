package org.ssoup.denv.server.service;

import org.ssoup.denv.server.domain.Environment;

import java.util.List;

/**
 * User: ALB
 * Date: 28/07/14 17:05
 */
public interface EnvsService {

    List<Environment> listEnvironments();
}
