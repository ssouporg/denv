package org.ssoup.denv.cli.command.envConf;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.ssoup.denv.cli.DenvCLI;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.cli.command.DenvCommand;
import org.ssoup.denv.cli.exception.DenvCLIException;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.client.format.fig.FigConfigurationConverter;
import org.ssoup.denv.client.format.panamax.PanamaxConfigurationConverter;
import org.ssoup.denv.core.containerization.model.conf.environment.ContainerizedEnvironmentConfiguration;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ALB
 * Date: 14/09/14 17:22
 */
@Service @Order(21)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Parameters(commandNames = "addconf", separators = "=", commandDescription = "Register a new configuration")
public class CommandAddConf implements DenvCommand {

    public static final String FIG_FORMAT = "fig";
    public static final String PANAMAX_FORMAT = "panamax";

    @Parameter(description = "Source file name or url", required = true)
    private List<String> sources;

    @Parameter(names={"-i", "--id"}, description = "Identifier of the configuration to add")
    private String envConfId;

    @Parameter(names={"-b", "--builder"}, description = "Identifier of the configuration of the builder environment")
    private String builderEnvConfId;

    @Parameter(names={"-f", "--format"}, description = "Configuration format")
    private String sourceFormat;

    private DenvConsole console;

    private DenvClient denvClient;

    private FigConfigurationConverter figConfigurationConverter;

    private PanamaxConfigurationConverter panamaxConfigurationConverter;

    @Autowired
    public CommandAddConf(DenvConsole console, DenvClient denvClient, FigConfigurationConverter figConfigurationConverter, PanamaxConfigurationConverter panamaxConfigurationConverter) {
        this.console = console;
        this.denvClient = denvClient;
        this.figConfigurationConverter = figConfigurationConverter;
        this.panamaxConfigurationConverter = panamaxConfigurationConverter;
    }

    @Override
    public void execute() throws DenvCLIException {
        for (String source : sources) {
            execute(source);
        }
    }

    private void execute(String source) throws DenvCLIException {
        String envConfStr = null;
        if (source == null) {
            // try to read from standard input
            try {
                envConfStr = FileCopyUtils.copyToString(new InputStreamReader(System.in));
            } catch (Exception e) {
                throw new DenvCLIException("An error occurred reading configuration from standard input", e);
            }
        } else {
            try {
                // try to read the source as an url
                URL sourceUrl = new URL(source);
                try {
                    InputStream is = (InputStream)sourceUrl.getContent();
                    envConfStr = StreamUtils.copyToString(is, Charset.defaultCharset());
                } catch (IOException ex) {
                    throw new DenvCLIException("An error occurred retrieving the configuration", ex);
                }
            } catch (MalformedURLException ex) {
                // try to open the source as a file
                File sourceFile = new File(DenvCLI.adjustUserPath(source));
                try {
                    envConfStr = FileCopyUtils.copyToString(new FileReader(sourceFile));
                } catch (FileNotFoundException e1) {
                    throw new DenvCLIException("Unrecognized source", e1);
                } catch (IOException e2) {
                    throw new DenvCLIException("An error occurred reading the configuration", e2);
                }
            }
        }

        if (envConfStr == null || envConfStr.trim().length() == 0) {
            throw new DenvCLIException("Empty configuration");
        }

        ContainerizedEnvironmentConfiguration envConf = null;
        if (sourceFormat == null) {
            // default to FIG format
            sourceFormat = FIG_FORMAT;
        }
        if (FIG_FORMAT.equals(sourceFormat)) {
            if (envConfId == null) {
                throw new DenvCLIException("For Fig format an id for the configuration needs to be specified with -i option");
            }
            envConf = figConfigurationConverter.convertEnvironmentConfiguration(envConfId, envConfId, builderEnvConfId, envConfStr);
        } else if (PANAMAX_FORMAT.equals(sourceFormat)) {
            envConf = panamaxConfigurationConverter.convertEnvironmentConfiguration(envConfStr);
        }

        try {
            String appConfId = denvClient.createOrUpdateContainerizedEnvConfig(envConf);
            console.println(appConfId);
        } catch (Exception ex) {
            throw new DenvCLIException("An error occurred: " + ex.getMessage(), ex);
        }
    }
}
