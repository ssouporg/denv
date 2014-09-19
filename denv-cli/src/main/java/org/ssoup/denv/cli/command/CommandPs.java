package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.client.DenvClient;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(40)
@Parameters(commandNames = "ps", separators = "=", commandDescription = "List the running applications")
public class CommandPs implements DenvCommand {

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandPs(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() {
    }
}