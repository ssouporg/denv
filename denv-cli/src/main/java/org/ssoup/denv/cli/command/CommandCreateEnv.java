package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.DenvConsole;

/**
 * User: ALB
 * Date: 14/09/14 17:22
 */
@Service
@Parameters(commandNames = "create", separators = "=", commandDescription = "Add a new environment")
public class CommandCreateEnv implements DenvCommand {

    private DenvConsole console;

    //private DenvClient denvClient;

    @Autowired
    public CommandCreateEnv(DenvConsole console /*, DenvClient denvClient*/) {
        this.console = console;
        //this.denvClient = denvClient;
    }

    @Override
    public void execute() {
       /* try {
            Environment env = null;
            denvClient.createEnv(env);
        } catch (DenvHttpException e) {
            console.error("An error occurred creating the environment");
        }*/
    }
}
