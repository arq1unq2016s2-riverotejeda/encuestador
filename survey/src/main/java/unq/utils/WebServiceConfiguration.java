package unq.utils;

import static spark.Spark.port;

/**
 * Created by mrivero on 21/9/16.
 */
public class WebServiceConfiguration {

    private static WebServiceConfiguration ourInstance = new WebServiceConfiguration();

    public static WebServiceConfiguration getInstance() {
        return ourInstance;
    }

    public void initConfiguration() {
        port(9090);
    }
}
