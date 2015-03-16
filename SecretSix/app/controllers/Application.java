package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result mysamplejson() {
        ObjectNode result = Json.newObject();

        result.put("content", "Get function works.");

        return ok(result);
    }
    //use http://localhost:9000/mysamplejsonWithParms?text=YOUR TEXT HERE TO SEE
    public static Result mysamplejsonWithParms(String text) {

        ObjectNode result = Json.newObject();

        result.put("content", text);

        return ok(result);
    }
}
