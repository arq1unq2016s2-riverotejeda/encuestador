package unq.api.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.servlet.SparkApplication;
import unq.api.controller.SurveyController;

/**
 * Created by mrivero on 17/9/16.
 */
public class ApiService implements SparkApplication {

    public static Logger LOGGER = LogManager.getLogger(ApiService.class);

    SurveyController surveyController;

    public static void main(String[] args) {
        LOGGER.info("Mira mira!!!");
        SparkApplication app = new ApiService();
        System.out.print("Chaaan");
        app.init();
    }

    @Override
    public void init() {
        surveyController = new SurveyController();
        surveyController.initSurveyEndopints();
    }

}