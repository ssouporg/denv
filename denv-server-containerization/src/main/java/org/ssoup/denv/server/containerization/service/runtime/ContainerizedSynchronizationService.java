package org.ssoup.denv.server.containerization.service.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.LinkConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.PortConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.*;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.*;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;
import org.ssoup.denv.core.model.runtime.EnvironmentState;
import org.ssoup.denv.server.containerization.exception.ContainerNotFoundException;
import org.ssoup.denv.server.containerization.service.container.ContainerManager;
import org.ssoup.denv.server.containerization.service.container.ImageManager;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.containerization.service.versioning.ContainerVersioningPolicy;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.persistence.EnvironmentRepository;
import org.ssoup.denv.server.persistence.VersionRepository;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentService;
import org.ssoup.denv.server.service.runtime.sync.AbstractSynchronizationService;
import org.ssoup.denv.server.service.versioning.VersionService;

import java.net.SocketTimeoutException;
import java.net.URI;

/**
 * User: ALB
 * Date: 15/11/2014 20:26
 */
@Service
public class ContainerizedSynchronizationService extends AbstractSynchronizationService {

    private EnvironmentService environmentService;

    private ImageManager imageManager;

    private ContainerManager containerManager;

    private NamingStrategy namingStrategy;

    private VersionService versionManager;

    private ContainerVersioningPolicy versioningPolicy;

    private SimpleClientHttpRequestFactory clientHttpRequestFactory;

    @Autowired
    protected ContainerizedSynchronizationService(EnvironmentRepository environmentRepository,
                                                  EnvironmentConfigRepository environmentConfigRepository,
                                                  VersionRepository versionRepository,
                                                  EnvironmentService environmentService,
                                                  ImageManager imageManager,
                                                  ContainerManager containerManager,
                                                  NamingStrategy namingStrategy,
                                                  VersionService versionManager,
                                                  ContainerVersioningPolicy versioningPolicy) {
        super(environmentRepository, environmentConfigRepository, versionRepository);
        this.environmentService = environmentService;
        this.imageManager = imageManager;
        this.containerManager = containerManager;
        this.namingStrategy = namingStrategy;
        this.versionManager = versionManager;
        this.versioningPolicy = versioningPolicy;
        this.clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
    }

    @Override
    protected void updateActualState(Environment env, EnvironmentConfiguration envConf) throws DenvException {
        DenvContainerizedEnvironment denv = ((DenvContainerizedEnvironment) env);
        if (envConf == null) {
            // configuration has been lost => put environment in inconsistent state
            denv.setActualState(EnvironmentState.CONF_UNKNOWN);
            return;
        }

        EnvironmentConfigurationVersion envConfVersion =
                (EnvironmentConfigurationVersion) versionManager.getVersion(denv.getEnvironmentConfigurationId(), env.getVersion());

        ContainerizedEnvironmentConfiguration cenvConf = (ContainerizedEnvironmentConfiguration) envConf;
        ContainerizedEnvironmentRuntimeInfo cenvInfo = (ContainerizedEnvironmentRuntimeInfo) env.getRuntimeInfo();
        boolean allContainersInDesiredState = true;
        boolean allContainersStopped = true;
        boolean allContainersUndeployed = true;
        boolean atLeastOneContainersUndeployed = false;
        boolean atLeastOneContainerStarted = false;
        boolean atLeastOneContainerStarting = false;
        boolean atLeastOneContainerSucceeded = false;
        boolean atLeastOneContainerFailed = false;
        for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
            ContainerRuntimeInfo containerInfo = cenvInfo.getContainerRuntimeInfo(imageConf.getId());
            if (containerInfo == null) {
                // could happen if configuration has changed since last run
                containerInfo = new ContainerRuntimeInfoImpl(imageConf);
                cenvInfo.setContainerRuntimeInfo(imageConf.getId(), containerInfo);
            }
            if (containerInfo.getId() != null) {
                Container container = containerManager.findContainerById(env, envConf, imageConf, containerInfo.getId());
                if (container != null) {
                    allContainersUndeployed = false;
                    updateRuntimeInfoFromRunningContainer(envConfVersion, imageConf, (ContainerRuntimeInfoImpl) containerInfo, container);
                    if (containerInfo.getActualState().isStarted()) {
                        atLeastOneContainerStarted = true;
                    } else if (containerInfo.getActualState() == ContainerState.STARTING) {
                        atLeastOneContainerStarting = true;
                    } else if (containerInfo.getActualState() == ContainerState.SUCCEEDED) {
                        atLeastOneContainerSucceeded = true;
                    } else if (containerInfo.getActualState() == ContainerState.FAILED) {
                        atLeastOneContainerFailed = true;
                    }
                } else {
                    containerInfo.setId(null);
                    containerInfo.setActualState(ContainerState.UNDEPLOYED);
                    atLeastOneContainersUndeployed = true;
                }
            } else {
                containerInfo.setActualState(ContainerState.UNDEPLOYED);
                atLeastOneContainersUndeployed = true;
            }
            if (!containerInfo.getDesiredState().isSatisfiedBy(containerInfo.getActualState())) {
                allContainersInDesiredState = false;
            }
            if (!containerInfo.getActualState().isStopped()) {
                allContainersStopped = false;
            }
        }

