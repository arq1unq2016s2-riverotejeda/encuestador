package unq.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unq.api.model.Student;
import unq.api.model.Subject;
import unq.api.model.Survey;
import unq.api.service.SurveyService;
import unq.api.service.impl.SurveyServiceImpl;
import unq.utils.GsonFactory;

import javax.servlet.http.HttpServletResponse;


import static spark.Spark.*;

/**
 * Created by mrivero on 17/9/16.
 */
public class SurveyController {

    public static Logger LOGGER = LoggerFactory.getLogger(SurveyController.class);

    private static SurveyService surveyService = new SurveyServiceImpl();

    public static void initSurveyEndopints(){

        get("/student/:legajo", (request, response) -> {
            response.type("application/json");
            Student student = surveyService.getStudentByID(request.params("legajo"));
            return GsonFactory.toJson(student);
        });

        post("/student", (request, response) -> {
            response.type("application/json");
            try {
                Student student = GsonFactory.fromJson(request.body(), Student.class);
                surveyService.saveStudent(student);
            } catch (Exception e) {
                LOGGER.error("Error while trying to save student", e);
                return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

            }
            return HttpServletResponse.SC_OK;
        });

        get("/subjects", (request, response) -> {
            response.type("application/json");
            return GsonFactory.toJson(surveyService.getAllSubjects());
        });

        post("/subject", (request, response) -> {
            response.type("application/json");
            try {
                Subject subject = GsonFactory.fromJson(request.body(), Subject.class);
                surveyService.saveSubject(subject);
            } catch (Exception e) {
                LOGGER.error("Error while trying to save student", e);
                return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

            }
            return HttpServletResponse.SC_OK;
        });

        get("/survey/:studentID", (request, response) -> {
            response.type("application/json");
            return GsonFactory.toJson(surveyService.getSurveyByStudent(request.params("studentID")));
        });

        post("/survey", (request, response) -> {
            response.type("application/json");
            try {
                Survey survey = GsonFactory.fromJson(request.body(), Survey.class);
                surveyService.saveSurvey(survey);
            } catch (Exception e) {
                LOGGER.error("Error while trying to save a survey", e);
                return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

            }
            return HttpServletResponse.SC_OK;
        });
    }
}
