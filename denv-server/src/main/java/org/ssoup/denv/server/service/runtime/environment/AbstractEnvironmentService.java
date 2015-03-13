package org.ssoup.denv.server.service.runtime.environment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentState;
import org.ssoup.denv.server.event.EnvsEvent;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.persistence.EnvironmentRepository;
import org.ssoup.denv.server.service.conf.EnvironmentConfigurationService;
import org.ssoup.denv.server.service.conf.node.NodeManager;
import org.ssoup.denv.server.service.runtime.runtime.EnvironmentRuntimeService;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ALB
 * Date: 08/01/14 17:41
 */
public abstract class AbstractEnvironmentService<T extends Environment> implements EnvironmentService {

    // used to generate next env id
    private int maxEnvironmentIdInUse = 0;

    private NodeManager nodeManager;

    private EnvironmentRepository<Environment> environmentRepository;

    private EnvironmentConfigurationService environmentConfigurationService;

    private EnvironmentRuntimeService environmentRuntimeService;

    private List<EnvsEventHandler> eventHandlers = new ArrayList<EnvsEventHandler>();

    public AbstractEnvironmentService(NodeManager nodeManager, EnvironmentRepository environmentRepository, EnvironmentConfigurationService environmentConfigurationService, EnvironmentRuntimeService environmentRuntimeService) {
        this.nodeManager = nodeManager;
        this.environmentRepository = environmentRepository;
        this.environmentConfigurationService = environmentConfigurationService;
        this.environmentRuntimeService = environmentRuntimeService;
    }

    @Override
    public Environment createEnvironment(Environment env) throws DenvException {
        EnvironmentConfiguration envConf = environmentConfigurationService.findById(env.getEnvironmentConfigurationId());
        if (envConf == null) {
            throw new DenvException("Could not find environment configuration with id " + env.getEnvironmentConfigurationId());
        }
        Environment envToCreate = environmentRepository.newEnvironmentInstance(env, envConf);
        Environment createdEnv = environmentRepository.save(envToCreate);
        return createdEnv;
    }

    @Override
    public Environment updateEnvironment(Environment actualEnv, Environment env) throws DenvException {
        DenvEnvironment aenv = (DenvEnvironment) actualEnv;
        aenv.setName(env.getName());
        aenv.setActualState(env.getActualState());
        aenv.setDesiredState(env.getDesiredState());
        Environment updatedEnv = environmentRepository.save(aenv);
        return updatedEnv;
    }

    @Override
    public Environment findById(String envId) throws DenvException {
        return (Environment) environmentRepository.findOne(envId);
    }

    @Override
    public Page<? extends Environment> listEnvs(Pageable pageable) {
        return environmentRepository.findAll(pageable);
    }

    @Override
    public void deleteEnvironment(String envId) throws DenvException {
        environmentRepository.updateDesiredState(envId, EnvironmentDesiredState.DELETED);
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

    public EnvironmentRuntimeService getEnvironmentRuntimeService() {
        return environmentRuntimeService;
    }
}
