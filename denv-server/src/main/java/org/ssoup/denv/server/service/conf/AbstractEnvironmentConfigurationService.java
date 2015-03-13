package org.ssoup.denv.server.service.conf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ssoup.denv.core.exception.DenvException;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.server.event.EnvsEventHandler;
import org.ssoup.denv.server.persistence.EnvironmentConfigRepository;
import org.ssoup.denv.server.service.conf.node.NodeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ALB
 * Date: 08/01/14 17:41
 */
public abstract class AbstractEnvironmentConfigurationService<T extends EnvironmentConfiguration> implements EnvironmentConfigurationService {

    private NodeManager nodeManager;

    private EnvironmentConfigRepository<EnvironmentConfiguration> environmentConfigRepository;

    private List<EnvsEventHandler> eventHandlers = new ArrayList<EnvsEventHandler>();

    public AbstractEnvironmentConfigurationService(EnvironmentConfigRepository environmentConfigRepository) {
        this.environmentConfigRepository = environmentConfigRepository;
    }

    @Override
    public EnvironmentConfiguration findById(String envConfId) throws DenvException {
        return (EnvironmentConfiguration) environmentConfigRepository.findOne(envConfId);
    }

    @Override
    public Page<? extends EnvironmentConfiguration> listEnvConfs(Pageable pageable) {
        return environmentConfigRepository.findAll(pageable);
    }

    @Override
    public void deleteEnvironmentConfiguration(String envConfId) throws DenvException {
        environmentConfigRepository.delete(envConfId);
    }

    @Override
    public EnvironmentConfiguration saveEnvironmentConfiguration(EnvironmentConfiguration envConf) throws DenvException {
        return environmentConfigRepository.save(envConf);
    }
}
