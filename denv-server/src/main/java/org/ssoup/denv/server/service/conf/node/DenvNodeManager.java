package org.ssoup.denv.server.service.conf.node;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.common.model.config.node.NodeConfiguration;
import org.ssoup.denv.server.exception.DenvException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: ALB
 * Date: 24/08/14 21:05
 */
@Service
@Scope("singleton")
public class DenvNodeManager implements NodeManager {

    private NodeConfiguration defaultNode;
    private Collection<NodeConfiguration> nodes;

    public DenvNodeManager() {
        nodes = new ArrayList();
    }

    @Override
    public void addNode(NodeConfiguration node) {
        nodes.add(node);
    }

    @Override
    public Collection<NodeConfiguration> listNodes() {
        return nodes;
    }

    @Override
    public NodeConfiguration getDefaultNode() {
        return defaultNode;
    }

    @Override
    public void setDefaultNode(NodeConfiguration node) throws DenvException {
        if (!nodes.contains(node)) {
            throw new DenvException("Not a registered node");
        }
        this.defaultNode = node;
    }
}
