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

import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(33)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "rmver", separators = "=", commandDescription = "Remove a version of an environment configuration")
public class CommandRmVer implements DenvCommand {

    @Parameter(description = "Versions of the environment configuration to remove", required = true)
    private List<String> versions;

    @Parameter(names={"-c", "--conf"}, description = "Id of the configuration", required = true)
    private String envConfId;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandRmVer(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String version : versions) {
            try {
                /* TODO
                denvClient.deleteVersion(envConfId, version);
                 */
            } catch (Exception e) {
                throw new DenvCLIException("An error occurred removing environment configuration version " +
                        envConfId + ":" + version, e);
            }
        }
    }
}
