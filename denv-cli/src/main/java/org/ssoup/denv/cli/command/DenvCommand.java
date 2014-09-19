package org.ssoup.denv.cli.command;

import org.ssoup.denv.cli.exception.DenvCLIException;

/**
 * User: ALB
 * Date: 14/09/14 17:29
 */
public interface DenvCommand {
    void execute() throws DenvCLIException;
}
