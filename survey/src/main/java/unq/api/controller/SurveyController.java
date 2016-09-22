package unq.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unq.api.model.Student;
import unq.utils.GsonFactory;
import unq.utils.WebServiceConfiguration;

import javax.servlet.http.HttpServletResponse;


import static spark.Spark.*;



/**
 * Created by mrivero on 17/9/16.
 */
public class SurveyController {

    public static Logger LOGGER = LoggerFactory.getLogger(SurveyController.class);


    public static void initSurveyEndopints(){

        get("/student/:name", (request, response) -> {
            response.type("application/json");
            Student student = new Student("legajo", request.params("name"));
            return GsonFactory.toJson(student);
        });

        post("/student", (request, response) -> {
            response.type("application/json");
            try {
                Student student = GsonFactory.fromJson(request.body(), Student.class);
                LOGGER.info(student.getName());
            } catch (Exception e) {

            }
            return HttpServletResponse.SC_OK;
        });
    }
}
