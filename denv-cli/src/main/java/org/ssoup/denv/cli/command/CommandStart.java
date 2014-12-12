package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.cli.exception.DenvCLIException;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;
import org.ssoup.denv.core.model.runtime.EnvironmentDesiredState;

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(14)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "start", separators = "=", commandDescription = "Start an environment")
public class CommandStart implements DenvCommand {

    @Parameter(description = "Ids of the environments to start", required = true)
    private List<String> envIds;

    @Parameter(names={"-w", "--wait"}, description = "Wait for start to complete")
    private boolean waitForDesiredState;

    @Parameter(names={"-m", "--max"}, description = "Maximum wait time for desired state in millis")
    private int maxWaitForDesiredStateTimeInMillis = 60000;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandStart(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String envId : envIds) {
            try {
                DenvEnvironment env = (DenvEnvironment)denvClient.getEnv(envId);
                env.setDesiredState(EnvironmentDesiredState.STARTED);
                denvClient.updateEnvironment(env);
                if (waitForDesiredState) {
                    denvClient.waitForDesiredState(envId, maxWaitForDesiredStateTimeInMillis);
                }
            } catch (Exception e) {
                throw new DenvCLIException("An error occurred starting environment" + envId, e);
            }
        }
    }
}
