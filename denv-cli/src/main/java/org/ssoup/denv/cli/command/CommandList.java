package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.cli.exception.DenvCLIException;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(10)
@Parameters(commandNames = "list", separators = "=", commandDescription = "List environments")
public class CommandList implements DenvCommand {

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandList(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        try {
            PagedResources<DenvEnvironment> envs = denvClient.listEnvs();
            console.printEnvs(envs.getContent());
        } catch (Exception e) {
            throw new DenvCLIException("An error occurred retrieving environments", e);
        }
    }
}