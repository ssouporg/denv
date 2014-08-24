package org.ssoup.denv.server.service.runtime.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.server.domain.conf.node.NodeConfiguration;
import org.ssoup.denv.server.domain.runtime.application.Application;
import org.ssoup.denv.server.domain.runtime.container.Container;
import org.ssoup.denv.server.domain.runtime.container.Image;
import org.ssoup.denv.server.domain.runtime.environment.DenvEnvironment;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.event.EnvsEvent;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.server.exception.DenvException;
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

    private NamingStrategy namingStrategy;

    private ApplicationManager applicationManager;

    private ContainerManager containerManager;

    private Map<String, Environment> environments = new HashMap<String, Environment>();

    private List<EnvsEventHandler> eventHandlers = new ArrayList<EnvsEventHandler>();

    @Autowired
    public DefaultEnvironmentManager(NamingStrategy namingStrategy, ApplicationManager applicationManager, ContainerManager containerManager) {
        this.namingStrategy = namingStrategy;
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
                    String version = image.getTag();
                    env = register(envId, version, node);
                }
                Application application = containerInfo.getApp();
                if (application == null) {
                    application = applicationManager.createApplication(env, containerInfo.getAppConf());
                    env.registerApp(application);
                }
                application.registerContainer(containerInfo.getImageType(), container);
            }
        }
    }

    @Override
    public Environment create(String version, NodeConfiguration node) throws DenvException {
        String envId = generateEnvironmentId();
        Environment environment = register(envId, version, node);
        applicationManager.createApplications(environment);
        return environment;
    }

    @Override
    public void start(Environment env) throws DenvException {
        applicationManager.startApplications(env);
    }
    @Override
    public void stop(Environment env) throws DenvException {
        applicationManager.stopApplications(env);
    }
    @Override
    public void delete(Environment env) throws DenvException {
        applicationManager.deleteApplications(env);
    }
    @Override
    public Environment register(String envId, String version, NodeConfiguration node) throws DenvException {
        int intEnvId = Integer.parseInt(envId);
        if (intEnvId > maxEnvironmentIdInUse) {
            maxEnvironmentIdInUse = intEnvId;
        }
        Environment environment = new DenvEnvironment(envId, version, node);
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
