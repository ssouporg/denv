package org.ssoup.denv.core.api;

/**
 * User: ALB
 * Date: 28/08/14 16:31
 */
public class DenvApiEndpoints {

    // absolute
    public static final String ENVS = "/environments";
    public static final String ENV_CONFIGS = "/environmentConfigs";
    public static final String ENV_CONFIG_VERSIONS = "/environmentConfigs/{envConfId}/versions";

    // relative
    public static final String ENV_SAVE_SNAPSHOT = "/{envId}/saveSnapshot";
    public static final String ENV_CONFIG_VERSION = "/{version:.+}";
    public static final String TEST_RESULTS = "/{envConfId}/{version}/testResults";
    public static final String GET_LATEST_STABLE_VERSION = "/{envConfId}/getLatestStableVersion";
}
