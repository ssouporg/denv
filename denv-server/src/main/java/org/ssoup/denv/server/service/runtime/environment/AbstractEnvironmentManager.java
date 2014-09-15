package org.ssoup.denv.server.service.runtime.environment;

import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
import org.ssoup.denv.core.model.conf.node.NodeConfiguration;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.ApplicationImpl;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.server.event.EnvsEvent;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.server.service.conf.application.ApplicationConfigurationManager;
import org.ssoup.denv.server.service.conf.node.NodeManager;
import org.ssoup.denv.server.service.runtime.application.ApplicationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: ALB
 * Date: 08/01/14 17:41
 */
public abstract class AbstractEnvironmentManager<T extends Environment> implements EnvironmentManager {

    // used to generate next env id
    private int maxEnvironmentIdInUse = 0;

    private NodeManager nodeManager;

    private ApplicationConfigurationManager applicationConfigurationManager;

    private ApplicationManager applicationManager;

    private List<EnvsEventHandler> eventHandlers = new ArrayList<EnvsEventHandler>();

    public AbstractEnvironmentManager(NodeManager nodeManager, ApplicationConfigurationManager applicationConfigurationManager, ApplicationManager applicationManager) {
        this.nodeManager = nodeManager;
        this.applicationConfigurationManager = applicationConfigurationManager;
        this.applicationManager = applicationManager;
    }

    @Override
    public Environment createEnvironment(Collection<Application> apps, Collection<String> labels) throws DenvException {
        return createEnvironment(apps, labels, null);
    }

    @Override
    public Environment createEnvironment(Collection<Application> apps, Collection<String> labels, String nodeConfId) throws DenvException {
        return createEnvironment(null, apps, labels, nodeConfId);
    }

    @Override
    public Environment createEnvironment(String envId, Collection<Application> apps, Collection<String> labels, String nodeConfId) throws DenvException {
        if (envId == null) {
            envId = generateEnvironmentId();
        }
        Environment env = newEnvironmentInstance(envId, apps, null);
        for (Application app : env.getApplications()) {
            applicationManager.deployApplication(env, app);
        }
        return env;
    }

    @Override
    public Environment addApplications(Environment env, Collection<Application> apps) throws DenvException {
        Collection<Application> capps = convertApplications(apps);
        for (Application app : capps) {
            env.addApplication(app);
            applicationManager.deployApplication(env, app);
        }
        return env;
    }

    @Override
    public void deleteEnvironment(Environment env) throws DenvException {
        for (Application app : env.getApplications()) {
            applicationManager.deleteApplication(env, app);
        }
    }

    protected Environment newEnvironmentInstance(String envId, Collection<Application> apps, NodeConfiguration node) throws DenvException {
        /*int intEnvId = Integer.parseInt(envId);
        if (intEnvId > maxEnvironmentIdInUse) {
            maxEnvironmentIdInUse = intEnvId;
        }*/
        Environment environment = new DenvEnvironment(envId, convertApplications(apps), node);
        return environment;
    }

    protected Collection<Application> convertApplications(Collection<Application> apps) throws DenvException {
        Collection<Application> capps = new ArrayList<Application>();
        for (Application app : apps) {
            Application capp = new ApplicationImpl(app);
            capp.setDeployed(false);
            capp.setStarted(false);
            capps.add(capp);
        }
        return capps;
    }

    private synchronized String generateEnvironmentId() {
        //return UUID.randomUUID().toString();
        maxEnvironmentIdInUse++;
        return Integer.toString(maxEnvironmentIdInUse);
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

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
