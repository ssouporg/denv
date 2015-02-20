package org.ssoup.denv.cli.command.version;

import com.beust.jcommander.DynamicParameter;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service @Order(31)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "addver", separators = "=", commandDescription = "Adds a new version of an environment")
public class CommandAddVer implements DenvCommand {

    @Parameter(description = "Version of the environment to add", required = true)
    private List<String> versions;

    @Parameter(names={"-c", "--conf"}, description = "Id of the configuration", required = true)
    private String envConfId;

    @DynamicParameter(names = "-D", description = "Version specific variables")
    private Map<String, String> variables = new HashMap<String, String>();

    @Parameter(names={"-w", "--wait"}, description = "Wait for operation to complete")
    private boolean wait;

    @Parameter(names={"-m", "--max"}, description = "Maximum wait time in millis")
    private int maxWaitTimeInMillis = 60000;

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandAddVer(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String version : versions) {
            try {
                denvClient.addVersion(envConfId, version, variables);
                /* TODO
                if (waitForBuild) {
                    denvClient.waitForBuild(envConfId, version, maxWaitForBuildTimeInMillis);
                }*/
            } catch (Exception e) {
                throw new DenvCLIException("An error occurred adding environment version " + version + " of " + envConfId, e);
            }
        }
    }
}
