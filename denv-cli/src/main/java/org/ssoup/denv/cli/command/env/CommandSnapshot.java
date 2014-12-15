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

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(17)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "snapshot", separators = "=", commandDescription = "Create a snapshot of an environment")
public class CommandSnapshot implements DenvCommand {

    @Parameter(description = "Id of the environment", required = true)
    private List<String> envIds;

    @Parameter(names={"-n", "--name"}, description = "The name of the snapshot", required = true)
    private String snapshotName;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandSnapshot(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        try {
            for (String envId : envIds) {
                denvClient.saveSnapshot(envId, snapshotName);
            }
        } catch (Exception e) {
            throw new DenvCLIException("An error occurred creating snapshot", e);
        }
    }
}
