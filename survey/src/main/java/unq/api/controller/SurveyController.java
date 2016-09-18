package unq.api.controller;

import static spark.Spark.*;



/**
 * Created by mrivero on 17/9/16.
 */
public class SurveyController {

    public void initSurveyEndopints(){
        get("/hello", (request, response) -> "hola mundooo");

        get("/hello/:name", (request, response) -> {
            return "Hello: " + request.params(":name");
        });
    }
}
