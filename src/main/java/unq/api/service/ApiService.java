package unq.api.service;


import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unq.api.controller.SurveyController;
import unq.utils.EnvConfiguration;
import unq.utils.WebServiceConfiguration;

/**
 * Created by mrivero on 17/9/16.
 */
public class ApiService {

   public static Logger LOGGER = LoggerFactory.getLogger(ApiService.class);

    public static void main(String[] args) {
        LOGGER.info("Starting services configuration");
        WebServiceConfiguration.getInstance().initConfiguration(EnvConfiguration.configuration.getString("port"));
        SurveyController.initSurveyEndopints();
        LOGGER.info("Finish services configuration");
    }

}