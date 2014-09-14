package org.ssoup.denv.cli;

import org.springframework.stereotype.Service;

import java.io.PrintStream;

/**
 * User: ALB
 * Date: 14/09/14 18:26
 */
@Service
public class DenvConsole {

    private PrintStream out = System.out;
    private PrintStream err = System.err;

    public void println(String str) {
        out.println(str);
    }

    public void error(String str) {
        out.println(str);
    }
}
