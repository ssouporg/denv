package org.ssoup.denv.cli.command.envConf;

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

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(20)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "confs", separators = "=", commandDescription = "List registered configurations")
public class CommandConfs implements DenvCommand {

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandConfs(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        try {
            PagedResources confs = denvClient.listEnvConfigs();
            console.printConfs(confs.getContent());
        } catch (Exception e) {
            throw new DenvCLIException("An error occurred retrieving configurations", e);
        }
    }
}
