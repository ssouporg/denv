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
import org.ssoup.denv.core.exception.ResourceNotFoundException;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(13)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "rmenv", separators = "=", commandDescription = "Remove an environment")
public class CommandRmEnv implements DenvCommand {

    @Parameter(description = "Ids of the environments to remove", required = true)
    private List<String> envIds;

    @Parameter(names={"-w", "--wait"}, description = "Wait for environment to be deleted")
    private boolean waitForDesiredState;

    @Parameter(names={"-m", "--max"}, description = "Maximum wait time for desired state in millis")
    private int maxWaitForDesiredStateTimeInMillis = 60000;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandRmEnv(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String envId : envIds) {
            try {
                try {
                    denvClient.getEnv(envId);
                } catch (ResourceNotFoundException ex) {
                    console.println("Could not find environemt " + envId);
                    continue;
                }

                denvClient.deleteEnvironment(envId);
                if (waitForDesiredState) {
                    denvClient.waitForEnvironmentDeleted(envId, maxWaitForDesiredStateTimeInMillis);
                }
            } catch (Exception e) {
                throw new DenvCLIException("An error occurred removing environment " + envId, e);
            }
        }
    }
}
