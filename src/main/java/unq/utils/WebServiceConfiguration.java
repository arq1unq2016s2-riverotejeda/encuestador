package unq.utils;

import java.util.Map;
import java.util.Optional;

import static spark.Spark.port;

/**
 * Created by mrivero on 21/9/16.
 */
public class WebServiceConfiguration {

    private static WebServiceConfiguration ourInstance = new WebServiceConfiguration();

    public static WebServiceConfiguration getInstance() {
        return ourInstance;
    }

    public void initConfiguration(String portConfig) {
        Integer port = Integer.valueOf(Optional.ofNullable(System.getProperty("PORT")).orElse(portConfig));
        port(port);

        //find a way to set a base url
    }


}
