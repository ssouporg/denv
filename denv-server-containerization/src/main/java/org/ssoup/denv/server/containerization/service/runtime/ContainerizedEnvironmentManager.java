package org.ssoup.denv.server.containerization.service.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.node.NodeConfiguration;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplication;
import org.ssoup.denv.server.containerization.domain.runtime.ContainerizedApplicationImpl;
import org.ssoup.denv.server.containerization.service.container.ContainerManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;
import org.ssoup.denv.server.service.conf.node.NodeManager;
import org.ssoup.denv.server.service.runtime.application.ApplicationManager;
import org.ssoup.denv.server.service.runtime.environment.AbstractEnvironmentManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: ALB
 * Date: 12/09/14 10:54
 */
@Service
@Scope("singleton")
public class ContainerizedEnvironmentManager extends AbstractEnvironmentManager<Environment> {

    private ContainerManager containerManager;

    private NamingStrategy namingStrategy;

    @Autowired
    public ContainerizedEnvironmentManager(NodeManager nodeManager, ApplicationConfigurationManager applicationConfigurationManager, ApplicationManager applicationManager, ContainerManager containerManager, NamingStrategy namingStrategy) {
        super(nodeManager, applicationConfigurationManager, applicationManager);
        this.containerManager = containerManager;
        this.namingStrategy = namingStrategy;
    }

    @Override
    public void registerExistingEnvironments() throws DenvException {
        // TODO: register existing environments
        // read current environments via container manager
        /*for (Container container : getContainerManager().getAllContainers()) {
            NodeConfiguration node = null; // TODO: use the correct node
            if (getNamingStrategy().isEnvContainer(container.getName())) {
                ContainerEnvsInfo containerInfo = getNamingStrategy().extractEnvsInfoFromContainer(container);
                String envId = containerInfo.getEnvId();
                Environment env = containerInfo.getEnv();
                if (env == null) {
                    Image image = container.getImageForMongo();
                    String envConfId = ""; // TODO
                    env = registerEnvironment(envId, env.getApplicationConfiguration(), null);
                }
                Application app = containerInfo.getApp();
                if (app == null) {
                    app = getApplicationManager().createApplication(env, appConf);
                    env.setDeployedApplication(app);
                }
                app.registerContainer(containerInfo.getImageType(), container);
            }
        }*/
    }

    @Override
    public void applyChanges(Environment asIsEnv, Environment toBeEnv) {
        // TODO: to be implemented
    }

    @Override
    protected Environment newEnvironmentInstance(String envId, Collection<Application> apps, NodeConfiguration node) throws DenvException {
        Collection<Application> capps = new ArrayList<Application>();
        for (Application app : apps) {
            ContainerizedApplication capp = new ContainerizedApplicationImpl(app);
            capp.setDeployed(false);
            capp.setStarted(false);
            capps.add(capp);
        }
        Environment environment = new DenvEnvironment(envId, capps, node);
        return environment;
    }

    public ContainerManager getContainerManager() {
        return containerManager;
    }
}
