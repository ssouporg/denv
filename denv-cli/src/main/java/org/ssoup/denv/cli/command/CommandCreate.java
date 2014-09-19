package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.cli.exception.DenvCLIException;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:22
 */
@Service @Order(11)
@Parameters(commandNames = "create", separators = "=", commandDescription = "Add a new environment")
public class CommandCreate implements DenvCommand {

    @Parameter(description = "Ids of the environments to create")
    private List<String> envIds;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandCreate(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String envId : envIds) {
           try {
               DenvEnvironment env = new DenvEnvironment(envId, null, null);
               denvClient.createEnv(env);
           } catch (Exception e) {
               throw new DenvCLIException("An error occurred removing environment" + envId, e);
           }
        }
    }
}
