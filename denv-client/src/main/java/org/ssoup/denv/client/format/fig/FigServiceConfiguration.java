package org.ssoup.denv.client.format.fig;

import java.util.Collection;
import java.util.Map;

/**
 * User: ALB
 * Date: 09/08/14 18:14
 */
public class FigServiceConfiguration {

    private String build;
    private String command;
    private String buildCommand;
    private Collection<String> servicesToVersionWhenBuildSucceeds;
    private String targetImage;
    private String image;
    private String[] ports;
    private String[] expose;
    private String[] links;
    private Map<String, String> environment;
    private String[] volumes;
    private String[] volumes_from;
    private String net;
    private Object dns; // can be String or String[]
    private String working_dir;
    private String entrypoint;
    private String user;
    private String hostname;
    private String domainname;
    private String mem_limit;
    private boolean privileged;
    private Map<String, String> variables;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getPorts() {
        return ports;
    }

    public void setPorts(String[] ports) {
        this.ports = ports;
    }

    public String[] getLinks() {
        return links;
    }

    public void setLinks(String[] links) {
        this.links = links;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    public String[] getVolumes() {
        return volumes;
    }

    public void setVolumes(String[] volumes) {
        this.volumes = volumes;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getExpose() {
        return expose;
    }

    public void setExpose(String[] expose) {
        this.expose = expose;
    }

    public String[] getVolumes_from() {
        return volumes_from;
    }

    public void setVolumes_from(String[] volumes_from) {
        this.volumes_from = volumes_from;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public Object getDns() {
        return dns;
    }

    public void setDns(Object dns) {
        this.dns = dns;
    }

    public String getWorking_dir() {
        return working_dir;
    }

    public void setWorking_dir(String working_dir) {
        this.working_dir = working_dir;
    }

    public String getEntrypoint() {
        return entrypoint;
    }

    public void setEntrypoint(String entrypoint) {
        this.entrypoint = entrypoint;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDomainname() {
        return domainname;
    }

    public void setDomainname(String domainname) {
        this.domainname = domainname;
    }

    public String getMem_limit() {
        return mem_limit;
    }

    public void setMem_limit(String mem_limit) {
        this.mem_limit = mem_limit;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged = privileged;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    ///
    /// public interface only (not for snake yaml use)
    ///

    public String[] getDnsList() {
        if (dns == null) return new String[]{};
        if (dns instanceof String) return new String[]{(String)dns};
        return (String[])dns;
    }

    public String getBuildCommand() {
        return buildCommand;
    }

    public void setBuildCommand(String buildCommand) {
        this.buildCommand = buildCommand;
    }

    public String getTargetImage() {
        return targetImage;
    }

    public void setTargetImage(String targetImage) {
        this.targetImage = targetImage;
    }

    public Collection<String> getServicesToVersionWhenBuildSucceeds() {
        return servicesToVersionWhenBuildSucceeds;
    }

    public void setServicesToVersionWhenBuildSucceeds(Collection<String> servicesToVersionWhenBuildSucceeds) {
        this.servicesToVersionWhenBuildSucceeds = servicesToVersionWhenBuildSucceeds;
    }
}
