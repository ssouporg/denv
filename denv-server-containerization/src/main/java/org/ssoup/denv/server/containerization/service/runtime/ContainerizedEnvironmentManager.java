package org.ssoup.denv.server.containerization.service.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.runtime.ContainerizedEnvironmentRuntimeInfoImpl;
import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.node.NodeConfiguration;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.containerization.model.runtime.ContainerizedEnvironmentRuntimeInfo;
import org.ssoup.denv.server.containerization.service.container.ContainerManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.persistence.EnvironmentRepository;
import org.ssoup.denv.server.service.conf.node.NodeManager;
import org.ssoup.denv.server.service.runtime.runtime.EnvironmentRuntimeManager;
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

    private ApplicationContext applicationContext;

    private ContainerManager containerManager;

    private NamingStrategy namingStrategy;

    @Autowired
    public ContainerizedEnvironmentManager(NodeManager nodeManager, EnvironmentRuntimeManager environmentRuntimeManager, ApplicationContext applicationContext, ContainerManager containerManager, NamingStrategy namingStrategy) {
        super(nodeManager, environmentRuntimeManager);
        this.applicationContext = applicationContext;
        this.containerManager = containerManager;
        this.namingStrategy = namingStrategy;
    }

    @Override
    protected Environment newEnvironmentInstance(Environment env) throws DenvException {
        return new DenvContainerizedEnvironment(env);
    }

    @Override
    public Environment updateEnvironment(Environment actualEnv, Environment env) throws DenvException {
        DenvContainerizedEnvironment acenv = (DenvContainerizedEnvironment)super.updateEnvironment(actualEnv, env);
        DenvContainerizedEnvironment cenv = (DenvContainerizedEnvironment)env;
        acenv.setRuntimeInfo(cenv.getRuntimeInfo());
        return acenv;
    }

    @Override
    public Environment createBuildEnvironment(EnvironmentConfiguration envConf) {
        EnvironmentRepository environmentRepository = applicationContext.getBean(EnvironmentRepository.class);
        String envId = envConf.getId() + "-builder";
        DenvContainerizedEnvironment env = new DenvContainerizedEnvironment(envId, envId, envConf.getId(), null, null);
        env.setBuilder(true);
        return environmentRepository.saveOnly(env);
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
                app.setContainerInfo(containerInfo.getImageType(), container);
            }
        }*/
    }

    /*@Override
    protected Collection<EnvironmentRuntimeInfo> convertApplications(Collection<EnvironmentRuntimeInfo> apps) throws DenvException {
        Collection<EnvironmentRuntimeInfo> capps = new ArrayList<EnvironmentRuntimeInfo>();
        for (EnvironmentRuntimeInfo app : apps) {
            ContainerizedEnvironmentRuntimeInfo capp = new ContainerizedEnvironmentRuntimeInfoImpl(app);
            capps.add(capp);
        }
        return capps;
    }*/

    public ContainerManager getContainerManager() {
        return containerManager;
    }
}
