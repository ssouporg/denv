package org.ssoup.denv.server.containerization.service.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.LinkConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.PortConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.*;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentState;
import org.ssoup.denv.server.containerization.exception.ContainerNotFoundException;
import org.ssoup.denv.server.containerization.service.container.ContainerManager;
import org.ssoup.denv.server.containerization.service.container.ImageManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.persistence.EnvironmentRepository;
import org.ssoup.denv.server.service.runtime.sync.AbstractSynchronizationService;

/**
 * User: ALB
 * Date: 15/11/2014 20:26
 */
@Service
public class ContainerizedSynchronizationService extends AbstractSynchronizationService {

    private ImageManager imageManager;

    private ContainerManager containerManager;

    private NamingStrategy namingStrategy;

    @Autowired
    protected ContainerizedSynchronizationService(EnvironmentRepository environmentRepository,
                                                  EnvironmentConfigRepository environmentConfigRepository,
                                                  ImageManager imageManager, ContainerManager containerManager,
                                                  NamingStrategy namingStrategy) {
        super(environmentRepository, environmentConfigRepository);
        this.imageManager = imageManager;
        this.containerManager = containerManager;
        this.namingStrategy = namingStrategy;
    }

    @Override
    protected void updateActualState(Environment env, EnvironmentConfiguration envConf) throws DenvException {
        DenvEnvironment denv = ((DenvEnvironment) env);
        if (envConf == null) {
            // configuration has been lost => put environment in inconsistent state
            denv.setActualState(EnvironmentState.INCONSISTENT);
            return;
        }

        ContainerizedEnvironmentConfiguration cenvConf = (ContainerizedEnvironmentConfiguration)envConf;
        ContainerizedEnvironmentRuntimeInfo cenvInfo = (ContainerizedEnvironmentRuntimeInfo)env.getRuntimeInfo();
        boolean allContainersInDesiredState = true;
        boolean allContainersStopped = true;
        boolean allContainersDeleted = true;
        for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
            ContainerRuntimeInfo containerInfo = cenvInfo.getContainerRuntimeInfo(imageConf.getId());
            if (containerInfo == null) {
                // could happen if configuration has changed since last run
                containerInfo = new ContainerRuntimeInfoImpl(imageConf);
                cenvInfo.setContainerRuntimeInfo(imageConf.getId(), containerInfo);
            }
            if (containerInfo.getId() != null) {
                Container container = containerManager.findContainerById(env, imageConf, containerInfo.getId());
                if (container != null) {
                    allContainersDeleted = false;
                    updateRuntimeInfoFromRunningContainer(imageConf, (ContainerRuntimeInfoImpl) containerInfo, container);
                } else {
                    containerInfo.setId(null);
                    containerInfo.setActualState(ContainerState.UNDEPLOYED);
                }
            }
            if (!containerInfo.getDesiredState().isSatisfiedBy(containerInfo.getActualState())) {
                allContainersInDesiredState = false;
            }
            if (!containerInfo.getActualState().isStopped()) {
                allContainersStopped = false;
            }
        }
        if (env.getDesiredState() == EnvironmentDesiredState.STARTED) {
            if (allContainersInDesiredState) {
                denv.setActualState(EnvironmentState.STARTED);
            } else {
                denv.setActualState(EnvironmentState.STARTING);
            }
        } else if (allContainersStopped) {
            denv.setActualState(EnvironmentState.STOPPED);
        } else if (allContainersDeleted) {
            denv.setActualState(EnvironmentState.DELETED);
        }
    }

    private void updateRuntimeInfoFromRunningContainer(ImageConfiguration imageConf, ContainerRuntimeInfoImpl containerInfo, Container container) {
        containerInfo.setId(container.getId());
        containerInfo.setNames(container.getAllNames());
        containerInfo.setHostname(container.getHostname());
        containerInfo.setPortMapping(container.getPortMapping());
        if (container == null) {
            containerInfo.setActualState(ContainerState.UNDEPLOYED);
        } else {
            containerInfo.setVariables(container.getVariables());
            if (container.isRunning()) {
                boolean responding = true;
                for (PortConfiguration portConfiguration : imageConf.getPorts()) {
                    if (!containerManager.isContainerListeningOnPort(container, portConfiguration)) {
                        responding = false;
                        break;
                    }
                }
                if (responding) {
                    containerInfo.setActualState(ContainerState.RESPONDING);
                } else if ( containerInfo.getActualState() != ContainerState.STARTING &&
                        containerInfo.getActualState() != ContainerState.RESTARTING) {
                    containerInfo.setActualState(ContainerState.NOT_RESPONDING);
                }
            } else if ( containerInfo.getActualState() != ContainerState.KILLED_BY_DENV &&
                    containerInfo.getActualState() != ContainerState.KILLED_BY_DENV) {
                containerInfo.setActualState(ContainerState.STOPPED);
            }
        }
    }

    @Override
    protected void moveTowardsDesiredState(Environment env, EnvironmentConfiguration envConf) throws DenvException {
        EnvironmentDesiredState ed = env.getDesiredState();
        if (ed == EnvironmentDesiredState.STARTED) {
            ContainerizedEnvironmentRuntimeInfo cenv = (ContainerizedEnvironmentRuntimeInfo) env.getRuntimeInfo();
            ContainerizedEnvironmentConfiguration cenvConf = (ContainerizedEnvironmentConfiguration) envConf;
            for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
                ContainerRuntimeInfo containerInfo = cenv.getContainerRuntimeInfo(imageConf.getId());
                ContainerDesiredState d = containerInfo.getDesiredState();
                ContainerState a = containerInfo.getActualState();
                if (d == ContainerDesiredState.UNDEPLOYED) {
                    if (a.isDeployed()) {
                        containerManager.deleteContainer(env, containerInfo.getId());
                    }
                } else if (d == ContainerDesiredState.STARTED || d == ContainerDesiredState.RESPONDING) {
                    startContainer(env, cenv, imageConf, containerInfo);
                } else if (d == ContainerDesiredState.STOPPED) {
                    stopContainer(env, cenv, imageConf, containerInfo);
                }
            }
        } else if (ed == EnvironmentDesiredState.STOPPED) {
            ((DenvEnvironment) env).setActualState(EnvironmentState.STOPPING);
            ContainerizedEnvironmentRuntimeInfo cenv = (ContainerizedEnvironmentRuntimeInfo) env.getRuntimeInfo();
            ContainerizedEnvironmentConfiguration cenvConf = (ContainerizedEnvironmentConfiguration) envConf;
            for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
                ContainerRuntimeInfo containerInfo = cenv.getContainerRuntimeInfo(imageConf.getId());
                ContainerState a = containerInfo.getActualState();
                if (a.isStarted()) {
                    stopContainer(env, cenv, imageConf, containerInfo);
                }
            }
        } else if (ed == EnvironmentDesiredState.DELETED) {
            ((DenvEnvironment) env).setActualState(EnvironmentState.DELETING);
            ContainerizedEnvironmentRuntimeInfo cenv = (ContainerizedEnvironmentRuntimeInfo) env.getRuntimeInfo();
            ContainerizedEnvironmentConfiguration cenvConf = (ContainerizedEnvironmentConfiguration) envConf;
            for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
                ContainerRuntimeInfo containerInfo = cenv.getContainerRuntimeInfo(imageConf.getId());
                ContainerState a = containerInfo.getActualState();
                if (a.isDeployed()) {
                    deleteContainer(env, containerInfo);
                }
            }
        }
    }

    protected Container startContainer(Environment env, ContainerizedEnvironmentRuntimeInfo cenv,
                                        ImageConfiguration imageConf, ContainerRuntimeInfo containerInfo) throws DenvException {
        ContainerState a = containerInfo.getActualState();
        boolean allLinkedContainersListening = true;
        if (imageConf.getLinks() != null) {
            for (LinkConfiguration link : imageConf.getLinks()) {
                ContainerRuntimeInfo linkedContainer = cenv.getContainerRuntimeInfo(link.getService());
                if (linkedContainer == null) {
                    throw new DenvException("Could not find container for image " + link.getService());
                }
                if (linkedContainer.getActualState() != ContainerState.RESPONDING) {
                    allLinkedContainersListening = false;
                    break;
                }
            }
        }
        Container container = null;
        if (allLinkedContainersListening) {
            if (a == null || !a.isDeployed()) {
                container = deployContainer(env, cenv, imageConf, containerInfo);
                containerInfo.setId(container.getId());
                containerManager.startContainer(env, container);
                containerInfo.setActualState(ContainerState.STARTING);
            } else if (a.isStopped()) {
                container = containerManager.findContainerById(env, imageConf, containerInfo.getId());
                containerManager.startContainer(env, container);
                containerInfo.setActualState(ContainerState.STARTING);
            }
        }
        return container;
    }

    protected Container deployContainer(Environment env, ContainerizedEnvironmentRuntimeInfo cenv,
                                   ImageConfiguration imageConf, ContainerRuntimeInfo containerInfo) throws DenvException {
        Image image = imageManager.findOrBuildImage(env, imageConf);
        String command = null; // getCmd(env, serviceConf);
        String containerName = namingStrategy.generateContainerName(env, imageConf);
        Container container = containerManager.createContainer(env, containerName, imageConf, image, command);
        cenv.setContainerRuntimeInfo(imageConf.getId(), containerInfo);
        return container;
    }

    protected Container stopContainer(Environment env, ContainerizedEnvironmentRuntimeInfo cenv,
                                       ImageConfiguration imageConf, ContainerRuntimeInfo containerInfo) throws DenvException {
        ContainerState a = containerInfo.getActualState();
        Container container = null;
        if (!a.isDeployed()) {
            container = deployContainer(env, cenv, imageConf, containerInfo);
        } else if (a.isStarted()) {
            container = containerManager.findContainerById(env, imageConf, containerInfo.getId());
            containerManager.stopContainer(env, container);
            containerInfo.setActualState(ContainerState.STOPPING);
        }
        return container;
    }

    protected void deleteContainer(Environment env, ContainerRuntimeInfo containerInfo) throws DenvException {
        ContainerState a = containerInfo.getActualState();
        try {
            containerManager.deleteContainer(env, containerInfo.getId());
        } catch (ContainerNotFoundException ex) {
            // if the container could not be found consider it as deleted
        }
        containerInfo.setActualState(ContainerState.KILLED_BY_DENV);
    }
}
