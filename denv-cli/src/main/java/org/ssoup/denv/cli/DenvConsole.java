package org.ssoup.denv.cli;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;
import org.ssoup.denv.core.containerization.model.conf.environment.ImageConfiguration;
import org.ssoup.denv.core.containerization.model.runtime.ContainerRuntimeInfo;
import org.ssoup.denv.core.containerization.model.runtime.ContainerizedEnvironmentRuntimeInfo;
import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfiguration;
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.Environment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;

/**
 * User: ALB
 * Date: 14/09/14 18:26
 */
@Service
@Scope("singleton")
public class DenvConsole {

    private boolean debug;

    private PrintStream out = System.out;
    private PrintStream err = System.err;

    private ByteArrayOutputStream localOutputStream;
    private ByteArrayOutputStream localErrorStream;

    public void setUseLocalStreams(boolean useLocalStreams) {
        if (useLocalStreams) {
            localOutputStream = new ByteArrayOutputStream();
            localErrorStream = new ByteArrayOutputStream();
            out = new PrintStream(localOutputStream);
            err = new PrintStream(localErrorStream);
        } else {
            out = System.out;
            err = System.err;
        }
    }

    public void println() {
        out.println();
    }

    public void println(String str) {
        out.println(str);
    }

    public void error(String str) {
        err.println(str);
    }

    public void error(Exception ex) {
        if (debug) ex.printStackTrace(err);
        else err.println(ex.getMessage());
    }

    public void printEnvs(Collection<? extends Environment> envs) {
        // see http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        String leftAlignTitleFormat = "%-25s %-20s %10s %20s %-18s %-18s %-12s %-12s %-12s %20s %n";
        String leftAlignFormat = "%-25s %-20s %10s %20s %-18s %-18s %-12d %-12d %-12d %20s %n";

        out.format(leftAlignTitleFormat, "ENVIRONMENT ID", "CONFIGURATION ID", "VERSION", "SNAPSHOT", "ACTUAL STATE", "DESIRED STATE", "IMAGES", "DEPLOYED", "STARTED", "BUILD TARGET");
        for (Environment env : envs) {
            int totalContainers = 0, deployedContainers = 0, runningContainers = 0;
            if (env.getRuntimeInfo() != null) {
                Collection<ContainerRuntimeInfo> containerRuntimeInfos = ((ContainerizedEnvironmentRuntimeInfo) env.getRuntimeInfo()).getContainersRuntimeInfo().values();
                totalContainers = containerRuntimeInfos.size();
                for (ContainerRuntimeInfo containerRuntimeInfo : containerRuntimeInfos) {
                    if (containerRuntimeInfo.getActualState() != null) {
                        if (containerRuntimeInfo.getActualState().isDeployed()) deployedContainers++;
                        if (containerRuntimeInfo.getActualState().isStarted()) runningContainers++;
                    }
                }
            }
            out.format(leftAlignFormat, env.getId(), env.getEnvironmentConfigurationId(),
                    pretty(env.getVersion()),
                    pretty(env.getSnapshotName()),
                    env.getActualState(), env.getDesiredState(), totalContainers, deployedContainers, runningContainers,
                    pretty(env.getBuilderTarget()));
        }
    }

    public void printEnv(DenvEnvironment env) {
        // see http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        String leftAlignTitleFormat = "%-20s %-20s %-18s %-18s %-20s %n";
        String leftAlignFormat = "%-20s %-20s %-18s %-18s %-20s %n";

        out.format(leftAlignTitleFormat, "CONTAINER ID", "IMAGE", "ACTUAL STATE", "DESIRED STATE", "PORTS");
        if (env.getRuntimeInfo() != null) {
            Collection<ContainerRuntimeInfo> containerRuntimeInfos = ((ContainerizedEnvironmentRuntimeInfo) env.getRuntimeInfo()).getContainersRuntimeInfo().values();
            for (ContainerRuntimeInfo containerRuntimeInfo : containerRuntimeInfos) {
                StringBuffer ports = new StringBuffer();
                if (containerRuntimeInfo.getPortMapping() != null) {
                    for (Integer containerPort : containerRuntimeInfo.getPortMapping().keySet()) {
                        Integer hostPort = containerRuntimeInfo.getPortMapping().get(containerPort);
                        if (ports.length() > 0) ports.append(", ");
                        ports.append(hostPort).append("->").append(containerPort);
                    }
                }
                out.format(leftAlignFormat, trimContainerId(containerRuntimeInfo.getId()),
                        containerRuntimeInfo.getImageConfigurationId(),
                        containerRuntimeInfo.getActualState(), containerRuntimeInfo.getDesiredState(),
                        ports);
            }
        }
    }

