package org.ssoup.denv.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.command.DenvCommand;
import org.ssoup.denv.cli.exception.DenvCLIException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 16:36
 */
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DenvCLI implements CommandLineRunner {

    private static Logger LOGGER = LoggerFactory.getLogger(DenvCLI.class);

    public static String PROGRAM_NAME = "denv";

    /**
     * Environment variable used to distinguish a standard Denv CLI from a Dockerized Denv CLI
     */
    public static String DOCKERIZED_DENV_CLI = "DOCKERIZED_DENV_CLI";

    /**
     * Path to the host root folder for Dockerized Denv CLI
     */
    public static String DOCKERIZED_DENV_CLI_HOST_ROOT = "/dockerHost";

    /**
     * Path to the host pwd folder for Dockerized Denv CLI
     */
    public static String DOCKERIZED_DENV_CLI_HOST_PWD = "/dockerHostPwd";

    private JCommander jc;

    private ApplicationContext applicationContext;

    private DenvConsole console;

    @Parameter(names = {"-d", "--debug"}, description = "Debug mode")
    private boolean debug = false;

    @Autowired
    public DenvCLI(ApplicationContext applicationContext, DenvConsole console) {
        this.applicationContext = applicationContext;
        this.console = console;
    }

    /**
     * Adjust a user specified path in case Denv CLI is running inside a Docker container.
     *
     * @param path The user specifiedPath
     * @return The adjusted path
     */
    public static String adjustUserPath(String path) {
        if ("true".equalsIgnoreCase(System.getenv(DenvCLI.DOCKERIZED_DENV_CLI))) {
            if (path.startsWith("/")) {
                return DenvCLI.DOCKERIZED_DENV_CLI_HOST_ROOT + path;
            } else {
                return DenvCLI.DOCKERIZED_DENV_CLI_HOST_PWD + "/" + path;
            }
        } else {
            return path;
        }
    }

    private void initJCommander() {
        List<DenvCommand> commands = new ArrayList(applicationContext.getBeansOfType(DenvCommand.class).values());
        Collections.sort(commands, AnnotationAwareOrderComparator.INSTANCE);

        jc = new JCommander(this);
        jc.setProgramName(PROGRAM_NAME);
        for (DenvCommand command : commands) {
            jc.addCommand(command);
        }
    }

    public void run(String[] args) {
        initJCommander();

        if (args.length == 0) {
            jc.usage();
            return;
        }

        try {
            jc.parse(args);
            console.setDebug(debug);

            String commandName = jc.getParsedCommand();
            JCommander commandJCommander = jc.getCommands().get(commandName);
            DenvCommand command = (DenvCommand)commandJCommander.getObjects().get(0);
            try {
                command.execute();
            } catch (DenvCLIException ex) {
                console.error(ex);
            }
        } catch (MissingCommandException e) {
            console.println("Error: Command not found: " + args[0]);
            console.println("");
            jc.usage();
        } catch (ParameterException e) {
            console.error(e);
            console.println("");
            String commandName = jc.getParsedCommand();
            jc.usage(commandName);
        }
    }
}
