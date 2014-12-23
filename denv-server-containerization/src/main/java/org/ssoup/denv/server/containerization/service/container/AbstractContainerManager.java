package org.ssoup.denv.server.containerization.service.container;

import org.ssoup.denv.core.containerization.model.conf.environment.DenvVariableConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.AbstractContainer;
import org.ssoup.denv.core.containerization.model.runtime.Container;
import org.ssoup.denv.core.model.runtime.Environment;
import org.ssoup.denv.server.containerization.exception.ContainerizationException;
import org.ssoup.denv.server.containerization.service.naming.NamingStrategy;
import org.ssoup.denv.server.containerization.service.versioning.VersioningPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ALB
 * Date: 13/01/14 14:28
 */
public abstract class AbstractContainerManager implements ContainerManager {

    private Map<String, Container> containers;

    private ImageManager imageManager;

    private NamingStrategy namingStrategy;

    private VersioningPolicy versioningPolicy;

    public AbstractContainerManager(ImageManager imageManager, NamingStrategy namingStrategy, VersioningPolicy versioningPolicy) {
        this.imageManager = imageManager;
        this.namingStrategy = namingStrategy;
        this.versioningPolicy = versioningPolicy;
    }

    private void initContainers() throws ContainerizationException {
        containers = new HashMap<String, Container>();
        registerExistingContainers();
    }

    protected abstract void registerExistingContainers();

    protected void registerContainer(String containerId, Container container) throws ContainerizationException {
        this.containers.put(containerId, container);
    }

    protected Map<String, Container> getContainers() throws ContainerizationException {
        //if (this.container == null) {
            this.initContainers();
        //}
        return this.containers;
    }

    @Override
    public List<Container> getAllContainers() throws ContainerizationException {
        return new ArrayList<Container>(this.getContainers().values());
    }

    protected void fillVariables(Environment env, ImageConfiguration imageConf, AbstractContainer container) {
        // fill in Denv variables
        container.setVariables(new HashMap<String, String>());
        if (imageConf.getVariables() != null) {
            for (DenvVariableConfiguration denvVariable : imageConf.getVariables()) {
                String v = denvVariable.getValue();
                v = resolveDenvVariables(v, container);
                container.getVariables().put(denvVariable.getVariable(), v);
            }
        }
    }

    protected String resolveDenvVariables(String value, AbstractContainer container) {
        Pattern p = Pattern.compile("\\$\\{[^\\}]*\\}");
        Matcher m = p.matcher(value);
        while (m.find()) {
            String g = m.group().substring(2, m.group().length() - 1);
            String resolvedVariable = resolveDenvVariable(g, container);
            value = value.replace(m.group(), resolvedVariable);
        }
        return value;
    }

    protected String resolveDenvVariable(String value, Container container) {
        if (value.startsWith("MAPPED_PORT ")) {
            int port = Integer.parseInt(value.substring("MAPPED_PORT ".length()));
            if (container.getPortMapping().containsKey(port)) {
                int mappedPort = container.getPortMapping().get(port);
                return "" + mappedPort;
            } else {
                return "";
            }
        }
        return value;
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public ImageManager getImageManager() {
        return imageManager;
    }

    public VersioningPolicy getVersioningPolicy() {
        return versioningPolicy;
    }
}
