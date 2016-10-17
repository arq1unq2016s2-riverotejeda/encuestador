package unq.utils;

import java.util.Map;

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
        //port(9090);
        Map<String, String> value = System.getenv();
        if (value.get("PORT").isEmpty() || value.get("PORT") == null){
            port(9090);
        }
        else port(Integer.valueOf(value.get("PORT")));
        //find a way to set a base url
    }
}
