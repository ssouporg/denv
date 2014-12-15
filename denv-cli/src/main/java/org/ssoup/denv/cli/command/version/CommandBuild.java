package org.ssoup.denv.cli.command.version;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.cli.command.DenvCommand;
import org.ssoup.denv.cli.exception.DenvCLIException;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(31)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "build", separators = "=", commandDescription = "Build a new version of an environment")
public class CommandBuild implements DenvCommand {

    @Parameter(description = "Id of the configuration", required = true)
    private List<String> envConfIds;

    @Parameter(names={"-v", "--version"}, description = "Version of the environment to build")
    private String version;

    @Parameter(names={"-w", "--wait"}, description = "Wait for build to complete")
    private boolean waitForBuild;

    @Parameter(names={"-m", "--max"}, description = "Maximum wait time for build in millis")
    private int maxWaitForBuildTimeInMillis = 60000;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandBuild(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String envConfId : envConfIds) {
            try {
                denvClient.buildVersion(envConfId, version);
                /* TODO
                if (waitForBuild) {
                    denvClient.waitForBuild(envConfId, version, maxWaitForBuildTimeInMillis);
                }*/
            } catch (Exception e) {
                throw new DenvCLIException("An error occurred building environment " + envConfId +
                        (version != null ? " " + version : ""), e);
            }
        }
    }
}
