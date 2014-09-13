package org.ssoup.denv.server.service.conf.node;

import org.ssoup.denv.core.model.conf.node.NodeConfiguration;
import org.ssoup.denv.core.exception.DenvException;

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
