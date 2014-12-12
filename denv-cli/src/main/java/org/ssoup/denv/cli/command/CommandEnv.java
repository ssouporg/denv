package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameter;
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
import org.ssoup.denv.core.containerization.model.runtime.DenvContainerizedEnvironment;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(12)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "env", separators = "=", commandDescription = "Show the details of an environment")
public class CommandEnv implements DenvCommand {

    @Parameter(description = "Id of the environment", required = true)
    private List<String> envIds;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandEnv(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        try {
            for (String envId : envIds) {
                DenvEnvironment env = denvClient.getEnv(envId);
                console.printEnv(env);
            }
        } catch (Exception e) {
            throw new DenvCLIException("An error occurred retrieving environments", e);
        }
    }
}
