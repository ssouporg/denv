package org.ssoup.denv.server.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.ssoup.denv.common.model.config.environment.EnvironmentConfiguration;
import org.ssoup.denv.server.domain.runtime.environment.DenvEnvironment;
import org.ssoup.denv.server.domain.runtime.environment.Environment;
import org.ssoup.denv.server.exception.DenvException;
import org.ssoup.denv.server.exception.DenvRuntimeException;
import org.ssoup.denv.server.service.runtime.environment.EnvironmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ALB
 * Date: 07/09/14 09:23
 */
public class DenvEnvironmentRepositoryImpl {

    private EnvironmentConfigRepository environmentConfigRepository;

    private EnvironmentManager environmentManager;
/*
    @Autowired
    public DenvEnvironmentRepositoryImpl(EnvironmentConfigRepository environmentConfigRepository, EnvironmentManager environmentManager) {
        this.environmentConfigRepository = environmentConfigRepository;
        this.environmentManager = environmentManager;
    }

    @Override
    public <S extends DenvEnvironment> S save(S env) {
        try {
            EnvironmentConfiguration envConf = env.getEnvironmentConfiguration();
            if (envConf == null) {
                envConf = (EnvironmentConfiguration)environmentConfigRepository.findOne(env.getEnvironmentConfigurationId());
            }
            return (S)environmentManager.createEnvironment(envConf);
        } catch (DenvException e) {
            throw new DenvRuntimeException(e);
        }
    }

    @Override
    public <S extends DenvEnvironment> Iterable<S> save(Iterable<S> envs) {
        List<S> savedEnvs = new ArrayList<S>();
        for (S env : envs) {
            savedEnvs.add(save(env));
        }
        return savedEnvs;
    }

    @Override
    public DenvEnvironment findOne(String envId) {
        try {
            return (DenvEnvironment)environmentManager.findEnvironment(envId);
        } catch (DenvException e) {
            throw new DenvRuntimeException(e);
        }
    }

    @Override
    public boolean exists(String envId) {
        return findOne(envId) != null;
    }

    @Override
    public Iterable<DenvEnvironment> findAll() {
        try {
            List<DenvEnvironment> envs = new ArrayList<DenvEnvironment>();
            for (Environment env : environmentManager.listEnvironments()) {
                envs.add((DenvEnvironment)env);
            }
            return envs;
        } catch (DenvException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<DenvEnvironment> findAll(Iterable<String> ids) {
        List<DenvEnvironment> envs = new ArrayList<DenvEnvironment>();
        for (String envId : ids) {
            envs.add(findOne(envId));
        }
        return envs;
    }

    @Override
    public long count() {
        try {
            return environmentManager.listEnvironments().size();
        } catch (DenvException e) {
            throw new DenvRuntimeException(e);
        }
    }

    @Override
    public void delete(String envId) {
        DenvEnvironment env = findOne(envId);
        delete(env);
    }

    @Override
    public void delete(DenvEnvironment env) {
        try {
            environmentManager.deleteEnvironment(env);
        } catch (DenvException e) {
            throw new DenvRuntimeException(e);
        }
    }

    @Override
    public void delete(Iterable<? extends DenvEnvironment> envs) {
        for (DenvEnvironment env : envs) {
            delete(env);
        }
    }

    @Override
    public void deleteAll() {
        for (DenvEnvironment env : findAll()) {
            delete(env);
        }
    }
    */
}
