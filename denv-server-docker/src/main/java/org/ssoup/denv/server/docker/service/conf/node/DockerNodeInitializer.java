package org.ssoup.denv.server.docker.service.conf.node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.docker.domain.conf.DockerNodeConfiguration;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.server.service.conf.node.DenvNodeManager;

import javax.annotation.PostConstruct;

/**
 * User: ALB
 * Date: 24/08/14 21:11
 */
@Service
@Scope("singleton")
public class DockerNodeInitializer {

    @Value("${DOCKER_HOST}")
    private String dockerHost;

    @Value("${DOCKER_PORT}")
    private Integer dockerPort;

    private DenvNodeManager denvNodeManager;

    @Autowired
    public DockerNodeInitializer(DenvNodeManager denvNodeManager) {
        this.denvNodeManager = denvNodeManager;
    }

    @PostConstruct
    public void init() throws DenvException {
        if (dockerHost != null && dockerPort != null) {
            DockerNodeConfiguration dockerNodeConfiguration = new DockerNodeConfiguration("defaultDockerNode", dockerHost, dockerPort);
            denvNodeManager.addNode(dockerNodeConfiguration);
            denvNodeManager.setDefaultNode(dockerNodeConfiguration);
        }
    }
}