    public void printEnvConf(ContainerizedEnvironmentConfiguration envConf) {
        // see http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        String leftAlignFormat = "%-30s %-40s %n";
        String leftAlignFormatImage = "    %-30s %-40s %n";

        printKey(leftAlignFormat, "Id", envConf.getId());
        printKey(leftAlignFormat, "Name", envConf.getName());
        printKey(leftAlignFormat, "Description", envConf.getDescription());
        printKey(leftAlignFormat, "Builder configuration id", envConf.getBuilderEnvConfId());
        out.println();

        if (envConf.getImages() != null) {
            out.println("Images:");
            out.println();
            Collection<? extends ImageConfiguration> imageConfs = envConf.getImages().values();
            for (ImageConfiguration imageConf : imageConfs) {
                printKey(leftAlignFormatImage, "Id", imageConf.getId());
                printKey(leftAlignFormatImage, "Name", imageConf.getName());
                printKey(leftAlignFormatImage, "Description", imageConf.getDescription());
                printKey(leftAlignFormatImage, "Source", imageConf.getSource());
                printKey(leftAlignFormatImage, "Initial desired state", imageConf.getInitialDesiredState());
                out.println();
            }
        }
    }

    public void printConfs(Collection<? extends EnvironmentConfiguration> confs) {
        // see http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        String leftAlignTitleFormat = "%-20s %-30s %n";
        String leftAlignFormat      = "%-20s %-30s %n";

        out.format(leftAlignTitleFormat, "CONFIGURATION ID", "DESCRIPTION");
        for (EnvironmentConfiguration conf : confs) {
            out.format(leftAlignFormat, conf.getId(), (conf.getDescription() != null ? conf.getDescription() : ""));
        }
    }

    public void printVers(Collection<? extends EnvironmentConfigurationVersion> vers) {
        // see http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        String leftAlignTitleFormat = "%-20s %-20s %-20s %-30s %-28s %-18s %n";
        String leftAlignFormat = "%-20s %-20s %-20s %-30s %-28s %-18s %n";

        out.format(leftAlignTitleFormat, "VERSION ID", "CONFIGURATION ID", "VERSION", "BUILD ENV ID", "ACTUAL STATE", "DESIRED STATE");
        for (EnvironmentConfigurationVersion ver : vers) {
            out.format(leftAlignFormat, ver.getId(), ver.getEnvConfId(), ver.getVersion(),
                    ver.getBuildEnvId() != null ? ver.getBuildEnvId() : "",
                    ver.getActualState(), ver.getDesiredState());
        }
    }

    public void printVer(EnvironmentConfigurationVersion ver) {
        // see http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        String leftAlignFormat = "%-30s %-40s %n";

        printKey(leftAlignFormat, "Id", ver.getId());
        printKey(leftAlignFormat, "Configuration id", ver.getEnvConfId());
        printKey(leftAlignFormat, "Version", ver.getVersion());
        printKey(leftAlignFormat, "Build environment id", ver.getBuildEnvId());
        out.println();
        String leftAlignTitleFormatVar = "%-20s %-50s %n";
        String leftAlignFormatVar = "%-20s %-50s %n";
        out.format(leftAlignTitleFormatVar, "VARIABLE", "VALUE");
        Map<String, String> vars = ver.getVariables();
        if (vars != null) {
            for (String var : vars.keySet()) {
                String val = vars.get(var);
                if (val == null) val = "";
                out.format(leftAlignFormatVar, var, val);
            }
        }
        out.println();
    }

    private void printKey(String format, String key, Object value) {
        out.format(format, key + ":", (value != null ? value : ""));
    }

    private String pretty(String str) {
        return str != null ? str : "";
    }

    public ByteArrayOutputStream getLocalOutputStream() {
        return localOutputStream;
    }

    public ByteArrayOutputStream getLocalErrorStream() {
        return localErrorStream;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private String trimContainerId(String id) {
        if (id == null) return "";
        return id.substring(0, 12);
    }

    public void printVariables(DenvEnvironment env) {
        // see http://stackoverflow.com/questions/15215326/how-can-i-create-ascii-table-in-java-in-a-console
        String leftAlignTitleFormat = "%-20s %-20s %-50s %n";
        String leftAlignFormat = "%-20s %-20s %-50s %n";

        out.format(leftAlignTitleFormat, "IMAGE", "VARIABLE", "VALUE");
        if (env.getRuntimeInfo() != null) {
            Collection<ContainerRuntimeInfo> containerRuntimeInfos = ((ContainerizedEnvironmentRuntimeInfo) env.getRuntimeInfo()).getContainersRuntimeInfo().values();
            for (ContainerRuntimeInfo containerRuntimeInfo : containerRuntimeInfos) {
                Map<String, String> vars = containerRuntimeInfo.getVariables();
                if (vars != null) {
                    for (String var : vars.keySet()) {
                        String val = vars.get(var);
                        if (val == null) val = "";
                        out.format(leftAlignFormat, containerRuntimeInfo.getImageConfigurationId(), var, val);
                    }
                }
            }
        }
    }
}
