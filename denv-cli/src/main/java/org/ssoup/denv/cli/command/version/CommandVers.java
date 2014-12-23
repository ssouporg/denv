package org.ssoup.denv.cli.command.version;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.cli.command.DenvCommand;
import org.ssoup.denv.cli.exception.DenvCLIException;
import org.ssoup.denv.client.DenvClient;

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(30)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "vers", separators = "=", commandDescription = "List all available versions of an environment configuration")
public class CommandVers implements DenvCommand {

    @Parameter(names={"-c", "--conf"}, description = "Id of the configuration", required = true)
    private String envConfId;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandVers(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        try {
            PagedResources vers = denvClient.listVers(envConfId);
            console.printVers(vers.getContent());
        } catch (Exception e) {
            throw new DenvCLIException("An error occurred retrieving environment versions", e);
        }
    }
}