        if (allContainersInDesiredState) {
            if (denv.getDesiredState() == EnvironmentDesiredState.STARTED) {
                if (atLeastOneContainerStarted) {
                    denv.setActualState(EnvironmentState.STARTED);
                }
            } else if (denv.getDesiredState() == EnvironmentDesiredState.SUCCEEDED) {
                if (atLeastOneContainerSucceeded) {
                    denv.setActualState(EnvironmentState.SUCCEEDED);
                }
            }
        } else if (atLeastOneContainerFailed) {
            denv.setActualState(EnvironmentState.FAILED);
        } else if (atLeastOneContainerStarting) {
            denv.setActualState(EnvironmentState.STARTING);
        } else if (allContainersStopped) {
            denv.setActualState(EnvironmentState.STOPPED);
        } else if (atLeastOneContainersUndeployed) {
            if (denv.getDesiredState() == EnvironmentDesiredState.DELETED) {
                if (allContainersUndeployed) {
                    denv.setActualState(EnvironmentState.DELETED);
                } else {
                    denv.setActualState(EnvironmentState.DELETING);
                }
            }
        } else {
            if (atLeastOneContainerStarted) {
                denv.setActualState(EnvironmentState.STARTING);
            } else {
                denv.setActualState(EnvironmentState.CREATED);
            }
        }
    }

    @Override
    protected void updateActualState(EnvironmentConfiguration envConf, EnvironmentConfigurationVersion envConfVersion) throws DenvException {
        if (envConfVersion.getDesiredState() == EnvironmentConfigVersionDesiredState.DELETED) {
            ((EnvironmentConfigurationVersionImpl) envConfVersion).setActualState(EnvironmentConfigVersionState.DELETED);
        } else {
            ContainerizedEnvironmentConfiguration cenvConf = (ContainerizedEnvironmentConfiguration) envConf;
            boolean allImagesFound = true;
            for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
                Image image = imageManager.findImage(envConf, envConfVersion.getVersion(), imageConf);
                if (image == null) {
                    allImagesFound = false;
                    break;
                }
            }
            if (allImagesFound) {
                ((EnvironmentConfigurationVersionImpl) envConfVersion).setActualState(EnvironmentConfigVersionState.AVAILABLE);
            } else {
                if (envConfVersion.getActualState() == EnvironmentConfigVersionState.CREATED ||
                        envConfVersion.getActualState() == EnvironmentConfigVersionState.AVAILABLE) {
                    ((EnvironmentConfigurationVersionImpl) envConfVersion).setActualState(EnvironmentConfigVersionState.NOT_AVAILABLE);
                }
            }
        }
    }

    private void updateRuntimeInfoFromRunningContainer(EnvironmentConfigurationVersion envConfVersion, ImageConfiguration imageConf, ContainerRuntimeInfoImpl containerInfo, Container container) throws DenvException {
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
                    if (imageConf.getReadyWhenRespondingOnUrl() != null) {
                        String url = containerInfo.substituteVariables(imageConf.getReadyWhenRespondingOnUrl());
                        if (url.startsWith("http") || url.startsWith("https")) {
                            try {
                                clientHttpRequestFactory.setReadTimeout(1000); // wait at most 1 second.
                                ClientHttpRequest request = clientHttpRequestFactory.createRequest(new URI(url), HttpMethod.GET);
                                ClientHttpResponse response = request.execute();
                                HttpStatus status = response.getStatusCode();
                                if (!status.is2xxSuccessful() && !status.is3xxRedirection()) {
                                    responding = false;
                                    break;
                                }
                            } catch (SocketTimeoutException ex) {
                                responding = false;
                                break;
                            } catch (Exception ex) {
                                throw new DenvException(ex);
                            }
                        } else {
                            throw new DenvException("Don't know how to verify url: " + url);
                        }
                    }
                }
                if (responding) {
                    containerInfo.setActualState(ContainerState.RESPONDING);
                } else if ( containerInfo.getActualState() != ContainerState.STARTING &&
                        containerInfo.getActualState() != ContainerState.RESTARTING) {
                    containerInfo.setActualState(ContainerState.NOT_RESPONDING);
                }
            } else if ( containerInfo.getActualState() != ContainerState.KILLED_BY_DENV &&
                    containerInfo.getActualState() != ContainerState.KILLED_BY_USER) {
                if (container.getExitStatus() != null) {
                    if (container.getExitStatus() == 0) {
                        containerInfo.setActualState(ContainerState.SUCCEEDED);
                    } else {
                        containerInfo.setActualState(ContainerState.FAILED);
                    }
                } else {
                    containerInfo.setActualState(ContainerState.STOPPED);
                }
            }
        }
    }

    @Override
    protected void moveTowardsDesiredState(Environment env, EnvironmentConfiguration envConf) throws DenvException {
        DenvEnvironment denv = ((DenvEnvironment) env);
        EnvironmentDesiredState ed = env.getDesiredState();
        ContainerizedEnvironmentRuntimeInfo cenv = (ContainerizedEnvironmentRuntimeInfo) env.getRuntimeInfo();
        ContainerizedEnvironmentConfiguration cenvConf = (ContainerizedEnvironmentConfiguration) envConf;
        if (ed == EnvironmentDesiredState.STARTED || // STARTED desired state => keep alive,restarting if necessary
                // execute once desired state(s) => execute only once
                (ed.toBeExecutedOnce() && (env.getActualState() == EnvironmentState.CREATED || env.getActualState() == EnvironmentState.STARTING)))
        {
            denv.setActualState(EnvironmentState.STARTING);
            for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
                ContainerRuntimeInfo containerInfo = cenv.getContainerRuntimeInfo(imageConf.getId());
                ContainerDesiredState d = containerInfo.getDesiredState();
                ContainerState a = containerInfo.getActualState();
                if (!d.isSatisfiedBy(a)) {
                    if (d == ContainerDesiredState.UNDEPLOYED) {
                        if (a.isDeployed()) {
                            containerManager.deleteContainer(env, containerInfo.getId());
                        }
                    } else if (d == ContainerDesiredState.STARTED ||
                            d == ContainerDesiredState.SUCCEEDED ||
                            d == ContainerDesiredState.RESPONDING) {
                        if (!a.isStarted() && a != ContainerState.STARTING) {
                            if (env.isBuilder()) {
                                if (imageConf.getBuildCommand() != null) {
                                    String buildCommand = imageConf.getBuildCommand();
                                    EnvironmentConfigurationVersion environmentConfigurationVersion =
                                            (EnvironmentConfigurationVersion) versionManager.getVersion(env.getBuilderTargetEnvConfId(), env.getBuilderTargetVersion());
                                    buildCommand = environmentConfigurationVersion.substituteVariables(buildCommand);
                                    startContainer(env, envConf, cenv, imageConf, containerInfo, buildCommand);
                                }
                            } else {
                                startContainer(env, envConf, cenv, imageConf, containerInfo);
                            }
                        }
                    } else if (d == ContainerDesiredState.STOPPED) {
                        stopContainer(env, envConf, cenv, imageConf, containerInfo);
                    }
                }
            }
        } else if (ed == EnvironmentDesiredState.STOPPED) {
            denv.setActualState(EnvironmentState.STOPPING);
            for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
                ContainerRuntimeInfo containerInfo = cenv.getContainerRuntimeInfo(imageConf.getId());
                ContainerState a = containerInfo.getActualState();
                if (a.isStarted()) {
                    stopContainer(env, envConf, cenv, imageConf, containerInfo);
                }
            }
        } else if (ed == EnvironmentDesiredState.DELETED) {
            denv.setActualState(EnvironmentState.DELETING);
            for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
                ContainerRuntimeInfo containerInfo = cenv.getContainerRuntimeInfo(imageConf.getId());
                ContainerState a = containerInfo.getActualState();
                if (a.isDeployed()) {
                    deleteContainer(env, containerInfo);
                }
            }
        } else if (ed == EnvironmentDesiredState.SUCCEEDED) {
            if (env.isBuilder() && env.getActualState() == EnvironmentState.SUCCEEDED) {
                // save built containers into persistent images
                EnvironmentConfigurationVersion environmentConfigurationVersion =
                        (EnvironmentConfigurationVersion)versionManager.getVersion(env.getBuilderTargetEnvConfId(), env.getBuilderTargetVersion());
                for (ImageConfiguration imageConf : cenvConf.getImages().values()) {
                    if (imageConf.getBuildCommand() != null && imageConf.getServicesToVersionWhenBuildSucceeds() != null) {
                        for (String serviceToVersion : imageConf.getServicesToVersionWhenBuildSucceeds()) {
                            ImageConfiguration serviceImageConf = cenvConf.getImages().get(serviceToVersion);
                            String imageName = environmentConfigurationVersion.substituteVariables(serviceImageConf.getTargetImage());
                            Image image = imageManager.findImage(imageName, serviceImageConf);
                            if (image == null) {
                                ContainerRuntimeInfo containerInfo = cenv.getContainerRuntimeInfo(serviceToVersion);
                                Container containerToVersion = containerManager.findContainerById(env, envConf, serviceImageConf, containerInfo.getId());
                                containerManager.saveContainer(env, containerToVersion, imageName);
                            }
                        }
                    }
                }
                // delete builder environment after successful build
                denv.setDesiredState(EnvironmentDesiredState.DELETED);
            }
        }
    }

    protected Container startContainer(Environment env, EnvironmentConfiguration envConf, ContainerizedEnvironmentRuntimeInfo cenv,
                                        ImageConfiguration imageConf, ContainerRuntimeInfo containerInfo) throws DenvException {
        return startContainer(env, envConf, cenv, imageConf, containerInfo, imageConf.getCommand());
    }

    protected Container startContainer(Environment env, EnvironmentConfiguration envConf, ContainerizedEnvironmentRuntimeInfo cenv,
                                       ImageConfiguration imageConf, ContainerRuntimeInfo containerInfo, String command) throws DenvException {
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
                container = deployContainer(env, envConf, cenv, imageConf, containerInfo, command);
                containerInfo.setId(container.getId());
                containerManager.startContainer(env, container);
                containerInfo.setActualState(ContainerState.STARTING);
            } else if (a.isStopped()) {
                container = containerManager.findContainerById(env, envConf, imageConf, containerInfo.getId());
                containerManager.startContainer(env, container);
                containerInfo.setActualState(ContainerState.STARTING);
            }
        }
        return container;
    }

    protected Container deployContainer(Environment env, EnvironmentConfiguration envConf, ContainerizedEnvironmentRuntimeInfo cenv,
                                   ImageConfiguration imageConf, ContainerRuntimeInfo containerInfo, String command) throws DenvException {
        Image image = imageManager.findOrBuildImage(envConf, env.getVersion(), imageConf);
        String containerName = namingStrategy.generateContainerName(env, imageConf);
        Container container = containerManager.createContainer(env, containerName, imageConf, image, command);
        cenv.setContainerRuntimeInfo(imageConf.getId(), containerInfo);
        return container;
    }

    protected Container stopContainer(Environment env, EnvironmentConfiguration envConf, ContainerizedEnvironmentRuntimeInfo cenv,
                                       ImageConfiguration imageConf, ContainerRuntimeInfo containerInfo) throws DenvException {
        ContainerState a = containerInfo.getActualState();
        Container container = null;
        if (a.isDeployed()) {
            container = containerManager.findContainerById(env, envConf, imageConf, containerInfo.getId());
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

    @Override
    protected void moveTowardsDesiredState(EnvironmentConfiguration envConf, EnvironmentConfigurationVersion envConfVersion)
            throws DenvException {
        EnvironmentConfigurationVersionImpl ecv = (EnvironmentConfigurationVersionImpl)envConfVersion;
        EnvironmentConfigVersionDesiredState cd = envConfVersion.getDesiredState();
        if (cd == EnvironmentConfigVersionDesiredState.AVAILABLE) {
            if (envConfVersion.getActualState() == EnvironmentConfigVersionState.NOT_AVAILABLE) {
                ContainerizedEnvironmentConfiguration cenvConf = (ContainerizedEnvironmentConfiguration) envConf;
                if (cenvConf.getBuilderEnvConfId() == null) {
                    ecv.setActualState(EnvironmentConfigVersionState.BUILD_ERROR);
                    ecv.setBuildError("Builder environment configuration not specified");
                } else {
                    EnvironmentConfiguration builderEnvConf = (EnvironmentConfiguration)getEnvironmentConfigRepository().findOne(cenvConf.getBuilderEnvConfId());
                    DenvContainerizedEnvironment env = (DenvContainerizedEnvironment)
                            this.environmentService.createBuildEnvironment(builderEnvConf, envConf.getId(), envConfVersion.getVersion());
                    ecv.setActualState(EnvironmentConfigVersionState.BUILDING);
                    ecv.setBuildEnvId(env.getId());
                }
            }
        }
    }
}
