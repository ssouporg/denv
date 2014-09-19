package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.cli.exception.DenvCLIException;
import org.ssoup.denv.client.DenvClient;

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(12)
@Parameters(commandNames = "rm", separators = "=", commandDescription = "Remove an environment")
public class CommandRm implements DenvCommand {

    @Parameter(description = "Ids of the environments to remove")
    private List<String> envIds;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandRm(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String envId : envIds) {
            try {
                denvClient.deleteEnv(envId);
            } catch (Exception e) {
                throw new DenvCLIException( "An error occurred removing environment" + envId, e);
            }
        }
    }
}
