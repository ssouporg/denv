package org.ssoup.denv.server.service.conf.node;

import org.ssoup.denv.common.model.node.NodeConfiguration;
import org.ssoup.denv.server.exception.DenvException;

import java.util.Collection;

/**
 * User: ALB
 * Date: 24/08/14 21:04
 */
public interface NodeManager {
    void addNode(NodeConfiguration node);
    Collection<NodeConfiguration> listNodes();
    NodeConfiguration getDefaultNode();
    void setDefaultNode(NodeConfiguration node) throws DenvException;
}