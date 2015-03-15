package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.PatientEntity;
import model.UserEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.UserRepository;
import views.html.index;

/**
 * Created by Samuel_Tjokrosoesilo on 3/10/2015.
 */
public class UserController extends Controller {
    public static Result index() {
        return ok(index.render("Your User Controller is ready."));
    }

    //use http://localhost:9000/user?id=<user_Id>
    public static Result GetUser(int userId) {
        ObjectNode result = Json.newObject();

        // TODO: get from user repository
        UserRepository repository = new UserRepository();
        UserEntity user = repository.GetUser(userId);

        result.put("content", user.getUserName());

        return ok(result);
    }
}
