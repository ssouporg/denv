package org.ssoup.denv.server.service.runtime.environment;

import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.node.NodeConfiguration;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfo;
import org.ssoup.denv.core.model.runtime.EnvironmentRuntimeInfoImpl;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.server.event.EnvsEvent;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.server.service.conf.node.NodeManager;
import org.ssoup.denv.server.service.runtime.runtime.EnvironmentRuntimeManager;

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

    private EnvironmentRuntimeManager environmentRuntimeManager;

    private List<EnvsEventHandler> eventHandlers = new ArrayList<EnvsEventHandler>();

    public AbstractEnvironmentManager(NodeManager nodeManager, EnvironmentRuntimeManager environmentRuntimeManager) {
        this.nodeManager = nodeManager;
        this.environmentRuntimeManager = environmentRuntimeManager;
    }

    @Override
    public Environment createEnvironment(Environment env) throws DenvException {
        Environment createdEnv = newEnvironmentInstance(env);
        return createdEnv;
    }

    @Override
    public Environment updateEnvironment(Environment actualEnv, Environment env) throws DenvException {
        DenvEnvironment aenv = (DenvEnvironment)actualEnv;
        aenv.setName(env.getName());
        aenv.setActualState(env.getActualState());
        aenv.setDesiredState(env.getDesiredState());
        return aenv;
    }

    protected abstract Environment newEnvironmentInstance(Environment env) throws DenvException;

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

    public EnvironmentRuntimeManager getEnvironmentRuntimeManager() {
        return environmentRuntimeManager;
    }
}
