package org.ssoup.denv.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.ssoup.denv.cli.command.DenvCommand;
import org.ssoup.denv.cli.exception.DenvCLIException;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 16:36
 */
@Controller
public class DenvCLI implements CommandLineRunner {

    private static Logger LOGGER = LoggerFactory.getLogger(DenvCLI.class);

    public static String PROGRAM_NAME = "denv";

    private JCommander jc;

    private DenvConsole console;

    private List<DenvCommand> commands;

    @Autowired
    public DenvCLI(DenvConsole console, List<DenvCommand> commands) {
        this.commands = commands;
        this.console = console;

        jc = new JCommander();
        jc.setProgramName(PROGRAM_NAME);
        for (DenvCommand command : commands) {
            jc.addCommand(command);
        }
    }

    @PostConstruct
    public void init() {
        Collections.sort(commands, AnnotationAwareOrderComparator.INSTANCE);
    }

    public void run(String[] args) {
        if (args.length == 0) {
            jc.usage();
            return;
        }

        try {
            jc.parse(args);
            String commandName = jc.getParsedCommand();
            JCommander commandJCommander = jc.getCommands().get(commandName);
            DenvCommand command = (DenvCommand)commandJCommander.getObjects().get(0);
            try {
                command.execute();
            } catch (DenvCLIException ex) {
                console.error(ex);
                jc.usage();
            }
        } catch (MissingCommandException e) {
            console.println("Error: Command not found: " + args[0]);
            jc.usage();
        } catch (ParameterException e) {
            console.error(e.getMessage());
            String commandName = jc.getParsedCommand();
            jc.usage(commandName);
        }
    }
}
