package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.cli.exception.DenvCLIException;
import org.ssoup.denv.client.DenvClient;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(20)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "apps", separators = "=", commandDescription = "List the registered applications")
public class CommandApps implements DenvCommand {

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandApps(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        try {
            PagedResources apps = denvClient.listAppConfigs();
            console.printApps(apps.getContent());
        } catch (Exception e) {
            throw new DenvCLIException("An error occurred retrieving applications", e);
        }
    }
}
