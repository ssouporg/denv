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

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(23)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "rmconf", separators = "=", commandDescription = "Remove a configuration")
public class CommandRmConf implements DenvCommand {

    @Parameter(description = "Ids of the configurations to remove")
    private List<String> appIds;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandRmConf(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String appId : appIds) {
            try {
                denvClient.deleteEnvConfig(appId);
            } catch (Exception e) {
                throw new DenvCLIException( "An error occurred removing configuration " + appId, e);
            }
        }
    }
}
