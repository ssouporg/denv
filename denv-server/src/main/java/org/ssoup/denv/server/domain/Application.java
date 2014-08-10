package org.ssoup.denv.server.domain;

/**
 * User: ALB
 * Date: 28/07/14 17:12
 */
public class Application {

    private String id;
    private String name;

    public Application(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
