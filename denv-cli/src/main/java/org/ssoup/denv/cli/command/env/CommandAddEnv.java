package org.ssoup.denv.cli.command.env;

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
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:22
 */
@Service @Order(11)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "addenv", separators = "=", commandDescription = "Add a new environment")
public class CommandAddEnv implements DenvCommand {

    @Parameter(description = "Ids of the environments to create")
    private List<String> envIds;

    @Parameter(names={"-c", "--conf"}, description = "Id of the configuration", required = true)
    private String envConfId;

    @Parameter(names={"-v", "--version"}, description = "Environment version")
    private String version;

    @Parameter(names={"-s", "--snapshot"}, description = "Environment snapshot name")
    private String snapshotName;

    @Parameter(names={"-w", "--wait"}, description = "Wait for desired state")
    private boolean waitForDesiredState;

    @Parameter(names={"-m", "--max"}, description = "Maximum wait time for desired state in millis")
    private int maxWaitForDesiredStateTimeInMillis = 60000;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandAddEnv(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String envId : envIds) {
           try {
               denvClient.createEnvironment(envId, envConfId, version, snapshotName);
               if (waitForDesiredState) {
                   denvClient.waitForDesiredState(envId, maxWaitForDesiredStateTimeInMillis);
               }
           } catch (Exception e) {
               throw new DenvCLIException("An error occurred adding environment " + envId, e);
           }
        }
    }
}
