package org.ssoup.denv.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.ssoup.denv.cli.DenvConsole;
import org.ssoup.denv.cli.exception.DenvCLIException;
import org.ssoup.denv.client.DenvClient;
import org.ssoup.denv.client.format.fig.FigConfigurationConverter;
import org.ssoup.denv.client.format.panamax.PanamaxConfigurationConverter;
import org.ssoup.denv.core.containerization.domain.conf.application.ContainerizedApplicationConfiguration;
import org.ssoup.denv.core.model.runtime.Application;
import org.ssoup.denv.core.model.runtime.ApplicationImpl;
import org.ssoup.denv.core.model.runtime.DenvEnvironment;

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
@Parameters(commandNames = "add", separators = "=", commandDescription = "Register a new application")
public class CommandAdd implements DenvCommand {

    public static final String FIG_FORMAT = "fig";
    public static final String PANAMAX_FORMAT = "panamax";

    @Parameter(names={"-i", "--id"}, description = "Identifier of the application to add")
    private String appConfId;

    @Parameter(names={"-s", "--source"}, description = "Source file name or url")
    private String source;

    @Parameter(names={"-f", "--format"}, description = "Application configuration format")
    private String sourceFormat;

    @Parameter(names={"-e", "--envs"}, description = "Ids of the environments", variableArity = true)
    private List<String> envIds = new ArrayList<String>();

    @Parameter(names={"-d", "--deploy"}, description = "Whether to deploy the application immediately or not")
    private boolean deploy;

    @Parameter(names={"-r", "--run"}, description = "Whether to run the application immediately or not")
    private boolean run;

    private DenvConsole console;

    private DenvClient denvClient;

    private FigConfigurationConverter figConfigurationConverter;

    private PanamaxConfigurationConverter panamaxConfigurationConverter;

    @Autowired
    public CommandAdd(DenvConsole console, DenvClient denvClient, FigConfigurationConverter figConfigurationConverter, PanamaxConfigurationConverter panamaxConfigurationConverter) {
        this.console = console;
        this.denvClient = denvClient;
        this.figConfigurationConverter = figConfigurationConverter;
        this.panamaxConfigurationConverter = panamaxConfigurationConverter;
    }

    @Override
    public void execute() throws DenvCLIException {
        String appConfStr = null;
        if (source == null) {
            // try to read from standard input
            try {
                appConfStr = FileCopyUtils.copyToString(new InputStreamReader(System.in));
            } catch (Exception e) {
                throw new DenvCLIException("An error occurred reading configuration from standard input", e);
            }
        } else {
            try {
                // try to read the source as an url
                URL sourceUrl = new URL(source);
                try {
                    InputStream is = (InputStream)sourceUrl.getContent();
                    appConfStr = StreamUtils.copyToString(is, Charset.defaultCharset());
                } catch (IOException ex) {
                    throw new DenvCLIException("An error occurred retrieving the application configuration", ex);
                }
            } catch (MalformedURLException ex) {
                // try to open the source as a file
                File sourceFile = new File(source);
                try {
                    appConfStr = FileCopyUtils.copyToString(new FileReader(sourceFile));
                } catch (FileNotFoundException e1) {
                    throw new DenvCLIException("Unrecognized source", e1);
                } catch (IOException e2) {
                    throw new DenvCLIException("An error occurred reading the application configuration", e2);
                }
            }
        }

        if (appConfStr == null || appConfStr.trim().length() == 0) {
            throw new DenvCLIException("Empty application configuration");
        }

        ContainerizedApplicationConfiguration appConf = null;
        if (sourceFormat == null) {
            // default to FIG format
            sourceFormat = FIG_FORMAT;
        }
        if (FIG_FORMAT.equals(sourceFormat)) {
            if (appConfId == null) {
                throw new DenvCLIException("For Fig format an id for the application needs to be specified with -i option");
            }
            appConf = figConfigurationConverter.convertApplicationConfiguration(appConfId, appConfId, appConfStr);
        } else if (PANAMAX_FORMAT.equals(sourceFormat)) {
            appConf = panamaxConfigurationConverter.convertApplicationConfiguration(appConfStr);
        }

        try {
            String appConfId = denvClient.createOrUpdateContainerizedAppConfig(appConf);
            console.println(appConfId);
        } catch (Exception ex) {
            throw new DenvCLIException("An error occurred: " + ex.getMessage(), ex);
        }

        // if the application must be deployed/started on one or more environments
        if (envIds.size() > 0 && (run || deploy)) {
            if (run && !deploy) {
                // if app is to be started, it has to be deployed first
                deploy = true;
            }

            List <Application> apps = new ArrayList<Application>();
            Application app = new ApplicationImpl(appConf.getId(), appConf.getId());
            app.setDeployed(deploy);
            app.setStarted(run);
            apps.add(app);

            for (String envId : envIds) {
                try {
                    DenvEnvironment env = new DenvEnvironment(envId, envId, apps, null);
                    denvClient.updateEnv(env);
                } catch (Exception e) {
                    throw new DenvCLIException("An error occurred adding applications to environment " + envId + ": " + e.getMessage(), e);
                }
            }
        }
    }
}
