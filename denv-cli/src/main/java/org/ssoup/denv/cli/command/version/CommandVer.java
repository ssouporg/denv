package org.ssoup.denv.cli.command.version;

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
import org.ssoup.denv.core.model.conf.environment.EnvironmentConfigurationVersion;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(32)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "ver", separators = "=", commandDescription = "Show the details of an environment configuration version")
public class CommandVer implements DenvCommand {

    @Parameter(description = "Versions", required = true)
    private List<String> versions;

    @Parameter(names={"-c", "--conf"}, description = "Id of the configuration")
    private String envConfId;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandVer(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        try {
            for (String version : versions) {
                EnvironmentConfigurationVersion ver = denvClient.getVersion(envConfId, version);
                console.printVer(ver);
            }
        } catch (Exception e) {
            throw new DenvCLIException("An error occurred retrieving environment configuration version", e);
        }
    }
}
