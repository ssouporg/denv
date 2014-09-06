package org.ssoup.denv.server.service.runtime.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.common.model.environment.DenvEnvironmentConfiguration;
import org.ssoup.denv.common.model.environment.EnvironmentConfiguration;
import org.ssoup.denv.common.model.node.NodeConfiguration;
import org.ssoup.denv.server.domain.runtime.application.Application;
import org.ssoup.denv.server.domain.runtime.container.Container;
import org.ssoup.denv.server.domain.runtime.container.Image;
import org.ssoup.denv.server.domain.runtime.environment.DenvEnvironment;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.event.EnvsEvent;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;
import org.ssoup.denv.server.service.conf.environment.EnvironmentConfigurationManager;
import org.ssoup.denv.server.service.conf.node.NodeManager;
import org.ssoup.denv.server.service.naming.ContainerEnvsInfo;
import org.ssoup.denv.server.service.naming.NamingStrategy;
import org.ssoup.denv.server.service.runtime.application.ApplicationManager;
import org.ssoup.denv.server.service.runtime.container.ContainerManager;

import java.util.*;

/**
 * User: ALB
 * Date: 08/01/14 17:41
 */
@Service
@Scope("singleton")
public class DefaultEnvironmentManager implements EnvironmentManager {

    // used to generate next env id
    private int maxEnvironmentIdInUse = 0;

    private NodeManager nodeManager;

    private NamingStrategy namingStrategy;

    private EnvironmentConfigurationManager environmentConfigurationManager;

    private ApplicationConfigurationManager applicationConfigurationManager;

    private ApplicationManager applicationManager;

    private ContainerManager containerManager;

    private Map<String, Environment> environments = new HashMap<String, Environment>();

    private List<EnvsEventHandler> eventHandlers = new ArrayList<EnvsEventHandler>();

    @Autowired
    public DefaultEnvironmentManager(NodeManager nodeManager, NamingStrategy namingStrategy, EnvironmentConfigurationManager environmentConfigurationManager, ApplicationConfigurationManager applicationConfigurationManager, ApplicationManager applicationManager, ContainerManager containerManager) {
        this.nodeManager = nodeManager;
        this.namingStrategy = namingStrategy;
        this.environmentConfigurationManager = environmentConfigurationManager;
        this.applicationConfigurationManager = applicationConfigurationManager;
        this.applicationManager = applicationManager;
        this.containerManager = containerManager;
    }

    @Override
    public void registerExistingEnvironments() throws DenvException {
        environments = new HashMap<String, Environment>();
        // read current environments via container manager
        for (Container container : getContainerManager().getAllContainers()) {
            NodeConfiguration node = null; // TODO: use the correct node
            if (namingStrategy.isEnvContainer(container.getName())) {
                ContainerEnvsInfo containerInfo = namingStrategy.extractEnvsInfoFromContainer(container);
                String envId = containerInfo.getEnvId();
                Environment env = containerInfo.getEnv();
                if (env == null) {
                    Image image = container.getImage();
                    String envConfId = ""; // TODO
                    EnvironmentConfiguration envConf = environmentConfigurationManager.getEnvironmentConfiguration(envConfId);
                    env = registerEnvironment(envId, envConf, node);
                }
                Application application = containerInfo.getApp();
                if (application == null) {
                    application = applicationManager.createApplication(env);
                    env.registerApp(application);
                }
                application.registerContainer(containerInfo.getImageType(), container);
            }
        }
    }

    @Override
    public Environment createEnvironment(EnvironmentConfiguration envConf) throws DenvException {
        return createEnvironment(envConf, nodeManager.getDefaultNode());
    }

    @Override
    public Environment createEnvironment(EnvironmentConfiguration envConf, NodeConfiguration node) throws DenvException {
        String envId = generateEnvironmentId();
        Environment env = registerEnvironment(envId, envConf, node);
        applicationManager.createApplication(env);
        return env;
    }

    @Override
    public void startEnvironment(Environment env) throws DenvException {
        applicationManager.startApplication(env, env.getApplication());
    }

    @Override
    public void stopEnvironment(Environment env) throws DenvException {
        applicationManager.stopApplication(env, env.getApplication());
    }

    @Override
    public void deleteEnvironment(Environment env) throws DenvException {
        applicationManager.deleteApplication(env, env.getApplication());
    }

    @Override
    public Environment registerEnvironment(String envId, EnvironmentConfiguration envConf, NodeConfiguration node) throws DenvException {
        int intEnvId = Integer.parseInt(envId);
        if (intEnvId > maxEnvironmentIdInUse) {
            maxEnvironmentIdInUse = intEnvId;
        }
        Environment environment = new DenvEnvironment(envId, envConf, node);
        getEnvironments().put(envId, environment);
        return environment;
    }

    @Override
    public Collection<Environment> listEnvironments() throws DenvException {
        return getEnvironments().values();
    }

    private synchronized String generateEnvironmentId() {
        //return UUID.randomUUID().toString();
        maxEnvironmentIdInUse++;
        return Integer.toString(maxEnvironmentIdInUse);
    }

    @Override
    public Environment findEnvironment(String envId) throws DenvException {
        return getEnvironments().get(envId);
    }

    @Override
    public void addEventHandler(EnvsEventHandler eventHandler) {
        this.eventHandlers.add(eventHandler);
    }

    private void notifyEvent(EnvsEvent event) {
        for (EnvsEventHandler eventHandler : this.eventHandlers) {
            eventHandler.handle(event);
        }
    }

    protected ContainerManager getContainerManager() {
        return this.containerManager;
    }

    public Map<String, Environment> getEnvironments() throws DenvException {
        return environments;
    }

    public Environment getEnvironment(String envId) throws DenvException {
        return getEnvironments().get(envId);
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
