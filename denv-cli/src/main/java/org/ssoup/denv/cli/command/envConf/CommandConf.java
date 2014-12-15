package org.ssoup.denv.cli.command.envConf;

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
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(22)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "conf", separators = "=", commandDescription = "Show the definition of a configuration")
public class CommandConf implements DenvCommand {

    @Parameter(description = "Id of the configuration", required = true)
    private List<String> envConfIds;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandConf(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        try {
            for (String envConfId : envConfIds) {
                ContainerizedEnvironmentConfiguration envConf = denvClient.getContainerizedEnvConfig(envConfId);
                console.printEnvConf(envConf);
            }
        } catch (Exception e) {
            throw new DenvCLIException("An error occurred retrieving configurations", e);
        }
    }
}
