package org.ssoup.denv.client.format.fig;

import java.util.HashMap;

/**
 * User: ALB
 * Date: 09/08/14 17:37
 */
public class FigApplicationConfiguration extends HashMap<String, FigServiceConfiguration> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
