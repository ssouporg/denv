package org.ssoup.denv.core.model.runtime;

import org.ssoup.denv.core.model.conf.node.NodeConfiguration;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.Environment;

import java.util.*;

/**
 * User: ALB
 * Date: 08/01/14 17:30
 */
public class DenvEnvironment implements Environment {

    private String id;

    // Config info
    private NodeConfiguration node;

    // Runtime info
    // map appId => Application
    private List<String> labels = new ArrayList<String>();
    private Map<String, Application> apps = new HashMap<String, Application>();

    public DenvEnvironment() {
    }

    public DenvEnvironment(Environment env) {
        this.id = env.getId();
        this.node = env.getNode();
        this.setApplications(env.getApplications());
    }

    public DenvEnvironment(String id, Collection<Application> apps, NodeConfiguration node) {
        this.id = id;
        this.setApplications(apps);
        this.node = node;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public NodeConfiguration getNode() {
        return node;
    }

    @Override
    public Collection<Application> getApplications() {
        return apps.values();
    }

    @Override
    public Application getApplication(String appId) {
        return apps.get(appId);
    }

    @Override
    public void addApplication(Application app) {
        apps.put(app.getId(), app);
    }

    @Override
    public void removeApplication(String appId) {
        apps.remove(appId);
    }

    @Override
    public void setApplications(Collection<Application> apps) {
        for (Application app : apps) {
            this.apps.put(app.getId(), app);
        }
    }

    @Override
    public Collection<String> getLabels() {
        return labels;
    }

    @Override
    public boolean hasLabel(String label) {
        return labels.contains(label);
    }

    public void setId(String id) {
        this.id = id;
    }
}
