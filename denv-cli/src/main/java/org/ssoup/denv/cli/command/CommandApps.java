package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.core.model.conf.application.ApplicationConfiguration;
//import org.ssoup.denv.client.DenvClient;

/**
 * User: ALB
 * Date: 14/09/14 17:07
 */
@Service
@Parameters(commandNames = "apps", separators = "=", commandDescription = "List application configurations")
public class CommandApps implements DenvCommand {

    private DenvConsole console;

    private DenvClient denvClient;

    @Autowired
    public CommandApps(DenvConsole console, DenvClient denvClient) {
        this.console = console;
        this.denvClient = denvClient;
    }

    @Override
    public void execute() {
        try {
            PagedResources appConfs = denvClient.listAppConfigs();
            for (Object o : appConfs.getContent()) {
                ApplicationConfiguration appConf = (ApplicationConfiguration)o;
                console.println(appConf.getId());
            }
        } catch (Exception e) {
            console.error("An error occurred retrieving application configurations");
            e.printStackTrace();
        }
    }
}
